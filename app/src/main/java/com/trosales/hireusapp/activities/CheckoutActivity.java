package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;

import org.json.JSONException;

import java.math.BigDecimal;
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
    @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;
    @BindView(R.id.btnCancelBooking) AppCompatButton btnCancelBooking;
    @BindView(R.id.btnPayUsingDebitOrCreditCard) AppCompatButton btnPayUsingDebitOrCreditCard;

    private String selectedDate, selectedTime, selectedTalentId;

    private Bundle bundle;

    private static final String PAYPAL_KEY = "AZ9DrSKHmGSwUNTXAe5kTJCw3yDXWhUwMjUDevXJaP3-4khsDuNb3dS-x1E0tEhg0C2jHBFMQ8gb5rKs";
    private static final int REQUEST_CODE_PAYMENT  = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static PayPalConfiguration payPalConfiguration;
    PayPalPayment payPalPayment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();
        selectedDate = Objects.requireNonNull(bundle).getString("temp_booking_date");
        selectedTime = Objects.requireNonNull(bundle).getString("temp_booking_time");
        selectedTalentId = Objects.requireNonNull(bundle).getString("temp_talent_id");

        configPayPal();

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

        StringBuilder sbBookingOtherDetails = new StringBuilder();
        sbBookingOtherDetails
                .append("Total hours: ")
                .append(bundle.getString("total_hours"))
                .append("\nTotal amount: ")
                .append(Html.fromHtml("&#8369;"))
                .append(bundle.getString("total_amount"));

        lblBookingOtherDetails.setText( sbBookingOtherDetails.toString());

        btnCancelBooking.setOnClickListener(v -> {
            //insert logic here
        });

        btnPayUsingDebitOrCreditCard.setOnClickListener(v -> {
            payUsingDebitOrCreditCard();
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

    private void configPayPal(){
        payPalConfiguration = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(PAYPAL_KEY)
                .merchantName("PayPal Login")
                .merchantPrivacyPolicyUri(Uri.parse("https://trosales.netlify.com/"))
                .merchantUserAgreementUri(Uri.parse("https://trosales.netlify.com/"));
    }

    private void payUsingDebitOrCreditCard(){
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);

        payPalPayment = new PayPalPayment(
                //new BigDecimal(Objects.requireNonNull(bundle.getString("total_amount")).replace(",","")),
                new BigDecimal("1.00"),
                "PHP",
                "Payment",
                PayPalPayment.PAYMENT_INTENT_ORDER
        );

        Intent intentPayment = new Intent(this, PaymentActivity.class);
        intentPayment.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        intentPayment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startActivityForResult(intentPayment, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PAYMENT){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation paymentConfirmation = Objects.requireNonNull(data).getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation != null){
                    try{
                        Log.d("debug", paymentConfirmation.toJSONObject().toString(4));
                        Log.d("debug", paymentConfirmation.getPayment().toJSONObject().toString(4));
                        Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Payment has been cancelled!", Toast.LENGTH_LONG).show();
            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Error occurred!", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == REQUEST_CODE_FUTURE_PAYMENT){
            if(resultCode == Activity.RESULT_OK){
                PayPalAuthorization payPalAuthorization = Objects.requireNonNull(data).getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if(payPalAuthorization != null){
                    try{
                        Log.d("debug", "FuturePaymentExample: " + payPalAuthorization.toJSONObject().toString(4));
                        String authCode = payPalAuthorization.getAuthorizationCode();
                        Log.d("debug", "Authorization Code: " + authCode);
                    }catch(Exception ex){
                        ex.getStackTrace();
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Payment has been cancelled!", Toast.LENGTH_LONG).show();
            }else if(resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this, "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
