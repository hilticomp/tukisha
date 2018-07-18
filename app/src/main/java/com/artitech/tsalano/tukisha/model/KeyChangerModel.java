package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/05/23.
 */

public class KeyChangerModel {

    private String meternumber,tokenThree,receiptHeader,distributer,vatNumber,operatorMessage,custMessage,newSgc,newKrn,newTi,oldSgc,oldKrn,
            address, date, dateOfPurchase, receiptNumber, clientID, terminalID, meterNumber, tokTech, alg, sgc, krn, ti, description,
            energyKWh, amount, tokenNumber, FBEtoken,maxPwrKw, FBEKwh, balance, totalCashBack,oldTi,tokenOne,tokenTwo;


    public KeyChangerModel(String receiptHeader, String maxPwrKw, String distributer, String vatNumber, String operatorMessage,String oldKrn,String oldTi,
                        String custMessage, String address, String date, String dateOfPurchase, String receiptNumber, String clientID, String terminalID,
                        String meterNumber,String tokenThree,String tokenTwo, String tokTech, String alg, String sgc, String krn, String ti, String description,String oldSgc,
                        String energyKWh, String amount,String tokenOne, String tokenNumber, String FBEtoken, String FBEKwh, String balance, String totalCashBack,String newSgc,String  newKrn,String newTi) {

        this.meternumber = meterNumber;
        this.newTi = newTi;
        this.maxPwrKw = maxPwrKw;
        this.tokenTwo =  tokenTwo;
        this.tokenOne = tokenOne;
        this.oldTi =  oldTi;
        this.oldKrn = oldKrn;
        this.oldSgc = oldSgc;
        this.tokenThree = tokenThree;
        this.newSgc = newSgc;
        this.newKrn= newKrn;
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
        this.totalCashBack = totalCashBack;

    }

    public String getNewSgc() {
        return newSgc;
    }

    public String getMaxPwrKw() {
        return maxPwrKw;
    }

    public String getTokenThree() {
        return tokenThree;
    }

    public String getTokenOne() {
        return tokenOne;
    }

    public String getTokenTwo() {
        return tokenTwo;
    }

    public String getOldTi() {
        return oldTi;
    }
    public String getNewTi() {
        return newTi;
    }

    public String getNewKrn() {
        return newKrn;
    }

    public String getOldKrn() {
        return oldKrn;
    }

    public String getOldSgc() {
        return oldSgc;
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

    public String getTotalCashBack() {
        return totalCashBack;
    }

    @Override
    public String toString() {
        return "meternumber=" + meternumber;
    }
}
