package com.agentcoder.hire_us_ph.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.agentcoder.hire_us_ph.R;
import com.agentcoder.hire_us_ph.activities.PotentialClientsActivity;
import com.agentcoder.hire_us_ph.classes.constants.EndPoints;
import com.agentcoder.hire_us_ph.classes.constants.Messages;
import com.agentcoder.hire_us_ph.classes.constants.Tags;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookingAndClientDetailsBottomSheetFragment extends BottomSheetDialogFragment {
    @BindView(R.id.linearLayoutBookingDeclineReason) LinearLayout linearLayoutBookingDeclineReason;
    @BindView(R.id.linearLayoutBookingApprovedOrDeclinedDate) LinearLayout linearLayoutBookingApprovedOrDeclinedDate;
    @BindView(R.id.lblBookingGeneratedId) TextView lblBookingGeneratedId;
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
    @BindView(R.id.lblApprovedDeclinedDateCaption) TextView lblApprovedDeclinedDateCaption;
    @BindView(R.id.lblBookingApprovedOrDeclinedDate) TextView lblBookingApprovedOrDeclinedDate;

    @BindView(R.id.lblClientGender) TextView lblClientGender;
    @BindView(R.id.lblClientType) TextView lblClientType;

    @BindView(R.id.btnCloseBookingAndClientDetailsDialog) Button btnCloseBookingAndClientDetailsDialog;
    @BindView(R.id.btnApproveBooking) Button btnApproveBooking;

    @BindView(R.id.linearLayoutChooseDeclineReasonCaption) LinearLayout linearLayoutChooseDeclineReasonCaption;
    @BindView(R.id.rgDeclineReason) RadioGroup rgDeclineReason;
    @BindView(R.id.rbTalentFee) RadioButton rbTalentFee;
    @BindView(R.id.rbDateUnavailability) RadioButton rbDateUnavailability;
    @BindView(R.id.rbLocation) RadioButton rbLocation;

    @BindView(R.id.btnDeclineBooking) Button btnDeclineBooking;

    private Bundle bookingAndClientDetailsBundleArgs;
    private Context context;

    private String selectedDeclineReason;

    private SweetAlertDialog sweetAlertDialog;

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

        //AppSecurity.disableScreenshotRecording(((AppCompatActivity) context));

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

        switch (bookingAndClientDetailsBundleArgs.getString("booking_offer_status")){
            case "APPROVED": lblApprovedDeclinedDateCaption.setText("Approved Date"); break;
            case "DECLINED": lblApprovedDeclinedDateCaption.setText("Declined Date"); break;
        }

        lblBookingCreatedDate.setText(bookingAndClientDetailsBundleArgs.getString("booking_created_date"));
        lblBookingDeclineReason.setText(bookingAndClientDetailsBundleArgs.getString("booking_decline_reason"));
        lblBookingApprovedOrDeclinedDate.setText(bookingAndClientDetailsBundleArgs.getString("booking_approved_or_declined_date"));
        lblBookingTalentFee.setText(Html.fromHtml("&#8369;") + bookingAndClientDetailsBundleArgs.getString("booking_talent_fee"));

        switch (Objects.requireNonNull(bookingAndClientDetailsBundleArgs.getString("booking_offer_status"))){
            case "WAITING":
                linearLayoutBookingDeclineReason.setVisibility(View.GONE);
                linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.GONE);

                btnApproveBooking.setVisibility(View.VISIBLE);
                btnDeclineBooking.setVisibility(View.VISIBLE);

                linearLayoutChooseDeclineReasonCaption.setVisibility(View.VISIBLE);
                rgDeclineReason.setVisibility(View.VISIBLE);

                break;
            case "APPROVED":
                linearLayoutBookingDeclineReason.setVisibility(View.GONE);
                linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.VISIBLE);

                btnApproveBooking.setVisibility(View.GONE);
                btnDeclineBooking.setVisibility(View.GONE);

                linearLayoutChooseDeclineReasonCaption.setVisibility(View.GONE);
                rgDeclineReason.setVisibility(View.GONE);

                break;
            case "DECLINED":
                linearLayoutBookingDeclineReason.setVisibility(View.VISIBLE);
                linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.VISIBLE);

                btnApproveBooking.setVisibility(View.GONE);
                btnDeclineBooking.setVisibility(View.GONE);

                linearLayoutChooseDeclineReasonCaption.setVisibility(View.GONE);
                rgDeclineReason.setVisibility(View.GONE);

                break;
        }

        lblClientGender.setText(bookingAndClientDetailsBundleArgs.getString("client_gender"));

        switch (Objects.requireNonNull(bookingAndClientDetailsBundleArgs.getString("role_code"))){
            case "CLIENT_INDIVIDUAL":
                lblClientType.setText(bookingAndClientDetailsBundleArgs.getString("client_type"));
                break;
            case "CLIENT_COMPANY":
                lblClientType.setText(bookingAndClientDetailsBundleArgs.getString("client_type") +
                        "\nCompany Name: " + bookingAndClientDetailsBundleArgs.getString("client_full_name"));
                break;
        }

        rbTalentFee.setChecked(true);

        rbTalentFee.setOnClickListener(view -> {
            chooseDeclineReason(rbTalentFee);
            selectedDeclineReason = "TALENT_FEE";
        });

        rbDateUnavailability.setOnClickListener(view -> {
            chooseDeclineReason(rbDateUnavailability);
            selectedDeclineReason = "DATE_UNAVAILABILITY";
        });

        rbLocation.setOnClickListener(view -> {
            chooseDeclineReason(rbLocation);
            selectedDeclineReason = "LOCATION";
        });

        btnCloseBookingAndClientDetailsDialog.setOnClickListener(view -> {
            this.dismiss();
        });

        btnApproveBooking.setOnClickListener(view -> {
            if(sweetAlertDialog != null){
                sweetAlertDialog.dismissWithAnimation();
            }

            sweetAlertDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirmation")
                    .setContentText("Are you sure you want to approve this booking?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        approveBooking(bookingAndClientDetailsBundleArgs.getString("booking_generated_id"));
                    })
                    .setCancelText("No")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation);

            sweetAlertDialog.show();
        });

        btnDeclineBooking.setOnClickListener(view -> {
            if(sweetAlertDialog != null){
                sweetAlertDialog.dismissWithAnimation();
            }

            sweetAlertDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirmation")
                    .setContentText("Are you sure you want to decline this booking?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        declineBooking(bookingAndClientDetailsBundleArgs.getString("booking_generated_id"), selectedDeclineReason);
                    })
                    .setCancelText("No")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation);

            sweetAlertDialog.show();
        });

        return itemView;
    }

    private void chooseDeclineReason(RadioButton radioButton){
        rbTalentFee.setChecked(false);
        rbDateUnavailability.setChecked(false);
        rbLocation.setChecked(false);

        radioButton.setChecked(true);
    }

    private void approveBooking(String bookingGeneratedId){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#48b1bf"));
        pDialog.setTitleText(Messages.PLEASE_WAIT_WHILE_APPROVING_BOOKING_MSG);
        pDialog.setCancelable(false);
        pDialog.show();

        AndroidNetworking.post(EndPoints.APPROVE_BOOKING_URL)
                .addBodyParameter("booking_generated_id", bookingGeneratedId)
                .setTag(Tags.POTENTIAL_CLIENTS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        getResponse(response);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        String errorResponse = "\n\nCode: " +
                                anError.getErrorCode() +
                                "\nDetail: " +
                                anError.getErrorDetail() +
                                "\nBody: " +
                                anError.getErrorBody() +
                                "\nResponse: " +
                                anError.getResponse() +
                                "\nMessage: " +
                                anError.getMessage();

                        Log.e(Tags.POTENTIAL_CLIENTS_ACTIVITY, errorResponse);
                    }
                });
    }

    private void declineBooking(String bookingGeneratedId, String bookingDeclineReason){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#48b1bf"));
        pDialog.setTitleText(Messages.PLEASE_WAIT_WHILE_DECLINING_BOOKING_MSG);
        pDialog.setCancelable(false);
        pDialog.show();

        AndroidNetworking.post(EndPoints.DECLINE_BOOKING_URL)
                .addBodyParameter("booking_generated_id", bookingGeneratedId)
                .addBodyParameter("booking_decline_reason", bookingDeclineReason)
                .setTag(Tags.POTENTIAL_CLIENTS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        getResponse(response);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(ANError anError) {
                        pDialog.dismiss();
                        String errorResponse = "\n\nCode: " +
                                anError.getErrorCode() +
                                "\nDetail: " +
                                anError.getErrorDetail() +
                                "\nBody: " +
                                anError.getErrorBody() +
                                "\nResponse: " +
                                anError.getResponse() +
                                "\nMessage: " +
                                anError.getMessage();

                        Log.e(Tags.POTENTIAL_CLIENTS_ACTIVITY, errorResponse);
                    }
                });
    }

    @SuppressLint("LongLogTag")
    private void getResponse(JSONObject response){
        try {
            int flag = response.getInt("flag");
            String msg = response.has("msg") ? response.getString("msg") : "";

            BookingAndClientDetailsBottomSheetFragment.this.dismiss();

            if(flag == 1){
                if(sweetAlertDialog != null){
                    sweetAlertDialog.dismissWithAnimation();
                }

                sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Congratulations!")
                        .setContentText(msg)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            startActivity(new Intent(context, PotentialClientsActivity.class));
                        });

                sweetAlertDialog.show();
            }else{
                if(sweetAlertDialog != null){
                    sweetAlertDialog.dismissWithAnimation();
                }

                sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops..")
                        .setContentText(msg);

                sweetAlertDialog.show();
            }
        } catch (JSONException e) {
            Log.d(Tags.POTENTIAL_CLIENTS_ACTIVITY, Objects.requireNonNull(e.getMessage()));
        }
    }
}
