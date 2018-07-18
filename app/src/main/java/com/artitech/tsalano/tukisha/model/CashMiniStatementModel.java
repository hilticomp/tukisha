package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/07/11.
 */

public class CashMiniStatementModel {

    private String electricityTotal, telcoTotal, DSTVTotal, total,municipalityTotal;

    public CashMiniStatementModel(String electricityTotal, String telcoTotal, String total,String DSTVTotal,String municipalityTotal) {

        this.electricityTotal = electricityTotal;
        this.telcoTotal = telcoTotal;
        this.DSTVTotal = DSTVTotal;
        this.municipalityTotal=municipalityTotal;
        this.total = total;

    }

    public String getElectricityTotal() {
        return electricityTotal;
    }
    public String getMunicipalityTotal() {
        return municipalityTotal;
    }

    public String getDstvtotal() {
        return DSTVTotal;
    }

    public String getTelcoTotal() {
        return telcoTotal;
    }

    public String getTotal() {
        return total;
    }

}
