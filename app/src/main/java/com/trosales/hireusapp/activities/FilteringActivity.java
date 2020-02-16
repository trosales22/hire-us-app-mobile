package com.trosales.hireusapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.beans.Regions;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.abhinay.input.CurrencyEditText;

public class FilteringActivity extends AppCompatActivity {
    @BindView(R.id.cmbRegion) NiceSpinner cmbRegion;
    @BindView(R.id.cmbAgeFrom) NiceSpinner cmbAgeFrom;
    @BindView(R.id.cmbAgeTo) NiceSpinner cmbAgeTo;
    @BindView(R.id.txtHeightFrom) CurrencyEditText txtHeightFrom;
    @BindView(R.id.txtHeightTo) CurrencyEditText txtHeightTo;
    @BindView(R.id.cmbGender) NiceSpinner cmbGender;
    @BindView(R.id.btnFilterNow) AppCompatButton btnFilterNow;

    private List<Regions> regionsList;

    String selectedRegionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Filtering Option");
        //Objects.requireNonNull(getSupportActionBar()).setSubtitle("Filter by height,age,etc.");

        //AppSecurity.disableScreenshotRecording(this);

        regionsList = new ArrayList<>();

        getAllRegions();

        cmbRegion.setOnSpinnerItemSelectedListener((parent, view, position, id) -> selectedRegionCode = regionsList.get(position).getRegionCode());

        setupAgeDropdown();

        List<String> genderList = new LinkedList<>(Arrays.asList("All" ,"Male", "Female"));
        cmbGender.attachDataSource(genderList);

        btnFilterNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, TalentsActivity.class);

            HashMap<String, String> filteringOption = new HashMap<>();
            filteringOption.put("age_from", cmbAgeFrom.getText().toString().equals("From") ? "" : cmbAgeFrom.getText().toString());
            filteringOption.put("age_to", cmbAgeTo.getText().toString().equals("To") ? "" : cmbAgeTo.getText().toString());
            filteringOption.put("height_from", txtHeightFrom.getText() == null ? "" : txtHeightFrom.getText().toString());
            filteringOption.put("height_to", txtHeightTo.getText() == null ? "" : txtHeightTo.getText().toString());
            filteringOption.put("gender", cmbGender.getText().toString().equals("All") ? "" : cmbGender.getText().toString());

            SharedPrefManager.getInstance(this).setFilteringOption(filteringOption);
            startActivity(intent);
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

    private void setupAgeDropdown(){
        List<String> ageFromList = new ArrayList<>();
        List<String> ageToList = new ArrayList<>();

        ageFromList.add("From");
        ageToList.add("To");

        for(int i = 17; i <= 60; i++){
            ageFromList.add(String.valueOf(i));
            ageToList.add(String.valueOf(i));
        }

        cmbAgeFrom.attachDataSource(ageFromList);
        cmbAgeTo.attachDataSource(ageToList);
    }

    private void getAllRegions(){
        regionsList.clear();
        regionsList.add(new Regions("", "Choose Region", ""));

        AndroidNetworking
                .get(EndPoints.GET_ALL_REGIONS_URL)
                .setTag(Tags.FILTERING_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getRegionResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.FILTERING_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getRegionResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("region_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    Regions regions = new Regions(
                            object.getString("id"),
                            object.getString("region_name"),
                            object.getString("regCode")
                    );

                    regionsList.add(regions);
                }

                cmbRegion.attachDataSource(regionsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
