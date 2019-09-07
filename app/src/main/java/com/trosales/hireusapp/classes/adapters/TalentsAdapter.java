package com.trosales.hireusapp.classes.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.wrappers.TalentsDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalentsAdapter extends RecyclerView.Adapter<TalentsAdapter.ViewHolder>{
    private List<TalentsDO> talentsDOList;
    private Context context;

    public TalentsAdapter(List<TalentsDO> talentsDOList, Context context) {
        this.talentsDOList = talentsDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public TalentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.talents_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TalentsAdapter.ViewHolder viewHolder, int i) {
        final TalentsDO talentsDO = talentsDOList.get(i);

        viewHolder.cardView_talents.setOnClickListener(v -> {
            SharedPrefManager.getInstance(v.getContext()).saveTalentId(talentsDO.getTalent_id());
            v.getContext().startActivity(new Intent(v.getContext(), TalentModelProfileActivity.class));
        });

        viewHolder.lblTalentFullname.setText(talentsDO.getFullname());
        viewHolder.lblTalentGenderAndAge.setText(talentsDO.getGender() + " | " + talentsDO.getTalentAge() + " years old");
        viewHolder.lblTalentLocation.setText(talentsDO.getLocation().getCityMuni() + ", " + talentsDO.getLocation().getProvince());
        viewHolder.lblTalentHourlyRate.setText(Html.fromHtml("&#8369;" + talentsDO.getHourlyRate() + " per hour"));
        viewHolder.lblTalentCategories.setText(talentsDO.getCategoryNames());

        Picasso
                .with(context)
                .load(talentsDO.getTalentDisplayPhoto())
                .placeholder(R.drawable.no_profile_pic)
                .into(viewHolder.imgTalentDisplayPhoto);
    }

    @Override
    public int getItemCount() {
        return talentsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_talents) CardView cardView_talents;
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
        @BindView(R.id.lblTalentGenderAndAge) TextView lblTalentGenderAndAge;
        @BindView(R.id.lblTalentLocation) TextView lblTalentLocation;
        @BindView(R.id.lblTalentHourlyRate) TextView lblTalentHourlyRate;
        @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
