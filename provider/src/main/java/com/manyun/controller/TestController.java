package com.manyun.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api("测试的controller")
@RestController
@RefreshScope
public class TestController {

    @ApiOperation("providerHello")
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
    @Value("${server.port}")
    private String index;
    @GetMapping("/index")
    public String index(){
        return "当前端口："+this.index;
    }

    @Value("${name:null}")
    private String name;
    @GetMapping("/name")
    @ApiOperation("getName")
    public String name(){
        return name;
    }
    static  {
        List<String> stringList = new ArrayList<String>();
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");
    }
    @PostMapping("/removeList")
    @ApiOperation("removeList")
    public List<String> getList(List<String> stringList){
        stringList.remove(1);
        return stringList;
    }
}