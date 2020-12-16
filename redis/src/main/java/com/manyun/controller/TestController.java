package com.manyun.controller;

import com.manyun.entity.WeatherData;
import com.manyun.service.RedisTest;
import com.manyun.service.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/redis")
public class TestController {
    @Autowired
    private WeatherServiceImpl weatherServiceImpl;
    @Autowired
    private RedisTest redisTest;

    @GetMapping("/getWeather")
    public WeatherData getWeather(){
        return weatherServiceImpl.getWeather();
    }
    @GetMapping("/testRedis/{value}")
    public String testRedis(@PathVariable("value") String value){
        return weatherServiceImpl.testRedis(value);
    }

    @GetMapping("/stringType")
    public void stringType(){
        redisTest.StringType();
    }
    @GetMapping("/hashType")
    public void hashType(){
        redisTest.hashType();
    }
    @GetMapping("/linkedType")
    public void linkedType(){
        redisTest.linkedType();
    }
    @GetMapping("/setType")
    public void setType(){
        redisTest.setType();
    }
    @GetMapping("/zsetType")
    public void zsetType(){
        redisTest.zsetType();
    }
    @GetMapping("/expire")
    public void expire() throws ParseException {
        redisTest.expire();
    }
}
