package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.TalentModelProfileActivity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.wrappers.ClientBookingsDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public void onBindViewHolder(@NonNull BookingsAdapter.ViewHolder viewHolder, int i) {
        final ClientBookingsDO clientBookingsDO = clientBookingsDOList.get(i);
        //insert logic here
        Picasso
                .with(context)
                .load(clientBookingsDO.getTalentDetails().getTalentDisplayPhoto())
                .placeholder(R.drawable.no_profile_pic)
                .into(viewHolder.imgTalentDisplayPhoto);

        viewHolder.lblTalentFullname.setText(clientBookingsDO.getTalentDetails().getFullname());
        viewHolder.lblTalentCategories.setText(clientBookingsDO.getTalentDetails().getCategoryNames());
        viewHolder.lblTalentRatePerHour.setText(Html.fromHtml("&#8369;" + clientBookingsDO.getTalentDetails().getHourlyRate() + " per hour"));
        viewHolder.lblBookingPreferredDate.setText(clientBookingsDO.getPreferredBookingDate());
        viewHolder.lblBookingPreferredTime.setText(clientBookingsDO.getPreferredBookingTime());
        viewHolder.lblBookingPreferredVenue.setText(clientBookingsDO.getPreferredBookingVenue());

        StringBuilder sbBookingOtherDetails = new StringBuilder();
        sbBookingOtherDetails
                .append("Payment Option: ")
                .append(getPaymentOption(clientBookingsDO.getBookingPaymentOption()))
                .append("\nDate Paid: ")
                .append(clientBookingsDO.getBookingDatePaid())
                .append("\nTotal Amount: ")
                .append(Html.fromHtml("&#8369;"))
                .append(clientBookingsDO.getBookingTotalAmount());

        viewHolder.lblBookingOtherDetails.setText( sbBookingOtherDetails.toString());
        viewHolder.btnMoreDetails.setOnClickListener(v -> {
            SharedPrefManager.getInstance(v.getContext()).saveTalentId(clientBookingsDO.getTalentDetails().getTalent_id());
            v.getContext().startActivity(new Intent(v.getContext(), TalentModelProfileActivity.class));
        });
    }

    private String getPaymentOption(String paymentOption){
        String returnValue = null;
        switch (paymentOption){
            case "DEBIT_CREDIT_CARD":
                returnValue = "Debit/Credit Card";
                break;
            case "BANK_TRANSFER":
                returnValue = "Bank Transfer";
                break;
            case "BANK_DEPOSIT":
                returnValue = "Bank Deposit";
                break;

        }
        return returnValue;
    }

    @Override
    public int getItemCount() {
        return clientBookingsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
        @BindView(R.id.lblTalentRatePerHour) TextView lblTalentRatePerHour;
        @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;
        @BindView(R.id.lblBookingPreferredDate)TextView lblBookingPreferredDate;
        @BindView(R.id.lblBookingPreferredTime) TextView lblBookingPreferredTime;
        @BindView(R.id.lblBookingPreferredVenue) TextView lblBookingPreferredVenue;
        @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;
        @BindView(R.id.btnMoreDetails) AppCompatButton btnMoreDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
