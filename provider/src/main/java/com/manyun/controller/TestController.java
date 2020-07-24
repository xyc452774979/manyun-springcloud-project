package com.manyun.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("测试的controller")
@RestController
public class TestController {

    @ApiOperation("providerhello")
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

}