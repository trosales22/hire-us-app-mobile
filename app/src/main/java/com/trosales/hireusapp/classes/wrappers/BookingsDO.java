package com.trosales.hireusapp.classes.wrappers;

public class BookingsDO {
    private int bookingId;
    private String bookingStatus, preferredBookingDateFrom, preferredBookingDateTo, bookingTotalAmount;
    private TalentsDO talentDetails;

    public BookingsDO(int bookingId, String bookingStatus, String preferredBookingDateFrom, String preferredBookingDateTo, String bookingTotalAmount, TalentsDO talentDetails) {
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.preferredBookingDateFrom = preferredBookingDateFrom;
        this.preferredBookingDateTo = preferredBookingDateTo;
        this.bookingTotalAmount = bookingTotalAmount;
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

    public String getBookingTotalAmount() {
        return bookingTotalAmount;
    }

    public TalentsDO getTalentDetails() {
        return talentDetails;
    }

    @Override
    public String toString() {
        return "BookingsDO{" +
                "bookingId=" + bookingId +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", preferredBookingDateFrom='" + preferredBookingDateFrom + '\'' +
                ", preferredBookingDateTo='" + preferredBookingDateTo + '\'' +
                ", bookingTotalAmount='" + bookingTotalAmount + '\'' +
                ", talentDetails=" + talentDetails +
                '}';
    }
}
