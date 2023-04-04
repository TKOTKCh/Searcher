package com.searchengine.dao;

import com.searchengine.entity.Segment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface SegmentDao {
    //查看所有分词
    List<Segment> selectAllSeg();

    //加入新分词
    int insertSeg(String word);

    //查询单个分词对应的id
    Segment selectOneSeg(String word);

    //根据id查询
    Segment selectOneById(@Param("id") int id);

    //查询最大id
    int getMaxId();

    //批量插入分词
    boolean insertBatchSeg(@org.apache.ibatis.annotations.Param("segs") List<String> segs);

    List<Segment> getAllByWords(String word);

    List<Segment> getAllSegByTableName(@Param("tableName") String tableName);
    List<Segment> getAllSeg();

    // 通过word获取一个Segment
    // 通过word获取一个Segment
    Segment getOneSeg(@Param("table")String table,@Param("word") String word);

    boolean createSegTable(@Param("table") String table);
}
