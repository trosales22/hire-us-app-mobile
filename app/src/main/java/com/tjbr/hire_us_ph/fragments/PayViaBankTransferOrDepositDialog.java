package com.tjbr.hire_us_ph.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tjbr.hire_us_ph.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayViaBankTransferOrDepositDialog extends DialogFragment {
    @BindView(R.id.lblBankTransferOrDepositDetails) TextView lblBankTransferOrDepositDetails;
    @BindView(R.id.btnCloseBankTransferDialog) AppCompatButton btnCloseBankTransferDialog;

    public PayViaBankTransferOrDepositDialog() {
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
        View view = inflater.inflate(R.layout.fragment_pay_via_bank_transfer_or_deposit_dialog, container, false);
        ButterKnife.bind(this, view);

        StringBuilder sbDetails = new StringBuilder();
        sbDetails
                .append("Kindly deposit the talent fee to any of the ff.\n\n")
                .append("BDO\n")
                .append("Account Name: ").append("Josh Saratan").append("\n")
                .append("Account Number: ").append("123456789").append("\n\n")
                .append("BPI\n")
                .append("Account Name: ").append("Albert Boholano").append("\n")
                .append("Account Number: ").append("987654321").append("\n\n")
                .append("Once done, please send a copy of deposit slip to hireusph@gmail.com for verification. Thank you!");

        lblBankTransferOrDepositDetails.setText(sbDetails.toString());

        btnCloseBankTransferDialog.setOnClickListener(v -> this.dismiss());

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
