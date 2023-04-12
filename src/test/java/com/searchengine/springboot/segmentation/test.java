package com.searchengine.springboot.segmentation;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.SegmentDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.QueryKeyword;
import com.searchengine.entity.Segment;
import com.searchengine.utils.PythonSocket;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.*;

import static java.lang.Integer.min;


@SpringBootTest
@Slf4j
public class test {
    @Autowired
    private DataDao dataDao;
    @Autowired
    private SegmentDao segmentDao;
    private JiebaSegmenter segmenter = new JiebaSegmenter();

    public static HashMap<String,Double> idfMap;
    public static HashSet<String> stopWordsSet;

    String[] sentences =
            new String[]{
                    "美沃可视数码裂隙灯,检查眼前节健康状况",
                    "欧美夏季ebay连衣裙 气质圆领通勤绑带收腰连衣裙 zc3730"
            };
    @Test
    public void segTest1(){
//        for (String sentence : sentences) {
//            long start = System.currentTimeMillis();
//            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);
//            TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
//            List<Content> list=tfidfAnalyzer.analyze(sentence,10);
//            long end = System.currentTimeMillis();
//            System.out.println((end - start) + "ms");
//            System.out.print(String.format(Locale.getDefault(), "\n%s\n%s", sentence, tokens.toString()));
//            for (SegToken token : tokens) {
//                System.out.println(token.word);
//            }
//        }
    }

    @Test
    public void tfidfTest1(){
//        String content="孩子上了幼儿园 安全防拐教育要做好 卧槽这也太牛逼了吧";
//        int topN=5;
//        TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
//        List<Content> list=tfidfAnalyzer.analyze(content,10);
//        for(Content word:list)
//            System.out.print(word.getName()+":"+word.getTfidfvalue()+",");
    }
    @Test
    public void testDemo() {
        String[] sentences =
                new String[] {"这是一个伸手不见五指的黑夜我叫孙悟空，我爱北京，我爱Python和C++", "我不喜欢日本和服", "雷猴回归人间",
                        "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作", "结果婚的和尚未结过婚的"};
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX).toString());
        }
        for (String sentence : sentences) {
            System.out.println(segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH).toString());
        }
    }

    @Test
    public void testReturn1(){
        int s = segmentDao.insertSeg("牛逼");
        log.info("id:{}",s);
    }

    @Test
    public void NDCG() throws IOException {
        init();
        LinkedHashMap<String,LinkedHashMap<Integer,Integer>> val=readVal("src/main/resources/val.txt");
        double avg_ndcg=0;
        double topHR=0;
        double HR=0;
        int K=10;
        int topvalsize=0;
        int valsize=0;
        for(String key : val.keySet()) {
            Map<Integer,Integer>temp=val.get(key);

            int i=1;
            double IDCG=0.0;
            int topcount_val=0;
            int count_val=0;
            for(Integer pid:temp.keySet()){

                if(i>K){
                    break;
                }
                double reli=temp.get(pid);
                double num=Math.log(i+1) / Math.log(2);
                double reli_log=reli/num;
                if(reli==5){
                    topcount_val++;
                }
                count_val++;
                IDCG+=reli_log;
                i++;
            }
            int size=temp.size();
            List<Data>datas=getDataByScore("datatitle",key,size,1);
            int j=1;
            double DCG=0.0;
            double topcount=0;
            double count=0;
            for(Data data:datas){
                if(j>K){
                    break;
                }
                int pid=data.getPolicyId();
                double reli=1;
                double num=Math.log(j+1) / Math.log(2);
                if(temp.containsKey(pid)){
                    reli=temp.get(pid);
                    if(reli==5){
                        topcount++;
                    }
                    count++;
                }
                double reli_log=reli/num;
                DCG+=reli_log;
                j++;
            }
            topvalsize+=topcount_val;
            valsize+=count_val;
            double ndcg=DCG/IDCG;
            avg_ndcg+=ndcg;
            topHR+=topcount;
            HR+=count;
            System.out.println(key+" IDCG:"+IDCG+" DCG:"+DCG+" NDCG:"+ndcg+" HR:"+HR+" topHR:"+topHR);
        }
        avg_ndcg/=val.size();
        topHR/=topvalsize;
        HR/=valsize;
        System.out.println(avg_ndcg);
        System.out.println(topHR);
        System.out.println(HR);
    }


    public void init(){
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


    public List<Data> getDataByScore(String tableName, String content, int pageSize, int pageNum) {
        String segmentname = "segment_" + tableName;
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();

        String []province={"黑龙江省","吉林省","辽宁省","上海市","江苏省","浙江省","安徽省","福建省","江西省","山东省","台湾省","北京市","天津市","山西省","河北省","内蒙古自治区","河南省","湖北省","湖南省","广东省","广西壮族自治区","海南省","香港特别行政区","澳门特别行政区","四川省","贵州省","云南省","重庆市","西藏自治区","陕西省","甘肃省","青海省","宁夏回族自治区","新疆维吾尔自治区"};
        String []brief_pro={"黑龙江","吉林","辽宁","上海","江苏","浙江","安徽","福建","江西","山东","台湾","北京","天津","山西","河北","内蒙古","河南","湖北","湖南","广东","广西","海南","香港","澳门","四川","贵州","云南","重庆","西藏","陕西","甘肃","青海","宁夏","新疆"};
        for(int i=0;i<province.length;i++){
            content=content.replaceAll(province[i],brief_pro[i]);
        }
        content=content.replaceAll("医保","医疗保险");
        content=content.replaceAll("环保","环境保护");
//        content=content.replaceAll("政策","");
        // 对输入的关键字进行分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process(content, JiebaSegmenter.SegMode.INDEX);

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
            // 通过segId找到去哪张表查找哪张data_segment_relation表，在建立的时候使用的，这里的100算是魔数了，不规范~
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

//        for (int i = 0; i < datas.size(); i++) {
//            Integer dataid = datas.get(i).getId();
//            Integer count = datas.get(i).getCount();
//            double bm25 = datas.get(i).getBm25();
//            datas.get(i).setScore(bm25 * count / availSeg);
//        }
//        Collections.sort(datas);
        int startIndex = pageSize * (pageNum - 1);
        int endIndex = startIndex + pageSize;
        List<Data> dataResult;
        if (datas.size() > endIndex) {
            dataResult = datas.subList(startIndex, endIndex);
        } else {
            dataResult = datas.subList(startIndex, datas.size()-1);
        }
        System.out.println();
        return dataResult;
    }

    public List<Data> getDataByScore2(String tableName, String content, int pageSize, int pageNum) {
        String segmentname = "segment_" + tableName;
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();

        String []province={"黑龙江省","吉林省","辽宁省","上海市","江苏省","浙江省","安徽省","福建省","江西省","山东省","台湾省","北京市","天津市","山西省","河北省","内蒙古自治区","河南省","湖北省","湖南省","广东省","广西壮族自治区","海南省","香港特别行政区","澳门特别行政区","四川省","贵州省","云南省","重庆市","西藏自治区","陕西省","甘肃省","青海省","宁夏回族自治区","新疆维吾尔自治区"};
        String []brief_pro={"黑龙江","吉林","辽宁","上海","江苏","浙江","安徽","福建","江西","山东","台湾","北京","天津","山西","河北","内蒙古","河南","湖北","湖南","广东","广西","海南","香港","澳门","四川","贵州","云南","重庆","西藏","陕西","甘肃","青海","宁夏","新疆"};
//        for(int i=0;i<province.length;i++){
//            content=content.replaceAll(province[i],brief_pro[i]);
//        }
        content=content.replace("医保","医疗保险");
        content=content.replace("环保","环境保护");
        content=content.replace("政策","");

        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process(content, JiebaSegmenter.SegMode.INDEX);
        List<QueryKeyword> qks= PythonSocket.getKeyWord(content);
        boolean flag = true;
        int availSeg = 0;//记录搜索词中有效关键词个数
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
        List<Data> datas = dataDao.getDataRelevance(sql);

//        for (int i = 0; i < datas.size(); i++) {
//            Integer dataid = datas.get(i).getId();
//            Integer count = datas.get(i).getCount();
//            double bm25 = datas.get(i).getBm25();
//            datas.get(i).setScore(bm25 * count / availSeg);
//        }
//        Collections.sort(datas);
        int startIndex = pageSize * (pageNum - 1);
        int endIndex = startIndex + pageSize;
        List<Data> dataResult;
        if (datas.size() > endIndex) {
            dataResult = datas.subList(startIndex, endIndex);
        } else {
            dataResult = datas.subList(startIndex, datas.size()-1);
        }
        System.out.println();
        return dataResult;
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

    private LinkedHashMap<String,LinkedHashMap<Integer,Integer>> readVal(String path) throws IOException {
        LinkedHashMap<String,LinkedHashMap<Integer,Integer>> result=new LinkedHashMap<>();
        FileInputStream fin = new FileInputStream(path);
        InputStreamReader reader = new InputStreamReader(fin,"GBK");
        BufferedReader buffReader = new BufferedReader(reader);
        String strTmp = "";
        while((strTmp = buffReader.readLine())!=null){
            String[] data=strTmp.split(" ");
            LinkedHashMap<Integer,Integer> temp=new LinkedHashMap<>();
            for(int i=1;i<data.length;i=i+2){
                int pid=Integer.parseInt(data[i]);
                int score=Integer.parseInt(data[i+1]);
                temp.put(pid,score);
            }
            result.put(data[0],temp);
        }
        buffReader.close();
        return result;
    }
}
