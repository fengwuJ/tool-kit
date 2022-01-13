package com.fengwuj.testdemo.word.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Table表单--单元格属性使用使用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WordTableCell {
    //cell的位置，纵坐标从0开始
    int cellIndex() default -1;

    //cell的位置，横坐标，从0开始
    int rowIndex() default -1;

    int fontSize() default -1;
}
