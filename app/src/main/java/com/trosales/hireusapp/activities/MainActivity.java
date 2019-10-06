package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.TalentsAdapter;
import com.trosales.hireusapp.classes.beans.Categories;
import com.trosales.hireusapp.classes.beans.Location;
import com.trosales.hireusapp.classes.commons.AndroidNetworkingShortcuts;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.TalentsDO;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.Font;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FilterListener<Categories>, DatePickerDialog.OnDateSetListener {
    private TextView lblLoggedInFullname,lblLoggedInRole;
    @BindView(R.id.btnChoosePreferredDate) Button btnChoosePreferredDate;
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_talents) SwipeRefreshLayout swipeToRefresh_talents;
    @BindView(R.id.recyclerView_talents) RecyclerView recyclerView_talents;

    private List<TalentsDO> talentsDOList;
    private TalentsAdapter talentsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

    private int[] mColors;
    private String[] mTitles;

    private boolean mAutoHighlight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        lblLoggedInFullname = header.findViewById(R.id.lblLoggedInFullname);
        lblLoggedInRole = header.findViewById(R.id.lblLoggedInRole);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        btnChoosePreferredDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    MainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );

            dpd.setAutoHighlight(mAutoHighlight);
            dpd.show(getFragmentManager(), "Datepickerdialog");
        });

        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.categories);

        Filter<Categories> mFilter = findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getCategoryTags()));
        mFilter.setListener(this);

        //the text to show when there's no selected items
        mFilter.setNoSelectedItemText(getString(R.string.str_all_selected));
        mFilter.build();

        talentsDOList = new ArrayList<>();
        handler = new Handler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_talents.setLayoutManager(linearLayoutManager);
        recyclerView_talents.setHasFixedSize(true);

        skeletonScreen = Skeleton.bind(recyclerView_talents)
                .adapter(talentsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        handler.postDelayed(this::getAllTalents, 500);

        swipeToRefresh_talents.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(talentsDOList != null){
                talentsDOList.clear();
            }

            handler.postDelayed(this::getAllTalents, 500);
            swipeToRefresh_talents.setRefreshing(false);
        });

        AndroidNetworkingShortcuts.prefetchPersonalInfo(
                EndPoints.GET_PERSONAL_INFO_URL.concat("?username_email={username_email}"),
                SharedPrefManager.getInstance(MainActivity.this).getEmailOrUsername(),
                Tags.MAIN_ACTIVITY,
                Priority.LOW);

        showInfoOfLoggedInUser(navigationView);
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }else if(id == R.id.action_filtering_option){
            startActivity(new Intent(this, FilteringActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_booking_list) {
            startActivity(new Intent(this, BookingListActivity.class));
        } else if (id == R.id.nav_check_availability) {
            //go to talent schedule page
        } else if (id == R.id.nav_logout) {
            showLogoutPrompt(Messages.CONFIRMATION_CAPTION, Messages.LOGOUT_PROMPT);
        } else if (id == R.id.nav_about_app) {
            //go to about app
        } else if (id == R.id.nav_about_dev) {
            //go to about dev
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Categories> getCategoryTags() {
        List<Categories> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Categories(mTitles[i], mColors[i]));
        }

        return tags;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String dateFrom = (++monthOfYear) + "/" + dayOfMonth + "/" + year;
        String dateTo = (++monthOfYearEnd) + "/" + dayOfMonthEnd + "/" + yearEnd;

        StringBuilder sbSelectedDates = new StringBuilder();
        sbSelectedDates.append(dateFrom).append(" - ").append(dateTo);

        btnChoosePreferredDate.setText(sbSelectedDates.toString());
        Toast.makeText(this, "Your preferred date: " + sbSelectedDates.toString() + "!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFilterDeselected(Categories categories) {

    }

    @Override
    public void onFilterSelected(Categories categories) {

    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Categories> arrayList) {
        for(Categories categories : arrayList){
            Log.d("debug", "categories: " + categories.getText());
        }
    }

    @Override
    public void onNothingSelected() {

    }

    class Adapter extends FilterAdapter<Categories> {
        Adapter(@NotNull List<? extends Categories> items) {
            super(items);
        }

        @NotNull
        @Override
        public FilterItem createView(int position, Categories item) {
            FilterItem filterItem = new FilterItem(getApplicationContext());

            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();

            return filterItem;
        }
    }

    private void getAllTalents(){
        AndroidNetworking
                .get(EndPoints.GET_ALL_TALENTS_URL)
                .setTag(Tags.MAIN_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeletonScreen.hide();
                        getTalentsResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeletonScreen.hide();
                        Log.e(Tags.MAIN_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getTalentsResponse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("talents_list");

            if(response.has("flag") && response.has("msg")){
                Log.d("debug", response.getString("msg"));
            }else{
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);

                    TalentsDO talentsDO = new TalentsDO(
                            object.getString("talent_id"),
                            object.getString("fullname"),
                            object.getString("height"),
                            object.getString("hourly_rate"),
                            object.getString("gender"),
                            EndPoints.UPLOADS_URL + "talents_or_models/" + object.getString("talent_display_photo"),
                            object.getString("category_names"),
                            Integer.parseInt(object.getString("age")),
                            new Location(
                                    object.getString("province"),
                                    object.getString("city_muni"),
                                    object.getString("barangay"),
                                    object.getString("bldg_village"),
                                    object.getString("zip_code")
                            )
                    );

                    talentsDOList.add(talentsDO);
                }
            }

            if(talentsDOList.isEmpty()){
                simpleStatefulLayout.showEmpty();
            }else{
                simpleStatefulLayout.showContent();
            }

            talentsAdapter = new TalentsAdapter(talentsDOList, this);
            recyclerView_talents.setAdapter(talentsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showLogoutPrompt(String title, String message) {
        new EZDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveBtnText("Yes")
                .setNegativeBtnText("No")
                .setButtonTextColor(R.color.colorPrimaryDarker)
                .setTitleTextColor(R.color.white)
                .setMessageTextColor(R.color.black)
                .setFont(Font.COMFORTAA)
                .setCancelableOnTouchOutside(false)
                .OnPositiveClicked(() -> {
                    SharedPrefManager.getInstance(getApplicationContext()).logoutUser();
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                })
                .OnNegativeClicked(() -> {
                    //todo
                })
                .build();
    }

    public void showInfoOfLoggedInUser(NavigationView navigationView){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(Messages.LOADING_ACCOUNT_INFO);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringBuilder sbParams = new StringBuilder();
        sbParams.append("?username_email={username_email}");

        AndroidNetworking.get(EndPoints.GET_PERSONAL_INFO_URL.concat(sbParams.toString()))
                .addPathParameter("username_email", SharedPrefManager.getInstance(MainActivity.this).getEmailOrUsername())
                .setTag(Tags.MAIN_ACTIVITY)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        getPersonalInfoResponse(response, navigationView);
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), anError.getErrorDetail(), Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getPersonalInfoResponse(JSONObject response, NavigationView navigationView){
        try{
            SharedPrefManager.getInstance(this).saveUserIdSession(response.getString("user_id"));
            checkUserRole(navigationView, response.getString("role_code"));

            lblLoggedInFullname.setText(response.getString("firstname") + " " + response.getString("lastname"));
            lblLoggedInRole.setText(response.getString("role_name"));
        }catch (JSONException e) {
            Log.e(Tags.MAIN_ACTIVITY, e.toString());
        }
    }

    private void checkUserRole(NavigationView navigationView, String userRole) {
        Menu navigationViewMenu = navigationView.getMenu();

        switch (userRole){
            case "CLIENT_COMPANY": case "CLIENT_INDIVIDUAL":
                navigationViewMenu.findItem(R.id.nav_booking_list).setVisible(true);
                break;
            default:
                navigationViewMenu.findItem(R.id.nav_check_availability).setVisible(true);
                break;
        }
    }
}
