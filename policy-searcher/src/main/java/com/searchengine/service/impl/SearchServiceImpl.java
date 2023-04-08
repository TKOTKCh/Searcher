package com.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.SegmentDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.Segment;
import com.searchengine.service.SearchService;
import com.searchengine.utils.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private DataDao dataDao;

    @Autowired
    private SegmentDao segmentDao;

    private Trie trie;
    public static HashMap<String,Double> idfMap;
    public static HashSet<String> stopWordsSet;

    @PostConstruct
    public void init() {
        List<Segment> segmentations = segmentDao.getAllSegByTableName("segment_datatitle");
        this.trie = new Trie();
        for (Segment segmentation : segmentations) {
            String word = segmentation.getWord();
            this.trie.add(word);
        }
        if(stopWordsSet==null) {
            stopWordsSet=new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));
        }
        if(idfMap==null) {
            idfMap=new HashMap<>();
            String fileName="/jieba/datatitle.txt";
            loadIDFMap(idfMap, this.getClass().getResourceAsStream(fileName));
        }
    }

    // 搜索业务
    @Override
    public List<Data> getDataByKeyword(String tableName, String keyword, int pageSize, int pageNum) {
        String segmentname = "segment_" + tableName;
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();

        // 对输入的关键字进行分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process(keyword, JiebaSegmenter.SegMode.INDEX);

        boolean flag = true;
        for (SegToken segToken : segTokens) {
            Segment segment = segmentDao.getOneSeg(segmentname, segToken.word);
            // 获取关键词的 segment
            if (segment == null) {
                continue;
            }

            // segment 为空 跳过
            if ("".equals(segToken.word.trim())) {
                continue;
            }

            // 获取segId
            int segId = segment.getId();

            // 通过segId找到去哪张表查找（哪张data_segment_relation表，在建立的时候使用的，这里的100算是魔数了，不规范~）
            int idx = segId % 1000;

            // 组合出一个sql语句：用于取各个关键词查出来的data_segment，union的方式去重
            if (flag) {
                sb.append("select * from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                flag = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select * from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
            }

        }
        String sql = sb.toString();

        if ("".equals(sql)) {
            return null;
        }

        // 通过sql去获取所有的Data，详细见DataMapper.xml
        // offset 是第几页搜索结果的意思
//        List<Data> dataList = dataDao.getDataBySplit(sql, pageSize, offset);
        List<Data> dataList = dataDao.getCompleteDataBySplit(sql, pageSize, offset);
        return dataList;
    }

    @Override
    public Map<String , Object> getDataByScore(String tableName, String keyword, int pageSize, int pageNum) {
        String segmentname = "segment_" + tableName;
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();

        // 对输入的关键字进行分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process(keyword, JiebaSegmenter.SegMode.INDEX);

        boolean flag = true;
        int availSeg = 0;//记录搜索词中有效关键词个数
        //检测关键词是否已经出现过，针对北京北京北京上海这样的搜索记录等价于北京上海
        Map<Integer, Integer> seghasht = new HashMap<Integer, Integer>();
        for (SegToken segToken : segTokens) {
            Segment segment = segmentDao.getOneSeg(segmentname, segToken.word);
            // 获取关键词的 segment
            if (segment == null) {
                continue;
            }

            String segword=segToken.word.trim();
            // segment 为空 跳过
            if ("".equals(segword)) {
                continue;
            }
            if(segword.equals("最新")){
                continue;
            }
            if(stopWordsSet.contains(segword)){
                continue;
            }
//            if(idfMap.containsKey(segword)){
//                if (idfMap.get(segword)<=1.5){
//                    continue;
//                }
//            }
            // 获取segId
            int segId = segment.getId();
            if (seghasht.containsKey(segId)) {
                continue;
            } else {
                availSeg++;
                seghasht.put(segId, 1);
            }
            // 通过segId找到去哪张表查找（哪张data_segment_relation表，在建立的时候使用的，这里的100算是魔数了，不规范~）
            int idx = segId % 1000;

            // 组合出一个sql语句：用于取各个关键词查出来的data_segment，union的方式去重
            if (flag) {
                sb.append("select * from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                flag = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select * from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
            }

        }
        String sql = sb.toString();

        if ("".equals(sql)) {
            return null;
        }
        List<Data> datas = dataDao.getDataRelevance(sql);

        for (int i = 0; i < datas.size(); i++) {
            Integer dataid = datas.get(i).getId();
            Integer count = datas.get(i).getCount();
            double bm25 = datas.get(i).getBm25();
            datas.get(i).setScore(bm25 * count / availSeg);
        }
        Collections.sort(datas);
        int startIndex = pageSize * (pageNum - 1);
        int endIndex = startIndex + pageSize;
        List<Data> dataResult;
        if (datas.size() > endIndex) {
            dataResult = datas.subList(startIndex, endIndex);
        } else {
            dataResult = datas.subList(startIndex, datas.size()-1);
        }
        for(int i=0;i<datas.size()&&i<30;i++){
            System.out.print(datas.get(i).getId()+datas.get(i).getPolicyTitle());
            System.out.print(" ");
        }
        System.out.println();
        Map<String , Object> result = new HashMap<>();
        result.put("data", dataResult);
        result.put("count", datas.size());
        return result;
    }

    @Override
    public List<String> complete(String query) {
        List<String> completion = trie.getRelatedWords(query);
        return completion;
    }

    //加载停顿词
    private void loadStopWords(Set<String> set, InputStream in){
        BufferedReader bufr;
        try
        {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line=bufr.readLine())!=null) {
                set.add(line.trim());
            }
            try
            {
                bufr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //加载IDFmap
    private void loadIDFMap(Map<String,Double> map, InputStream in ){
        BufferedReader bufr;
        try
        {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line=bufr.readLine())!=null) {
                String[] kv=line.trim().split(" ");
                if(kv.length<=1){
                    continue;
                }
                map.put(kv[0],Double.parseDouble(kv[1]));


            }
            try
            {
                bufr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
