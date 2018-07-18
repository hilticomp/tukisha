package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2018/04/05.
 */

public class DSTVResponseModel {


    private String status;

    private String balance;

    private String amountPaid;

    private String transactionNumber;

    private String receiptNumber;

    private String paymentDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }
    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }
    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;

    }

    public String getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

}