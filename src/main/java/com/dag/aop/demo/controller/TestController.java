package com.dag.aop.demo.controller;

import com.dag.aop.demo.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: donganguo
 * @date: 2021/6/8 10:57 上午
 * @Description:
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @RequestMapping("/test")
    public String getString() {
        return testService.getSting();
    }
}
