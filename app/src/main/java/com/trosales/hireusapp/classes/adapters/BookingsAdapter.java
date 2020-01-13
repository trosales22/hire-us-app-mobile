package com.trosales.hireusapp.classes.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.BookingListActivity;
import com.trosales.hireusapp.classes.wrappers.ClientBookingsDO;
import com.trosales.hireusapp.fragments.BookingDetailsBottomSheetFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyTextView;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder>{
    private List<ClientBookingsDO> clientBookingsDOList;
    private Context context;

    public BookingsAdapter(List<ClientBookingsDO> clientBookingsDOList, Context context) {
        this.clientBookingsDOList = clientBookingsDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bookings_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder viewHolder, int i) {
        final ClientBookingsDO clientBookingsDO = clientBookingsDOList.get(i);

        Glide
                .with(context)
                .load(clientBookingsDO.getTalentDetails().getTalentDisplayPhoto())
                .placeholder(R.drawable.no_profile_pic)
                .into(viewHolder.imgTalentDisplayPhoto);

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

            BookingDetailsBottomSheetFragment bookingDetailsBottomSheetFragment = new BookingDetailsBottomSheetFragment(bookingsBundleArgs, view.getContext());
            bookingDetailsBottomSheetFragment.show(((BookingListActivity) context).getSupportFragmentManager(), "BookingDetailsBottomSheetFragment");
        });

        viewHolder.lblTalentFullName.setText(clientBookingsDO.getTalentDetails().getFullname());
        viewHolder.lblTalentCategories.setText(clientBookingsDO.getTalentDetails().getCategoryNames());
        viewHolder.lblBookingGeneratedId.setText(Html.fromHtml("<b>Booking ID:</b>") + "\n" + clientBookingsDO.getBookingGeneratedId());
        viewHolder.lblBookingOfferStatus.setText(Html.fromHtml("<b>Booking Offer Status:</b>") + "\n" + clientBookingsDO.getBookingOfferStatus());
    }

    @Override
    public int getItemCount() {
        return clientBookingsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_bookings) CardView cardView_bookings;
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullName) MyTextView lblTalentFullName;
        @BindView(R.id.lblTalentCategories) MyTextView lblTalentCategories;
        @BindView(R.id.lblBookingGeneratedId) MyTextView lblBookingGeneratedId;
        @BindView(R.id.lblBookingOfferStatus) MyTextView lblBookingOfferStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
