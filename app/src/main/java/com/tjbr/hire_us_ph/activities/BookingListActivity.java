package com.tjbr.hire_us_ph.activities;

import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.tjbr.hire_us_ph.R;
import com.tjbr.hire_us_ph.classes.adapters.BookedTalentsAdapter;
import com.tjbr.hire_us_ph.classes.beans.Location;
import com.tjbr.hire_us_ph.classes.commons.AppSecurity;
import com.tjbr.hire_us_ph.classes.commons.SharedPrefManager;
import com.tjbr.hire_us_ph.classes.constants.EndPoints;
import com.tjbr.hire_us_ph.classes.constants.Tags;
import com.tjbr.hire_us_ph.classes.wrappers.ClientBookingsDO;
import com.tjbr.hire_us_ph.classes.wrappers.TalentsDO;

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
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_bookingList) SwipeRefreshLayout swipeToRefresh_bookingList;
    @BindView(R.id.recyclerView_bookingList) RecyclerView recyclerView_bookingList;

    private List<ClientBookingsDO> clientBookingsDOList;
    private BookedTalentsAdapter bookedTalentsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        AppSecurity.disableScreenshotRecording(this);

        clientBookingsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_bookingList.setLayoutManager(linearLayoutManager);
        recyclerView_bookingList.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_bookingList)
                .adapter(bookedTalentsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::showAllBookings, 500);

        swipeToRefresh_bookingList.setOnRefreshListener(() -> {
            simpleStatefulLayout.showContent();
            skeletonScreen.show();

            if (clientBookingsDOList != null) {
                clientBookingsDOList.clear();
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
        AndroidNetworking
                .get(EndPoints.GET_BOOKING_LIST_BY_CLIENT_ID_URL.concat("?client_id={client_id}"))
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
                            object.getJSONObject("talent_id").getString("region"),
                            object.getJSONObject("talent_id").getString("province"),
                            object.getJSONObject("talent_id").getString("city_muni"),
                            object.getJSONObject("talent_id").getString("barangay"),
                            object.getJSONObject("talent_id").getString("bldg_village"),
                            object.getJSONObject("talent_id").getString("zip_code")
                    );

                    TalentsDO talentsDO = new TalentsDO(
                            object.getJSONObject("talent_id").getString("talent_id"),
                            object.getJSONObject("talent_id").getString("screen_name").isEmpty() ? object.getJSONObject("talent_id").getString("fullname") : object.getJSONObject("talent_id").getString("screen_name"),
                            object.getJSONObject("talent_id").getString("height"),
                            object.getJSONObject("talent_id").getString("gender"),
                            object.getJSONObject("talent_id").getString("talent_display_photo"),
                            object.getJSONObject("talent_id").getString("category_ids"),
                            Integer.parseInt(object.getJSONObject("talent_id").getString("age")),
                            location
                    );

                    ClientBookingsDO clientBookingsDO = new ClientBookingsDO(
                            Integer.parseInt(object.getString("booking_id")),
                            object.getString("booking_generated_id"),
                            object.getString("booking_event_title"),
                            object.getString("booking_talent_fee"),
                            object.getString("booking_venue_location"),
                            object.getString("booking_payment_option"),
                            object.getString("booking_date"),
                            object.getString("booking_time"),
                            object.getString("booking_other_details").isEmpty() ? "N/A" : object.getString("booking_other_details"),
                            object.getString("booking_offer_status"),
                            object.getString("booking_created_date"),
                            object.getString("booking_decline_reason"),
                            object.getString("booking_approved_or_declined_date"),
                            object.getString("booking_date_paid"),
                            object.getString("booking_pay_on_or_before"),
                            object.getString("booking_payment_status"),
                            talentsDO
                    );

                    clientBookingsDOList.add(clientBookingsDO);
                }
            }

            if (clientBookingsDOList.isEmpty()) {
                simpleStatefulLayout.showEmpty();
            } else {
                simpleStatefulLayout.showContent();
            }

            bookedTalentsAdapter = new BookedTalentsAdapter(clientBookingsDOList, this);
            recyclerView_bookingList.setAdapter(bookedTalentsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
