package com.fengwuj.testdemo.excel;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class CellConf {
    private int rowIndex;
    private int cellIndex;
    private DataType type;
    private String property;

    public CellConf(int rowIndex, int cellIndex, DataType type, String property) {
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
        this.type = type;
        this.property = property;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public DataType getType() {
        return type;
    }

    public String getProperty() {
        return property;
    }
}
