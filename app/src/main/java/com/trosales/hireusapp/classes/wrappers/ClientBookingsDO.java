package com.trosales.hireusapp.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class ClientBookingsDO {
    private int bookingId;
    private String preferredBookingDate, preferredBookingTime, preferredBookingVenue, bookingPaymentOption, bookingTotalAmount, bookingDatePaid;
    private TalentsDO talentDetails;

    public ClientBookingsDO(int bookingId, String preferredBookingDate, String preferredBookingTime, String preferredBookingVenue, String bookingPaymentOption, String bookingTotalAmount, String bookingDatePaid, TalentsDO talentDetails) {
        this.bookingId = bookingId;
        this.preferredBookingDate = preferredBookingDate;
        this.preferredBookingTime = preferredBookingTime;
        this.preferredBookingVenue = preferredBookingVenue;
        this.bookingPaymentOption = bookingPaymentOption;
        this.bookingTotalAmount = bookingTotalAmount;
        this.bookingDatePaid = bookingDatePaid;
        this.talentDetails = talentDetails;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getPreferredBookingDate() {
        return preferredBookingDate;
    }

    public String getPreferredBookingTime() {
        return preferredBookingTime;
    }

    public String getPreferredBookingVenue() {
        return preferredBookingVenue;
    }

    public String getBookingPaymentOption() {
        return bookingPaymentOption;
    }

    public String getBookingTotalAmount() {
        return bookingTotalAmount;
    }

    public String getBookingDatePaid() {
        return bookingDatePaid;
    }

    public TalentsDO getTalentDetails() {
        return talentDetails;
    }

    @NotNull
    @Override
    public String toString() {
        return "ClientBookingsDO{" +
                "bookingId=" + bookingId +
                ", preferredBookingDate='" + preferredBookingDate + '\'' +
                ", preferredBookingTime='" + preferredBookingTime + '\'' +
                ", preferredBookingVenue='" + preferredBookingVenue + '\'' +
                ", bookingPaymentOption='" + bookingPaymentOption + '\'' +
                ", bookingTotalAmount='" + bookingTotalAmount + '\'' +
                ", bookingDatePaid='" + bookingDatePaid + '\'' +
                ", talentDetails=" + talentDetails +
                '}';
    }
}
