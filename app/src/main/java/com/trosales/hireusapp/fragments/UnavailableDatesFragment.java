package com.trosales.hireusapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.trosales.hireusapp.classes.adapters.UnavailableDatesAdapter;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.UnavailableDatesDO;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;

public class UnavailableDatesFragment extends BottomSheetDialogFragment {
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.recyclerView_unavailableDates) RecyclerView recyclerView_unavailableDates;

    private List<UnavailableDatesDO> unavailableDatesDOList;
    private UnavailableDatesAdapter unavailableDatesAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    public static UnavailableDatesFragment getInstance() {
        return new UnavailableDatesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_unavailable_dates, container, false);
        ButterKnife.bind(this, view);

        unavailableDatesDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView_unavailableDates.setLayoutManager(linearLayoutManager);
        recyclerView_unavailableDates.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_unavailableDates)
                .adapter(unavailableDatesAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::getUnavailableDates, 500);

        return view;
    }

    private void getUnavailableDates(){
        StringBuilder sbParams = new StringBuilder();
        sbParams.append("?talent_id={talent_id}");

        AndroidNetworking
                .get(EndPoints.GET_UNAVAILABLE_DATES_URL.concat(sbParams.toString()))
                .addPathParameter("talent_id", SharedPrefManager.getInstance(getContext()).getTalentId())
                .setTag(Tags.UNAVAILABLE_DATES_FRAGMENT)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getResponse(response);
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.UNAVAILABLE_DATES_FRAGMENT, anError.getErrorDetail());
                    }
                });
    }

    private void getResponse(JSONObject response){
        try {
            JSONArray unavailableDatesArray = response.getJSONArray("talent_unavailable_dates");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < unavailableDatesArray.length(); i++) {
                    JSONObject object = unavailableDatesArray.getJSONObject(i);

                    UnavailableDatesDO unavailableDatesDO = new UnavailableDatesDO(
                            object.getString("ud_talent_id"),
                            object.getString("ud_sched"),
                            object.getString("ud_month_year"),
                            object.getString("ud_created_date")
                    );

                    unavailableDatesDOList.add(unavailableDatesDO);
                }

                if(unavailableDatesDOList.isEmpty()){
                    simpleStatefulLayout.showEmpty();
                }else{
                    simpleStatefulLayout.showContent();
                }

                unavailableDatesAdapter = new UnavailableDatesAdapter(unavailableDatesDOList, getContext());
                recyclerView_unavailableDates.setAdapter(unavailableDatesAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
