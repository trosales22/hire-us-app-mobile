package com.agentcoder.hire_us_ph.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.agentcoder.hire_us_ph.R;
import com.agentcoder.hire_us_ph.activities.BookingListActivity;
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

public class PayViaDebitCreditCardDialog extends DialogFragment {
    @BindView(R.id.cardMultilineWidget) CardMultilineWidget cardMultilineWidget;
    @BindView(R.id.btnContinuePayment) AppCompatButton btnContinuePayment;
    @BindView(R.id.btnClosePayment) AppCompatButton btnClosePayment;

    private Stripe stripe;
    private SweetAlertDialog sweetAlertDialog;
    private Bundle bookingArgs;

    public PayViaDebitCreditCardDialog(Bundle bookingArgs) {
        this.bookingArgs = bookingArgs;
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            return keyCode == KeyEvent.KEYCODE_BACK;

        });
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_via_debit_credit_card_dialog, container, false);
        ButterKnife.bind(this, view);

        stripe = new Stripe(view.getContext(), "pk_test_pHHixfchBGl7tkRAanAoe7TN00O13SSzVp");

        btnClosePayment.setOnClickListener(v -> this.dismiss());

        btnContinuePayment.setOnClickListener(v -> {
            final Card cardToSave = cardMultilineWidget.getCard();

            if (cardToSave != null) {
                stripe.createToken(cardToSave, new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(Token token) {
                        if(sweetAlertDialog != null){
                            sweetAlertDialog.dismissWithAnimation();
                        }

                        sweetAlertDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#48b1bf"));
                        sweetAlertDialog.setTitleText(Messages.PLEASE_WAIT_MSG);
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();

                        StringBuilder sbDescription = new StringBuilder();
                        sbDescription
                                .append(bookingArgs.getString("booking_generated_id"))
                                .append(" (").append(bookingArgs.getString("booking_talent_fee")).append(") ");

                        AndroidNetworking.post(EndPoints.START_PAYMENT_URL)
                                .addBodyParameter("stripe_token", token.getId())
                                .addBodyParameter("booking_generated_id", bookingArgs.getString("booking_generated_id"))
                                .addBodyParameter("booking_payment_option", bookingArgs.getString("booking_payment_option"))
                                .addBodyParameter("amount", bookingArgs.getString("booking_talent_fee").replace(",", ""))
                                .addBodyParameter("description", sbDescription.toString())
                                .setTag(Tags.CHECKOUT_ACTIVITY)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if(sweetAlertDialog != null){
                                            sweetAlertDialog.dismissWithAnimation();
                                        }

                                        try {
                                            String status = response.getString("flag");
                                            String msg = response.has("msg") ? response.getString("msg") : "";

                                            if(status.equals("1")){
                                                sweetAlertDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                        .setTitleText("Congratulations!")
                                                        .setContentText(msg)
                                                        .setConfirmClickListener(sweetAlertDialog -> {
                                                            sweetAlertDialog.dismissWithAnimation();
                                                            PayViaDebitCreditCardDialog.this.dismiss();
                                                            startActivity(new Intent(getContext(), BookingListActivity.class));
                                                        });

                                                sweetAlertDialog.show();
                                            }else{
                                                sweetAlertDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("Oops..")
                                                        .setContentText(msg);

                                                sweetAlertDialog.show();
                                            }
                                        } catch (JSONException e) {
                                            if(sweetAlertDialog != null){
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        if(sweetAlertDialog != null){
                                            sweetAlertDialog.dismissWithAnimation();
                                        }

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
                        if(sweetAlertDialog != null){
                            sweetAlertDialog.dismissWithAnimation();
                        }

                        sweetAlertDialog = new SweetAlertDialog(view.getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops..")
                                .setContentText(e.getLocalizedMessage());

                        sweetAlertDialog.show();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = Objects.requireNonNull(getDialog()).getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
