package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.DetectMonthName;
import com.trosales.hireusapp.classes.wrappers.UnavailableDatesDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UnavailableDatesAdapter extends RecyclerView.Adapter<UnavailableDatesAdapter.ViewHolder>{
    private List<UnavailableDatesDO> unavailableDatesDOList;
    private Context context;

    public UnavailableDatesAdapter(List<UnavailableDatesDO> unavailableDatesDOList, Context context) {
        this.unavailableDatesDOList = unavailableDatesDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public UnavailableDatesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.unavailable_dates_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnavailableDatesAdapter.ViewHolder viewHolder, int i) {
        final UnavailableDatesDO unavailableDatesDO = unavailableDatesDOList.get(i);

        String[] monthYearValues = unavailableDatesDO.getMonthYear().split("_");
        StringBuilder sbMonthYear = new StringBuilder();
        sbMonthYear
                .append(DetectMonthName.getMonthName(monthYearValues[0]))
                .append(" ")
                .append(monthYearValues[1]);

        viewHolder.lblMonthYear.setText(sbMonthYear.toString());
        viewHolder.lblDays.setText(unavailableDatesDO.getDays());
    }

    @Override
    public int getItemCount() {
        return unavailableDatesDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_unavailableDates) CardView cardView_unavailableDates;
        @BindView(R.id.lblMonthYear) TextView lblMonthYear;
        @BindView(R.id.lblDays) TextView lblDays;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
