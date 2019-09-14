package com.trosales.hireusapp.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.BookingsAdapter;
import com.trosales.hireusapp.classes.wrappers.BookingsDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class BookingListActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_bookingList) SwipeRefreshLayout swipeToRefresh_bookingList;
    @BindView(R.id.recyclerView_bookingList) RecyclerView recyclerView_bookingList;

    private List<BookingsDO> bookingsDOList;
    private BookingsAdapter bookingsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        bookingsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_bookingList.setLayoutManager(linearLayoutManager);
        recyclerView_bookingList.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_bookingList)
                .adapter(bookingsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::showAllBookings, 500);

        swipeToRefresh_bookingList.setOnRefreshListener(() -> {
            simpleStatefulLayout.showContent();
            skeletonScreen.show();

            if(bookingsDOList != null){
                bookingsDOList.clear();
            }

            handler.postDelayed(this::showAllBookings, 500);
            swipeToRefresh_bookingList.setRefreshing(false);
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

    private void showAllBookings(){
        if(bookingsDOList.isEmpty()){
            simpleStatefulLayout.showEmpty();
        }else{
            simpleStatefulLayout.showContent();
        }

        bookingsAdapter = new BookingsAdapter(bookingsDOList, this);
        recyclerView_bookingList.setAdapter(bookingsAdapter);
    }
}
