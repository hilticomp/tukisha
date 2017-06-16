package com.artitech.tsalano.tukisha.model;

/**
 * Created by solly on 2017/06/01.
 */

public class AgentModel {

    private String agentBalance;
    private String status;

    public AgentModel(String balance, String status) {

        this.agentBalance = balance;
        this.status = status;

    }

    public String getAgentBalance() {
        return agentBalance;
    }

    public String getStatus() {
        return status;
    }

}
