package com.agentcoder.hire_us_ph.activities;

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
import com.agentcoder.hire_us_ph.R;
import com.agentcoder.hire_us_ph.classes.adapters.NewsAdapter;
import com.agentcoder.hire_us_ph.classes.commons.AppSecurity;
import com.agentcoder.hire_us_ph.classes.constants.EndPoints;
import com.agentcoder.hire_us_ph.classes.constants.Tags;
import com.agentcoder.hire_us_ph.classes.wrappers.NewsDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class NewsActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_news) SwipeRefreshLayout swipeToRefresh_news;
    @BindView(R.id.recyclerView_news) RecyclerView recyclerView_news;

    private List<NewsDO> newsDOList;
    private NewsAdapter newsAdapter;
    private Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppSecurity.disableScreenshotRecording(this);

        newsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                            object.getString("news_author"),
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

            newsAdapter = new NewsAdapter(newsDOList, this);
            recyclerView_news.setAdapter(newsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
