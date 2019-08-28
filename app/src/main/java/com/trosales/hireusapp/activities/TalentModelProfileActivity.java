package com.trosales.hireusapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.beans.Reviews;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyTextView;
import ws.wolfsoft.get_detail.ChildAnimationExample;
import ws.wolfsoft.get_detail.ExpandableHeightListView;
import ws.wolfsoft.get_detail.JayBaseAdapter;
import ws.wolfsoft.get_detail.SliderLayout;

public class TalentModelProfileActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{
    @BindView(R.id.lblTalentFullname) MyTextView lblTalentFullname;
    @BindView(R.id.lblTalentCategory) MyTextView lblTalentCategory;
    @BindView(R.id.lblTalentHourlyRate_Genre_VitalStatsCaption) MyTextView lblTalentHourlyRate_Genre_VitalStatsCaption;
    @BindView(R.id.lblTalentHourlyRate_Genre_VitalStats) MyTextView lblTalentHourlyRate_Genre_VitalStats;
    @BindView(R.id.lblTalentFollowers) MyTextView lblTalentFollowers;
    @BindView(R.id.lblTalentAge) MyTextView lblTalentAge;
    @BindView(R.id.lblTalentLocation) MyTextView lblTalentLocation;
    @BindView(R.id.lblTalentDescription) MyTextView lblTalentDescription;
    @BindView(R.id.lblExperiencesOrPreviousClients) MyTextView lblExperiencesOrPreviousClients;
    @BindView(R.id.btnAddToBookingList) MyTextView btnAddToBookingList;
    @BindView(R.id.btnInquireNow) MyTextView btnInquireNow;
    @BindView(R.id.linearLayoutShowMore) LinearLayout linearLayoutShowMore;
    @BindView(R.id.linearLayoutShowLess) LinearLayout linearLayoutShowLess;
    @BindView(R.id.linearLayoutShowMoreDetails) LinearLayout linearLayoutShowMoreDetails;
    @BindView(R.id.reviewsListView) ExpandableHeightListView reviewsListView;
    @BindView(R.id.talentGallerySlider) SliderLayout talentGallerySlider;

    private int[] REVIEWEE_DISPLAY_PIC = {R.drawable.p1, R.drawable.p2, R.drawable.p3};
    private String[] COMMENT = {"The best!", "Highly recommended!", "Awesome. Worth the fee!"};
    private String[] RATING = {"4.5 rating", "5 rating", "4 rating"};
    private String[] REVIEWEE = {"by Josh Saratan","by Albert Boholano","by Tristan Rosales"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_model_profile);
        ButterKnife.bind(this);

        ArrayList<Reviews> reviewsArrayList = new ArrayList<>();

        for (int i= 0; i< COMMENT.length; i++){
            Reviews reviews = new Reviews(REVIEWEE_DISPLAY_PIC[i], COMMENT[i], RATING[i], REVIEWEE[i]);
            reviewsArrayList.add(reviews);
        }

        JayBaseAdapter baseAdapter = new JayBaseAdapter(TalentModelProfileActivity.this, reviewsArrayList) {
        };

        reviewsListView.setAdapter(baseAdapter);

        linearLayoutShowMore.setOnClickListener(v -> {
            linearLayoutShowMoreDetails.setVisibility(View.VISIBLE);
            linearLayoutShowMore.setVisibility(View.GONE);
        });

        linearLayoutShowLess.setOnClickListener(v -> {
            linearLayoutShowMoreDetails.setVisibility(View.GONE);
            linearLayoutShowLess.setVisibility(View.VISIBLE);
        });

//         ********Slider*********

        HashMap<String,Integer> file_maps = new HashMap<>();
        file_maps.put("1", R.drawable.iohone1);
        file_maps.put("2",R.drawable.iphone2);
        file_maps.put("3",R.drawable.iphone3);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            talentGallerySlider.addSlider(textSliderView);
        }

        talentGallerySlider.setPresetTransformer(SliderLayout.Transformer.Default);
        talentGallerySlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        talentGallerySlider.setCustomAnimation(new ChildAnimationExample());
        talentGallerySlider.setDuration(3000);
        talentGallerySlider.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }
}
