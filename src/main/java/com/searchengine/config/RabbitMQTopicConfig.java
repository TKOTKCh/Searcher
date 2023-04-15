package com.searchengine.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类-Topic
 *
 * @author: LC
 * @date 2022/3/8 5:24 下午
 * @ClassName: RabbitMQTopicConfig
 */
@Configuration
public class RabbitMQTopicConfig {

    /**
     * 交换机名称
     */
    public static final String TOPIC_EXCHANGE_NAME = "topic_exchange";

    /**
     * 定义bindingKey,模糊匹配
     */
    public static final String BINGDING_KEY_USER = "user.*";

    public static final String BINGDING_KEY_CLICK = "days.click.*";

    public static final String BINGDING_KEY_DATA_CLICK = "data.count.*";

    /**
     * 两个queue名字
     */
    public static final String SET_USER_COUNT_QUEUE = "setUserCountQueue";
    public static final String SET_CLICK_QUEUE = "setClickQueue";
    public static final String SET_DATA_CLICK_COUNT_QUEUE = "setDataClickCountQue";


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue userCountQueue() {
        // 支持持久化
        return new Queue(SET_USER_COUNT_QUEUE, true);
    }

    @Bean
    public Queue clickQueue() {
        // 支持持久化
        return new Queue(SET_CLICK_QUEUE, true);
    }


    @Bean
    public Queue dataClickCountQueue() {
        // 支持持久化
        return new Queue(SET_DATA_CLICK_COUNT_QUEUE, true);
    }
    /**
     * 绑定交交换机和队列
     * topic_queue1的bindingKey为：*.orange.*
     * topic_queue2的bindingKey有两个：*.*.rabbit和lazy.#
     *
     * @return
     */
    @Bean
    public Binding userBinding() {
        return BindingBuilder
                .bind(userCountQueue())
                .to(topicExchange())
                .with(BINGDING_KEY_USER);
    }

    @Bean
    public Binding clickBinding() {
        return BindingBuilder
                .bind(clickQueue())
                .to(topicExchange())
                .with(BINGDING_KEY_CLICK);
    }

    @Bean
    public Binding dataClickCountBinding() {
        return BindingBuilder
                .bind(dataClickCountQueue())
                .to(topicExchange())
                .with(BINGDING_KEY_DATA_CLICK);
    }

}
