package com.trosales.hireusapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.TalentsAdapter;
import com.trosales.hireusapp.classes.beans.Categories;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.TalentsDO;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class HomeActivity extends AppCompatActivity implements FilterListener<Categories> {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_talents) SwipeRefreshLayout swipeToRefresh_talents;
    @BindView(R.id.recyclerView_talents) RecyclerView recyclerView_talents;

    private List<TalentsDO> talentsDOList;
    private TalentsAdapter talentsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    private int[] mColors;
    private String[] mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Objects.requireNonNull(this.getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        getSupportActionBar().getCustomView();

        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.categories);

        Filter<Categories> mFilter = findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getCategoryTags()));
        mFilter.setListener(this);

        //the text to show when there's no selected items
        mFilter.setNoSelectedItemText(getString(R.string.str_all_selected));
        mFilter.build();

        talentsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_talents.setLayoutManager(linearLayoutManager);
        recyclerView_talents.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_talents)
                .adapter(talentsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::getAllTalents, 500);

        swipeToRefresh_talents.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(talentsDOList != null){
                talentsDOList.clear();
            }

            handler.postDelayed(this::getAllTalents, 500);
            swipeToRefresh_talents.setRefreshing(false);
        });
    }

    private List<Categories> getCategoryTags() {
        List<Categories> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Categories(mTitles[i], mColors[i]));
        }

        return tags;
    }

    @Override
    public void onFilterDeselected(Categories categories) {

    }

    @Override
    public void onFilterSelected(Categories categories) {

    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Categories> arrayList) {
        for(Categories categories : arrayList){
            Log.d("debug", "categories: " + categories.getText());
        }
    }

    @Override
    public void onNothingSelected() {

    }

    class Adapter extends FilterAdapter<Categories> {
        Adapter(@NotNull List<? extends Categories> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Categories item) {
            FilterItem filterItem = new FilterItem(HomeActivity.this);

            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();

            return filterItem;
        }
    }

    private void getAllTalents(){
        AndroidNetworking
                .get(EndPoints.GET_ALL_TALENTS_URL)
                .setTag(Tags.HOME_ACTIVITY)
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
                        Log.e(Tags.HOME_ACTIVITY, anError.getErrorDetail());
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
                            object.getString("fullname"),
                            object.getString("height"),
                            object.getString("talent_fee"),
                            object.getString("talent_fee_type"),
                            object.getString("location"),
                            Integer.parseInt(object.getString("age")),
                            object.getString("gender"),
                            EndPoints.UPLOADS_BASE_URL + "talents_or_models/" + object.getString("talent_display_photo"),
                            object.getString("category_names")
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
