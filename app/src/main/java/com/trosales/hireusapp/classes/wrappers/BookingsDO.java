package com.trosales.hireusapp.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class BookingsDO {
    private int bookingId;
    private String bookingStatus, preferredBookingDateFrom, preferredBookingDateTo, preferredBookingTimeFrom, preferredBookingTimeTo, bookingTotalAmount, bookingDatePaid;
    private TalentsDO talentDetails;

    public BookingsDO(int bookingId, String bookingStatus, String preferredBookingDateFrom, String preferredBookingDateTo, String preferredBookingTimeFrom, String preferredBookingTimeTo, String bookingTotalAmount, String bookingDatePaid, TalentsDO talentDetails) {
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.preferredBookingDateFrom = preferredBookingDateFrom;
        this.preferredBookingDateTo = preferredBookingDateTo;
        this.preferredBookingTimeFrom = preferredBookingTimeFrom;
        this.preferredBookingTimeTo = preferredBookingTimeTo;
        this.bookingTotalAmount = bookingTotalAmount;
        this.bookingDatePaid = bookingDatePaid;
        this.talentDetails = talentDetails;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getPreferredBookingDateFrom() {
        return preferredBookingDateFrom;
    }

    public String getPreferredBookingDateTo() {
        return preferredBookingDateTo;
    }

    public String getPreferredBookingTimeFrom() {
        return preferredBookingTimeFrom;
    }

    public String getPreferredBookingTimeTo() {
        return preferredBookingTimeTo;
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
        return "BookingsDO{" +
                "bookingId=" + bookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", preferredBookingDateFrom='" + preferredBookingDateFrom + '\'' +
                ", preferredBookingDateTo='" + preferredBookingDateTo + '\'' +
                ", preferredBookingTimeFrom='" + preferredBookingTimeFrom + '\'' +
                ", preferredBookingTimeTo='" + preferredBookingTimeTo + '\'' +
                ", bookingTotalAmount='" + bookingTotalAmount + '\'' +
                ", bookingDatePaid='" + bookingDatePaid + '\'' +
                ", talentDetails=" + talentDetails +
                '}';
    }
}
