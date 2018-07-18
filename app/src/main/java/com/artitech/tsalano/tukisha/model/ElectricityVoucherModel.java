package com.artitech.tsalano.tukisha.model;

/**
 * Created by rammalamn on 2017/05/23.
 */

public class ElectricityVoucherModel {

    private String agentID;

    private String agentName;
    private String tokenNumber;
    private String processedDate;
    private String tokenAmount;
    private String productType;
    private String meterNumber;
    private String messageID;

    public ElectricityVoucherModel(String agentID, String agentName, String tokenNumber, String processedDate, String tokenAmount, String productType, String messageID, String meterNumber) {

        this.agentID= agentID;
        this.agentName = agentName;
        this.tokenNumber = tokenNumber;
        this.processedDate = processedDate;
        this.productType = productType;
        this.tokenAmount = tokenAmount;
        this.meterNumber = meterNumber;
        this.messageID = messageID;

    }

    public String getAgentID() { return agentID;}

    public String getAgentName() { return agentName;}

    public String getTokenNumber() {
        return tokenNumber;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public String getProductType() {
        return productType;
    }




    public String getTokenAmount() { return
        tokenAmount;
    }

    public String getMeterNumber() { return meterNumber; }

    public String getMessageID() { return messageID; }

    @Override
    public String toString() {
        return "messageID=" + messageID;
    }
}
