package com.agentcoder.hire_us_ph.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class ClientDetailsDO {
    private String clientId, clientFullname, clientEmail, clientContactNumber, clientGender, clientRoleCode, clientRoleName;

    public ClientDetailsDO(String clientId, String clientFullname, String clientEmail, String clientContactNumber, String clientGender, String clientRoleCode, String clientRoleName) {
        this.clientId = clientId;
        this.clientFullname = clientFullname;
        this.clientEmail = clientEmail;
        this.clientContactNumber = clientContactNumber;
        this.clientGender = clientGender;
        this.clientRoleCode = clientRoleCode;
        this.clientRoleName = clientRoleName;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientFullname() {
        return clientFullname;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientContactNumber() {
        return clientContactNumber;
    }

    public String getClientGender() {
        return clientGender;
    }

    public String getClientRoleCode() {
        return clientRoleCode;
    }

    public String getClientRoleName() {
        return clientRoleName;
    }

    @NotNull
    @Override
    public String toString() {
        return "ClientDetailsDO{" +
                "clientId='" + clientId + '\'' +
                ", clientFullname='" + clientFullname + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientContactNumber='" + clientContactNumber + '\'' +
                ", clientGender='" + clientGender + '\'' +
                ", clientRoleCode='" + clientRoleCode + '\'' +
                ", clientRoleName='" + clientRoleName + '\'' +
                '}';
    }
}
