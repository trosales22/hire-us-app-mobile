package com.trosales.hireusapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.beans.TalentCategory;
import com.trosales.hireusapp.classes.beans.Talents;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddReviewsActivity extends AppCompatActivity {

    @BindView(R.id.cmbTalentCategory) MaterialBetterSpinner cmbTalentCategory;
    @BindView(R.id.cmbTalentName) MaterialBetterSpinner cmbTalentName;
    @BindView(R.id.txtReviewsDescription) EditText txtReviewsDescription;

    protected Handler handler;
    private List<TalentCategory> categoriesList;
    private List<Talents> talentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reviews);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        categoriesList = new ArrayList<>();
        talentsList = new ArrayList<>();

        handler = new Handler();
        cmbTalentName.setVisibility(View.GONE);

        loadAllTalentCategories();

        cmbTalentCategory.setOnItemClickListener((parent, view1, position, id) -> {
            talentsList.clear();
            TalentCategory talentCategory = categoriesList.get(position);
            loadAllTalentBasedOnSelectedCategory(talentCategory.getCategoryId());
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

    private void loadAllTalentCategories(){
        categoriesList.clear();

        AndroidNetworking.get(EndPoints.GET_ALL_TALENT_CATEGORIES_URL)
                .setTag(Tags.ADD_REVIEWS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("category_list");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject category = array.getJSONObject(i);
                                categoriesList.add(new TalentCategory(category.getString("category_id"), category.getString("category_name")));
                            }

                            cmbTalentCategory.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getApplicationContext()), android.R.layout.simple_spinner_dropdown_item, categoriesList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Snackbar.make(findViewById(android.R.id.content), anError.getErrorDetail(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadAllTalentBasedOnSelectedCategory(String selectedCategory){
        talentsList.clear();

        AndroidNetworking.get(EndPoints.GET_ALL_TALENT_BY_SELECTED_CATEGORY_URL.concat("?category_id={category_id}"))
                .addPathParameter("category_id", selectedCategory)
                .setTag(Tags.ADD_REVIEWS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("talents_per_category_list");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject talent = array.getJSONObject(i);
                                talentsList.add(new Talents(talent.getString("talent_id"), talent.getString("firstname") + " " + talent.getString("lastname")));
                            }

                            cmbTalentName.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getApplicationContext()), android.R.layout.simple_spinner_dropdown_item, talentsList));
                            cmbTalentName.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Snackbar.make(findViewById(android.R.id.content), anError.getErrorDetail(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
