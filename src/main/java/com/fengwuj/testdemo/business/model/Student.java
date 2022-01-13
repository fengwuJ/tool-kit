package com.fengwuj.testdemo.business.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "student")
@Data
public class Student {

    private Long id;

    private String name;

    private Integer age;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;


}
