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

    private String strPlusOne(String str) {
        try {
            int i = Integer.parseInt(str);
            return String.valueOf(i + 1);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean addCurrentClick() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String key = simpleDateFormat.format(date);

        String today = statisticDao.getRecordByKey(key).getValue();
        statisticDao.setTodayClick(key,strPlusOne(today));

        String total = statisticDao.getRecordByKey("total_click").getValue();
        statisticDao.setTotalClick(strPlusOne(total));

        return true;
    }


    public int addUserCount() {
        String currentNum = statisticDao.getRecordByKey("user_count").getValue();
        return statisticDao.setUserCount(strPlusOne(currentNum));
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


    public void subOneUserCount() {
        String userCount = statisticDao.getRecordByKey("user_count").getValue();
        statisticDao.setKeyVal("user_count", String.valueOf(Integer.parseInt(userCount) - 1));
    }
}
