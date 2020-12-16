package com.manyun.controller;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageListener implements MessageListener {
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取渠道名称
        System.out.println("渠道名称："+ new String(pattern));
        //获得消息
        byte[] body = message.getBody();
        //获得值序列化转换器
        String msg= (String)redisTemplate.getValueSerializer().deserialize(body);
        System.out.println("消息为："+msg);
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
