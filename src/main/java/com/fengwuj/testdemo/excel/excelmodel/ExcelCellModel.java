package com.fengwuj.testdemo.excel.excelmodel;

import com.fengwuj.testdemo.excel.support.ExcelDef;
import lombok.Data;

import java.util.List;

/**
 * @Date 2021/08/23 14:41
 */
@Data
public class ExcelCellModel extends ExcelBaseModel{

    /**
     * 员工姓名
     */
    private ExcelDef empName;

    /**
     * 工号
     */
    private ExcelDef empNo;

    /**
     * 退休原因
     */
    private ExcelDef retireReason;

    /**
     * 退休日期
     */
    private ExcelDef retireTime;

    /**
     * 截止日期
     */
    private ExcelDef endTime;

    /**
     * 下载日期
     */
    private ExcelDef downloadTime;

}
