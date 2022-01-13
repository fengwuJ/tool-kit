package com.fengwuj.testdemo.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ReflectProxyUtil {
    private ReflectProxyUtil(){}

    public static Object getValueByPropertyName(Object obj,String propertyName){
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        return beanWrapper.getPropertyValue(propertyName);
    }

    public static void setValueByPropertyName(Object obj,String propertyName,Object value){
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        beanWrapper.setPropertyValue(propertyName, value);
    }

}
