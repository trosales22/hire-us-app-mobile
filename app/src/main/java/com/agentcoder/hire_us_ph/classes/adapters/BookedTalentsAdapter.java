package com.agentcoder.hire_us_ph.classes.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.agentcoder.hire_us_ph.R;
import com.agentcoder.hire_us_ph.activities.BookingListActivity;
import com.agentcoder.hire_us_ph.classes.wrappers.ClientBookingsDO;
import com.agentcoder.hire_us_ph.fragments.BookingAndTalentDetailsBottomSheetFragment;
import com.agentcoder.hire_us_ph.fragments.PayViaBankTransferOrDepositDialog;
import com.agentcoder.hire_us_ph.fragments.PayViaDebitCreditCardDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyTextView;

public class BookedTalentsAdapter extends RecyclerView.Adapter<BookedTalentsAdapter.ViewHolder>{
    private List<ClientBookingsDO> clientBookingsDOList;
    private Context context;

    public BookedTalentsAdapter(List<ClientBookingsDO> clientBookingsDOList, Context context) {
        this.clientBookingsDOList = clientBookingsDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookedTalentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bookings_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookedTalentsAdapter.ViewHolder viewHolder, int i) {
        final ClientBookingsDO clientBookingsDO = clientBookingsDOList.get(i);

        Glide
                .with(context)
                .load(clientBookingsDO.getTalentDetails().getTalentDisplayPhoto())
                .apply(new RequestOptions().fitCenter())
                .placeholder(R.drawable.ic_no_image)
                .into(viewHolder.imgTalentDisplayPhoto);

        if(clientBookingsDO.getBookingDatePaid().equalsIgnoreCase("PENDING")){
            switch (clientBookingsDO.getBookingOfferStatus()){
                case "WAITING":
                    viewHolder.lblBookingPaymentStatus.setVisibility(View.GONE);
                    viewHolder.btnPayNow.setVisibility(View.GONE);

                    break;
                case "APPROVED":
                    switch (clientBookingsDO.getBookingPaymentStatus()){
                        case "ACTIVE":
                            viewHolder.lblBookingPaymentStatus.setText("Pay " + Html.fromHtml("&#8369;") + clientBookingsDO.getBookingTalentFee() + " on or before " + clientBookingsDO.getBookingPayUntil());
                            viewHolder.btnPayNow.setVisibility(View.VISIBLE);

                            break;
                        case "EXPIRED":
                            viewHolder.lblBookingPaymentStatus.setText("Payment Expired");
                            viewHolder.btnPayNow.setVisibility(View.GONE);

                            break;
                    }

                    break;
            }
        }else{
            viewHolder.lblBookingPaymentStatus.setText("Paid on " + clientBookingsDO.getBookingDatePaid());
            viewHolder.btnPayNow.setVisibility(View.GONE);
        }

        viewHolder.btnPayNow.setOnClickListener(v -> {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                    .setTitle("Pay Via")
                    .addButton("DEBIT/CREDIT CARD", Color.parseColor("#FFFFFF"), Color.parseColor("#48b1bf"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();

                        Bundle bookingsBundleArgs = new Bundle();
                        bookingsBundleArgs.putString("talent_id", clientBookingsDO.getTalentDetails().getTalent_id());
                        bookingsBundleArgs.putString("booking_generated_id", clientBookingsDO.getBookingGeneratedId());
                        bookingsBundleArgs.putString("booking_event_title", clientBookingsDO.getBookingEventTitle());
                        bookingsBundleArgs.putString("booking_talent_fee", clientBookingsDO.getBookingTalentFee());
                        bookingsBundleArgs.putString("booking_venue_location", clientBookingsDO.getBookingVenueLocation());
                        bookingsBundleArgs.putString("booking_payment_option", "DEBIT/CREDIT CARD");
                        bookingsBundleArgs.putString("booking_date", clientBookingsDO.getBookingDate());
                        bookingsBundleArgs.putString("booking_time", clientBookingsDO.getBookingTime());
                        bookingsBundleArgs.putString("booking_other_details", clientBookingsDO.getBookingOtherDetails());
                        bookingsBundleArgs.putString("booking_offer_status", clientBookingsDO.getBookingOfferStatus());
                        bookingsBundleArgs.putString("booking_created_date", clientBookingsDO.getBookingCreatedDate());
                        bookingsBundleArgs.putString("booking_decline_reason", clientBookingsDO.getBookingDeclineReason());
                        bookingsBundleArgs.putString("booking_approved_or_declined_date", clientBookingsDO.getBookingApprovedOrDeclinedDate());

                        PayViaDebitCreditCardDialog payViaDebitCreditCardDialog = new PayViaDebitCreditCardDialog(bookingsBundleArgs);
                        payViaDebitCreditCardDialog.show(((BookingListActivity) context).getSupportFragmentManager(), "payViaDebitCreditCardDialog");
                    })
                    .addButton("BANK DEPOSIT/TRANSFER", Color.parseColor("#FFFFFF"), Color.parseColor("#003f3f"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        dialog.dismiss();

                        PayViaBankTransferOrDepositDialog payViaBankTransferOrDepositDialog = new PayViaBankTransferOrDepositDialog();
                        payViaBankTransferOrDepositDialog.show(((BookingListActivity) context).getSupportFragmentManager(), "payViaBankTransferOrDepositDialog");
                    });

            builder.show();
        });

        viewHolder.cardView_bookings.setOnClickListener(view -> {
            Bundle bookingsBundleArgs = new Bundle();
            bookingsBundleArgs.putString("talent_id", clientBookingsDO.getTalentDetails().getTalent_id());
            bookingsBundleArgs.putString("booking_generated_id", clientBookingsDO.getBookingGeneratedId());
            bookingsBundleArgs.putString("booking_event_title", clientBookingsDO.getBookingEventTitle());
            bookingsBundleArgs.putString("booking_talent_fee", clientBookingsDO.getBookingTalentFee());
            bookingsBundleArgs.putString("booking_venue_location", clientBookingsDO.getBookingVenueLocation());
            bookingsBundleArgs.putString("booking_payment_option", clientBookingsDO.getBookingPaymentOption());
            bookingsBundleArgs.putString("booking_date", clientBookingsDO.getBookingDate());
            bookingsBundleArgs.putString("booking_time", clientBookingsDO.getBookingTime());
            bookingsBundleArgs.putString("booking_other_details", clientBookingsDO.getBookingOtherDetails());
            bookingsBundleArgs.putString("booking_offer_status", clientBookingsDO.getBookingOfferStatus());
            bookingsBundleArgs.putString("booking_created_date", clientBookingsDO.getBookingCreatedDate());
            bookingsBundleArgs.putString("booking_decline_reason", clientBookingsDO.getBookingDeclineReason());
            bookingsBundleArgs.putString("booking_approved_or_declined_date", clientBookingsDO.getBookingApprovedOrDeclinedDate());
            bookingsBundleArgs.putString("booking_pay_on_or_before", clientBookingsDO.getBookingPayUntil());
            bookingsBundleArgs.putString("booking_payment_status", clientBookingsDO.getBookingPaymentStatus());

            BookingAndTalentDetailsBottomSheetFragment bookingAndTalentDetailsBottomSheetFragment = new BookingAndTalentDetailsBottomSheetFragment(bookingsBundleArgs, view.getContext());
            bookingAndTalentDetailsBottomSheetFragment.setCancelable(false);
            bookingAndTalentDetailsBottomSheetFragment.show(((BookingListActivity) context).getSupportFragmentManager(), "BookingAndTalentDetailsBottomSheetFragment");
        });

        viewHolder.lblTalentFullName.setText(clientBookingsDO.getTalentDetails().getFullname());
        viewHolder.lblBookingGeneratedId.setText(clientBookingsDO.getBookingGeneratedId());

        if(clientBookingsDO.getBookingPayUntil().equalsIgnoreCase("NOT YET APPROVED/DECLINED") && clientBookingsDO.getBookingPaymentStatus().equalsIgnoreCase("NOT YET APPROVED/DECLINED")){
            viewHolder.lblBookingOfferStatus.setText(clientBookingsDO.getBookingPaymentStatus());
        }else{
            viewHolder.lblBookingOfferStatus.setText(clientBookingsDO.getBookingOfferStatus());
        }
    }

    @Override
    public int getItemCount() {
        return clientBookingsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_bookings) CardView cardView_bookings;
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullName) MyTextView lblTalentFullName;
        @BindView(R.id.lblBookingGeneratedId) MyTextView lblBookingGeneratedId;
        @BindView(R.id.lblBookingOfferStatus) MyTextView lblBookingOfferStatus;
        @BindView(R.id.lblBookingPaymentStatus) MyTextView lblBookingPaymentStatus;
        @BindView(R.id.btnPayNow) Button btnPayNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
