package com.fengwuj.testdemo.excel.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelDef {

    /**
     * excel行号
     */
    private Integer xIndex;

    /**
     * excel列号
     */
    private Integer yIndex;

    /**
     * 单元格的值
     */
    private String value;

}
