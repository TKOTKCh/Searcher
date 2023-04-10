package com.searchengine.service.impl;

import com.searchengine.dao.DataDao;
import com.searchengine.dao.StatisticDao;
import com.searchengine.entity.StatisticHistory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticService {

    @Autowired
    private StatisticDao statisticDao;

    @Autowired
    private DataDao dataDao;

    public int addCurrentClick() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String key = simpleDateFormat.format(date);
        return statisticDao.addOneTodayClick(key);
    }

    public int addUserCount() {
        return statisticDao.addOneUserCount();
    }

    public List<StatisticHistory> getLatestSevenDayClick() {
        Calendar cal = Calendar.getInstance();

        List<StatisticHistory> list = new ArrayList<>();

        int dayCount = 7;

        for (int i = 1; i < dayCount; i++) {
            cal.add(Calendar.DATE, -i);
            String date = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
            StatisticHistory one = statisticDao.getRecordByKey(date);
            list.add(one);
        }
        return list;
    }

    public StatisticHistory getTodayClick() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String key = simpleDateFormat.format(date);
        return statisticDao.getRecordByKey(key);
    }

    public StatisticHistory getUserCount() {
        return statisticDao.getRecordByKey("user_count");
    }

    public Map<String ,Object> getAllStatistic() {
        StatisticHistory todayClick = getTodayClick();
        List<StatisticHistory> latest7Click= getLatestSevenDayClick();
        StatisticHistory userCount = getUserCount();
        Integer numberOfData = dataDao.getNumberOfData();

        HashMap<String ,Object> rs = new HashMap();
        rs.put("todayClick", todayClick);
        rs.put("7days", latest7Click);
        rs.put("userCount", userCount);
        rs.put("policyNumber", numberOfData);
        return rs;
    }




}
