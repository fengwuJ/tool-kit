package com.fengwuj.testdemo;

import com.fengwuj.testdemo.business.mapper.StudentMapper;
import com.fengwuj.testdemo.business.model.Student;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class TestdemoApplicationTests {

	@Autowired
	private StudentMapper studentMapper;

	@Test
	void contextLoads() throws IntrospectionException {
		List<Student> students = studentMapper.selectAll();
		System.out.println();
	}
}
