package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/07/16.
 */

public class IssueAdvise {

    private String receiptHeader, operatorMessage, custMessage;

    public IssueAdvise(String receiptHeader, String operatorMessage, String custMessage) {

        this.receiptHeader = receiptHeader;
        this.operatorMessage = operatorMessage;
        this.custMessage = custMessage;

    }

    public String getReceiptHeader() {
        return receiptHeader;
    }

    public String getOperatorMessage() {
        return operatorMessage;
    }

    public String getCustMessage() {
        return custMessage;
    }
}
