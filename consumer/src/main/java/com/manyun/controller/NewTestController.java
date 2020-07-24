package com.manyun.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Api("测试的controller")
@RestController
public class NewTestController {

    @Autowired
    private RestTemplate restTemplate;
    @ApiOperation("hello")
    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }

    @ApiOperation("providerHello")
    @RequestMapping(value = "/providerHello", method = RequestMethod.GET)
    public String providerHello() {
        return restTemplate.getForObject("http://service-provider/hello",String.class);
    }
    public String testIndex(){
        return restTemplate.getForObject("http://service-provider/index",String.class);
    }
}
