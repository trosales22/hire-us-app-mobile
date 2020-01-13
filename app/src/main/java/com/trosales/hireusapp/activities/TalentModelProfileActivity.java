package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.beans.CategoryDetector;
import com.trosales.hireusapp.classes.beans.Reviews;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.CategoriesConstants;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.constants.Variables;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    @BindView(R.id.linearLayoutVitalStats) LinearLayout linearLayoutVitalStats;
    @BindView(R.id.lblTalentVitalStats) MyTextView lblTalentVitalStats;
    @BindView(R.id.linearLayoutTalentGenre) LinearLayout linearLayoutTalentGenre;
    @BindView(R.id.lblTalentGenre) MyTextView lblTalentGenre;
    @BindView(R.id.lblTalentFollowers) MyTextView lblTalentFollowers;
    @BindView(R.id.linearLayoutLocation) LinearLayout linearLayoutLocation;
    @BindView(R.id.lblTalentLocation) MyTextView lblTalentLocation;
    @BindView(R.id.lblTalentDescription) MyTextView lblTalentDescription;
    @BindView(R.id.lblTalentHeightCaption) MyTextView lblTalentHeightCaption;
    @BindView(R.id.lblTalentHeight) MyTextView lblTalentHeight;
    @BindView(R.id.lblExperiencesOrPreviousClientsCaption) MyTextView lblExperiencesOrPreviousClientsCaption;
    @BindView(R.id.lblExperiencesOrPreviousClients) MyTextView lblExperiencesOrPreviousClients;
    @BindView(R.id.btnAddToBookingList) MyTextView btnAddToBookingList;
    @BindView(R.id.btnInquireNow) MyTextView btnInquireNow;
    @BindView(R.id.linearLayoutShowMore) LinearLayout linearLayoutShowMore;
    @BindView(R.id.linearLayoutShowLess) LinearLayout linearLayoutShowLess;
    @BindView(R.id.linearLayoutShowMoreDetails) LinearLayout linearLayoutShowMoreDetails;
    @BindView(R.id.linearLayoutTalentDescription) LinearLayout linearLayoutTalentDescription;
    @BindView(R.id.linearLayoutTalentHeight) LinearLayout linearLayoutTalentHeight;
    @BindView(R.id.linearLayoutExperienceOrPreviousClients) LinearLayout linearLayoutExperienceOrPreviousClients;
    @BindView(R.id.reviewsListView) ExpandableHeightListView reviewsListView;
    @BindView(R.id.talentGallerySlider) SliderLayout talentGallerySlider;

    private ArrayList<Reviews> reviewsArrayList;
    private String talentFullName, talentProfilePic, selectedDate = "", selectedTime = "", selectedTalentId = "";
    private List<String> availableDateScheduleItems, availableTimeScheduleItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_model_profile);
        ButterKnife.bind(this);

        AppSecurity.disableScreenshotRecording(this);

        Bundle talentProfileBundleArgs = getIntent().getExtras();
        selectedTalentId = Objects.requireNonNull(talentProfileBundleArgs).getString("talent_id");

        reviewsArrayList = new ArrayList<>();
        availableDateScheduleItems = new ArrayList<>();
        availableTimeScheduleItems = new ArrayList<>();

        availableDateScheduleItems = getDatesUpToSpecificMonths(1);
        setMorningSchedule();
        setAfternoonSchedule();

        getTalentDetails();
        getTalentReviews();

        linearLayoutShowMore.setOnClickListener(v -> {
            linearLayoutShowMoreDetails.setVisibility(View.VISIBLE);
            linearLayoutShowLess.setVisibility(View.VISIBLE);
            linearLayoutShowMore.setVisibility(View.GONE);
        });

        linearLayoutShowLess.setOnClickListener(v -> {
            linearLayoutShowMoreDetails.setVisibility(View.GONE);
            linearLayoutShowLess.setVisibility(View.GONE);
            linearLayoutShowMore.setVisibility(View.VISIBLE);
        });

        btnAddToBookingList.setOnClickListener(v -> {
            new LovelyChoiceDialog(this, R.style.TintTheme)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Choose Preferred Date")
                    .setIcon(R.drawable.ic_date_range_white)
                    .setItemsMultiChoice(availableDateScheduleItems, (datePositions, dateItems) -> {
                                selectedDate = TextUtils.join(",", dateItems);

                                new LovelyChoiceDialog(TalentModelProfileActivity.this, R.style.TintTheme)
                                        .setTopColorRes(R.color.colorPrimary)
                                        .setTitle("Choose Preferred Time")
                                        .setMessage("Selected date: " + selectedDate)
                                        .setIcon(R.drawable.ic_access_time_white)
                                        .setItemsMultiChoice(availableTimeScheduleItems, (timePositions, timeItems) -> {
                                                    selectedTime = TextUtils.join(",", timeItems);

                                                    Intent intent = new Intent(this, SetBookingDetailsActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("talent_id", selectedTalentId);
                                                    bundle.putString("client_id", SharedPrefManager.getInstance(this).getUserId());
                                                    bundle.putString("talent_fullname", talentFullName);
                                                    bundle.putString("talent_profile_pic", talentProfilePic);
                                                    bundle.putString("talent_category", lblTalentCategory.getText().toString().trim());
                                                    bundle.putString("selected_date", selectedDate);
                                                    bundle.putString("selected_time", selectedTime);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                        )
                                        .setConfirmButtonText("Done")
                                        .show();
                            }
                    )
                    .setConfirmButtonText("Next")
                    .show();
        });
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
    }

    private List<String> getDatesUpToSpecificMonths(int months){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        List<String> availableDatesList = new ArrayList<>();

        try {
            c.setTime(Objects.requireNonNull(sdf.parse(sdf.format(new Date()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH) * months;
        for(int co=0; co<=maxDay; co++){
            c.add(Calendar.DATE, 1);
            availableDatesList.add(sdf.format(c.getTime()));
        }

        return availableDatesList;
    }

    private void setMorningSchedule(){
        int initialValue = 1;
        String meridian;

        availableTimeScheduleItems.add("12-1 AM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " PM";
            }else{
                meridian = " AM";
            }

            availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }

    private void setAfternoonSchedule(){
        int initialValue = 1;
        String meridian;

        availableTimeScheduleItems.add("12-1 PM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " AM";
            }else{
                meridian = " PM";
            }

            availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }

    private void getTalentDetails(){
        AndroidNetworking
                .get(EndPoints.GET_SELECTED_TALENT_DETAILS_URL.concat("?talent_id={talent_id}"))
                .addPathParameter("talent_id", selectedTalentId)
                .setTag(Tags.TALENT_DETAILS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("flag") && response.has("msg")){
                                Log.d("debug", response.getString("msg"));
                            }else{
                                talentFullName = response.getString("fullname");
                                talentProfilePic = response.getString("talent_display_photo");

                                HashMap<String,String> file_maps = new HashMap<>();

                                if(talentProfilePic.isEmpty() || "null".equals(talentProfilePic)){
                                    file_maps.put("1", Uri.parse(getResources().getDrawable(R.drawable.no_profile_pic).toString()).toString());
                                }else{
                                    file_maps.put("1", talentProfilePic);
                                }

                                for(String name : file_maps.keySet()){
                                    TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                                    textSliderView
                                            //  .description(name)
                                            .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(TalentModelProfileActivity.this);

                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("extra", name);

                                    talentGallerySlider.addSlider(textSliderView);
                                }

                                talentGallerySlider.setPresetTransformer(SliderLayout.Transformer.Default);
                                talentGallerySlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                talentGallerySlider.setCustomAnimation(new ChildAnimationExample());
                                talentGallerySlider.setDuration(3000);
                                talentGallerySlider.addOnPageChangeListener(TalentModelProfileActivity.this);

                                StringBuilder sbTalentDisplayPhoto = new StringBuilder();
                                sbTalentDisplayPhoto.append(response.getString("talent_display_photo"));

                                StringBuilder sbFullnameAndAge = new StringBuilder();
                                sbFullnameAndAge
                                        .append(response.getString("screen_name").isEmpty() ? talentFullName : response.getString("screen_name"))
                                        .append(", ")
                                        .append(response.getString("age"))
                                        .append(" years old");

                                lblTalentFullname.setText(sbFullnameAndAge.toString());

                                String categories = response.getString("category_names");
                                lblTalentCategory.setText(categories);

                                CategoryDetector categoryDetector = new CategoryDetector(
                                        categories.contains(CategoriesConstants.CELEBRITY),
                                        categories.contains(CategoriesConstants.COMEDIANS),
                                        categories.contains(CategoriesConstants.DANCER),
                                        categories.contains(CategoriesConstants.DISK_JOCKEY),
                                        categories.contains(CategoriesConstants.EMCEE_HOST),
                                        categories.contains(CategoriesConstants.MODELS_BA),
                                        categories.contains(CategoriesConstants.MOVIE_TV_TALENTS),
                                        categories.contains(CategoriesConstants.SINGER_BAND)
                                );

                                Log.d("debug", "categories_detector: " + categoryDetector.toString());

                                detectCategory(categoryDetector);

                                lblTalentVitalStats.setText(response.getString("vital_stats"));

                                String genre = response.getString("genre").isEmpty() ? "N/A" : response.getString("genre");
                                lblTalentGenre.setText(genre.concat("\n"));

                                StringBuilder sbFollowers = new StringBuilder();
                                sbFollowers
                                        .append("Facebook: ")
                                        .append(response.getString("fb_followers"))
                                        .append("\nInstagram: ")
                                        .append(response.getString("instagram_followers"))
                                        .append("\n");

                                lblTalentFollowers.setText(sbFollowers.toString());

                                lblTalentHeight.setText(response.getString("height") + " ft.\n");
                                lblTalentDescription.setText((response.getString("talent_description").isEmpty() ? "No description as of the moment." : response.getString("talent_description")));

                                StringBuilder sbLocation = new StringBuilder();
                                sbLocation
                                        .append(response.getString("city_muni"))
                                        .append(", ")
                                        .append(response.getString("province"))
                                        .append("\n");

                                lblTalentLocation.setText(sbLocation.toString());
                                lblExperiencesOrPreviousClients.setText(response.getString("talent_experiences") + "\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.TALENT_DETAILS_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getTalentReviews(){
        AndroidNetworking
                .get(EndPoints.GET_TALENT_REVIEWS_URL.concat("?talent_id={talent_id}"))
                .addPathParameter("talent_id", selectedTalentId)
                .setTag(Tags.TALENT_DETAILS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("flag") && response.has("msg")){
                                Log.d("debug", response.getString("msg"));
                            }else{
                                JSONArray array = response.getJSONArray("client_reviews_list");

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    int clientDisplayPhoto = 0;

                                    switch (object.getJSONObject("review_from").getString("role_code")){
                                        case "CLIENT_COMPANY":
                                            clientDisplayPhoto = R.drawable.customer_company;
                                            break;
                                        case "CLIENT_INDIVIDUAL":
                                            switch (object.getJSONObject("review_from").getString("gender")){
                                                case "Male":
                                                    clientDisplayPhoto = R.drawable.customer_male;
                                                    break;
                                                case "Female":
                                                    clientDisplayPhoto = R.drawable.customer_female;
                                                    break;
                                            }

                                            break;
                                    }

                                    Reviews reviews = new Reviews(
                                            clientDisplayPhoto,
                                            object.getString("review_feedback"),
                                            object.getString("review_rating") + " rating",
                                            "by " + object.getJSONObject("review_from").getString("fullname"));

                                    reviewsArrayList.add(reviews);
                                }

                                JayBaseAdapter baseAdapter = new JayBaseAdapter(TalentModelProfileActivity.this, reviewsArrayList) {
                                };

                                reviewsListView.setAdapter(baseAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.TALENT_DETAILS_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void detectCategory(CategoryDetector categoryDetector){
        if(categoryDetector.isModelOrBrandAmbassador() || categoryDetector.isMovieOrTvTalent()){
            linearLayoutTalentGenre.setVisibility(View.GONE);
            linearLayoutTalentDescription.setVisibility(View.GONE);
            lblExperiencesOrPreviousClientsCaption.setText(Variables.PREVIOUS_CLIENTS);
        }else if(categoryDetector.isSingerOrBand()){
            linearLayoutVitalStats.setVisibility(View.GONE);
            linearLayoutTalentDescription.setVisibility(View.GONE);
            linearLayoutTalentHeight.setVisibility(View.GONE);
            lblExperiencesOrPreviousClientsCaption.setText(Variables.PREVIOUS_CLIENTS);
        }else if(categoryDetector.isComedian() || categoryDetector.isEmceeOrHost()){
            linearLayoutVitalStats.setVisibility(View.GONE);
            linearLayoutTalentDescription.setVisibility(View.GONE);
            linearLayoutTalentGenre.setVisibility(View.GONE);
            linearLayoutTalentHeight.setVisibility(View.GONE);
            lblExperiencesOrPreviousClientsCaption.setText(Variables.PREVIOUS_CLIENTS);
        }else if(categoryDetector.isCelebrity()){
            linearLayoutTalentDescription.setVisibility(View.VISIBLE);
            linearLayoutVitalStats.setVisibility(View.GONE);
            linearLayoutLocation.setVisibility(View.GONE);
            linearLayoutTalentHeight.setVisibility(View.GONE);
            linearLayoutTalentGenre.setVisibility(View.GONE);
            linearLayoutExperienceOrPreviousClients.setVisibility(View.GONE);
            btnAddToBookingList.setVisibility(View.GONE);
            btnInquireNow.setVisibility(View.VISIBLE);
        }else if(categoryDetector.isDancer() || categoryDetector.isDiskJockey()){
            btnAddToBookingList.setVisibility(View.GONE);
            btnInquireNow.setVisibility(View.VISIBLE);
        }
    }
}
