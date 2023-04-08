package com.searchengine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentDao;
import com.searchengine.dto.RecordDto;
import com.searchengine.entity.*;
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
    public Map<String, Object> getHotdata() {
        List<Data>datas=dataDao.getHotdata();
        Map<String, Object> mp = new HashMap<>();
//        System.out.println(datas);

        // 返回搜索结果
        mp.put("result", datas);
        return mp;
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

    @Override
    //增加点击量
    public boolean addCount(Integer id){
        dataDao.addCount(id);
        return true;
    }
}
