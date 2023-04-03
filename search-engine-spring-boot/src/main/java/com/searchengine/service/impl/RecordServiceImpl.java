package com.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.dto.DataDto;
import com.searchengine.entity.Data;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordSegService;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.utils.RedisUtil_db0;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Slf4j
public class RecordServiceImpl implements RecordService {

    @Autowired
    private DataDao dataDao;
    @Autowired
    private SegmentationDao segmentationDao;

    @Autowired
    private RecordSegDao recordSegDao;

    @Autowired
    private SegmentationService segmentationService;

    @Autowired
    private RecordSegService recordSegService;

    @Autowired
    private RedisUtil_db0 redisUtil;


    TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();
    JiebaSegmenter segmenter = new JiebaSegmenter();

    @Override
    public List<Data> queryAllRecord() {
        return dataDao.selectAllRecords();
    }

    @Override
    public List<Data> selectPartialRecords(int limit, int offset) {
        return dataDao.selectPartialRecords(limit, offset);
    }

    @Override
    public List<Data> queryRecordByWord(String word) {
        word = "%" + word + "%";
        return dataDao.selectRecordsByWord(word);
    }

    @Override
    public List<Data> queryRecordFilter(String word) {
        return null;
    }

    @Override
    public List<DataDto> search(String searchInfo) {

        // ======检查是否需要过滤======start
        Set<Integer> set = new HashSet<>();
        List<SegToken> segTokensFilter = new ArrayList<>();
        String[] searchWords = searchInfo.split("\\s+");  // \\s表示空格、回车, +表示多个
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
            Segmentation oneSeg = segmentationDao.selectOneSeg(token.word);
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
        List<DataDto> recordDtoList = new ArrayList<>();
        for (SegToken token : segTokens) {

            //查出每个分词对应的caption
            log.info("分词为{}", token.word);
            Segmentation oneSeg = segmentationDao.selectOneSeg(token.word);
//            String s = String.valueOf(redisUtil.get("seg_" + token.word));
//            Segmentation oneSeg = null;
//            if (!s.equals(null) && !s.equals("null")) {
//                int id = Integer.parseInt(s)+1;
//                oneSeg = segmentationDao.selectOneById(id);
//            }
            Double tidif = new Double(0);
            if (oneSeg != null) {
                List<Integer> recordsIdList = recordSegService.queryRecordBySeg(oneSeg);//包含该分词的所有recordID

                for (Integer dataId : recordsIdList) {
                    if (set.contains(dataId)) {
                        continue;  // 若包含需要过滤的词 continue
                    }
                    if (!recordIds.contains(dataId)) {
                        DataDto recordDto = new DataDto();
                        recordIds.add(dataId);
                        //对于每个record对象 查询该分词对应的tidif加入recordDto
                        //分表查询
                        //找出需要的record，赋给recordDto
                        BeanUtils.copyProperties(dataDao.selectById(dataId), recordDto);


                        List<RecordSeg> recordSegList = new ArrayList<>();
                        RecordSeg recordSeg = recordSegDao.selectOneRecordSeg(dataId, oneSeg.getId());
                        tidif = recordSeg.getTidifValue();
                        recordSegList.add(recordSeg);

                        recordDto.setRecordSegs(recordSegList);
                        Double weight = recordDto.getWeight() + tidif;
                        recordDto.setWeight(weight);

                        recordDtoList.add(recordDto);
                    } else {
                        //找出对应的recordDto
                        for (DataDto dto : recordDtoList) {
                            if (dto.getId().equals(dataId)) {
                                List<RecordSeg> recordSegs = dto.getRecordSegs();
                                RecordSeg recordSeg = recordSegDao.selectOneRecordSeg(dataId, oneSeg.getId());
                                tidif = recordSeg.getTidifValue();
                                //更新对应recordDto的 recordSegs List
                                recordSegs.add(recordSeg);
                                dto.setRecordSegs(recordSegs);
                                //更新对应recordDto的weight
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
    public Boolean addRecord(Data data) {
        //文本信息加入data表
        dataDao.insertRecord(data);
        //分词处理
        String sentence = data.getCaption();
        List<SegToken> segTokens = segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
        List<Keyword> list = tfidfAnalyzer.analyze(sentence, 5);
        Integer recordId = data.getId();
        Double tidifValue = new Double(0);
        for (SegToken segToken : segTokens) {
            //对应tidif值
            for (Keyword keyword : list) {
                if (keyword.getName().equals(segToken.word)) {
                    tidifValue = keyword.getTfidfvalue();
                }
            }
            //分词信息加入分词表
            segmentationService.addSeg(segToken.word, recordId, tidifValue);
        }
        return true;
    }
}
