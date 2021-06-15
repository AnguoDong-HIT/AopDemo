package com.dag.aop.demo.controller;

import com.dag.aop.demo.pojo.User;
import com.dag.aop.demo.service.TestService;
import com.dag.aop.demo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Resource
    private UserService userService;

    @RequestMapping("/test")
    public String getString() {
        return testService.getSting();
    }

    @PostMapping("/insert")
    public int insertSelective(User record) {
        return userService.insertSelective(record);
    }
}
