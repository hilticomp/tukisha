package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/05/23.
 */

public class MuniEkurhuleniVoucherModel {

    private String receiptHeader, distributer, date, receiptNumber, meterNumber, sgc, krn, ti, description, energyKWh, amount,
    tokenNumber, balance, excise, actualReceipt;


    public MuniEkurhuleniVoucherModel(String receiptHeader, String distributer, String date, String receiptNumber,
                                      String meterNumber, String sgc, String krn, String ti, String description, String energyKWh, String amount,
                                      String tokenNumber, String balance, String excise, String actualReceipt) {

        this.meterNumber = meterNumber;
        this.distributer = distributer;
        this.receiptHeader = receiptHeader;
        this.distributer = receiptHeader;
        this.date = date;
        this.receiptNumber = receiptNumber;
        this.meterNumber = meterNumber;
        this.sgc = sgc;
        this.krn = krn;
        this.ti = ti;
        this.description = description;
        this.energyKWh = energyKWh;
        this.amount = amount;
        this.tokenNumber = tokenNumber;
        this.balance = balance;
        this.excise = excise;
        this.actualReceipt = actualReceipt;

    }

    public String getReceiptHeader() {
        return receiptHeader;
    }

    public String getDistributer() {
        return distributer;
    }

    public String getDate() {
        return date;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public String getSGC() {
        return sgc;
    }

    public String getKrn() {
        return krn;
    }

    public String getTI() {
        return ti;
    }

    public String getDescription() {
        return description;
    }

    public String getEnergyKWh() {
        return energyKWh;
    }

    public String getAmount() {
        return amount;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public String getActualReceipt() {
        return actualReceipt;
    }

    public String getExcise() {
        return excise;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "meternumber=" + meterNumber;
    }
}
