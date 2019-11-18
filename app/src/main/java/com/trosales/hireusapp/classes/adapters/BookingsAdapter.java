package com.trosales.hireusapp.classes.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.CheckoutActivity;
import com.trosales.hireusapp.activities.TalentModelProfileActivity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.ClientBookingsDO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.Font;

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

            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Submit", (dialogBox, id) -> {
                        if(txtReviewsDescription.getText().toString().trim().isEmpty()){
                            txtReviewsDescription.setError("Please write some reviews..");
                        }else{
                            new EZDialog.Builder(context)
                                    .setTitle("Review Confirmation")
                                    .setMessage("Are you sure you want to submit this review?")
                                    .setPositiveBtnText("Yes")
                                    .setNegativeBtnText("No")
                                    .setButtonTextColor(R.color.colorPrimaryDarker)
                                    .setTitleTextColor(R.color.white)
                                    .setMessageTextColor(R.color.black)
                                    .setFont(Font.COMFORTAA)
                                    .setCancelableOnTouchOutside(false)
                                    .OnPositiveClicked(() -> {
                                        final ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(Messages.PLEASE_WAIT_MSG);
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();

                                        AndroidNetworking.post(EndPoints.ADD_TALENT_REVIEWS_URL)
                                                .addBodyParameter("review_feedback", txtReviewsDescription.getText().toString().trim())
                                                .addBodyParameter("review_rating", String.valueOf(talentRatingBar.getRating()))
                                                .addBodyParameter("review_to", clientBookingsDO.getTalentDetails().getTalent_id())
                                                .addBodyParameter("review_from", SharedPrefManager.getInstance(context).getUserId())
                                                .setTag(Tags.BOOKING_LIST_ACTIVITY)
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
                                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                                                dialogBox.dismiss();
                                                            }else{
                                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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

                                                        Log.e(Tags.BOOKING_LIST_ACTIVITY, errorResponse);
                                                    }
                                                });
                                    })
                                    .OnNegativeClicked(() -> {
                                        //todo
                                    })
                                    .build();
                        }



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
