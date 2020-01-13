package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutActivity extends AppCompatActivity {
    @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
    @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
    @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;
    @BindView(R.id.lblBookingPreferredDate)TextView lblBookingPreferredDate;
    @BindView(R.id.lblBookingPreferredTime) TextView lblBookingPreferredTime;
    @BindView(R.id.lblBookingPreferredVenue) TextView lblBookingPreferredVenue;
    @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;
    @BindView(R.id.btnCancelBooking) AppCompatButton btnCancelBooking;
    @BindView(R.id.card_multiline_widget) CardMultilineWidget cardMultilineWidget;
    @BindView(R.id.btnPayUsingDebitOrCreditCard) AppCompatButton btnPayUsingDebitOrCreditCard;
    @BindView(R.id.btnPayUsingBankDeposit) AppCompatButton btnPayUsingBankDeposit;
    @BindView(R.id.btnPayUsingBankTransfer) AppCompatButton btnPayUsingBankTransfer;

    private String selectedDate, selectedTime, selectedVenue;
    private Bundle bundle;

    private Stripe stripe;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        stripe = new Stripe(getApplicationContext(), "pk_test_pHHixfchBGl7tkRAanAoe7TN00O13SSzVp");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        AppSecurity.disableScreenshotRecording(this);

        bundle = getIntent().getExtras();
        selectedDate = Objects.requireNonNull(bundle).getString("temp_booking_date");
        selectedTime = Objects.requireNonNull(bundle).getString("temp_booking_time");
        selectedVenue = Objects.requireNonNull(bundle).getString("selected_venue");

        Glide
                .with(getApplicationContext())
                .load(bundle.getString("talent_profile_pic"))
                .placeholder(R.drawable.no_profile_pic)
                .into(imgTalentDisplayPhoto);

        lblTalentFullname.setText(bundle.getString("talent_fullname"));
        lblTalentCategories.setText(bundle.getString("talent_category"));
        lblBookingPreferredDate.setText(selectedDate);
        lblBookingPreferredTime.setText(selectedTime);
        lblBookingPreferredVenue.setText(selectedVenue);

        StringBuilder sbBookingOtherDetails = new StringBuilder();
        sbBookingOtherDetails
                .append("Total Hours: ")
                .append(bundle.getString("total_hours"))
                .append("\nTotal Amount: ")
                .append(Html.fromHtml("&#8369;"))
                .append(bundle.getString("total_amount"));

        lblBookingOtherDetails.setText( sbBookingOtherDetails.toString());

        btnCancelBooking.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        btnPayUsingDebitOrCreditCard.setOnClickListener(v -> {
            final Card cardToSave = cardMultilineWidget.getCard();

            if (cardToSave == null) {
                Toast.makeText(this, "Invalid Card Details", Toast.LENGTH_SHORT).show();
            }else{
                stripe.createToken(cardToSave, new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(Token token) {
                        final ProgressDialog progressDialog = new ProgressDialog(CheckoutActivity.this);
                        progressDialog.setMessage(Messages.PLEASE_WAIT_MSG);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        StringBuilder sbDescription = new StringBuilder();
                        sbDescription
                                .append(bundle.getString("talent_fullname"))
                                .append(" (").append(bundle.getString("talent_category")).append(") ")
                                .append(selectedDate).append(" (").append(selectedTime).append(")");

                        AndroidNetworking.post(EndPoints.START_PAYMENT_URL)
                                .addBodyParameter("stripe_token", token.getId())
                                .addBodyParameter("amount", Objects.requireNonNull(bundle.getString("total_amount")).replace(",", ""))
                                .addBodyParameter("description", sbDescription.toString())
                                .setTag(Tags.CHECKOUT_ACTIVITY)
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
                                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

                                        Log.e(Tags.CHECKOUT_ACTIVITY, errorResponse);
                                    }
                                });
                    }

                    @Override
                    public void onError(@NotNull Exception e) {
                        Toast.makeText(
                                getApplicationContext(),
                                e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnPayUsingBankTransfer.setOnClickListener(v -> {
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                    .setTopColorRes(R.color.colorPrimary)
                    .setButtonsColorRes(R.color.colorPrimary)
                    .setIcon(R.drawable.ic_add_alert_white)
                    .setTitle("Attention!")
                    .setMessage("Please pay your booking fee via Bank Transfer to this account: 123456789\n\nNote: After you clicked Ok, you have 24hrs to pay your pending booking fee.\n\nThank you for supporting Hire Us PH.")
                    .setPositiveButton(android.R.string.ok, v1 -> {
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        btnPayUsingBankDeposit.setOnClickListener(v -> {
            new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                    .setTopColorRes(R.color.colorPrimary)
                    .setButtonsColorRes(R.color.colorPrimary)
                    .setIcon(R.drawable.ic_add_alert_white)
                    .setTitle("Attention!")
                    .setMessage("Please pay your booking fee via Bank Deposit to this account: 123456789\n\nNote: After you clicked Ok, you have 24hrs to pay your pending booking fee.\n\nThank you for supporting Hire Us PH.")
                    .setPositiveButton(android.R.string.ok, v1 -> {
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
