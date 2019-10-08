package com.trosales.hireusapp.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class ClientDetailsDO {
    private String clientId, clientFullname, clientGender, clientRoleCode, clientRoleName;

    public ClientDetailsDO(String clientId, String clientFullname, String clientGender, String clientRoleCode, String clientRoleName) {
        this.clientId = clientId;
        this.clientFullname = clientFullname;
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
                ", clientGender='" + clientGender + '\'' +
                ", clientRoleCode='" + clientRoleCode + '\'' +
                ", clientRoleName='" + clientRoleName + '\'' +
                '}';
    }
}
