package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.constants.FilesConstants;
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

    @Override
    public void onBindViewHolder(@NonNull TalentsAdapter.ViewHolder viewHolder, int i) {
        final TalentsDO talentsDO = talentsDOList.get(i);

        viewHolder.lblTalentFullname.setText(talentsDO.getFullname());
        viewHolder.lblTalentHeight.setText(talentsDO.getHeight());
        viewHolder.lblTalentEmail.setText(talentsDO.getEmail());
        viewHolder.lblTalentContactNumber.setText(talentsDO.getContactNumber());
        viewHolder.lblTalentLocation.setText(talentsDO.getLocation());

        Picasso
                .with(context)
                .load(FilesConstants.IMAGES_DIRECTORY.concat(talentsDO.getTalentDisplayPhoto()))
                .placeholder(R.drawable.no_profile_pic)
                .into(viewHolder.imgTalentDisplayPhoto);
    }

    @Override
    public int getItemCount() {
        return talentsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
        @BindView(R.id.lblTalentHeight) TextView lblTalentHeight;
        @BindView(R.id.lblTalentEmail) TextView lblTalentEmail;
        @BindView(R.id.lblTalentContactNumber) TextView lblTalentContactNumber;
        @BindView(R.id.lblTalentLocation) TextView lblTalentLocation;
        @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
