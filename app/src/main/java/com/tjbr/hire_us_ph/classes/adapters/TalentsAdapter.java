package com.tjbr.hire_us_ph.classes.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tjbr.hire_us_ph.R;
import com.tjbr.hire_us_ph.activities.TalentModelProfileActivity;
import com.tjbr.hire_us_ph.classes.beans.CategoryDetector;
import com.tjbr.hire_us_ph.classes.commons.CategorySetter;
import com.tjbr.hire_us_ph.classes.constants.CategoriesConstants;
import com.tjbr.hire_us_ph.classes.wrappers.TalentsDO;

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

        Glide
                .with(context)
                .load(talentsDO.getTalentDisplayPhoto())
                .apply(new RequestOptions().fitCenter())
                .placeholder(R.drawable.ic_no_image)
                .into(viewHolder.imgTalentDisplayPhoto);

        viewHolder.lblTalentFullname.setText(talentsDO.getFullname());
        viewHolder.lblTalentAgeAndRegion.setText(talentsDO.getTalentAge() + " yrs old from " + talentsDO.getLocation().getRegion());

        CategoryDetector categoryDetector = new CategoryDetector(
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.CELEBRITY_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.COMEDIANS_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.DANCER_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.DISK_JOCKEY_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.EMCEE_HOST_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.MODELS_BA_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.MOVIE_TV_TALENTS_ID)),
                talentsDO.getCategoryIds().contains(String.valueOf(CategoriesConstants.SINGER_BAND_ID))
        );

        viewHolder.lblTalentCategories.setText(CategorySetter.getTalentCategory(categoryDetector));

        viewHolder.imgTalentDisplayPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TalentModelProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("talent_id", talentsDO.getTalent_id());

            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });

        viewHolder.cardView_talents.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TalentModelProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("talent_id", talentsDO.getTalent_id());

            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return talentsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_talents) CardView cardView_talents;
        @BindView(R.id.imgTalentDisplayPhoto) ImageView imgTalentDisplayPhoto;
        @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
        @BindView(R.id.lblTalentAgeAndRegion) TextView lblTalentAgeAndRegion;
        @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
