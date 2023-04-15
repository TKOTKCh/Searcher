package com.searchengine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentDao;
import com.searchengine.dao.StatisticDao;
import com.searchengine.dto.RecordDto;
import com.searchengine.entity.*;
import com.searchengine.entity.Record;
import com.searchengine.rabbitmq.MQSender;
import com.searchengine.service.RecordSegService;
import com.searchengine.service.DataService;
import com.searchengine.service.SegmentService;
import com.searchengine.utils.RedisUtil_db0;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class DataServiceImpl extends ServiceImpl<DataDao, Data> implements DataService {

    @Autowired
    private DataDao dataDao;
    @Autowired
    private SegmentDao segmentDao;

    @Autowired
    private RecordSegDao recordSegDao;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private RecordSegService recordSegService;

    @Autowired
    private RedisUtil_db0 redisUtil;

    //记录上次更新的时间
    private double lastupdate=0;


    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter segmenter = new JiebaSegmenter();
    @Override
    public List<Record> queryAllRecord() {
        return dataDao.selectAllRecords();
    }

    @Override
    public List<Record> selectPartialRecords(int limit, int offset) {
        return dataDao.selectPartialRecords(limit, offset);
    }

    @Override
    public List<Record> queryRecordByWord(String word) {
        word = "%" + word + "%";
        return dataDao.selectRecordsByWord(word);
    }

    @Override
    public List<Record> queryRecordFilter(String word) {
        return null;
    }

    @Override
    public List<RecordDto> search(String searchInfo) {

        // ======检查是否需要过滤======start
        Set<Integer> set = new HashSet<>();
        List<SegToken> segTokensFilter = new ArrayList<>();
        String[] searchWords = searchInfo.split("\\s+");
        for (int i = searchWords.length - 1; i >= 1; i--) {
            if (Pattern.matches("^-.*?$", searchWords[i])) {
                List<SegToken> l = segmenter.process(searchWords[i].substring(1, searchWords[i].length()), JiebaSegmenter.SegMode.INDEX);
                for (SegToken v : l) {
                    segTokensFilter.add(v);
                }
            } else {
                break;
            }
        }
        for (SegToken token : segTokensFilter) {
            Segment oneSeg = segmentDao.selectOneSeg(token.word);
            //在redis中搜索Segmentation
//            String s = String.valueOf(redisUtil.get("seg_" + token.word));
//            Segmentation oneSeg = null;
//            if(!s.equals(null) && !s.equals("null")){
//                int id =  Integer.parseInt(s) + 1;
//                oneSeg = segmentationDao.selectOneById(id);
//            }
            if (oneSeg != null) {
                List<Integer> RecordsIdList = recordSegService.queryRecordBySeg(oneSeg);  // 包含过滤词分词的所有recordID
                for (Integer v : RecordsIdList) {
                    set.add(v);
                }
            }
        }
        String temp = "";
        String[] strs = searchInfo.split(" ");
        for (String v : strs) {
            if (!Pattern.matches("^-.*?$", v)) {
                temp = temp + v;
            }
        }
        searchInfo = temp;
        // ======检查是否需要过滤======end

        Set<Integer> recordIds = new HashSet<>();
        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.SEARCH);
        List<RecordDto> recordDtoList = new ArrayList<>();
        for (SegToken token : segTokens) {

            //查出每个分词对应的caption
            log.info("分词为{}",token.word);
            Segment oneSeg = segmentDao.selectOneSeg(token.word);
//            String s = String.valueOf(redisUtil.get("seg_" + token.word));
//            Segmentation oneSeg = null;
//            if (!s.equals(null) && !s.equals("null")) {
//                int id = Integer.parseInt(s)+1;
//                oneSeg = segmentationDao.selectOneById(id);
//            }
            Double tidif = new Double(0);
            if (oneSeg!=null) {
                List<Integer> RecordsIdList = recordSegService.queryRecordBySeg(oneSeg);//包含该分词的所有recordID

                for (Integer dataId : RecordsIdList) {
                    if (set.contains(dataId)) {
                        continue;  // 若包含需要过滤的词 continue
                    }
                    if (!recordIds.contains(dataId)){
                        RecordDto recordDto = new RecordDto();
                        recordIds.add(dataId);
                        //对于每个record对象 查询该分词对应的tidif加入recordDto
                        //分表查询
                        BeanUtils.copyProperties(dataDao.selectById(dataId),recordDto);

                        List<RecordSeg> recordSegList= new ArrayList<>();
                        RecordSeg recordSeg = recordSegDao.selectOneRecordSeg(dataId, oneSeg.getId());
                        tidif =recordSeg.getTidifValue();
                        recordSegList.add(recordSeg);
                        recordDto.setRecordSegs(recordSegList);
                        Double weight = recordDto.getWeight() + tidif;
                        recordDto.setWeight(weight);
                        recordDtoList.add(recordDto);
                    }else {
                        //找出对应的recordDto
                        for (RecordDto dto : recordDtoList) {
                            if (dto.getId().equals(dataId)) {
                                List<RecordSeg> recordSegs = dto.getRecordSegs();
                                RecordSeg recordSeg = recordSegDao.selectOneRecordSeg(dataId, oneSeg.getId());
                                tidif =recordSeg.getTidifValue();
                                recordSegs.add(recordSeg);
                                dto.setRecordSegs(recordSegs);
                                Double weight = dto.getWeight() + tidif;
                                dto.setWeight(weight);
                            }
                        }


                    }

                }
            }
        }
        return recordDtoList;
    }

    @Override
    public Boolean addRecord(Record record) {
//        //文本信息加入data表
//        dataDao.insertRecord(record);
//        //分词处理
//        String sentence = record.getCaption();
//        List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
//        List<Keyword> list=tfidfAnalyzer.analyze(sentence,5);
//        Integer recordId = record.getId();
//        Double tidifValue = new Double(0);
//        for (SegToken segToken : segTokens) {
//            //对应tidif值
//            for (Keyword keyword : list) {
//                if (keyword.getName().equals(segToken.word)){
//                    tidifValue = keyword.getTfidfvalue();
//                }
//            }
//            //分词信息加入分词表
//            segmentService.addSeg(segToken.word,recordId,tidifValue);
//        }
        return true;
    }

    @Override
    public boolean addData(Integer policyId,
                           String policyTitle,
                           String policyGrade,
                           String pubAgencyId,
                           String pubAgency,
                           String pubAgencyFullname,
                           String pubNumber,
                           String pubTime,
                           String policyType,
                           String policyBody,
                           String province,
                           String city,
                           String policySource,
                           String pubTimeYear){
//        int id=dataDao.getNumberOfData()+1;
//        String content=policyId+policyTitle+policyGrade+pubAgencyId+pubAgency+pubAgencyFullname+pubNumber+pubTime+policyType+policyBody+province+city+
//                policySource+pubTimeYear;
//        List<SegToken> segTokens = segmenter.process(content, JiebaSegmenter.SegMode.INDEX);
//        List<Keyword> keywords = tfidfAnalyzer.analyze(content,20,"datatitle");
//        Map<String, DataSegment> segmentMap = new HashMap<>();
//        // 获取到所有的 segment 分词
//        List<Segment> segments = segmentService.getAllSeg("segment_datatitle");
//
//        // 将分词按照 word->id 的方式放入 map
//        // 其实就是键值对，不过这里的Key是word，因为我们建立的是倒排索引
//        Map<String, Integer> wordToId = new HashMap<>();
//        for (Segment seg : segments) {
//            wordToId.put(seg.getWord(), seg.getId());
//        }
//        for (SegToken segToken : segTokens) {
//            String seg = segToken.word;
//
//
//            // 不在 segment 表中的分词，去掉
//            if (!wordToId.containsKey(seg)) continue;
//
//            int segId = wordToId.get(seg);
//            int dataId = id;
//            double tfidf = 0;
//
//            // 如果是 tfidf 值最高的topn个关键词之一，就将 tf 值保存起来
//            for (Keyword v : keywords) {
//                if (v.getName().equals(seg)) {
//                    tfidf = v.getTfidfvalue();
//                    break;
//                }
//            }
//            if (tfidf==0){
//                continue;
//            }
//            if (!segmentMap.containsKey(seg)){
//                int count = 1;
//                double tf;
//                double idf;
//                if (TFIDFAnalyzer.idfMap.containsKey(seg)){
//                    idf=TFIDFAnalyzer.idfMap.get(seg);
//                    tf=tfidf/idf;
//                }else{
//                    idf=TFIDFAnalyzer.idfMedian;
//                    tf=tfidf/idf;
//                }
//                double L=content.length()*1.0/totallength;
//                double bm=bm25.cal(idf,tf,L);
//                segmentMap.put(seg, new DataSegment(dataId, segId, tf,idf,bm, count));
//            } else {
//                DataSegment dataSegment = segmentMap.get(seg);
//                int count = dataSegment.getCount();
//                dataSegment.setCount(++count);
//                segmentMap.put(seg, dataSegment);
//            }
//        }
        return true;
    }
    @Override
    public List<Data> getSomeDatas(int limit, int offset) {
        return dataDao.getSomeDatas(limit, offset);
    }

    @Override
    public IPage<Data> findPage(Integer pageNum, Integer pageSize, String username, String email, String phone, String address) {
        IPage<Data> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Data> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("uid");
        IPage<Data> userPage = this.page(page, queryWrapper);
        return userPage;
    }

    public Data getById(String id) {
        QueryWrapper<Data> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Data one = getOne(queryWrapper);
        return one;
    }

    @Override
    public List<Data> getHotdata() {
        List<Data> datas=dataDao.getHotdata();
//        System.out.println(datas);

        // 返回搜索结果
        return datas;
    }

    @Override
    //更新热门
    public boolean updateHotdata(){
        //以下三者顺序不能调换！
        dataDao.clearHotdata();
        dataDao.updateHotdata();
        dataDao.clearCount();
        return true;
    }

    @Override
    //判断当前时间点需不需要更新，如果现在的时间距离上次更新时间已经过去2小时就更新
    public boolean judgeUpdate(double currentTime){
//        System.out.println(currentTime);
//        System.out.println(lastupdate);
        if(currentTime-7200>=lastupdate){
            lastupdate=currentTime;

            return true;
        }
        return false;
    }

    @Autowired
    private MQSender mqSender;

    @Override
    //增加点击量
    public boolean addCount(Integer id){
        try {
            long incr = redisUtil.incr("total-click", 1);
            StatisticHistory statisticHistory = new StatisticHistory("total_click", String.valueOf(incr) );
            mqSender.sendClickMsg(JSONObject.toJSONString(statisticHistory));

            String todayDate = statisticService.getTodayDate();
            long todayClick = redisUtil.incr("click-"+todayDate, 1);
            StatisticHistory todayClickMsg = new StatisticHistory(todayDate,String.valueOf(todayClick) );
            mqSender.sendClickMsg(JSONObject.toJSONString(todayClickMsg));

            mqSender.sendDataClickCountMsg(id.toString());

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
