package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2018/03/11.
 */

public class DSTVTransactionHistoryModel {

    private String transactionNumber;

    private String datetime;

    private String accountNumber;

    private String receiptNumber;

    private String firstName;

    private String surname;

    private String amountDue;

    private String amount;


    public DSTVTransactionHistoryModel(String datetime,String transactionNumber, String accountNumber,String receiptNumber,String firstName,String surname, String amountDue, String amount)
    {
        this.datetime = datetime;
        this.accountNumber = accountNumber;
        this.receiptNumber = receiptNumber;
        this.firstName = firstName;
        this.surname = surname;
        this.amountDue = amountDue;
        this.amount = amount;
        this.transactionNumber = transactionNumber;



    }

    public String getDatetime() {
        return datetime;
    }
    public String getTransactionNumber(){return transactionNumber;}
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getReceiptNumber() {
        return receiptNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSurname() {
        return surname;
    }
    public String getAmountDue() {
        return amountDue;
    }
    public String getAmount() {
        return amount;
    }




}
