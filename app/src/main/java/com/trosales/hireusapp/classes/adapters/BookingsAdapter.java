package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.activities.TalentModelProfileActivity;
import com.trosales.hireusapp.classes.commons.DateTimeHelper;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.wrappers.BookingsDO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder>{
    private List<BookingsDO> bookingsDOList;
    private Context context;

    public BookingsAdapter(List<BookingsDO> bookingsDOList, Context context) {
        this.bookingsDOList = bookingsDOList;
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
        final BookingsDO bookingsDO = bookingsDOList.get(i);
        //insert logic here
        Picasso
                .with(context)
                .load(bookingsDO.getTalentDetails().getTalentDisplayPhoto())
                .placeholder(R.drawable.no_profile_pic)
                .into(viewHolder.imgTalentDisplayPhoto);

        viewHolder.lblTalentFullname.setText(bookingsDO.getTalentDetails().getFullname());
        viewHolder.lblTalentCategories.setText(bookingsDO.getTalentDetails().getCategoryNames());
        viewHolder.lblTalentRatePerHour.setText(Html.fromHtml("&#8369;" + bookingsDO.getTalentDetails().getHourlyRate() + " per hour"));
        String preferredBookingTimeFrom = DateTimeHelper.convert24HrsTimeTo12hrs(bookingsDO.getPreferredBookingTimeFrom());
        String preferredBookingTimeTo = DateTimeHelper.convert24HrsTimeTo12hrs(bookingsDO.getPreferredBookingTimeTo());
        int totalHours = Integer.parseInt(
                            DateTimeHelper.getTotalHours(
                                bookingsDO.getPreferredBookingDateFrom(),
                                bookingsDO.getPreferredBookingDateTo(),
                                preferredBookingTimeFrom,
                                preferredBookingTimeTo
                            )
                        );

        BigDecimal hourlyRate = new BigDecimal(bookingsDO.getTalentDetails().getHourlyRate().replaceAll(",", ""));
        BigDecimal totalAmount = hourlyRate.multiply(new BigDecimal(totalHours));

        StringBuilder sbPreferredDateTime = new StringBuilder();
        sbPreferredDateTime
                .append("Start: ")
                .append(bookingsDO.getPreferredBookingDateFrom())
                .append(" ")
                .append(preferredBookingTimeFrom)
                .append("\nEnd: ")
                .append(bookingsDO.getPreferredBookingDateTo())
                .append(" ")
                .append(preferredBookingTimeTo)
                .append("\nTotal: ")
                .append(totalHours)
                .append(" hrs");

        viewHolder.lblBookingPreferredDateTime.setText(sbPreferredDateTime.toString());

        StringBuilder sbTotalAmount = new StringBuilder();
        sbTotalAmount
                .append(Html.fromHtml("Total Amount: &#8369"))
                .append(String.format(Locale.ENGLISH, "%,.2f", totalAmount.setScale(2, RoundingMode.DOWN)))
                .append(" (")
                .append(Html.fromHtml("<b>" + bookingsDO.getBookingStatus() + "</b>"))
                .append(")");

        viewHolder.lblBookingTotalAmount.setText(sbTotalAmount.toString());

        if(bookingsDO.getBookingStatus().equals("PAID")){
            viewHolder.lblBookingDatePaid.setVisibility(View.VISIBLE);
            viewHolder.lblBookingDatePaid.setText(bookingsDO.getBookingDatePaid());
        }else{
            viewHolder.lblBookingDatePaid.setVisibility(View.GONE);
        }

        viewHolder.btnMoreDetails.setOnClickListener(v -> {
            SharedPrefManager.getInstance(v.getContext()).saveTalentId(bookingsDO.getTalentDetails().getTalent_id());
            v.getContext().startActivity(new Intent(v.getContext(), TalentModelProfileActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return bookingsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_bookings) CardView cardView_bookings;
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
        @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;
        @BindView(R.id.lblTalentRatePerHour) TextView lblTalentRatePerHour;
        @BindView(R.id.lblBookingPreferredDateTime) TextView lblBookingPreferredDateTime;
        @BindView(R.id.lblBookingTotalAmount) TextView lblBookingTotalAmount;
        @BindView(R.id.lblBookingDatePaid) TextView lblBookingDatePaid;
        @BindView(R.id.btnMoreDetails) AppCompatButton btnMoreDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
