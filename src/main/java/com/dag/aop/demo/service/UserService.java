package com.dag.aop.demo.service;

import com.dag.aop.demo.annotation.CatAopTest;
import com.dag.aop.demo.mapper.UserMapper;
import com.dag.aop.demo.domain.po.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: donganguo
 * @date: 2021/6/15 5:42 下午
 * @Description:
 */
@Service
@Transactional
public class UserService {
    @Resource
    private UserMapper userMapper;

    @CatAopTest
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @CatAopTest
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
