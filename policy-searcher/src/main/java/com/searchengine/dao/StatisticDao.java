package com.searchengine.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.searchengine.entity.StatisticHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticDao extends BaseMapper<StatisticHistory> {

    int put(@Param("key")String key, @Param("value") String value);

    int addOneUserCount();

    int removeOneUserCount();

    int addOneTotalClick();

    int addOneTodayClick(@Param("key") String date);

    StatisticHistory getRecordByKey(@Param("key") String key);
}
