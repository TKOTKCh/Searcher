package com.searchengine.springboot;

import com.searchengine.entity.User;
import com.searchengine.utils.RedisUtil_db0;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestJedis {

    @Autowired
    private RedisUtil_db0 redisUtil;

    @Test
    public void test() {
        System.out.println(redisUtil.get("login2")); ;
    }

}
