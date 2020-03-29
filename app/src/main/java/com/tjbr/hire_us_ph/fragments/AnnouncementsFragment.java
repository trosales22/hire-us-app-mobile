package com.tjbr.hire_us_ph.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.tjbr.hire_us_ph.R;
import com.tjbr.hire_us_ph.classes.adapters.AnnouncementsAdapter;
import com.tjbr.hire_us_ph.classes.constants.EndPoints;
import com.tjbr.hire_us_ph.classes.constants.Tags;
import com.tjbr.hire_us_ph.classes.wrappers.AnnouncementsDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class AnnouncementsFragment extends Fragment {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_announcements) SwipeRefreshLayout swipeToRefresh_announcements;
    @BindView(R.id.recyclerView_announcements) RecyclerView recyclerView_announcements;

    private List<AnnouncementsDO> announcementsDOList;
    private AnnouncementsAdapter announcementsAdapter;

    private Handler handler;
    private SkeletonScreen skeletonScreen;

    public static AnnouncementsFragment newInstance() {
        return new AnnouncementsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announcements, container, false);
        ButterKnife.bind(this, view);

        announcementsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

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

        return view;
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

            announcementsAdapter = new AnnouncementsAdapter(announcementsDOList, getContext());
            recyclerView_announcements.setAdapter(announcementsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
