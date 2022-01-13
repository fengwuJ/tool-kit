package com.fengwuj.testdemo.excel;


 public class SheetConf {
    private int index;
    private CellConf[] cellConfs;

    public SheetConf(int index, CellConf[] cellConfs) {
        this.index = index;
        this.cellConfs = cellConfs;
    }

    public int getIndex() {
        return index;
    }

    public CellConf[] getCellConfs() {
        return cellConfs;
    }
}

