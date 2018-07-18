package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/06/29.
 */

public class TransactionHistoryModel {

    public String customerID;
    public String customerName;
    public String voucherNumber;
    public String dateProcessed;
    public String amount;
    public String productType;
    public String Provider;
    public String messageID;


    public TransactionHistoryModel(String customerName, String voucherNumber, String dateProcessed, String amount,String productType, String provider)
    {
        this.customerName = customerName;
        this.voucherNumber = voucherNumber;
        this.dateProcessed = dateProcessed;
        this.amount = amount;
        this.productType = productType;
        this.Provider = provider;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public String getDateProcessed() {
        return dateProcessed;
    }

    public String getAmount() {
        return amount;
    }

    public String getProductType() {
        return productType;
    }

    public String getProvider() {
        return Provider;
    }

    public String getMessageID() {
        return messageID;
    }


}
