package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.PotentialClientsActivity;
import com.trosales.hireusapp.classes.wrappers.ClientsBookedDO;
import com.trosales.hireusapp.fragments.BookingAndClientDetailsBottomSheetFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyTextView;

public class PotentialClientsAdapter extends RecyclerView.Adapter<PotentialClientsAdapter.ViewHolder>{
    private List<ClientsBookedDO> clientsBookedDOList;
    private Context context;

    public PotentialClientsAdapter(List<ClientsBookedDO> clientsBookedDOList, Context context) {
        this.clientsBookedDOList = clientsBookedDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public PotentialClientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.potential_clients_list, viewGroup, false);

        return new ViewHolder(view);
    }

//    private String getPaymentOption(String paymentOption){
//        String returnValue = null;
//        switch (paymentOption){
//            case "DEBIT_CREDIT_CARD":
//                returnValue = "Debit/Credit Card";
//                break;
//            case "BANK_TRANSFER":
//                returnValue = "Bank Transfer";
//                break;
//            case "BANK_DEPOSIT":
//                returnValue = "Bank Deposit";
//                break;
//
//        }
//        return returnValue;
//    }

    @Override
    public void onBindViewHolder(@NonNull PotentialClientsAdapter.ViewHolder viewHolder, int i) {
        final ClientsBookedDO clientsBookedDO = clientsBookedDOList.get(i);

        int clientDisplayPhoto = 0;

        switch (clientsBookedDO.getClientDetailsDO().getClientRoleCode()){
            case "CLIENT_COMPANY":
                clientDisplayPhoto = R.drawable.customer_company;
                break;
            case "CLIENT_INDIVIDUAL":
                switch (clientsBookedDO.getClientDetailsDO().getClientGender()){
                    case "Male":
                        clientDisplayPhoto = R.drawable.customer_male;
                        break;
                    case "Female":
                        clientDisplayPhoto = R.drawable.customer_female;
                        break;
                }

                break;
        }

        Glide
                .with(context)
                .load(clientDisplayPhoto)
                .placeholder(R.drawable.customer_company)
                .into(viewHolder.imgClientDisplayPhoto);

        viewHolder.lblClientFullName.setText(clientsBookedDO.getClientDetailsDO().getClientFullname());
        viewHolder.lblBookingGeneratedId.setText(clientsBookedDO.getClientBookingsDO().getBookingGeneratedId());
        viewHolder.lblBookingOfferStatus.setText(clientsBookedDO.getClientBookingsDO().getBookingOfferStatus());

        viewHolder.clientsBookedCardView.setOnClickListener(view -> {
            Bundle bookingAndClientDetailsBundleArgs = new Bundle();

            //booking details
            bookingAndClientDetailsBundleArgs.putString("talent_id", clientsBookedDO.getClientBookingsDO().getTalentDetails().getTalent_id());
            bookingAndClientDetailsBundleArgs.putString("booking_generated_id", clientsBookedDO.getClientBookingsDO().getBookingGeneratedId());
            bookingAndClientDetailsBundleArgs.putString("booking_event_title", clientsBookedDO.getClientBookingsDO().getBookingEventTitle());
            bookingAndClientDetailsBundleArgs.putString("booking_talent_fee", clientsBookedDO.getClientBookingsDO().getBookingTalentFee());
            bookingAndClientDetailsBundleArgs.putString("booking_venue_location", clientsBookedDO.getClientBookingsDO().getBookingVenueLocation());
            bookingAndClientDetailsBundleArgs.putString("booking_payment_option", clientsBookedDO.getClientBookingsDO().getBookingPaymentOption());
            bookingAndClientDetailsBundleArgs.putString("booking_date", clientsBookedDO.getClientBookingsDO().getBookingDate());
            bookingAndClientDetailsBundleArgs.putString("booking_time", clientsBookedDO.getClientBookingsDO().getBookingTime());
            bookingAndClientDetailsBundleArgs.putString("booking_other_details", clientsBookedDO.getClientBookingsDO().getBookingOtherDetails());
            bookingAndClientDetailsBundleArgs.putString("booking_offer_status", clientsBookedDO.getClientBookingsDO().getBookingOfferStatus());
            bookingAndClientDetailsBundleArgs.putString("booking_created_date", clientsBookedDO.getClientBookingsDO().getBookingCreatedDate());
            bookingAndClientDetailsBundleArgs.putString("booking_decline_reason", clientsBookedDO.getClientBookingsDO().getBookingDeclineReason());
            bookingAndClientDetailsBundleArgs.putString("booking_approved_or_declined_date", clientsBookedDO.getClientBookingsDO().getBookingApprovedOrDeclinedDate());

            //client information
            bookingAndClientDetailsBundleArgs.putString("client_full_name", clientsBookedDO.getClientDetailsDO().getClientFullname());
            bookingAndClientDetailsBundleArgs.putString("client_email", clientsBookedDO.getClientDetailsDO().getClientEmail());
            bookingAndClientDetailsBundleArgs.putString("client_contact_number", clientsBookedDO.getClientDetailsDO().getClientContactNumber());
            bookingAndClientDetailsBundleArgs.putString("client_gender", clientsBookedDO.getClientDetailsDO().getClientGender());
            bookingAndClientDetailsBundleArgs.putString("client_type", clientsBookedDO.getClientDetailsDO().getClientRoleName());

            BookingAndClientDetailsBottomSheetFragment bookingAndClientDetailsBottomSheetFragment = new BookingAndClientDetailsBottomSheetFragment(bookingAndClientDetailsBundleArgs, view.getContext());
            bookingAndClientDetailsBottomSheetFragment.setCancelable(false);
            bookingAndClientDetailsBottomSheetFragment.show(((PotentialClientsActivity) context).getSupportFragmentManager(), "BookingAndClientDetailsBottomSheetFragment");
        });
    }

    @Override
    public int getItemCount() {
        return clientsBookedDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.clientsBookedCardView) CardView clientsBookedCardView;
        @BindView(R.id.imgClientDisplayPhoto) ImageView imgClientDisplayPhoto;
        @BindView(R.id.lblClientFullName) TextView lblClientFullName;
        @BindView(R.id.lblBookingGeneratedId) MyTextView lblBookingGeneratedId;
        @BindView(R.id.lblBookingOfferStatus) MyTextView lblBookingOfferStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
