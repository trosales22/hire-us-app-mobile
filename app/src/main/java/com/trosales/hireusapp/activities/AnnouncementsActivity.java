package com.trosales.hireusapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.trosales.hireusapp.classes.adapters.AnnouncementsAdapter;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.AnnouncementsDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class AnnouncementsActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_announcements) SwipeRefreshLayout swipeToRefresh_announcements;
    @BindView(R.id.recyclerView_announcements) RecyclerView recyclerView_announcements;

    private List<AnnouncementsDO> announcementsDOList;
    private AnnouncementsAdapter announcementsAdapter;

    private Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppSecurity.disableScreenshotRecording(this);

        announcementsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView_announcements.setLayoutManager(layoutManager);
        recyclerView_announcements.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_announcements)
                .adapter(announcementsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::getAllAnnouncements, 500);

        swipeToRefresh_announcements.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(announcementsDOList != null){
                announcementsDOList.clear();
            }

            handler.postDelayed(this::getAllAnnouncements, 500);
            swipeToRefresh_announcements.setRefreshing(false);
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

    private void getAllAnnouncements(){
        announcementsDOList.clear();

        AndroidNetworking.get(EndPoints.GET_ALL_ANNOUNCEMENTS_URL)
                .setTag(Tags.ANNOUNCEMENTS_FRAGMENT)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeletonScreen.hide();
                        getResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeletonScreen.hide();
                        Log.e(Tags.ANNOUNCEMENTS_FRAGMENT, anError.getErrorDetail());
                    }
                });
    }

    private void getResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("announcements_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    AnnouncementsDO announcementsDO = new AnnouncementsDO(
                            object.getString("announcement_id"),
                            object.getString("announcement_caption"),
                            object.getString("announcement_details"),
                            object.getString("user_id"),
                            object.getString("announcement_creator"),
                            object.getString("announcement_created_date")
                    );

                    announcementsDOList.add(announcementsDO);
                }
            }

            if(announcementsDOList.isEmpty()){
                simpleStatefulLayout.showEmpty();
            }else{
                simpleStatefulLayout.showContent();
            }

            announcementsAdapter = new AnnouncementsAdapter(announcementsDOList, this);
            recyclerView_announcements.setAdapter(announcementsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
