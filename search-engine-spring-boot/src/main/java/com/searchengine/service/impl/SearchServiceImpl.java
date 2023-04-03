package com.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.bm25.BM25;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.Data;
import com.searchengine.service.SearchService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private DataDao dataDao;

    @Autowired
    private SegmentationDao segmentationDao;

    // 这个是为BM2.5 做准备，用来储存Query的分词

    // 搜索业务
    @Override
    public Pair<List<String>, Map<String, Object>> getDataByKeyword(String keyword, int pageSize, int pageNum) {

        List<String> keywords = new ArrayList<String>();
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();

        // 对输入的关键字进行分词
        JiebaSegmenter segment = new JiebaSegmenter();
        List<SegToken> segTokens = segment.process(keyword, JiebaSegmenter.SegMode.SEARCH);

        boolean flag = true;
        for (SegToken segToken : segTokens) {
            // 获取关键词的 segment
            if (segmentationDao.selectOneSeg(segToken.word) == null) {
                continue;
            }

            // segment 为空 跳过
            if ("".equals(segToken.word.trim())) {
                continue;
            }

            // 获取segId
            String word = String.valueOf(segmentationDao.selectOneSeg(segToken.word).getWord());
            keywords.add(word);
            int segId = segmentationDao.selectOneSeg(segToken.word).getId();

            // 通过segId找到去哪张表查找（哪张data_segment_relation表，在建立的时候使用的，这里的100算是魔数了，不规范~）
            int idx = segId % 100;

            // 组合出一个sql语句：用于取各个关键词查出来的data_segment，union的方式去重
            if (flag) {
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                flag = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
            }

        }
        String sql = sb.toString();

        if ("".equals(sql)) {
            return null;
        }

        // 通过sql去获取所有的Data，详细见DataMapper.xml
        // offset 是第几页搜索结果的意思
        List<Data> datas = dataDao.getDataBySplit(sql, pageSize, offset);
        Map<String, Object> mp = new HashMap<>();

        // 返回搜索结果
        mp.put("result", datas);
        return new Pair<>(keywords,mp);
    }

    public Map<Double, Object> getBM25( List<String> keywords, Map<String, Object> mp){
        BM25 bm25= new BM25(1.2,0.75);
        Map<Double, Object> map = new HashMap<>();
        List<Data> datas = (List<Data>) mp.get("result");

        List<String> docs = new ArrayList<String>();
        for (Data data : datas) {
            docs.add(data.getCaption());
        }
        Map<String,Double> idfMap = new HashMap<String,Double>();
        loadIDFMap(idfMap, this.getClass().getResourceAsStream("/jieba/idf_dict.txt"));

        List<Double> scores = new ArrayList<Double>();
        int i = 0;
        for(String doc:docs){
            i++;
            double score = 0;
            for (String keyword :keywords){
                System.out.println("第"+ i +"个值关于关键词"+keyword+"的bm25计算值是:"+bm25.cal(keyword, doc, docs, idfMap));
                score += bm25.cal(keyword, doc, docs, idfMap);
            }
            scores.add(score);
            map.put(score,datas.get(i-1));
        }

        System.out.println("分数为:"+scores);
        System.out.println(map);
        return map;
    }

    public static void loadIDFMap(Map<String, Double> map, InputStream in){
        BufferedReader bufr;
        try
        {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line=bufr.readLine())!=null) {
                String[] kv=line.trim().split(" ");
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
