package com.trosales.hireusapp.activities;

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
import com.trosales.hireusapp.classes.adapters.ClientsAdapter;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.ClientDetailsDO;
import com.trosales.hireusapp.classes.wrappers.ClientsBookedDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class ClientsActivity extends AppCompatActivity {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_clientsBooked) SwipeRefreshLayout swipeToRefresh_clientsBooked;
    @BindView(R.id.recyclerView_clientsBooked) RecyclerView recyclerView_clientsBooked;

    private List<ClientsBookedDO> clientsBookedDOList;
    private ClientsAdapter clientsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        AppSecurity.disableScreenshotRecording(this);

        clientsBookedDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_clientsBooked.setLayoutManager(linearLayoutManager);
        recyclerView_clientsBooked.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_clientsBooked)
                .adapter(clientsAdapter)
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
        StringBuilder sbParams = new StringBuilder();
        sbParams.append("?talent_id={talent_id}");

        AndroidNetworking
                .get(EndPoints.GET_ALL_CLIENT_BOOKED_URL.concat(sbParams.toString()))
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

                    @Override
                    public void onError(ANError anError) {
                        skeletonScreen.hide();
                        Log.e(Tags.CLIENTS_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getClientsBookedListResponse(JSONObject response) {
        try {
            JSONArray array = response.getJSONArray("clients_booked_list");

            if (response.has("flag") && response.has("msg")) {
                Log.d("debug", response.getString("msg"));
            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    ClientDetailsDO clientDetailsDO = new ClientDetailsDO(
                            object.getString("client_id"),
                            object.getString("fullname"),
                            object.getString("gender"),
                            object.getString("role_id"),
                            object.getString("role_name")
                    );

                    ClientsBookedDO clientsBookedDO = new ClientsBookedDO(
                            clientDetailsDO,
                            object.getString("booking_id"),
                            object.getString("preferred_date"),
                            object.getString("preferred_time"),
                            object.getString("payment_option"),
                            object.getString("total_amount"),
                            object.getString("date_paid")
                    );

                    clientsBookedDOList.add(clientsBookedDO);
                }
            }

            if (clientsBookedDOList.isEmpty()) {
                simpleStatefulLayout.showEmpty();
            } else {
                simpleStatefulLayout.showContent();
            }

            clientsAdapter = new ClientsAdapter(clientsBookedDOList, this);
            recyclerView_clientsBooked.setAdapter(clientsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
