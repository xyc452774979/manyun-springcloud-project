package com.manyun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * String - 字符串类型的操作方式
     * redisTemplate.opsForValue()
     */
    public void StringType(){
        //	改为String序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        ValueOperations<String,String> ops = redisTemplate.opsForValue();
        //	redis命令：set key value
        ops.set("age","22");
        // redis命令：get key
        String value = ops.get("age");
        System.out.println("age:"+value);
        // redis命令：mset key value key value ...
        Map<String,String> map = new HashMap<String, String>();
        map.put("key1","a");
        map.put("key2","b");
        map.put("key3","c");
        ops.multiSet(map);
        // redis命令：mget key key key...
        List<String> list = new ArrayList<String>();
        list.add("key1");
        list.add("key2");
        list.add("key3");
        List<String> getList = ops.multiGet(list);
        for(String keys : getList){
            System.out.println(keys);
        }
        boolean boo = redisTemplate.delete("key");
        System.out.println(boo+"");
        Long size = ops.size("age");
        System.out.println(size+"");
        // redis命令：getset key value
        String v = ops.getAndSet("age","30");
        System.out.println(v);
        // redis命令：getrange key start end - 可能会因为序列化的原因造成长度不准
        String age1 = ops.get("age",0,1);
        System.out.println(age1);
        // redis命令：append - 可能会因为序列化的原因造成长度不准
        int age2 = ops.append("age","40");
        System.out.println(age2);
        // redis命令：incr key - 自增 - 可能会因为序列化的原因造成长度不准
        Long long1 = ops.increment("age",10);
        System.out.println(long1);
        // redis命令：decr key - 自减
        Long long2 = ops.increment("age",-10);
        Long decr = redisTemplate.getConnectionFactory().getConnection().decr("age".getBytes());
        System.out.println("decr --> " + decr);
    }
    /**
     * Hash数据类型的操作
     */
    public void hashType(){
        HashOperations<String, String , String> hops = redisTemplate.opsForHash();
        // redis命令：mset key field value
        hops.put("person","age","22");
        hops.put("person","name","李三");
        // redis命令：mget key field
        String value = hops.get("person","name");
        System.out.println("mget-->" + value);
        // redis命令：hmset key field1 value1 field2 value2 ...
        Map<String, String> map = new HashMap<String, String>();
        map.put("bookname", "Java精通之路");
        map.put("price", "100.99");
        hops.putAll("book",map);
        // redis命令：hmget key field1 field2 ...
        List<String> list = new ArrayList<String>();
        list.add("bookname");
        list.add("price");
        List books = hops.multiGet("book",list);
        System.out.println("hmget-->" + books);
        // redis命令：del key
        hops.delete("book","price");
        // redis命令：hdel key field1 field2...
        redisTemplate.opsForHash().delete("book", "bookname", "price");
        // redis命令：hexists key field
        Boolean boo = hops.hasKey("book","bookname");
        System.out.println("hexists-->" + boo);
        // redis命令：hlen key
        Long size = hops.size("person");
        System.out.println("hlen-->" + size);
        // redis命令：hkeys key - 展示key对应的所有字段名称
        Set<String> set = redisTemplate.opsForHash().keys("person");
        System.out.println("hkeys-->" + set);
        // redis命令：hvals key - 展示key对应的所有字段的值
        List<String> values = redisTemplate.opsForHash().values("person");
        System.out.println("hvals-->" + values);
        // redis命令：hgetall key - field and value
        Map<String,String> personmap = hops.entries("person");
        System.out.println("hgetall-->" + personmap);
    }

    /**
     * 链表数据结构
     */
    public void linkedType(){
        ListOperations<String, String> lops = redisTemplate.opsForList();
        /*// redis命令：lpush key value1 value2...
        redisTemplate.opsForList().leftPush("book","c++");
        redisTemplate.opsForList().leftPushAll("book","c","java","phython");
        // redis命令：rpush key value1 value2
        lops.rightPush("book","mysql");
        lops.rightPushAll("book","oracle","sqlserver");
        // redis命令：lindex key index
        String book0 = (String)redisTemplate.opsForList().index("book",0);
        System.out.println("lindex-->" + book0);
        // redis命令：llen key
        Long booklen = redisTemplate.opsForList().size("book");
        System.out.println("llen-->" + booklen);
        // redis命令：lpop key
        String leftBook = lops.leftPop("book");
        System.out.println("lpop-->" + leftBook);
        // redis命令：rpop key
        String rightBook = lops.rightPop("book");
        System.out.println("rpop-->" + rightBook);
        // redis命令：linsert key before|after oldnode newnode*/
        lops.leftPush("book","java","phthod");
        lops.rightPush("book","java","jquery");
        // redis命令：lrange key start end
        List rangeList = lops.range("book",0,lops.size("book")-1);
        System.out.println("lrange-->" + rangeList);
        // redis命令：lset key index value
        lops.set("book",0,"db");
        // redis命令：ltrim key start end
        lops.trim("book",1,3);
        // redis命令：lrange key start end
        List rangeList2 = lops.range("book",0,lops.size("book")-1);
        System.out.println("lrange-->" + rangeList2);
    }
    /**
     * 集合操作
     */
    public void setType(){
        // redis命令：sadd
        redisTemplate.opsForSet().add("user","小明","小红","小刚");
        // redis命令：scard
        Long person = redisTemplate.opsForSet().size("user");
        System.out.println("scard-->" + person);
        // redis命令：smembers
        Set set = redisTemplate.opsForSet().members("user");
        System.out.println("smembers-->" + set);
    }
    /**
     * 有序集合
     */
    public void zsetType(){
        redisTemplate.opsForZSet().add("zoo","狮子",1.2);
        redisTemplate.opsForZSet().add("zoo","老鼠",2.0);
        redisTemplate.opsForZSet().add("zoo","大象",2.1);
        Set<String> zoo = redisTemplate.opsForZSet().range("zoo",0,redisTemplate.opsForZSet().size("zoo")-1);
        System.out.println(zoo);
    }

    /**
     * 超时key无效
     * @throws ParseException
     */
    public void expire() throws ParseException {
        redisTemplate.opsForValue().set("name","小明");
        //设置超时时间 - 5 ~ 10分钟
        redisTemplate.expire("name",10, TimeUnit.SECONDS);
        //设置超时时间到指定的时间
        String time = "2020-08-04 16:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(time);
        redisTemplate.expireAt("name",date);
        //移除超时时间
        redisTemplate.persist("name");
        //获得还能活多久
        redisTemplate.getExpire("name");
        String name = (String)redisTemplate.opsForValue().get("name");
        System.out.println(name);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name);
    }
}
