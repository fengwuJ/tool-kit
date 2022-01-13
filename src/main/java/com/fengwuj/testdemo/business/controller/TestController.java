package com.fengwuj.testdemo.business.controller;

import com.fengwuj.testdemo.business.mapper.StudentMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
public class TestController {

    @Resource
    private StudentMapper studentMapper;

    @GetMapping("/shardingjdbc")
    public Object ops(){
        return studentMapper.selectAll();
    }

}
