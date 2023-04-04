package com.searchengine.dao;
import com.searchengine.entity.DataNoBody;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataNoBodyDao {
    // 通过 limit & offset 获取一些Data
    List<DataNoBody> getAllDataNoBodys();
//    List<DataNoBody> getSomeDataNoBodys(@Param("limit") int limit, @Param("offset") int offset);
//    List<DataNoBody> getDataNoBodyBySplit(@Param("sql")String sql, @Param("pageSize")int pageSize, @Param("offset")int offset);
}
