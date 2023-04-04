package com.searchengine.springboot;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataSegmentDao;
import com.searchengine.entity.DataNoBody;
import com.searchengine.entity.DataSegment;
import com.searchengine.entity.Segment;
import com.searchengine.service.DataNoBodyService;
import com.searchengine.service.SegmentService;
import com.searchengine.utils.jieba.keyword.BM25;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.StrictMath.log10;

// 扫描datanobody表把所有内容，对其挨个分词并加入分词表segment
@SpringBootTest
public class DBInitial {


    @Autowired
    private DataNoBodyService dataNoBodyService;
    @Autowired
    private DataSegmentDao dataSegmentDao;
    @Autowired
    private SegmentService segmentService;
    private JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
    static HashSet<String> stopWordsSet = new HashSet<>();
    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    BM25 bm25=new BM25(1.2,0.75);
    //所有datanobody数据的长度之和，用于计算bm25值
    Double totallength;

    @Test
    //计算totallength
    public void calTotalLength(){
        // 获取所有的datanobody数据
        List<DataNoBody> dnbs = dataNoBodyService.getAllDataNoBodys();
        totallength=0.0;
        for(DataNoBody dnb:dnbs){
            totallength+=dnb.getWord().length();
        }
    }
    @Test
    //初始化分词表
    public void InitSegmentTable() {

        List<String> segs = new ArrayList<>();
        // 布隆过滤器
        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),10000000);

        // 加载停顿词
        loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));


        // 获取所有的datanobody数据
        List<DataNoBody> dnbs = dataNoBodyService.getAllDataNoBodys();

        for (DataNoBody dnb:dnbs) {


            String word=dnb.getWord();

            //对每一条数据都进行分词
            //将获取到的word进行分词
            List<SegToken> segTokens = jiebaSegmenter.process(word, JiebaSegmenter.SegMode.INDEX);
            for (SegToken segToken : segTokens) {
                String seg = segToken.word;
                if (stopWordsSet.contains(seg)) continue; // 判断是否是停用词
                // 布隆过滤器判断是否已经包括了
                if (!bf.mightContain(seg)) {
                    bf.put(seg);   // 放进布隆过滤器
                    segs.add(seg); // 加入分词set中
                }
            }

        }

        dataSegmentDao.initSegmentTable(segs); // 将分词全部添加到segment表里面
    }

    @Test
    //根据idf公式 idf=log(语料库文档总数/（包含该词的文档数+1）)
    public void createIDF() throws IOException {
        List<String> segs = new ArrayList<>();
        HashMap<String,Double> hasht=new HashMap<String, Double>();

        // 加载停顿词
        loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));


        // 获取所有的datanobody数据
        List<DataNoBody> dnbs = dataNoBodyService.getAllDataNoBodys();
        //计算各分词出现在几个文档中
        for (DataNoBody dnb:dnbs) {
            String word=dnb.getWord();
            //对每一条数据都进行分词
            //将获取到的word进行分词
            List<SegToken> segTokens = jiebaSegmenter.process(word, JiebaSegmenter.SegMode.INDEX);
            HashMap<String,Integer>hashl=new HashMap<String,Integer>();
            for (SegToken segToken : segTokens) {
                String seg = segToken.word;
                if (stopWordsSet.contains(seg)) continue; // 判断是否是停用词
                if(hashl.containsKey(seg)){
                    continue;
                }else{
                    hashl.put(seg,1);
                    if(hasht.containsKey(seg)){
                        hasht.put(seg,hasht.get(seg)+1.0);
                    }else{
                        hasht.put(seg,1.0);
                    }
                }
            }
        }
        int textnums=dnbs.size();
        File file =new File("src/main/resources/jieba/myidf_dict.txt");

        if(!file.exists()){
            file.createNewFile();
        }

        //使用true，即进行append file
        FileWriter fileWritter = new FileWriter(file,false);
        for (String key : hasht.keySet()) {
            double num=log10(textnums/(hasht.get(key)+1.0));
            String text=key+' '+num+'\n';
            fileWritter.write(text);
        }

        fileWritter.close();

        System.out.println("finish");

    }

    @Test
    //请运行这个函数
    public void initDataSegRelationTable() {
        // 获取到所有的 segment 分词
        List<Segment> segments = segmentService.getAllSeg();

        // 将分词按照 word->id 的方式放入 map
        // 其实就是键值对，不过这里的Key是word，因为我们建立的是倒排索引
        Map<String, Integer> wordToId = new HashMap<>();
        for (Segment seg : segments) {
            wordToId.put(seg.getWord(), seg.getId());
        }

        loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));

        if(totallength== null||totallength==0.0){
            calTotalLength();
        }
        // 一个List<DataSegment>代表一张DataSegment表
        // dataSegmentListMap代表很多张DataSegment表
        Map<Integer, List<DataSegment>> dataSegmentListMap = new HashMap<>(1);
        int cnt = 0;

        //计算分词对应文档中的tf,idf,bm25值
        List<DataNoBody> dnbs = dataNoBodyService.getAllDataNoBodys();
        for(DataNoBody dnb:dnbs){
            String word=dnb.getWord();
            // 进行分词
            List<SegToken> segTokens = jiebaSegmenter.process(word, JiebaSegmenter.SegMode.INDEX);


            // 获取返回的 tfidf 值最高的topn个关键词
            List<Keyword> keywords = tfidfAnalyzer.analyze(word,20);
            Map<String, DataSegment> segmentMap = new HashMap<>();

            for (SegToken segToken : segTokens) {
                String seg = segToken.word;

                // 去掉停顿词
                if (stopWordsSet.contains(seg)) continue;

                // 不在 segment 表中的分词，去掉
                if (!wordToId.containsKey(seg)) continue;

                int segId = wordToId.get(seg);
                int dataId = dnb.getId();
                double tfidf = 0;

                // 如果是 tfidf 值最高的topn个关键词之一，就将 tf 值保存起来
                for (Keyword v : keywords) {
                    if (v.getName().equals(seg)) {
                        tfidf = v.getTfidfvalue();
                        break;
                    }
                }
                if (tfidf==0){
                    continue;
                }
                if (!segmentMap.containsKey(seg)){
                    int count = 1;
                    double tf;
                    double idf;
                    if (TFIDFAnalyzer.idfMap.containsKey(seg)){
                        idf=TFIDFAnalyzer.idfMap.get(seg);
                        tf=tfidf/idf;
                    }else{
                        idf=TFIDFAnalyzer.idfMedian;
                        tf=tfidf/idf;
                    }
                    double L=word.length()*1.0/totallength;
                    double bm=bm25.cal(idf,tf,L);
                    segmentMap.put(seg, new DataSegment(dataId, segId, tf,idf,bm, count));
                } else {
                    DataSegment dataSegment = segmentMap.get(seg);
                    int count = dataSegment.getCount();
                    dataSegment.setCount(++count);
                    segmentMap.put(seg, dataSegment);
                }
            }

            // 将Segment放入DataSegment表中，但是按照Segment的Id来区分放入哪一张DataSegment表, idx就是区分键
            for (DataSegment dataSegment : segmentMap.values()) {
                int segId = dataSegment.getSegId();
                int idx = segId % 1000;
                List list = dataSegmentListMap.getOrDefault(idx, new ArrayList<>());
                list.add(dataSegment);
                dataSegmentListMap.put(idx, list);
                cnt++;
            }
        }



        // 最后通过 dataSegmentList 来创建所有的 DataSegment 表：data_seg_relation
        if (cnt > 0) {
            for (Integer idx : dataSegmentListMap.keySet()) {
                String tableName = "data_seg_relation_" + idx;
                dataSegmentDao.createNewTable(tableName);
                dataSegmentDao.initRelationTable(dataSegmentListMap.get(idx), tableName);
            }
        }
    }
    // 加载停顿词
    private void loadStopWords(Set<String> set, InputStream in){
        BufferedReader bufferReader;
        try
        {
            bufferReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufferReader.readLine())!=null) {
                set.add(line.trim());
            }
            bufferReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
