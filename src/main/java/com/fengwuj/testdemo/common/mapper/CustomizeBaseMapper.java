package com.fengwuj.testdemo.common.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
import tk.mybatis.mapper.provider.SpecialProvider;

public interface CustomizeBaseMapper<T> extends BaseMapper<T>, InsertListMapper<T>, ExampleMapper<T> {


    @Options(
            useGeneratedKeys = true
    )
    @InsertProvider(
            type = SpecialProvider.class,
            method = "dynamicSQL"
    )
    @Override
    int insert(T var1);

    @Options(
            useGeneratedKeys = true
    )
    @InsertProvider(
            type = SpecialProvider.class,
            method = "dynamicSQL"
    )
    @Override
    int insertSelective(T var1);
}
