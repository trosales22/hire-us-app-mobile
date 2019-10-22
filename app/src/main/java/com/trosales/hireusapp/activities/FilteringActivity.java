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
import com.trosales.hireusapp.classes.beans.CityMuni;
import com.trosales.hireusapp.classes.beans.Provinces;
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
    @BindView(R.id.cmbProvince) NiceSpinner cmbProvince;
    @BindView(R.id.cmbCity) NiceSpinner cmbCity;
    @BindView(R.id.cmbAgeFrom) NiceSpinner cmbAgeFrom;
    @BindView(R.id.cmbAgeTo) NiceSpinner cmbAgeTo;
    @BindView(R.id.txtHeightFrom) CurrencyEditText txtHeightFrom;
    @BindView(R.id.txtHeightTo) CurrencyEditText txtHeightTo;
    @BindView(R.id.txtRatePerHourFrom) CurrencyEditText txtRatePerHourFrom;
    @BindView(R.id.txtRatePerHourTo) CurrencyEditText txtRatePerHourTo;
    @BindView(R.id.cmbGender) NiceSpinner cmbGender;
    @BindView(R.id.btnFilterNow) AppCompatButton btnFilterNow;

    private List<Provinces> provincesList;
    private List<CityMuni> cityMuniList;

    String selectedProvCode,selectedCityMuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Filtering Option");
        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Filter by height,age,etc.");

        provincesList = new ArrayList<>();
        cityMuniList = new ArrayList<>();

        getAllProvinces();

        cmbProvince.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            selectedProvCode = provincesList.get(position).getProvinceCode();
            getAllCityMuni(selectedProvCode);
        });

        cmbCity.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            selectedCityMuni = cityMuniList.get(position).getCityMuniCode();
        });

        setupAgeDropdown();

        List<String> genderList = new LinkedList<>(Arrays.asList("All" ,"Male", "Female"));
        cmbGender.attachDataSource(genderList);

        btnFilterNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);

            HashMap<String, String> filteringOption = new HashMap<>();
            filteringOption.put("province_code", selectedProvCode);
            filteringOption.put("city_muni_code", selectedCityMuni);
            filteringOption.put("age_from", cmbAgeFrom.getText().toString().equals("From") ? "" : cmbAgeFrom.getText().toString());
            filteringOption.put("age_to", cmbAgeTo.getText().toString().equals("To") ? "" : cmbAgeTo.getText().toString());
            filteringOption.put("height_from", txtHeightFrom.getText() == null ? "" : txtHeightFrom.getText().toString());
            filteringOption.put("height_to", txtHeightTo.getText() == null ? "" : txtHeightTo.getText().toString());
            filteringOption.put("rate_per_hour_from", txtRatePerHourFrom.getText() == null ? "" : txtRatePerHourFrom.getText().toString());
            filteringOption.put("rate_per_hour_to", txtRatePerHourTo.getText() == null ? "" : txtRatePerHourTo.getText().toString());
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

    private void getProvinceResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("provinces_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    Provinces provinces = new Provinces(
                            object.getString("id"),
                            object.getString("provDesc"),
                            object.getString("provCode")
                    );

                    provincesList.add(provinces);
                }

                cmbProvince.attachDataSource(provincesList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAllProvinces(){
        provincesList.clear();
        provincesList.add(new Provinces("", "Choose Province", ""));

        AndroidNetworking
                .get(EndPoints.GET_ALL_PROVINCES_URL)
                .setTag(Tags.FILTERING_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getProvinceResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.FILTERING_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getCityMuniResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("city_muni_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    CityMuni cityMuni = new CityMuni(
                            object.getString("id"),
                            object.getString("citymunDesc"),
                            object.getString("citymunCode")
                    );

                    cityMuniList.add(cityMuni);
                }

                cmbCity.attachDataSource(cityMuniList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAllCityMuni(String provCode){
        StringBuilder sbParams = new StringBuilder();
        sbParams.append("?province_code={province_code}");

        cityMuniList.clear();
        cityMuniList.add(new CityMuni("", "Choose City/Muni", ""));

        AndroidNetworking
                .get(EndPoints.GET_ALL_CITY_MUNI_URL.concat(sbParams.toString()))
                .addPathParameter("province_code", provCode)
                .setTag(Tags.FILTERING_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getCityMuniResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.FILTERING_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }
}
