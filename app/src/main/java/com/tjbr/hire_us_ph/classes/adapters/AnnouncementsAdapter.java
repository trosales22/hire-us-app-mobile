package com.tjbr.hire_us_ph.classes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tjbr.hire_us_ph.R;
import com.tjbr.hire_us_ph.classes.wrappers.AnnouncementsDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyTextView;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder>{
    private List<AnnouncementsDO> announcementsDOList;
    private Context context;

    public AnnouncementsAdapter(List<AnnouncementsDO> announcementsDOList, Context context) {
        this.announcementsDOList = announcementsDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_list, parent, false);

        return new AnnouncementsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementsAdapter.ViewHolder holder, int position) {
        final AnnouncementsDO announcementsDO = announcementsDOList.get(position);

        holder.lblAnnouncementCaption.setText(announcementsDO.getAnnouncementCaption());
        holder.lblAnnouncementCreator.setText(announcementsDO.getAnnouncementCreator());
        holder.lblAnnouncementCreatedDate.setText(announcementsDO.getAnnouncementCreatedDate());
        holder.lblAnnouncementDetails.setText(announcementsDO.getAnnouncementDetails());

        holder.announcementCardView.setOnClickListener(view -> {
            //do something
        });
    }

    @Override
    public int getItemCount() {
        return announcementsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.announcementCardView) CardView announcementCardView;
        @BindView(R.id.lblAnnouncementCaption) MyTextView lblAnnouncementCaption;
        @BindView(R.id.lblAnnouncementCreator) MyTextView lblAnnouncementCreator;
        @BindView(R.id.lblAnnouncementCreatedDate) MyTextView lblAnnouncementCreatedDate;
        @BindView(R.id.lblAnnouncementDetails) MyTextView lblAnnouncementDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
