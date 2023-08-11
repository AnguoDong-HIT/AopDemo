package com.dag.aop.demo.mapper;

import com.dag.aop.demo.domain.po.Sku;
import com.dag.aop.demo.domain.example.SkuExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuMapper {
    int deleteByPrimaryKey(String id);

    int insert(Sku record);

    int insertSelective(Sku record);

    List<Sku> selectByExample(SkuExample example);

    Sku selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);
}