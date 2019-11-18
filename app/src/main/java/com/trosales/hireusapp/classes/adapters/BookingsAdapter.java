package com.trosales.hireusapp.classes.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.TalentModelProfileActivity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.wrappers.ClientBookingsDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

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
        viewHolder.lblBookingPreferredDate.setText(clientBookingsDO.getPreferredBookingDate());
        viewHolder.lblBookingPreferredTime.setText(clientBookingsDO.getPreferredBookingTime());
        viewHolder.lblBookingPreferredVenue.setText(clientBookingsDO.getPreferredBookingVenue());

        String sbBookingOtherDetails = "Payment Option: " +
                getPaymentOption(clientBookingsDO.getBookingPaymentOption()) +
                "\nDate Paid: " +
                clientBookingsDO.getBookingDatePaid() +
                "\nTotal Amount: " +
                Html.fromHtml("&#8369;") +
                clientBookingsDO.getBookingTotalAmount();

        viewHolder.lblBookingOtherDetails.setText(sbBookingOtherDetails);
        viewHolder.btnTalentDetails.setOnClickListener(v -> {
            SharedPrefManager.getInstance(v.getContext()).saveTalentId(clientBookingsDO.getTalentDetails().getTalent_id());
            v.getContext().startActivity(new Intent(v.getContext(), TalentModelProfileActivity.class));
        });

        viewHolder.btnAddTalentReviews.setOnClickListener(v -> {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(v.getContext());
            @SuppressLint("InflateParams") View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_talent_reviews, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogTheme);
            alertDialogBuilderUserInput.setView(mView);

            EditText txtReviewsDescription = mView.findViewById(R.id.txtReviewsDescription);
            MaterialRatingBar talentRatingBar = mView.findViewById(R.id.talentRatingBar);

            //talentRatingBar.getRating();

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Submit", (dialogBox, id) -> {
//                        selectedVenue = userInputDialogEditText.getText().toString().trim();
//                        lblBookingVenueCaption.setText("Booking Venue: " + selectedVenue);
                        Toast.makeText(context, "rating: " + talentRatingBar.getRating(), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
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
        @BindView(R.id.lblBookingPreferredDate)TextView lblBookingPreferredDate;
        @BindView(R.id.lblBookingPreferredTime) TextView lblBookingPreferredTime;
        @BindView(R.id.lblBookingPreferredVenue) TextView lblBookingPreferredVenue;
        @BindView(R.id.lblBookingOtherDetails) TextView lblBookingOtherDetails;
        @BindView(R.id.btnTalentDetails) AppCompatButton btnTalentDetails;
        @BindView(R.id.btnAddTalentReviews) AppCompatButton btnAddTalentReviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
