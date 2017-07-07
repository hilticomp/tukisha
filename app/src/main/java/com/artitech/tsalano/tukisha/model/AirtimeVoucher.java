package com.artitech.tsalano.tukisha.model;

import android.text.TextUtils;

/**
 * Created by solly on 2017/05/23.
 */

public class AirtimeVoucher {

    private String agentID;
    private String voucher;
    private String operator;
    private String date;
    private String amount;
    private String instructions;
    private String balance = "0";

    public AirtimeVoucher(String agentID,String voucher, String operator, String date, String amount, String instructions, String balance) {

        this.agentID = agentID;
        this.voucher = voucher;
        this.operator = operator;
        this.date = date;
        this.amount = amount;
        this.instructions = instructions;
        this.balance = balance;

    }

    public String getBalance() {
        return balance;
    }

    public String getVoucher() {
        return TextUtils.isEmpty(voucher) ? "" : voucher;
    }

    public String getOperator() {
        return operator;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        return "agentID=" + agentID;
    }
}
