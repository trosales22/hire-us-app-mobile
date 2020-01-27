package com.trosales.hireusapp.fragments;


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
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.NewsAdapter;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.NewsDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class NewsFragment extends Fragment {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_news) SwipeRefreshLayout swipeToRefresh_news;
    @BindView(R.id.recyclerView_news) RecyclerView recyclerView_news;

    private List<NewsDO> newsDOList;
    private NewsAdapter newsAdapter;
    private Handler handler;
    private SkeletonScreen skeletonScreen;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        newsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView_news.setLayoutManager(layoutManager);
        recyclerView_news.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_news)
                .adapter(newsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::getAllNews, 500);

        swipeToRefresh_news.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(newsDOList != null){
                newsDOList.clear();
            }

            handler.postDelayed(this::getAllNews, 500);
            swipeToRefresh_news.setRefreshing(false);
        });

        return view;
    }

    private void getAllNews(){
        newsDOList.clear();

        AndroidNetworking.get(EndPoints.GET_ALL_NEWS_URL)
                .setTag(Tags.NEWS_FRAGMENT)
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
                        Log.e(Tags.NEWS_FRAGMENT, anError.getErrorDetail());
                    }
                });
    }

    private void getResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("news_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    NewsDO newsDO = new NewsDO(
                            object.getString("news_id"),
                            object.getString("news_caption"),
                            object.getString("news_details"),
                            object.getString("news_link"),
                            object.getString("news_display_photo"),
                            object.getString("news_creator"),
                            object.getString("news_created_date"),
                            object.getString("active_flag")
                    );

                    newsDOList.add(newsDO);
                }
            }

            if(newsDOList.isEmpty()){
                simpleStatefulLayout.showEmpty();
            }else{
                simpleStatefulLayout.showContent();
            }

            newsAdapter = new NewsAdapter(newsDOList, getContext());
            recyclerView_news.setAdapter(newsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
