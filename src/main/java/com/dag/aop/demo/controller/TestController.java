package com.dag.aop.demo.controller;

import com.dag.aop.demo.domain.po.User;
import com.dag.aop.demo.service.TestService;
import com.dag.aop.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user/{id}")
    public User selectByPrimaryKey(@PathVariable Integer id) {
        return userService.selectByPrimaryKey(id);
    }
}
