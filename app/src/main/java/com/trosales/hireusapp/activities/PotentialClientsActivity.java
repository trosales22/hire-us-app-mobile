package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.PotentialClientsAdapter;
import com.trosales.hireusapp.classes.beans.Location;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.ClientBookingsDO;
import com.trosales.hireusapp.classes.wrappers.ClientDetailsDO;
import com.trosales.hireusapp.classes.wrappers.ClientsBookedDO;
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

public class PotentialClientsActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_clientsBooked) SwipeRefreshLayout swipeToRefresh_clientsBooked;
    @BindView(R.id.recyclerView_clientsBooked) RecyclerView recyclerView_clientsBooked;

    private List<ClientsBookedDO> clientsBookedDOList;
    private PotentialClientsAdapter potentialClientsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_clients);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        AppSecurity.disableScreenshotRecording(this);

        clientsBookedDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_clientsBooked.setLayoutManager(linearLayoutManager);
        recyclerView_clientsBooked.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_clientsBooked)
                .adapter(potentialClientsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::showAllClientsBooked, 500);

        swipeToRefresh_clientsBooked.setOnRefreshListener(() -> {
            simpleStatefulLayout.showContent();
            skeletonScreen.show();

            if (clientsBookedDOList != null) {
                clientsBookedDOList.clear();
            }

            handler.postDelayed(this::showAllClientsBooked, 500);
            swipeToRefresh_clientsBooked.setRefreshing(false);
        });
    }

    private void showAllClientsBooked(){
        AndroidNetworking
                .get(EndPoints.GET_ALL_CLIENT_BOOKED_URL.concat("?talent_id={talent_id}"))
                .addPathParameter("talent_id", SharedPrefManager.getInstance(getApplicationContext()).getUserId())
                .setTag(Tags.CLIENTS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeletonScreen.hide();
                        getClientsBookedListResponse(response);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(ANError anError) {
                        skeletonScreen.hide();
                        Log.e(Tags.CLIENTS_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    @SuppressLint("LongLogTag")
    private void getClientsBookedListResponse(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("clients_booked_list");

            if (response.has("flag") && response.has("msg")) {
                Log.d("debug", response.getString("msg"));
            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    ClientDetailsDO clientDetailsDO = new ClientDetailsDO(
                            object.getJSONObject("client_id").getString("user_id"),
                            object.getJSONObject("client_id").getString("fullname"),
                            object.getJSONObject("client_id").getString("email"),
                            object.getJSONObject("client_id").getString("contact_number"),
                            object.getJSONObject("client_id").getString("gender"),
                            object.getJSONObject("client_id").getString("role_code"),
                            object.getJSONObject("client_id").getString("role_name")
                    );

                    Location location = new Location(
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
                            object.getJSONObject("talent_id").getString("hourly_rate"),
                            object.getJSONObject("talent_id").getString("gender"),
                            object.getJSONObject("talent_id").getString("talent_display_photo"),
                            object.getJSONObject("talent_id").getString("category_names"),
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
                            talentsDO
                    );

                    ClientsBookedDO clientsBookedDO = new ClientsBookedDO(
                            clientDetailsDO,
                            clientBookingsDO
                    );

                    clientsBookedDOList.add(clientsBookedDO);
                }
            }

            if (clientsBookedDOList.isEmpty()) {
                simpleStatefulLayout.showEmpty();
            } else {
                simpleStatefulLayout.showContent();
            }

            potentialClientsAdapter = new PotentialClientsAdapter(clientsBookedDOList, this);
            recyclerView_clientsBooked.setAdapter(potentialClientsAdapter);
        } catch (JSONException e) {
            Log.e(Tags.CLIENTS_ACTIVITY, e.toString());
        }
    }
}
