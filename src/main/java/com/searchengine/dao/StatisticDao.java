package com.searchengine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.searchengine.entity.StatisticHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticDao extends BaseMapper<StatisticHistory> {

    int put(@Param("key")String key, @Param("value") String value);

    int setUserCount(@Param("value")String value);

    int setKeyVal(@Param("key") String key, @Param("value") String value);


    int setTotalClick(@Param("value") String value);

    int setTodayClick(@Param("key") String date, @Param("value") String value);

    StatisticHistory getRecordByKey(@Param("key") String key);

    void increTotalClick();
}
