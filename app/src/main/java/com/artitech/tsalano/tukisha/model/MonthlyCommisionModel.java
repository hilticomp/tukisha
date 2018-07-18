package com.artitech.tsalano.tukisha.model;

public class MonthlyCommisionModel {

    private String eskomSales, eskomPercentage, eskomCommission, telcoSales, telcoPercentage,
            telcoCommission, municipalitySales, municipalityPercentage,municipalityCommission,dstvBelowSales,dstvBelowPercentage,dstvBelowCommission,dstvAboveSales,dstvAbovePercentage,dstvAboveCommission,totalSales,totalCommission;



    public MonthlyCommisionModel(String eskomSales,String eskomPercentage, String eskomCommission,String telcoSales, String telcoPercentage,
                                  String telcoCommission, String municipalitySales, String mmunicipalityPercentage,
                                  String municipalityCommission,String dstvBelowSales,String dstvBelowPercentage, String dstvBelowCommission,String dstvAboveSales, String dstvAbovePercentage,
                                 String dstvAboveCommission, String totalSales, String totalCommission) {

        this.eskomSales = eskomSales;
        this.eskomPercentage =eskomPercentage;
        this.eskomCommission= eskomCommission;
        this.telcoSales = telcoSales;
        this.telcoPercentage = telcoPercentage;
        this.telcoCommission = telcoCommission;
        this.municipalitySales = municipalitySales;
        this.municipalityPercentage= mmunicipalityPercentage;
        this.municipalityCommission=municipalityCommission;
        this.dstvBelowSales=dstvBelowSales;
        this.dstvBelowPercentage=dstvBelowPercentage;
        this.dstvBelowCommission=dstvBelowCommission;
        this.dstvAboveSales=dstvAboveSales;
        this.dstvAbovePercentage=dstvAbovePercentage;
        this.dstvAboveCommission=dstvAboveCommission;
        this.totalSales=totalSales;
        this.totalCommission=totalCommission;


    }



    public String getTotalEskom() {
        return eskomSales;
    }
    public String getEskomPecent() {
        return eskomPercentage;
    }
    public String getEskomCommission() {
        return eskomCommission;
    }


    public String getTelcoSales() {
        return telcoSales;
    }
    public String getTelcoPercentage() {
        return telcoPercentage;
    }
    public String getTelcoCommission() {
        return telcoCommission;
    }

    public String getMunicipalitySales() {
        return municipalitySales;
    }
    public String getMunicipalityPercentage() {
        return municipalityPercentage;
    }
    public String getMunicipalityCommission() {
        return municipalityCommission;
    }


    public String getDstvBelowSales() {
        return dstvBelowSales;
    }
    public String getDstvBelowPercentage() {
        return dstvBelowPercentage;
    }
    public String getDstvBelowCommission() {
        return dstvBelowCommission;
    }


    public String getDstvaboveAmount() {
        return dstvAboveSales;
    }
    public String getDstvabovePercentage() {
        return dstvAbovePercentage;
    }
    public String getDstvAboveCommission() {
        return dstvAboveCommission;
    }





    public String getTotalAmount() {
        return totalSales;
    }
    public String getTotalCommission() {
        return totalCommission;
    }


}

