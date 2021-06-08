package com.dag.aop.demo;

import com.dag.aop.demo.service.TestService;
import org.junit.Test;


import javax.annotation.Resource;

/**
 * @author: donganguo
 * @date: 2021/6/8 11:07 上午
 * @Description:
 */
public class TestServiceTest extends TestBase {

    @Resource
    private TestService testService;

    @Test
    public void testGetString() {
        System.out.println(testService.getSting());
    }
}