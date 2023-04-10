package com.searchengine.dao;
import com.searchengine.entity.DataNoBody;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataNoBodyDao {

    List<DataNoBody> getAllDataNoBodys(@Param("table")String name);

}
