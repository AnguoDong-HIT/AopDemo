package com.dag.aop.demo.service;

import com.dag.aop.demo.annotation.CatAopTest;
import com.dag.aop.demo.mapper.UserMapper;
import com.dag.aop.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: donganguo
 * @date: 2021/6/15 5:42 下午
 * @Description:
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @CatAopTest
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }
}
