package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2018/03/11.
 */

public class DSTVCustomerModel {


    private String customerNumber;

    private String initials;

    private String surname;

    private String cellNumber;

    private String paymentAmount;

    private String maximumLimitAmount;

    private String accountStatus;

    private String accountNumber;

    public DSTVCustomerModel(String customerNumber,String initials,String surname,String cellNumber,String paymentAmount,String maximumLimitAmount,String accountStatus,String accountNumber)
    {
        this.customerNumber = customerNumber;
        this.initials = initials;
        this.surname = surname;
        this.cellNumber = cellNumber;
        this.paymentAmount = paymentAmount;
        this.maximumLimitAmount= maximumLimitAmount;
        this.accountStatus = accountStatus;
        this.accountNumber = accountNumber;

    }

    public String getCustomerNumber() { return customerNumber; }

    public String getInitials() {
        return initials;
    }

    public String getSurname() {
        return surname;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getMaximumLimitAmount() {
        return maximumLimitAmount;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String displayCustomerInfo()
    {
       return String.format("Customer Number : " + "%s \n Initials : " + "%s \n Surname : " + "%s \n Cellphone : " +
                "%s\n Payment Amount : %s\n Maximum Limit Amount : " + "%s\n Account Status : %s\n Account Number : %s\n",
                customerNumber, initials, surname, cellNumber,paymentAmount,maximumLimitAmount,accountStatus,accountNumber);
    }



}