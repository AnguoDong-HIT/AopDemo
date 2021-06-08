package com.dag.aop.demo.service;

import com.dag.aop.demo.annotation.CatAopTest;
import org.springframework.stereotype.Service;

/**
 * @author: donganguo
 * @date: 2021/6/8 10:55 上午
 * @Description:
 */
@Service
public class TestService {
    @CatAopTest
    public String getSting() {
        return "TestService";
    }
}
