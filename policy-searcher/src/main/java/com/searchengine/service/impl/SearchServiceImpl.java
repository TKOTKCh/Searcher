package com.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.SegmentDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.Segment;
import com.searchengine.service.SearchService;
import com.searchengine.utils.Trie;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private DataDao dataDao;

    @Autowired
    private SegmentDao segmentDao;

    private Trie trie;

    @PostConstruct
    public void init() {
        List<Segment> segmentations = segmentDao.getAllSegByTableName("segment_datatitle");
        this.trie = new Trie();
        for (Segment segmentation : segmentations) {
            String word = segmentation.getWord();
            this.trie.add(word);
        }
    }

    // 搜索业务
    @Override
    public List<Data> getDataByKeyword(String tableName,String keyword, int pageSize, int pageNum) {
        String segmentname="segment_"+tableName;
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();

        // 对输入的关键字进行分词
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process(keyword, JiebaSegmenter.SegMode.INDEX);

        boolean flag = true;
        for (SegToken segToken : segTokens) {
            Segment segment=segmentDao.getOneSeg(segmentname,segToken.word);
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
//        List<Data> datas = dataDao.getDataBySplit(sql, pageSize, offset);
        List<Data> dataList = dataDao.getCompleteDataBySplit(sql, pageSize, offset);
        return dataList;
    }

    @Override
    public List<String> complete(String query) {
        List<String> completion = trie.getRelatedWords(query);
        return completion;
    }

}
