package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/07/11.
 */

public class CashMiniStatementModel {

    private String electricityTotal, telcoTotal, total;

    public CashMiniStatementModel(String electricityTotal, String telcoTotal, String total) {

        this.electricityTotal = electricityTotal;
        this.telcoTotal = telcoTotal;
        this.total = total;

    }

    public String getElectricityTotal() {
        return electricityTotal;
    }

    public String getTelcoTotal() {
        return telcoTotal;
    }

    public String getTotal() {
        return total;
    }

}
