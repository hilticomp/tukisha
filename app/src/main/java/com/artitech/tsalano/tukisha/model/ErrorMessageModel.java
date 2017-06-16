package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/05/23.
 */


public class ErrorMessageModel {

    private String code, respDateTime, dispHeader, custMsg, terminalID, operatorMessage;


    public ErrorMessageModel(String code, String respDateTime, String dispHeader, String custMsg, String terminalID, String operatorMessage) {

        this.code = code;
        this.respDateTime = respDateTime;
        this.dispHeader = dispHeader;
        this.custMsg = custMsg;
        this.terminalID = terminalID;
        this.operatorMessage = operatorMessage;

    }

    public String getCode() {
        return code;
    }

    public String getResponseDateTime() {
        return respDateTime;
    }

    public String getDisplayHeader() {
        return dispHeader;
    }

    public String getOperatorMessage() {
        return operatorMessage;
    }

    public String getCustMessage() {
        return custMsg;
    }


    @Override
    public String toString() {
        return "error message=" + custMsg;
    }
}
