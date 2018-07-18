package com.artitech.tsalano.tukisha.model;

import java.util.List;

public class MunicipalityHistoryModel {

    private Status status;

    private List<ElectricityVoucherModel> results;

    public MunicipalityHistoryModel(Status status, List<ElectricityVoucherModel> results) {

        this.status= status;
        this.results = results;

    }
    public Status getStatusID() { return status;}

    public List<ElectricityVoucherModel> getResults() { return results;}
    @Override
    public String toString() {
        return "messageID=" + status.toString();
    }
}




