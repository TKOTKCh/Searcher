package com.searchengine.dao;

import com.searchengine.entity.Data;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataDao {

    List<Data> selectAllRecords();

    List<Data> selectPartialRecords(@Param("limit") int limit, @Param("offset") int offset);

    List<Data> selectRecordsByWord(String word);

    Data selectById(@Param("id") int id);

    int insertRecord(Data data);

    List<Data> getDataBySplit(@Param("sql")String sql, @Param("pageSize")int pageSize, @Param("offset")int offset);
}
