package com.daon.admin_onorder.model;

public class PrintOrderModel {
    String table;
    String order;
    String time;
    String printStatus;

    public PrintOrderModel(){

    }
    public PrintOrderModel(String table, String order, String time, String printStatus) {
        this.table = table;
        this.order = order;
        this.time = time;
        this.printStatus = printStatus;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
