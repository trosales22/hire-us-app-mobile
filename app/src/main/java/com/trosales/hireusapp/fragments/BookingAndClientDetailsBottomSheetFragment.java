package com.trosales.hireusapp.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingAndClientDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    @BindView(R.id.linearLayoutBookingDeclineReason)
    LinearLayout linearLayoutBookingDeclineReason;
    @BindView(R.id.linearLayoutBookingApprovedOrDeclinedDate) LinearLayout linearLayoutBookingApprovedOrDeclinedDate;
    @BindView(R.id.lblBookingGeneratedId)
    TextView lblBookingGeneratedId;
    @BindView(R.id.lblBookingEventTitle) TextView lblBookingEventTitle;
    @BindView(R.id.lblBookingTalentFee) TextView lblBookingTalentFee;
    @BindView(R.id.lblBookingVenueOrLocation) TextView lblBookingVenueOrLocation;
    @BindView(R.id.lblBookingPaymentOption) TextView lblBookingPaymentOption;
    @BindView(R.id.lblBookingDate) TextView lblBookingDate;
    @BindView(R.id.lblBookingTime) TextView lblBookingTime;
    @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;
    @BindView(R.id.lblBookingOfferStatus) TextView lblBookingOfferStatus;
    @BindView(R.id.lblBookingCreatedDate) TextView lblBookingCreatedDate;
    @BindView(R.id.lblBookingDeclineReason) TextView lblBookingDeclineReason;
    @BindView(R.id.lblBookingApprovedOrDeclinedDate) TextView lblBookingApprovedOrDeclinedDate;

    @BindView(R.id.lblClientFullName) TextView lblClientFullName;
    @BindView(R.id.lblClientEmail) TextView lblClientEmail;
    @BindView(R.id.lblClientContactNumber) TextView lblClientContactNumber;
    @BindView(R.id.lblClientGender) TextView lblClientGender;
    @BindView(R.id.lblClientType) TextView lblClientType;

    @BindView(R.id.btnCloseBookingAndClientDetailsDialog) Button btnCloseBookingAndClientDetailsDialog;
    @BindView(R.id.btnApproveBooking) Button btnApproveBooking;
    @BindView(R.id.btnDeclineBooking) Button btnDeclineBooking;

    private Bundle bookingAndClientDetailsBundleArgs;
    private Context context;

    public BookingAndClientDetailsBottomSheetFragment(Bundle bookingAndClientDetailsBundleArgs, Context context) {
        this.bookingAndClientDetailsBundleArgs = bookingAndClientDetailsBundleArgs;
        this.context = context;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog bottomSheetDialog1 = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = bottomSheetDialog1.findViewById(R.id.design_bottom_sheet);

            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return bottomSheetDialog;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.booking_and_client_details_bottom_sheet, container, false);
        ButterKnife.bind(this, itemView);

        AppSecurity.disableScreenshotRecording(((AppCompatActivity) context));

        linearLayoutBookingDeclineReason.setVisibility(View.GONE);
        linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.GONE);

        lblBookingGeneratedId.setText(bookingAndClientDetailsBundleArgs.getString("booking_generated_id"));
        lblBookingEventTitle.setText(bookingAndClientDetailsBundleArgs.getString("booking_event_title"));
        lblBookingVenueOrLocation.setText(bookingAndClientDetailsBundleArgs.getString("booking_venue_location"));
        lblBookingPaymentOption.setText(bookingAndClientDetailsBundleArgs.getString("booking_payment_option"));
        lblBookingDate.setText(bookingAndClientDetailsBundleArgs.getString("booking_date"));
        lblBookingTime.setText(bookingAndClientDetailsBundleArgs.getString("booking_time"));
        lblBookingOtherDetails.setText(bookingAndClientDetailsBundleArgs.getString("booking_other_details"));
        lblBookingOfferStatus.setText(bookingAndClientDetailsBundleArgs.getString("booking_offer_status"));
        lblBookingCreatedDate.setText(bookingAndClientDetailsBundleArgs.getString("booking_created_date"));
        lblBookingDeclineReason.setText(bookingAndClientDetailsBundleArgs.getString("booking_decline_reason"));
        lblBookingApprovedOrDeclinedDate.setText(bookingAndClientDetailsBundleArgs.getString("booking_approved_or_declined_date"));
        lblBookingTalentFee.setText(Html.fromHtml("&#8369;") + bookingAndClientDetailsBundleArgs.getString("booking_talent_fee"));

        switch (Objects.requireNonNull(bookingAndClientDetailsBundleArgs.getString("booking_offer_status"))){
            case "WAITING":
                linearLayoutBookingDeclineReason.setVisibility(View.GONE);
                linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.GONE);
                break;
            case "APPROVED":
                linearLayoutBookingDeclineReason.setVisibility(View.GONE);
                linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.VISIBLE);
                break;
            case "DECLINED":
                linearLayoutBookingDeclineReason.setVisibility(View.VISIBLE);
                linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.VISIBLE);
                break;
        }

        lblClientFullName.setText(bookingAndClientDetailsBundleArgs.getString("client_full_name"));
        lblClientEmail.setText(bookingAndClientDetailsBundleArgs.getString("client_email"));
        lblClientContactNumber.setText(bookingAndClientDetailsBundleArgs.getString("client_contact_number"));
        lblClientGender.setText(bookingAndClientDetailsBundleArgs.getString("client_gender"));
        lblClientType.setText(bookingAndClientDetailsBundleArgs.getString("client_type"));

        btnCloseBookingAndClientDetailsDialog.setOnClickListener(view -> {
            this.dismiss();
        });

        btnApproveBooking.setOnClickListener(view -> {

        });

        btnDeclineBooking.setOnClickListener(view -> {

        });

        return itemView;
    }
}
