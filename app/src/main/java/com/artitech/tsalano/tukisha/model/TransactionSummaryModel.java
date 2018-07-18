package com.artitech.tsalano.tukisha.model;

public class TransactionSummaryModel {

    private String eskomSales, eskomTransactions, eskomCommission, telcoSales, telcoTransactions,totalTransactions,
            telcoCommission, municipalitySales, municipalityTransactions,municipalityCommission,
            dstvBelowSales,below70Transactions,below70Commission,dstvSales,above70Transactions,
            above70Commission,totalSales,totalCommission;



    public TransactionSummaryModel(String eskomSales, String eskomTransactions, String eskomCommission, String telcoSales,
                                   String telcoTransactions,
                                   String telcoCommission, String municipalitySales, String municipalityTransactions,
                                   String municipalityCommission, String dstvBelowSales, String below70Transactions,
                                   String below70Commission, String dstvSales, String above70Transactions,
                                   String above70Commission, String totalSales, String totalCommission, String totalTransactions) {

        this.eskomSales = eskomSales;
        this.eskomTransactions =eskomTransactions;
        this.eskomCommission= eskomCommission;
        this.telcoSales = telcoSales;
        this.telcoTransactions = telcoTransactions;
        this.telcoCommission = telcoCommission;
        this.municipalitySales = municipalitySales;
        this.municipalityTransactions= municipalityTransactions;
        this.municipalityCommission=municipalityCommission;
        this.dstvBelowSales=dstvBelowSales;
        this.below70Transactions=below70Transactions;
        this.below70Commission=below70Commission;
        this.dstvSales=dstvSales;
        this.above70Transactions=above70Transactions;
        this.above70Commission=above70Commission;
        this.totalSales=totalSales;
        this.totalCommission=totalCommission;
        this.totalTransactions=totalTransactions;


    }



    public String getTotalEskom() {
        return eskomSales;
    }
    public String getEskomTransactions() {
        return eskomTransactions;
    }
    public String getEskomCommission() {
        return eskomCommission;
    }


    public String getTelcoSales() {
        return telcoSales;
    }
    public String getTelcoTransactions() {
        return telcoTransactions;
    }
    public String getTelcoCommission() {
        return telcoCommission;
    }

    public String getMunicipalitySales() {
        return municipalitySales;
    }
    public String getMunicipalityTransactions() {
        return municipalityTransactions;
    }
    public String getMunicipalityCommission() {
        return municipalityCommission;
    }


    public String getDstvBelowSales() {
        return dstvBelowSales;
    }
    public String getBelow70Transactions() {
        return below70Transactions;
    }
    public String getBelow70Commission() {
        return below70Commission;
    }


    public String getDstvSales() {
        return dstvSales;
    }
    public String getAbove70Transactions() {
        return above70Transactions;
    }
    public String getAbove70Commission() {
        return above70Commission;
    }





    public String getTotalAmount() {
        return totalSales;
    }
    public String getTotalTransactions() {
        return totalTransactions;
    }
    public String getTotalCommission() {
        return totalCommission;
    }


}

