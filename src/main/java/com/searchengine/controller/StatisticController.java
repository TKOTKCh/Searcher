package com.searchengine.controller;

import com.searchengine.common.Constants;
import com.searchengine.common.Result;
import com.searchengine.dao.UserDao;
import com.searchengine.entity.StatisticHistory;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import com.searchengine.service.impl.StatisticService;
import com.searchengine.utils.RedisUtil_db0;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("statistic")
public class StatisticController{

    @Autowired
    StatisticService statisticService;
    @Autowired
    RedisUtil_db0 redisUtil;


    @GetMapping("today_click")
    public Result getTodayClick() {
        StatisticHistory todayClick = (StatisticHistory) redisUtil.get("click-"+statisticService.getTodayDate());
        return Result.success(todayClick);
    }

    @GetMapping("all_statistic")
    public Result getAllStatistic() {

        return Result.success(statisticService.getAllStatistic());
    }


}
