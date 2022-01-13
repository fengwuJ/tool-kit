package com.fengwuj.testdemo.word.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 按书签替换，键值对--散列值使用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WordBookMark {
    //word中书签的值
    String value();

    int fontSize() default -1;
}
