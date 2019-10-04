package com.trosales.hireusapp.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.BookingsAdapter;
import com.trosales.hireusapp.classes.beans.Location;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.BookingsDO;
import com.trosales.hireusapp.classes.wrappers.TalentsDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class BookingListActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout)
    SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_bookingList)
    SwipeRefreshLayout swipeToRefresh_bookingList;
    @BindView(R.id.recyclerView_bookingList)
    RecyclerView recyclerView_bookingList;

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

            if (bookingsDOList != null) {
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

    private void showAllBookings() {
        StringBuilder sbParams = new StringBuilder();
        sbParams.append("?client_id={client_id}");

        AndroidNetworking
                .get(EndPoints.GET_BOOKING_LIST_BY_CLIENT_ID_URL.concat(sbParams.toString()))
                .addPathParameter("client_id", SharedPrefManager.getInstance(getApplicationContext()).getUserId())
                .setTag(Tags.BOOKING_LIST_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeletonScreen.hide();
                        getBookingListResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeletonScreen.hide();
                        Log.e(Tags.BOOKING_LIST_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getBookingListResponse(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("booking_list");

            if (response.has("flag") && response.has("msg")) {
                Log.d("debug", response.getString("msg"));
            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    Location location = new Location(
                            object.getJSONObject("talent_id").getString("province"),
                            object.getJSONObject("talent_id").getString("city_muni"),
                            object.getJSONObject("talent_id").getString("barangay"),
                            object.getJSONObject("talent_id").getString("bldg_village"),
                            object.getJSONObject("talent_id").getString("zip_code")
                    );

                    StringBuilder sbTalentDisplayPhoto = new StringBuilder();
                    sbTalentDisplayPhoto
                            .append(object.getJSONObject("talent_id").getString("talent_display_photo"));

                    TalentsDO talentsDO = new TalentsDO(
                            object.getJSONObject("talent_id").getString("talent_id"),
                            object.getJSONObject("talent_id").getString("fullname"),
                            object.getJSONObject("talent_id").getString("height"),
                            object.getJSONObject("talent_id").getString("hourly_rate"),
                            object.getJSONObject("talent_id").getString("gender"),
                            sbTalentDisplayPhoto.toString(),
                            object.getJSONObject("talent_id").getString("category_names"),
                            Integer.parseInt(object.getJSONObject("talent_id").getString("age")),
                            location
                    );

                    BookingsDO bookingsDO = new BookingsDO(
                            Integer.parseInt(object.getString("booking_id")),
                            object.getString("status"),
                            object.getString("preferred_date_from"),
                            object.getString("preferred_date_to"),
                            object.getString("preferred_time_from"),
                            object.getString("preferred_time_to"),
                            object.getString("total_amount"),
                            object.getString("date_paid"),
                            talentsDO
                    );

                    bookingsDOList.add(bookingsDO);
                }
            }

            if (bookingsDOList.isEmpty()) {
                simpleStatefulLayout.showEmpty();
            } else {
                simpleStatefulLayout.showContent();
            }

            bookingsAdapter = new BookingsAdapter(bookingsDOList, this);
            recyclerView_bookingList.setAdapter(bookingsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
