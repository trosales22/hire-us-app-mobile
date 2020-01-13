package com.trosales.hireusapp.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.TalentModelProfileActivity;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.Font;

public class BookingDetailsBottomSheetFragment extends BottomSheetDialogFragment {
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
    @BindView(R.id.lblBookingApprovedOrDeclinedDate) TextView lblBookingApprovedOrDeclinedDate;
    @BindView(R.id.btnShowTalentDetails) Button btnShowTalentDetails;
    @BindView(R.id.btnAddTalentReviews) Button btnAddTalentReviews;

    private Bundle bookingDetailsBundleArgs;
    private Context context;

    public BookingDetailsBottomSheetFragment(Bundle bookingDetailsBundleArgs, Context context) {
        this.bookingDetailsBundleArgs = bookingDetailsBundleArgs;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.booking_details_bottom_sheet, container, false);
        ButterKnife.bind(this, itemView);

        AppSecurity.disableScreenshotRecording(((AppCompatActivity) context));

        linearLayoutBookingDeclineReason.setVisibility(View.GONE);
        linearLayoutBookingApprovedOrDeclinedDate.setVisibility(View.GONE);

        lblBookingGeneratedId.setText(bookingDetailsBundleArgs.getString("booking_generated_id"));
        lblBookingEventTitle.setText(bookingDetailsBundleArgs.getString("booking_event_title"));
        lblBookingVenueOrLocation.setText(bookingDetailsBundleArgs.getString("booking_venue_location"));
        lblBookingPaymentOption.setText(bookingDetailsBundleArgs.getString("booking_payment_option"));
        lblBookingDate.setText(bookingDetailsBundleArgs.getString("booking_date"));
        lblBookingTime.setText(bookingDetailsBundleArgs.getString("booking_time"));
        lblBookingOtherDetails.setText(bookingDetailsBundleArgs.getString("booking_other_details"));
        lblBookingOfferStatus.setText(bookingDetailsBundleArgs.getString("booking_offer_status"));
        lblBookingCreatedDate.setText(bookingDetailsBundleArgs.getString("booking_created_date"));
        lblBookingDeclineReason.setText(bookingDetailsBundleArgs.getString("booking_decline_reason"));
        lblBookingApprovedOrDeclinedDate.setText(bookingDetailsBundleArgs.getString("booking_approved_or_declined_date"));
        lblBookingTalentFee.setText(Html.fromHtml("&#8369;") + bookingDetailsBundleArgs.getString("booking_talent_fee"));

        switch (Objects.requireNonNull(bookingDetailsBundleArgs.getString("booking_offer_status"))){
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

        btnShowTalentDetails.setOnClickListener(view -> {
            Intent intent = new Intent(context, TalentModelProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("talent_id", bookingDetailsBundleArgs.getString("talent_id"));

            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnAddTalentReviews.setOnClickListener(view -> {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(itemView.getContext());
            @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_talent_reviews, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(itemView.getContext(), R.style.AlertDialogTheme);
            alertDialogBuilderUserInput.setView(mView);

            EditText txtReviewsDescription = mView.findViewById(R.id.txtReviewsDescription);
            MaterialRatingBar talentRatingBar = mView.findViewById(R.id.talentRatingBar);

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Submit", (dialogBox, id) -> {
                        if(txtReviewsDescription.getText().toString().trim().isEmpty()){
                            txtReviewsDescription.setError("Please write some reviews..");
                        }else{
                            new EZDialog.Builder(context)
                                    .setTitle("Review Confirmation")
                                    .setMessage("Are you sure you want to submit this review?")
                                    .setPositiveBtnText("Yes")
                                    .setNegativeBtnText("No")
                                    .setButtonTextColor(R.color.colorPrimaryDarker)
                                    .setTitleTextColor(R.color.white)
                                    .setMessageTextColor(R.color.black)
                                    .setFont(Font.COMFORTAA)
                                    .setCancelableOnTouchOutside(false)
                                    .OnPositiveClicked(() -> {
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(Messages.PLEASE_WAIT_MSG);
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        AndroidNetworking.post(EndPoints.ADD_TALENT_REVIEWS_URL)
                                                .addBodyParameter("review_feedback", txtReviewsDescription.getText().toString().trim())
                                                .addBodyParameter("review_rating", String.valueOf(talentRatingBar.getRating()))
                                                .addBodyParameter("review_to", bookingDetailsBundleArgs.getString("talent_id"))
                                                .addBodyParameter("review_from", SharedPrefManager.getInstance(context).getUserId())
                                                .setTag(Tags.BOOKING_LIST_ACTIVITY)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        progressDialog.dismiss();
                                                        try {
                                                            String status = response.getString("flag");
                                                            String msg = response.has("msg") ? response.getString("msg") : "";

                                                            if(status.equals("1")){
                                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                                dialogBox.dismiss();
                                                            }else{
                                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            progressDialog.dismiss();
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        progressDialog.dismiss();
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

                                                        Log.e(Tags.BOOKING_LIST_ACTIVITY, errorResponse);
                                                    }
                                                });
                                    })
                                    .OnNegativeClicked(() -> {
                                        //todo
                                    })
                                    .build();
                        }



                    })
                    .setNegativeButton("Cancel",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
        });

        return itemView;
    }
}
