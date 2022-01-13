package com.fengwuj.testdemo.excel;

import java.lang.annotation.*;

/**
 * @Description ExcelProperty
 * @Author bopeng@deloitte.com.cn
 * @Date 2021/08/19 16:03
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

    String value() default "key";
}
