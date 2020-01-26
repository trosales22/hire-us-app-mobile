package com.trosales.hireusapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.NewsAdapter;
import com.trosales.hireusapp.classes.wrappers.NewsDO;

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

        handler.postDelayed(() -> {
            getAllNews();
        }, 500);

        swipeToRefresh_news.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(newsDOList != null){
                newsDOList.clear();
            }

            handler.postDelayed(() -> {
                getAllNews();
            }, 500);
            swipeToRefresh_news.setRefreshing(false);
        });

        return view;
    }

    private void getAllNews(){
        if(newsDOList.isEmpty()){
            simpleStatefulLayout.showEmpty();
        }else{
            simpleStatefulLayout.showContent();
        }
    }
}
