package com.searchengine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.SegmentDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.QueryKeyword;
import com.searchengine.entity.Segment;
import com.searchengine.entity.User;
import com.searchengine.service.SearchService;
import com.searchengine.service.UserService;
import com.searchengine.utils.PythonSocket;
import com.searchengine.utils.RedisUtil_db0;
import com.searchengine.utils.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private DataDao dataDao;
    @Autowired
    private SegmentDao segmentDao;
    @Autowired
    private RedisUtil_db0 redisUtil;
    @Autowired
    private UserService userService;

    private Trie trie;
    public static HashMap<String,Double> idfMap;
    public static HashSet<String> stopWordsSet;
    public static Map<String,String>provinces_map;
    public static Map<String,String>brief_pro_map;
    public static Map<String,Integer>profession_map;
    public static JiebaSegmenter segmenter;

    String []provinces={"黑龙江省","吉林省","辽宁省","上海市","江苏省","浙江省","安徽省","福建省","江西省","山东省","台湾省","北京市","天津市","山西省","河北省","内蒙古自治区","河南省","湖北省","湖南省","广东省","广西壮族自治区","海南省","香港特别行政区","澳门特别行政区","四川省","贵州省","云南省","重庆市","西藏自治区","陕西省","甘肃省","青海省","宁夏回族自治区","新疆维吾尔自治区"};
    String []brief_pro={"黑龙江","吉林","辽宁","上海","江苏","浙江","安徽","福建","江西","山东","台湾","北京","天津","山西","河北","内蒙古","河南","湖北","湖南","广东","广西","海南","香港","澳门","四川","贵州","云南","重庆","西藏","陕西","甘肃","青海","宁夏","新疆"};
    String []professions={"农业","工商业","科技","教育","文化","民生"};
    @PostConstruct
    public void init() throws IOException {
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
        if(profession_map==null){
            profession_map=new LinkedHashMap<>();
            for(int i=0;i<professions.length;i++){
                profession_map.put(professions[i],1);
            }
        }
        if(provinces_map==null||brief_pro_map==null){
            provinces_map=new LinkedHashMap<String,String>();
            brief_pro_map=new LinkedHashMap<String,String>();
            for(int i=0;i<provinces.length;i++){
                provinces_map.put(provinces[i],brief_pro[i]);
                brief_pro_map.put(brief_pro[i],provinces[i]);
            }
        }

        if(segmenter==null){
            segmenter = new JiebaSegmenter();
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


//    public Map<String , Object> getDataByScoreAndAddQuery(
//            String tableName, String content, int pageSize, int pageNum,String province,String type,String year,String id
//    ) throws IOException, ExecutionException, InterruptedException {
//
//        long start = System.currentTimeMillis();
//
//        Future<Map<String, Object>> task1 = this.getDataByScore(
//                tableName, content, pageSize,  pageNum, province, type, year, id
//        );
//
//        Future<String> task2 = this.addUserQuery(Integer.valueOf(id),content);
//
//        while(true) {
//            if(task1.isDone() && task2.isDone()) {
//                // 三个任务都调用完成，退出循环等待
//                break;
//            }
//            Thread.sleep(100);
//        }
//
//        long end = System.currentTimeMillis();
//
//        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
//
//        return task1.get();
//    }

    public AsyncResult<String >  addUserQuery(Integer userid,String query) {
        try {
            double time = System.currentTimeMillis();
            userService.addUserQuery(userid, query, time);
            return new AsyncResult<>("success") ;
        } catch (Exception e) {
            return new AsyncResult<>("fail");
        }

    }
    @Override
    @Async
    public Map<String, Object>  getDataByScore(
            String tableName, String content, int pageSize, int pageNum,String province,String type,String year,String id
    ) throws IOException {
//        String segmentname = "segment_" + tableName;
//        int offset = pageSize * (pageNum - 1);
//        StringBuilder sb = new StringBuilder();
//
//        // 对输入的关键字进行分词
//        JiebaSegmenter segmenter = new JiebaSegmenter();
//        List<SegToken> segTokens = segmenter.process(keyword, JiebaSegmenter.SegMode.INDEX);
//
//        boolean flag = true;
//        int availSeg = 0;//记录搜索词中有效关键词个数
//        //检测关键词是否已经出现过，针对北京北京北京上海这样的搜索记录等价于北京上海
//        Map<Integer, Integer> seghasht = new HashMap<Integer, Integer>();
//        for (SegToken segToken : segTokens) {
//            Segment segment = segmentDao.getOneSeg(segmentname, segToken.word);
//            // 获取关键词的 segment
//            if (segment == null) {
//                continue;
//            }
//
//            String segword=segToken.word.trim();
//            // segment 为空 跳过
//            if ("".equals(segword)) {
//                continue;
//            }
//            if(segword.equals("最新")){
//                continue;
//            }
//            if(stopWordsSet.contains(segword)){
//                continue;
//            }
////            if(idfMap.containsKey(segword)){
////                if (idfMap.get(segword)<=1.5){
////                    continue;
////                }
////            }
//            // 获取segId
//            int segId = segment.getId();
//            if (seghasht.containsKey(segId)) {
//                continue;
//            } else {
//                availSeg++;
//                seghasht.put(segId, 1);
//            }
//            // 通过segId找到去哪张表查找（哪张data_segment_relation表，在建立的时候使用的，这里的100算是魔数了，不规范~）
//            int idx = segId % 1000;
//
//            // 组合出一个sql语句：用于取各个关键词查出来的data_segment，union的方式去重
//            if (flag) {
//                sb.append("select * from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
//                flag = false;
//            } else {
//                sb.append("union").append('\n');
//                sb.append("select * from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
//            }
//
//        }
//        String sql = sb.toString();
//
//        if ("".equals(sql)) {
//            return null;
//        }
//        StringBuilder sb2=new StringBuilder();
//        if(province!=null&&!province.equals("")){
//            sb2.append(" and data.province = ").append("'").append(province).append("'");
//        }
//        if(type!=null&&!type.equals("")){
//            sb2.append(" and data.policy_type = ").append("'").append(type).append("'");
//        }
//        if(year!=null&&!year.equals("")){
//            int year_int=Integer.parseInt(year);
//            sb2.append(" and data.pub_time_year = ").append(year_int);
//        }
//        String sql2=sb2.toString();
//        List<Data> datas;
////        if(sb2.equals("")||sb2==null){
//            datas= dataDao.getDataRelevance(sql);
////        }else{
//            datas= dataDao.getDataRelevanceLimit(sql,sql2);
////        }
//
//
//        for (int i = 0; i < datas.size(); i++) {
//            Integer dataid = datas.get(i).getId();
//            Integer count = datas.get(i).getCount();
//            double bm25 = datas.get(i).getBm25();
//            datas.get(i).setScore(bm25 * count / availSeg);
//        }
//        Collections.sort(datas);
//        int startIndex = pageSize * (pageNum - 1);
//        int endIndex = startIndex + pageSize;
//        List<Data> dataResult;
//        if(datas.size()==0){
//            dataResult=datas;
//        }
//        else if (datas.size() > endIndex) {
//            dataResult = datas.subList(startIndex, endIndex);
//        } else {
//            dataResult = datas.subList(startIndex, datas.size()-1);
//        }
//        for(int i=0;i<datas.size()&&i<30;i++){
//            System.out.print(datas.get(i).getId()+datas.get(i).getPolicyTitle());
//            System.out.print(" ");
//        }
//        System.out.println();
//        Map<String , Object> result = new HashMap<>();
//        result.put("data", dataResult);
//        result.put("count", datas.size());
//        return result;
        String segmentname = "segment_" + tableName;
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();


        content=content.replace("医保","医疗保险");
        content=content.replace("环保","环境保护");
        content=content.replace("政策","");

        String position = null;
        String profession=null;
        if (id != null && !id.equals("") ) {
            String str = (String) redisUtil.get("login-userObj-" + id);
            User user = JSONObject.parseObject(str, User.class);
            position = user.getAddress();
            profession=user.getCareer();
        }
        if(content.equals("")){
            Map<String , Object> result = new HashMap<>();
            result.put("data", null);
            result.put("count", 0);
        }

        List<QueryKeyword> qks = new ArrayList<>();
        List<SegToken> segTokens=null;
        if( qks.size()==0){
            segTokens = segmenter.process(content, JiebaSegmenter.SegMode.INDEX);
            for(SegToken seg:segTokens){
                qks.add(new QueryKeyword(seg.word.trim(),1));
            }
        }
        if(position!=null&&!position.equals("")){
            int flag=0;
            for(QueryKeyword qk:qks){
                String word=qk.getWord();
                if(brief_pro_map.containsKey(word)||provinces_map.containsKey(word)){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                qks.add(new QueryKeyword(position,0.3));
            }
        }
        if(profession!=null&&!profession.equals("")){
            String[] professionList=profession.split("#");
            int flag=0;
            for(QueryKeyword qk:qks){
                String word=qk.getWord();
                if(profession_map.containsKey(word)){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                for(String str:professionList){
                    qks.add(new QueryKeyword(str,0.3));
                }
            }
        }
        boolean flag = true;

        //检测关键词是否已经出现过，针对北京北京北京上海这样的搜索记录等价于北京上海
        Map<Integer, Integer> seghasht = new HashMap<Integer, Integer>();
        for (QueryKeyword qk:qks) {
            String segword=qk.getWord().trim();
            double weight=qk.getWeight();
            Segment segment = segmentDao.getOneSeg(segmentname, segword);
            // 获取关键词的 segment
            if (segment == null) {
                continue;
            }


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
            // 获取segId
            int segId = segment.getId();
            if (seghasht.containsKey(segId)) {
                continue;
            } else {
                seghasht.put(segId, 1);
            }
            // 通过segId找到去哪张表查找哪张data_segment_relation表，在建立的时候使用的，这里的100算是魔数了，不规范~
            int idx = segId % 1000;

            // 组合出一个sql语句：用于取各个关键词查出来的data_segment，union的方式去重
            if (flag) {
                sb.append("select data_id,(bm25* ").append(weight).append(")").append(" as bm25, ").append(weight).append(" as weight from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                flag = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select data_id,(bm25* ").append(weight).append(")").append(" as bm25, ").append(weight).append(" as weight from ").append(tableName).append("_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
            }

        }
        String sql = sb.toString();

        if ("".equals(sql)) {
            return null;
        }
        StringBuilder sb2=new StringBuilder();
        if(province!=null&&!province.equals("")){
            sb2.append(" and data.province = ").append("'").append(province).append("'");
        }
        if(type!=null&&!type.equals("")){
            sb2.append(" and data.policy_type = ").append("'").append(type).append("'");
        }
        if(year!=null&&!year.equals("")){
            int year_int=Integer.parseInt(year);
            sb2.append(" and data.pub_time_year = ").append(year_int);
        }
        String sql2=sb2.toString();
        List<Data> datas;
        if(sql2!=null&&!sql2.equals("")){
            datas = dataDao.getDataRelevanceLimit(sql,sql2);
        }else{
            datas=dataDao.getDataRelevance(sql);
        }

        int startIndex = pageSize * (pageNum - 1);
        int endIndex = startIndex + pageSize;
        List<Data> dataResult=new LinkedList<>();
        if(datas.size()!=0){
            if (datas.size() > endIndex) {
                dataResult = datas.subList(startIndex, endIndex);
            } else {
                dataResult = datas.subList(startIndex, datas.size()-1);
            }
        }
        Collections.sort(dataResult);


        Map<String , Object> result = new HashMap<>();
        result.put("data", dataResult);
        result.put("count", datas.size());
        if (segTokens != null) {
            segTokens.sort(new Comparator<SegToken>() {
                @Override
                public int compare(SegToken o1, SegToken o2) {
                    return o1.word.trim().length() > o2.word.trim().length()?-1:1;
                }
            });
            result.put("segments", segTokens);
        }
        System.out.println(content);
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
