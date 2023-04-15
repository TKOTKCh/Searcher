package com.searchengine.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.searchengine.config.RabbitMQTopicConfig;
import com.searchengine.dao.DataDao;
import com.searchengine.dao.StatisticDao;
import com.searchengine.entity.Data;
import com.searchengine.entity.StatisticHistory;
import com.searchengine.entity.StatisticMessage;
import com.searchengine.entity.User;
import com.searchengine.service.impl.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private DataDao dataDao;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private StatisticDao statisticDao;

    @RabbitListener(queues = RabbitMQTopicConfig.SET_USER_COUNT_QUEUE)
    public void setUser(String message) {
        log.info("1接收消息：" + message);
        statisticDao.setUserCount(message);
    }

    @RabbitListener(queues = RabbitMQTopicConfig.SET_CLICK_QUEUE)
    public void setClick(String message) {
        log.info("2接收消息：" + message);
        StatisticHistory statisticMessage = JSONObject.parseObject(message, StatisticHistory.class);
        statisticDao.setKeyVal(statisticMessage.getKey(), statisticMessage.getValue());
    }

    @RabbitListener(queues = RabbitMQTopicConfig.SET_DATA_CLICK_COUNT_QUEUE)
    public void setDataClickById(String message) {
        log.info("3接收消息：" + message);
        dataDao.increDataByID(message);
    }


}
