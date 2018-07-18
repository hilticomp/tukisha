package com.artitech.tsalano.tukisha.model;

import java.util.List;

/**
 * Created by rammalamn on 2017/05/23.
 */

public class EskomHistoryModel {

    private Status status;

    private List<ElectricityVoucherModel> results;

    public EskomHistoryModel(Status status, List<ElectricityVoucherModel> results) {

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


