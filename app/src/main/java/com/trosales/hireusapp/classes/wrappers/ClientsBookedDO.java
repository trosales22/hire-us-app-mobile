package com.trosales.hireusapp.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class ClientsBookedDO {
    private ClientDetailsDO clientDetailsDO;
    private String bookingId, selectedDate, selectedTime,
            selectedPaymentOption, totalAmount, datePaid;


    public ClientsBookedDO(ClientDetailsDO clientDetailsDO, String bookingId, String selectedDate, String selectedTime, String selectedPaymentOption, String totalAmount, String datePaid) {
        this.clientDetailsDO = clientDetailsDO;
        this.bookingId = bookingId;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.selectedPaymentOption = selectedPaymentOption;
        this.totalAmount = totalAmount;
        this.datePaid = datePaid;
    }

    public ClientDetailsDO getClientDetailsDO() {
        return clientDetailsDO;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public String getSelectedPaymentOption() {
        return selectedPaymentOption;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getDatePaid() {
        return datePaid;
    }

    @NotNull
    @Override
    public String toString() {
        return "ClientsBookedDO{" +
                "clientDetailsDO=" + clientDetailsDO +
                ", bookingId='" + bookingId + '\'' +
                ", selectedDate='" + selectedDate + '\'' +
                ", selectedTime='" + selectedTime + '\'' +
                ", selectedPaymentOption='" + selectedPaymentOption + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", datePaid='" + datePaid + '\'' +
                '}';
    }
}
