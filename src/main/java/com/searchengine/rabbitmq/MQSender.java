package com.searchengine.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 *
 * @author: LC
 * @date 2022/3/7 7:42 下午
 * @ClassName: MQSender
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendUserMsg(String message) {
        log.info("1发送消息" + message);
        rabbitTemplate.convertAndSend("topic_exchange", "user.set", message);
    }

    public void sendClickMsg(String message) {
        log.info("2发送消息" + message);
        rabbitTemplate.convertAndSend("topic_exchange", "days.click.set", message);
    }

    public void sendDataClickCountMsg(String message) {
        log.info("3发送消息" + message);
        rabbitTemplate.convertAndSend("topic_exchange", "data.count.set", message);
    }

}
