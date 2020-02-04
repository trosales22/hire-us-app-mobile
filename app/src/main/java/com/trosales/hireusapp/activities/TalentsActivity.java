package com.trosales.hireusapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.TalentsAdapter;
import com.trosales.hireusapp.classes.beans.Location;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.TalentsDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class TalentsActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_talents) SwipeRefreshLayout swipeToRefresh_talents;
    @BindView(R.id.recyclerView_talents) RecyclerView recyclerView_talents;

    private String selectedCategory = "";
    private List<TalentsDO> talentsDOList;
    private TalentsAdapter talentsAdapter;
    private Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talents);
        ButterKnife.bind(this);

        Bundle bookingsBundleArgs = getIntent().getExtras();

        if(bookingsBundleArgs != null){
            selectedCategory = bookingsBundleArgs.getString("selectedCategory");
        }

        talentsDOList = new ArrayList<>();
        handler = new Handler();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView_talents.setLayoutManager(gridLayoutManager);

        skeletonScreen = Skeleton.bind(recyclerView_talents)
                .adapter(talentsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(() -> {
            getAllTalents(selectedCategory);
        }, 500);

        swipeToRefresh_talents.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(talentsDOList != null){
                talentsDOList.clear();
            }

            handler.postDelayed(() -> {
                getAllTalents(selectedCategory);
            }, 500);
            swipeToRefresh_talents.setRefreshing(false);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private StringBuilder getParams(HashMap<String, String> filteringOption){
        StringBuilder sbParams = new StringBuilder();

        if(!filteringOption.isEmpty()){
            sbParams.append("?");

            Log.d("debug", "filteringOption: " + filteringOption.toString());

            if(filteringOption.get("height_from") != null){
                if(!Objects.requireNonNull(filteringOption.get("height_from")).isEmpty()){
                    sbParams.append("&height_from={height_from}");
                }
            }

            if(filteringOption.get("height_to") != null) {
                if (!Objects.requireNonNull(filteringOption.get("height_to")).isEmpty()) {
                    sbParams.append("&height_to={height_to}");
                }
            }

            if(filteringOption.get("age_from") != null) {
                if (!Objects.requireNonNull(filteringOption.get("age_from")).isEmpty()) {
                    sbParams.append("&age_from={age_from}");
                }
            }

            if(filteringOption.get("age_to") != null) {
                if (!Objects.requireNonNull(filteringOption.get("age_to")).isEmpty()) {
                    sbParams.append("&age_to={age_to}");
                }
            }

            if(filteringOption.get("rate_per_hour_from") != null) {
                if (!Objects.requireNonNull(filteringOption.get("rate_per_hour_from")).isEmpty()) {
                    sbParams.append("&rate_per_hour_from={rate_per_hour_from}");
                }
            }

            if(filteringOption.get("rate_per_hour_to") != null) {
                if (!Objects.requireNonNull(filteringOption.get("rate_per_hour_to")).isEmpty()) {
                    sbParams.append("&rate_per_hour_to={rate_per_hour_to}");
                }
            }

            if(filteringOption.get("province_code") != null) {
                if (filteringOption.get("province_code") != null) {
                    sbParams.append("&province_code={province_code}");
                }
            }

            if(filteringOption.get("city_muni_code") != null) {
                if (filteringOption.get("city_muni_code") != null) {
                    sbParams.append("&city_muni_code={city_muni_code}");
                }
            }

            if(filteringOption.get("gender") != null) {
                if (!Objects.requireNonNull(filteringOption.get("gender")).isEmpty()) {
                    sbParams.append("&gender={gender}");
                }
            }
        }

        return sbParams;
    }

    private void getAllTalents(String selectedCategory){
        talentsDOList.clear();
        HashMap<String, String> filteringOption = SharedPrefManager.getInstance(this).getFilteringOption();
        Log.d("debug", EndPoints.GET_ALL_TALENTS_URL.concat(getParams(filteringOption).toString()));

        StringBuilder sbExtraFilteringParams = new StringBuilder();

        if(!selectedCategory.isEmpty()){
            sbExtraFilteringParams.append("&selected_categories={selected_categories}");
        }

        AndroidNetworking
                .get(EndPoints.GET_ALL_TALENTS_URL.concat(getParams(filteringOption).toString()).concat(sbExtraFilteringParams.toString()))
                .addPathParameter("height_from", filteringOption.get("height_from"))
                .addPathParameter("height_to", filteringOption.get("height_to"))
                .addPathParameter("age_from", filteringOption.get("age_from"))
                .addPathParameter("age_to", filteringOption.get("age_to"))
                .addPathParameter("rate_per_hour_from", filteringOption.get("rate_per_hour_from"))
                .addPathParameter("rate_per_hour_to", filteringOption.get("rate_per_hour_to"))
                .addPathParameter("province_code", filteringOption.get("province_code"))
                .addPathParameter("city_muni_code", filteringOption.get("city_muni_code"))
                .addPathParameter("gender", filteringOption.get("gender"))
                .addPathParameter("selected_categories", selectedCategory)
                .setTag(Tags.BOOKINGS_FRAGMENT)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeletonScreen.hide();
                        getTalentsResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeletonScreen.hide();
                        Log.e(Tags.BOOKINGS_FRAGMENT, anError.getErrorDetail());
                    }
                });
    }

    private void getTalentsResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("talents_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    TalentsDO talentsDO = new TalentsDO(
                            object.getString("talent_id"),
                            object.getString("screen_name").isEmpty() ? object.getString("fullname") : object.getString("screen_name"),
                            object.getString("height"),
                            object.getString("gender"),
                            object.getString("talent_display_photo"),
                            object.getString("category_names"),
                            Integer.parseInt(object.getString("age")),
                            new Location(
                                    object.getString("region"),
                                    object.getString("province"),
                                    object.getString("city_muni"),
                                    object.getString("barangay"),
                                    object.getString("bldg_village"),
                                    object.getString("zip_code")
                            )
                    );

                    talentsDOList.add(talentsDO);
                }
            }

            if(talentsDOList.isEmpty()){
                simpleStatefulLayout.showEmpty();
            }else{
                simpleStatefulLayout.showContent();
            }

            talentsAdapter = new TalentsAdapter(talentsDOList, this);
            recyclerView_talents.setAdapter(talentsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
