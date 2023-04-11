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
        statisticDao.setTodayClick(key, strPlusOne(today));

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
        int step=1;

        for (int i = 0; i < dayCount; i++) {
            cal.add(Calendar.DATE, -step);
            String date = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
            StatisticHistory one = statisticDao.getRecordByKey(date);
            if (one == null) {
                statisticDao.put(date, "0");
                list.add(new StatisticHistory(date, "0"));
            } else {
                list.add(one);
            }
        }


        return list;
    }

    public StatisticHistory getTodayClick() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String key = simpleDateFormat.format(date);
        StatisticHistory one = statisticDao.getRecordByKey(key);

        if (one == null) {
            statisticDao.put(key, "0");
            return new StatisticHistory(key, "0");

        } else {
            return one;
        }

    }

    public StatisticHistory getUserCount() {
        return statisticDao.getRecordByKey("user_count");
    }

    public Map<String, Object> getAllStatistic() {
        StatisticHistory todayClick = getTodayClick();
        List<StatisticHistory> latest7Click = getLatestSevenDayClick();
        StatisticHistory userCount = getUserCount();
        Integer numberOfData = dataDao.getNumberOfData();
        Integer totalClick = new Integer(statisticDao.getRecordByKey("total_click").getValue());

        HashMap<String, Object> rs = new HashMap();
        rs.put("todayClick", todayClick);
        rs.put("seven_days", latest7Click);
        rs.put("userCount", userCount);
        rs.put("policyNumber", numberOfData);
        rs.put("total_click", totalClick);
        return rs;
    }


    public void subOneUserCount() {
        String userCount = statisticDao.getRecordByKey("user_count").getValue();
        statisticDao.setKeyVal("user_count", String.valueOf(Integer.parseInt(userCount) - 1));
    }
}
