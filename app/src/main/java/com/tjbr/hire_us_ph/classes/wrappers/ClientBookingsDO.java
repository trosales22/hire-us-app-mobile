package com.tjbr.hire_us_ph.classes.wrappers;

import org.jetbrains.annotations.NotNull;

public class ClientBookingsDO {
    private int bookingId;
    private String bookingGeneratedId, bookingEventTitle, bookingTalentFee, bookingVenueLocation,
            bookingPaymentOption,  bookingDate, bookingTime, bookingOtherDetails, bookingOfferStatus,
            bookingCreatedDate, bookingDeclineReason, bookingApprovedOrDeclinedDate, bookingDatePaid,
            bookingPayUntil, bookingPaymentStatus;
    private TalentsDO talentDetails;

    public ClientBookingsDO(int bookingId, String bookingGeneratedId, String bookingEventTitle, String bookingTalentFee, String bookingVenueLocation, String bookingPaymentOption, String bookingDate, String bookingTime, String bookingOtherDetails, String bookingOfferStatus, String bookingCreatedDate, String bookingDeclineReason, String bookingApprovedOrDeclinedDate, String bookingDatePaid, String bookingPayUntil, String bookingPaymentStatus, TalentsDO talentDetails) {
        this.bookingId = bookingId;
        this.bookingGeneratedId = bookingGeneratedId;
        this.bookingEventTitle = bookingEventTitle;
        this.bookingTalentFee = bookingTalentFee;
        this.bookingVenueLocation = bookingVenueLocation;
        this.bookingPaymentOption = bookingPaymentOption;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingOtherDetails = bookingOtherDetails;
        this.bookingOfferStatus = bookingOfferStatus;
        this.bookingCreatedDate = bookingCreatedDate;
        this.bookingDeclineReason = bookingDeclineReason;
        this.bookingApprovedOrDeclinedDate = bookingApprovedOrDeclinedDate;
        this.bookingDatePaid = bookingDatePaid;
        this.bookingPayUntil = bookingPayUntil;
        this.bookingPaymentStatus = bookingPaymentStatus;
        this.talentDetails = talentDetails;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getBookingGeneratedId() {
        return bookingGeneratedId;
    }

    public String getBookingEventTitle() {
        return bookingEventTitle;
    }

    public String getBookingTalentFee() {
        return bookingTalentFee;
    }

    public String getBookingVenueLocation() {
        return bookingVenueLocation;
    }

    public String getBookingPaymentOption() {
        return bookingPaymentOption;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public String getBookingOtherDetails() {
        return bookingOtherDetails;
    }

    public String getBookingOfferStatus() {
        return bookingOfferStatus;
    }

    public String getBookingCreatedDate() {
        return bookingCreatedDate;
    }

    public String getBookingDeclineReason() {
        return bookingDeclineReason;
    }

    public String getBookingApprovedOrDeclinedDate() {
        return bookingApprovedOrDeclinedDate;
    }

    public String getBookingDatePaid() {
        return bookingDatePaid;
    }

    public String getBookingPayUntil() {
        return bookingPayUntil;
    }

    public String getBookingPaymentStatus() {
        return bookingPaymentStatus;
    }

    public TalentsDO getTalentDetails() {
        return talentDetails;
    }

    @NotNull
    @Override
    public String toString() {
        return "ClientBookingsDO{" +
                "bookingId=" + bookingId +
                ", bookingGeneratedId='" + bookingGeneratedId + '\'' +
                ", bookingEventTitle='" + bookingEventTitle + '\'' +
                ", bookingTalentFee='" + bookingTalentFee + '\'' +
                ", bookingVenueLocation='" + bookingVenueLocation + '\'' +
                ", bookingPaymentOption='" + bookingPaymentOption + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", bookingTime='" + bookingTime + '\'' +
                ", bookingOtherDetails='" + bookingOtherDetails + '\'' +
                ", bookingOfferStatus='" + bookingOfferStatus + '\'' +
                ", bookingCreatedDate='" + bookingCreatedDate + '\'' +
                ", bookingDeclineReason='" + bookingDeclineReason + '\'' +
                ", bookingApprovedOrDeclinedDate='" + bookingApprovedOrDeclinedDate + '\'' +
                ", bookingDatePaid='" + bookingDatePaid + '\'' +
                ", bookingPayUntil='" + bookingPayUntil + '\'' +
                ", bookingPaymentStatus='" + bookingPaymentStatus + '\'' +
                ", talentDetails=" + talentDetails +
                '}';
    }
}
