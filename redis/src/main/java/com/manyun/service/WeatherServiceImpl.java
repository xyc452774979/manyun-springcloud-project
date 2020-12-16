package com.manyun.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manyun.entity.WeatherData;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class WeatherServiceImpl implements WeatherService{

    @Autowired
    @Qualifier(value = "remoteRestTemplate")
    RestTemplate restTemplate;
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    String uri = "http://wthrcdn.etouch.cn/weather_mini?city=青岛";

    @Override
    public WeatherData getWeather() {

        String body = null;
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        //key就是uri,如果redis有这个key，就从缓存中获取，没有的话，就去请求
        if(stringRedisTemplate.hasKey(uri)){
            //redis缓存数据
            body = ops.get(uri);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                WeatherData data = objectMapper.readValue(body, WeatherData.class);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //如果缓存中没有，需要取出数据，并存入缓存
            ResponseEntity<String> weatherData = restTemplate.getForEntity(uri, String.class);
            //使用ObjectMapper进行处理
            ObjectMapper mapper = new ObjectMapper();
            if(weatherData.getStatusCodeValue() == 200){
//                body = weatherData.getBody();
                body = "{\"data\":{\"yesterday\":{\"date\":\"30日星期四\",\"high\":\"高温 26℃\",\"fx\":\"东南风\",\"low\":\"低温 22℃\",\"fl\":\"<![CDATA[2级]]>\",\"type\":\"晴\"},\"city\":\"青岛\",\"forecast\":[{\"date\":\"31日星期五\",\"high\":\"高温 26℃\",\"fengli\":\"<![CDATA[3级]]>\",\"low\":\"低温 23℃\",\"fengxiang\":\"南风\",\"type\":\"雷阵雨\"},{\"date\":\"1日星期六\",\"high\":\"高温 26℃\",\"fengli\":\"<![CDATA[3级]]>\",\"low\":\"低温 24℃\",\"fengxiang\":\"南风\",\"type\":\"雷阵雨\"},{\"date\":\"2日星期天\",\"high\":\"高温 26℃\",\"fengli\":\"<![CDATA[3级]]>\",\"low\":\"低温 24℃\",\"fengxiang\":\"南风\",\"type\":\"雷阵雨\"},{\"date\":\"3日星期一\",\"high\":\"高温 28℃\",\"fengli\":\"<![CDATA[4级]]>\",\"low\":\"低温 25℃\",\"fengxiang\":\"南风\",\"type\":\"小雨\"},{\"date\":\"4日星期二\",\"high\":\"高温 27℃\",\"fengli\":\"<![CDATA[3级]]>\",\"low\":\"低温 24℃\",\"fengxiang\":\"东南风\",\"type\":\"阴\"}],\"ganmao\":\"感冒低发期，天气舒适，请注意多吃蔬菜水果，多喝水哦。\",\"wendu\":\"26\"},\"status\":1000,\"desc\":\"OK\"}";
//                JSONObject json = JSONObject.parseObject(body);
//                body = json.toString();
                WeatherData weatherData2 = null;
                try {
//                    weatherData2 = mapper.readValue(body, WeatherData.class);
                    ops.set(uri, body, 5*60*1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return weatherData2;
            }
        }
        return null;
    }

    public String testRedis(String value){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        System.out.println("=========开始查询============");
        if(stringRedisTemplate.hasKey("value")){
            String body = ops.get("value");
            System.out.printf("----------1--------");
            return body;
        }else{
            ops.set(
                    "value",
                    value,
                    60*1000
            );
            System.out.printf("----------2--------");
            return value;
        }
    }



}
