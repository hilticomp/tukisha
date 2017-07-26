package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/05/23.
 */

public class VoucherModel {

    private String meternumber,receiptHeader,distributer,vatNumber,operatorMessage,custMessage,
            address, date, dateOfPurchase, receiptNumber, clientID, terminalID, meterNumber, tokTech, alg, sgc, krn, ti, description,
            energyKWh, amount, tokenNumber, FBEtoken, FBEKwh, balance;


    public VoucherModel(String receiptHeader, String distributer, String vatNumber, String operatorMessage,
                        String custMessage, String address, String date, String dateOfPurchase, String receiptNumber, String clientID, String terminalID,
                        String meterNumber, String tokTech, String alg, String sgc, String krn, String ti, String description,
                        String energyKWh, String amount, String tokenNumber, String FBEtoken, String FBEKwh, String balance) {

        this.meternumber = meterNumber;
        this.distributer = distributer;
        this.receiptHeader = receiptHeader;
        this.distributer = receiptHeader;
        this.vatNumber = vatNumber;
        this.operatorMessage = operatorMessage;
        this.custMessage = custMessage;
        this.address = address;
        this.date = date;
        this.dateOfPurchase = dateOfPurchase;
        this.receiptNumber = receiptNumber;
        this.clientID = clientID;
        this.terminalID = terminalID;
        this.meterNumber = meterNumber;
        this.tokTech = tokTech;
        this.alg = alg;
        this.sgc = sgc;
        this.krn = krn;
        this.ti = ti;
        this.description = description;
        this.energyKWh = energyKWh;
        this.amount = amount;
        this.tokenNumber = tokenNumber;
        this.FBEtoken = FBEtoken;
        this.FBEKwh = FBEKwh;
        this.balance = balance;

    }

    public String getReceiptHeader() {
        return receiptHeader;
    }

    public String getDistributer() {
        return distributer;
    }

    public String getVATNumber() {
        return vatNumber;
    }

    public String getOperatorMessage() {
        return operatorMessage;
    }

    public String getCustMessage() {
        return custMessage;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getDatePurchased() {
        return dateOfPurchase;
    }

    public String ReceiptNumber() {
        return receiptNumber;
    }

    public String getClientID() {
        return clientID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public String getTokenTech() {
        return tokTech;
    }

    public String getAlg() {
        return alg;
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

    public String getReceiptNumber() {
        return receiptNumber;
    }


    public String getFBEtoken() {
        return FBEtoken;
    }

    public String getFBEKwh() {
        return FBEKwh;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "meternumber=" + meternumber;
    }
}
