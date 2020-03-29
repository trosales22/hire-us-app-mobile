package com.tjbr.hire_us_ph.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class ClientsBookedDO {
    private ClientDetailsDO clientDetailsDO;
    private ClientBookingsDO clientBookingsDO;

    public ClientsBookedDO(ClientDetailsDO clientDetailsDO, ClientBookingsDO clientBookingsDO) {
        this.clientDetailsDO = clientDetailsDO;
        this.clientBookingsDO = clientBookingsDO;
    }

    public ClientDetailsDO getClientDetailsDO() {
        return clientDetailsDO;
    }

    public ClientBookingsDO getClientBookingsDO() {
        return clientBookingsDO;
    }

    @NotNull
    @Override
    public String toString() {
        return "ClientsBookedDO{" +
                "clientDetailsDO=" + clientDetailsDO +
                ", clientBookingsDO=" + clientBookingsDO +
                '}';
    }
}
