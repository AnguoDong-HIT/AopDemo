package com.dag.aop.demo;

import com.dag.aop.demo.service.TestService;
import com.dag.aop.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


import javax.annotation.Resource;

/**
 * @author: donganguo
 * @date: 2021/6/8 11:07 上午
 * @Description:
 */
@Slf4j
public class TestServiceTest extends TestBase {

    @Resource
    private TestService testService;

    @Resource
    private UserService userService;

    @Test
    public void testGetString() {
        System.out.println(testService.getSting());
    }

    @Test
    public void test() {
        log.info("测试log");
        System.out.println(userService.selectByPrimaryKey(3));;
    }
}