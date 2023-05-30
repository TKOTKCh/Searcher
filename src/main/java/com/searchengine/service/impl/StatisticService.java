package com.searchengine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.StatisticDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.StatisticHistory;
import com.searchengine.rabbitmq.MQSender;
import com.searchengine.utils.DateUtil;
import com.searchengine.utils.RedisUtil_db0;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StatisticService implements InitializingBean {

    @Autowired
    private StatisticDao statisticDao;

    @Autowired
    private DataDao dataDao;
    @Autowired
    RedisUtil_db0 redisUtil;
    @Autowired
    MQSender mqSender;

    private String todayDate;

    private String strPlusOne(String str) {
        try {
            int i = Integer.parseInt(str);
            return String.valueOf(i + 1);
        } catch (Exception e) {
            return null;
        }
    }


    public int addUserCountToDB() {
        String currentNum = statisticDao.getRecordByKey("user_count").getValue();
        return statisticDao.setUserCount(strPlusOne(currentNum));
    }

    public int setUserCountToDB(Integer integer) {
        return 1;
    }


//    public List<StatisticHistory> getLatestSevenDayClick() {
//
//    }

//    public StatisticHistory getTodayClick() {
//
//    }

    public StatisticHistory getUserCount() {
        return statisticDao.getRecordByKey("user_count");
    }

    public Map<String, Object> getAllStatistic() {
        List<StatisticHistory> latest7Click = (List<StatisticHistory>) redisUtil.get("click-7-days-" + DateUtil.getTodayDate());
        Integer userCount =(Integer)  redisUtil.get("user-count");
        Integer numberOfData = (Integer) redisUtil.get("data-num");
        Integer totalClick = (Integer) redisUtil.get("total-click");
        Integer todayClick = (Integer) redisUtil.get("click-" + DateUtil.getTodayDate());


        HashMap<String, Object> rs = new HashMap();
        rs.put("todayClick", todayClick);
        rs.put("seven_days", latest7Click);
        rs.put("userCount", userCount);
        rs.put("policyNumber", numberOfData);
        rs.put("total_click", totalClick);
        return rs;
    }


    public void subOneUserCount() {
        Long stock = redisUtil.decr("user-count",1);
    }

    public void addOneUserCount() {
        Long stock = redisUtil.incr("user-count",1);
        mqSender.sendUserMsg(stock.toString());

    }

    public List<Data> getHotData() {
        return  (List<Data>) redisUtil.get("hot");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.todayDate = DateUtil.getTodayDate();

        StatisticHistory todayClick = statisticDao.getRecordByKey(this.todayDate);
        if (todayClick == null) {
            statisticDao.put(this.todayDate, "0");
            todayClick= new StatisticHistory(this.todayDate, "0");
        }
        redisUtil.set("click-" + todayClick.getKey(), Integer.parseInt(todayClick.getValue()));


        List<StatisticHistory> list = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int dayCount = 7;
        int step=1;
        for (int i = 0; i < dayCount; i++) {
            cal.add(Calendar.DATE, -step);
            String dateStr = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
            StatisticHistory one = statisticDao.getRecordByKey(dateStr);
            if (one == null) {
                statisticDao.put(dateStr, "0");
                list.add(new StatisticHistory(dateStr, "0"));
            } else {
                list.add(one);
            }
        }
        List<StatisticHistory> latestSevenDayClick = list;
        redisUtil.set("click-7-days-"+todayClick.getKey(), latestSevenDayClick);

        StatisticHistory userCount = statisticDao.getRecordByKey("user_count");
        redisUtil.set("user-count",Integer.parseInt(userCount.getValue()) );

        Integer numberOfData = dataDao.getNumberOfData();
        redisUtil.set("data-num", numberOfData);

        Integer totalClick = new Integer(statisticDao.getRecordByKey("total_click").getValue());
        redisUtil.set("total-click", totalClick);

        List<Data> hotData = dataDao.getHotdata();
        redisUtil.set("hot", hotData);
    }
}
