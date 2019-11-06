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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutActivity extends AppCompatActivity {
    @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
    @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
    @BindView(R.id.lblTalentRatePerHour) TextView lblTalentRatePerHour;
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

    private String selectedDate, selectedTime, selectedVenue, selectedTalentId, selectedPaymentOption;
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

        bundle = getIntent().getExtras();
        selectedDate = Objects.requireNonNull(bundle).getString("temp_booking_date");
        selectedTime = Objects.requireNonNull(bundle).getString("temp_booking_time");
        selectedVenue = Objects.requireNonNull(bundle).getString("selected_venue");
        selectedTalentId = Objects.requireNonNull(bundle).getString("temp_talent_id");

        Picasso
                .with(getApplicationContext())
                .load(bundle.getString("talent_profile_pic"))
                .placeholder(R.drawable.no_profile_pic)
                .into(imgTalentDisplayPhoto);

        lblTalentFullname.setText(bundle.getString("talent_fullname"));
        lblTalentRatePerHour.setText(Html.fromHtml("&#8369;" + bundle.getString("talent_rate_per_hour") + " per hour"));
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
            selectedPaymentOption = "DEBIT_CREDIT_CARD";
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
                                                addToClientBookingList(getClientBookingParams());
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
            selectedPaymentOption = "BANK_TRANSFER";
            addToTempBookingList(getTemporaryClientBookingParams());
        });

        btnPayUsingBankDeposit.setOnClickListener(v -> {
            selectedPaymentOption = "BANK_DEPOSIT";
            addToTempBookingList(getTemporaryClientBookingParams());
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

    private HashMap<String, String> getClientBookingParams(){
        HashMap<String, String> params = new HashMap<>();
        params.put("talent_id", selectedTalentId);
        params.put("client_id", SharedPrefManager.getInstance(this).getUserId());
        params.put("preferred_date", selectedDate);
        params.put("preferred_time", selectedTime);
        params.put("total_amount", bundle.getString("total_amount"));
        params.put("payment_option", selectedPaymentOption);

        return params;
    }

    private HashMap<String, String> getTemporaryClientBookingParams(){
        HashMap<String, String> params = new HashMap<>();
        params.put("temp_talent_id", selectedTalentId);
        params.put("temp_client_id", SharedPrefManager.getInstance(this).getUserId());
        params.put("temp_booking_date", selectedDate);
        params.put("temp_booking_time", selectedTime);
        params.put("temp_total_amount", bundle.getString("total_amount"));
        params.put("temp_payment_option", selectedPaymentOption);

        return params;
    }

    private void addToTempBookingList(HashMap<String, String> params){
        final ProgressDialog progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setMessage(Messages.PLEASE_WAIT_WHILE_ADDING_TO_BOOKING_LIST_MSG);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d(Tags.CHECKOUT_ACTIVITY, "params: " + params.toString());

        AndroidNetworking.post(EndPoints.ADD_TO_TEMP_BOOKING_LIST_URL)
                .addBodyParameter("temp_talent_id", params.get("temp_talent_id"))
                .addBodyParameter("temp_client_id", params.get("temp_client_id"))
                .addBodyParameter("temp_booking_date", params.get("temp_booking_date"))
                .addBodyParameter("temp_booking_time", params.get("temp_booking_time"))
                .addBodyParameter("temp_booking_venue", selectedVenue)
                .addBodyParameter("temp_total_amount", params.get("temp_total_amount"))
                .addBodyParameter("temp_status", "WAITING_FOR_PAYMENT")
                .addBodyParameter("temp_payment_option", params.get("temp_payment_option"))
                .setTag(Tags.CHECKOUT_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        getAddToTempBookingListResponse(response);
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
                        Snackbar.make(findViewById(android.R.id.content), "Something's wrong! Please contact administrator.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void getAddToTempBookingListResponse(JSONObject response){
        try {
            String status = response.getString("flag");
            String msg = response.has("msg") ? response.getString("msg") : "";

            if(status.equals("1")){
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }else{
                //error message
                Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar.make(findViewById(android.R.id.content), "Something's wrong! Please contact administrator.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void addToClientBookingList(HashMap<String, String> params){
        final ProgressDialog progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setMessage(Messages.PLEASE_WAIT_WHILE_ADDING_TO_BOOKING_LIST_MSG);
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.post(EndPoints.ADD_TO_CLIENT_BOOKING_LIST_URL)
                .addBodyParameter("talent_id", params.get("talent_id"))
                .addBodyParameter("client_id", params.get("client_id"))
                .addBodyParameter("preferred_date", params.get("preferred_date"))
                .addBodyParameter("preferred_time", params.get("preferred_time"))
                .addBodyParameter("preferred_venue", selectedVenue)
                .addBodyParameter("total_amount", params.get("total_amount"))
                .addBodyParameter("payment_option", params.get("payment_option"))
                .setTag(Tags.CHECKOUT_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        getClientBookingResponse(response);
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

    private void getClientBookingResponse(JSONObject response){
        try {
            String status = response.getString("status");
            String msg = response.has("msg") ? response.getString("msg") : "";

            if(status.equals("OK")){
                finish();
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), BookingListActivity.class));
            }else{
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
