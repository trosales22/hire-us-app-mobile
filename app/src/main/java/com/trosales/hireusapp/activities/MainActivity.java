package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.adapters.TalentsAdapter;
import com.trosales.hireusapp.classes.beans.Location;
import com.trosales.hireusapp.classes.commons.AndroidNetworkingShortcuts;
import com.trosales.hireusapp.classes.commons.AutoFitGridLayoutManager;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.classes.wrappers.TalentsDO;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.kinst.jakub.view.SimpleStatefulLayout;
import spencerstudios.com.ezdialoglib.EZDialog;
import spencerstudios.com.ezdialoglib.Font;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private TextView lblLoggedInFullname,lblLoggedInRole;
    @BindView(R.id.stateful_layout) SimpleStatefulLayout simpleStatefulLayout;
    @BindView(R.id.swipeToRefresh_talents) SwipeRefreshLayout swipeToRefresh_talents;
    @BindView(R.id.recyclerView_talents) RecyclerView recyclerView_talents;
    @BindView(R.id.tapBarMenu) TapBarMenu tapBarMenu;
    @BindView(R.id.btnChoosePreferredDateAndTime) ImageView btnChoosePreferredDateAndTime;
    @BindView(R.id.btnSearchTalent) ImageView btnSearchTalent;
    @BindView(R.id.btnFilterCategory) ImageView btnFilterCategory;
    @BindView(R.id.btnFilteringOption) ImageView btnFilteringOption;

    private List<String> availableDateScheduleItems, availableTimeScheduleItems;
    private String selectedCategory, selectedDate = "", selectedTime = "", reservedDate, reservedTime;
    private HashMap<String, String> extraFiltering;

    private List<TalentsDO> talentsDOList;
    private TalentsAdapter talentsAdapter;
    protected Handler handler;
    private SkeletonScreen skeletonScreen;

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

        availableDateScheduleItems = new ArrayList<>();
        availableTimeScheduleItems = new ArrayList<>();
        extraFiltering = new HashMap<>();

        availableDateScheduleItems = getDatesUpToSpecificMonths(3);
        setMorningSchedule();
        setAfternoonSchedule();

        tapBarMenu.setOnClickListener(v -> tapBarMenu.toggle());

        btnChoosePreferredDateAndTime.setOnClickListener(v -> {
            new LovelyChoiceDialog(this, R.style.TintTheme)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Choose Preferred Date")
                    .setIcon(R.drawable.ic_date_range_white)
                    .setItemsMultiChoice(availableDateScheduleItems, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                                @Override
                                public void onItemsSelected(List<Integer> datePositions, List<String> dateItems) {
                                    selectedDate = TextUtils.join(",", dateItems);
                                    extraFiltering.put("selectedDate", selectedDate);

                                    new LovelyChoiceDialog(MainActivity.this, R.style.TintTheme)
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setTitle("Choose Preferred Time")
                                            .setMessage("Selected date: " + selectedDate)
                                            .setIcon(R.drawable.ic_access_time_white)
                                            .setItemsMultiChoice(availableTimeScheduleItems, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                                                        @Override
                                                        public void onItemsSelected(List<Integer> timePositions, List<String> timeItems) {
                                                            selectedTime = TextUtils.join(",", timeItems);
                                                            extraFiltering.put("selectedTime", selectedTime);
                                                            getAllTalents(extraFiltering);
                                                        }
                                                    }
                                            )
                                            .setConfirmButtonText("Done")
                                            .show();
                                }
                            }
                    )
                    .setConfirmButtonText("Next")
                    .show();
        });

        btnFilterCategory.setOnClickListener(v -> {
            String[] items = getResources().getStringArray(R.array.categories);
            new LovelyChoiceDialog(this, R.style.TintTheme)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Choose Categories")
                    .setIcon(R.drawable.ic_account_circle_white)
                    .setItemsMultiChoice(items, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                                @Override
                                public void onItemsSelected(List<Integer> categoryPositions, List<String> categoryItems) {
                                    selectedCategory = TextUtils.join(",", categoryItems);
                                    extraFiltering.put("selectedCategory", selectedCategory);
                                    getAllTalents(extraFiltering);
                                }
                            }
                    )
                    .setConfirmButtonText("Done")
                    .show();
        });

        btnFilteringOption.setOnClickListener(v -> startActivity(new Intent(v.getContext(), FilteringActivity.class)));

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

        talentsDOList = new ArrayList<>();
        handler = new Handler();

        AutoFitGridLayoutManager autoFitGridLayoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView_talents.setLayoutManager(autoFitGridLayoutManager);

        skeletonScreen = Skeleton.bind(recyclerView_talents)
                .adapter(talentsAdapter)
                .color(R.color.shimmer_color)
                .load(R.layout.talents_placeholder_layout)
                .show();

        skeletonScreen.show();

        //bundle = getIntent().getExtras();

        handler.postDelayed(() -> {
            getAllTalents(extraFiltering);
        }, 500);

        swipeToRefresh_talents.setOnRefreshListener(() -> {
            skeletonScreen.show();

            if(talentsDOList != null){
                talentsDOList.clear();
            }

            handler.postDelayed(() -> {
                getAllTalents(extraFiltering);
            }, 500);
            swipeToRefresh_talents.setRefreshing(false);
        });

        if(SharedPrefManager.getInstance(this).getUserRole().equals("TALENT_MODEL")){
            AndroidNetworkingShortcuts.prefetchPersonalInfo(
                    EndPoints.GET_TALENT_PERSONAL_INFO_URL.concat("?username_email={username_email}"),
                    SharedPrefManager.getInstance(MainActivity.this).getEmailOrUsername(),
                    Tags.MAIN_ACTIVITY,
                    Priority.LOW);
        }else {
            AndroidNetworkingShortcuts.prefetchPersonalInfo(
                    EndPoints.GET_PERSONAL_INFO_URL.concat("?username_email={username_email}"),
                    SharedPrefManager.getInstance(MainActivity.this).getEmailOrUsername(),
                    Tags.MAIN_ACTIVITY,
                    Priority.LOW);
        }

        showInfoOfLoggedInUser(navigationView);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        } else if (id == R.id.nav_clients_booked) {
            startActivity(new Intent(this, ClientsActivity.class));
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

    private List<String> getDatesUpToSpecificMonths(int months){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        List<String> availableDatesList = new ArrayList<>();

        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int maxDay = c.getActualMaximum(Calendar.DAY_OF_MONTH) * months;
        for(int co=0; co<=maxDay; co++){
            c.add(Calendar.DATE, 1);
            availableDatesList.add(sdf.format(c.getTime()));
        }

        return availableDatesList;
    }

    private void setMorningSchedule(){
        int initialValue = 1;
        String meridian = " AM";

        availableTimeScheduleItems.add("12-1 AM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " PM";
            }

            availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
        }

    }

    private void setAfternoonSchedule(){
        int initialValue = 1;
        String meridian = " AM";

        availableTimeScheduleItems.add("12-1 PM");

        for(int i = 1; i <= 11; i++){
            initialValue++;

            if(initialValue >= 12){
                meridian = " AM";
            }

            availableTimeScheduleItems.add(i + "-" + initialValue + meridian);
        }
    }

    private StringBuilder getParams(HashMap<String, String> filteringOption){
        StringBuilder sbParams = new StringBuilder();

        if(!filteringOption.isEmpty()){
            sbParams.append("?");

            Log.d("debug", "filteringOption: " + filteringOption.toString());

            if(filteringOption.get("height_from") != null){
                if(!Objects.requireNonNull(filteringOption.get("height_from")).isEmpty()){
                    sbParams.append("&height_from={height_from}");
                }
            }

            if(filteringOption.get("height_to") != null) {
                if (!Objects.requireNonNull(filteringOption.get("height_to")).isEmpty()) {
                    sbParams.append("&height_to={height_to}");
                }
            }

            if(filteringOption.get("age_from") != null) {
                if (!Objects.requireNonNull(filteringOption.get("age_from")).isEmpty()) {
                    sbParams.append("&age_from={age_from}");
                }
            }

            if(filteringOption.get("age_to") != null) {
                if (!Objects.requireNonNull(filteringOption.get("age_to")).isEmpty()) {
                    sbParams.append("&age_to={age_to}");
                }
            }

            if(filteringOption.get("rate_per_hour_from") != null) {
                if (!Objects.requireNonNull(filteringOption.get("rate_per_hour_from")).isEmpty()) {
                    sbParams.append("&rate_per_hour_from={rate_per_hour_from}");
                }
            }

            if(filteringOption.get("rate_per_hour_to") != null) {
                if (!Objects.requireNonNull(filteringOption.get("rate_per_hour_to")).isEmpty()) {
                    sbParams.append("&rate_per_hour_to={rate_per_hour_to}");
                }
            }

            if(filteringOption.get("province_code") != null) {
                if (filteringOption.get("province_code") != null) {
                    sbParams.append("&province_code={province_code}");
                }
            }

            if(filteringOption.get("city_muni_code") != null) {
                if (filteringOption.get("city_muni_code") != null) {
                    sbParams.append("&city_muni_code={city_muni_code}");
                }
            }

            if(filteringOption.get("gender") != null) {
                if (!Objects.requireNonNull(filteringOption.get("gender")).isEmpty()) {
                    sbParams.append("&gender={gender}");
                }
            }
        }

        return sbParams;
    }

    private void getAllReservedScheduleOfTalent(String talent_id){
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(EndPoints.GET_RESERVED_SCHEDULE_OF_TALENT);
        sbUrl.append("?talent_id={talent_id}");

        AndroidNetworking
                .get(sbUrl.toString())
                .addPathParameter("talent_id", talent_id)
                .setTag(Tags.MAIN_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("reserved_schedule_list");

                            if(response.has("flag") && response.has("msg")){
                                Log.d("debug", response.getString("msg"));
                            }else{
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    reservedDate = object.getString("reserved_date");
                                    reservedTime = object.getString("reserved_time");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.MAIN_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }

    private void getAllTalents(HashMap<String, String> extraFiltering){
        talentsDOList.clear();
        HashMap<String, String> filteringOption = SharedPrefManager.getInstance(this).getFilteringOption();
        Log.d("debug", EndPoints.GET_ALL_TALENTS_URL.concat(getParams(filteringOption).toString()));

        StringBuilder sbExtraFilteringParams = new StringBuilder();

        if(!extraFiltering.isEmpty()) {
            Log.d("debug", "extraFiltering: " + extraFiltering.toString());
            if(extraFiltering.get("selectedCategory") != null) {
                if (!Objects.requireNonNull(extraFiltering.get("selectedCategory")).isEmpty()) {
                    sbExtraFilteringParams.append("&selected_categories={selected_categories}");
                }
            }
        }

        AndroidNetworking
                .get(EndPoints.GET_ALL_TALENTS_URL.concat(getParams(filteringOption).toString()).concat(sbExtraFilteringParams.toString()))
                .addPathParameter("height_from", filteringOption.get("height_from"))
                .addPathParameter("height_to", filteringOption.get("height_to"))
                .addPathParameter("age_from", filteringOption.get("age_from"))
                .addPathParameter("age_to", filteringOption.get("age_to"))
                .addPathParameter("rate_per_hour_from", filteringOption.get("rate_per_hour_from"))
                .addPathParameter("rate_per_hour_to", filteringOption.get("rate_per_hour_to"))
                .addPathParameter("province_code", filteringOption.get("province_code"))
                .addPathParameter("city_muni_code", filteringOption.get("city_muni_code"))
                .addPathParameter("gender", filteringOption.get("gender"))
                .addPathParameter("selected_categories", extraFiltering.get("selectedCategory"))
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
                boolean isAvailable = true;

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    getAllReservedScheduleOfTalent(object.getString("talent_id"));

                    Log.d("debug", "selectedDate: " + selectedDate);
                    Log.d("debug", "selectedTime: " + selectedTime);

                    Log.d("debug", "reservedDate: " + reservedDate);
                    Log.d("debug", "reservedTime: " + reservedTime);

                    String[] selectedDateList = selectedDate.split(",");
                    String[] selectedTimeList = selectedTime.split(",");
                    TalentsDO talentsDO = new TalentsDO(
                            object.getString("talent_id"),
                            object.getString("fullname"),
                            object.getString("height"),
                            object.getString("hourly_rate"),
                            object.getString("gender"),
                            object.getString("talent_display_photo"),
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

                    if(!selectedDate.isEmpty() && !selectedTime.isEmpty()) {
                        for (String date : selectedDateList) {
                            if (!reservedDate.contains(date)) {
                                Log.d("debug", "WALANG NAKARESERVE NA DATE");

                                for (String time : selectedTimeList) {
                                    if (!reservedTime.contains(time)) {
                                        Log.d("debug", "WALANG NAKARESERVE NA TIME");
                                    } else {
                                        Log.d("debug", "MAY NAKARESERVE NA TIME");
                                        Toast.makeText(this, time + " was already reserved!", Toast.LENGTH_LONG).show();
                                        isAvailable = false;
                                    }
                                }
                            } else {
                                isAvailable = false;
                                Log.d("debug", "MAY NAKARESERVE NA DATE");
                                Toast.makeText(this, date + " was already reserved!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    if(isAvailable){
                        talentsDOList.add(talentsDO);
                    }
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

        String finalUrl = EndPoints.GET_PERSONAL_INFO_URL;

        if(SharedPrefManager.getInstance(this).getUserRole().equals("TALENT_MODEL")){
            finalUrl = EndPoints.GET_TALENT_PERSONAL_INFO_URL;
        }

        AndroidNetworking.get(finalUrl.concat(sbParams.toString()))
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
            if(SharedPrefManager.getInstance(this).getUserRole().equals("TALENT_MODEL")){
                SharedPrefManager.getInstance(this).saveUserIdSession(response.getString("talent_id"));
                checkUserRole(navigationView, response.getString("role_name"));
            }else{
                SharedPrefManager.getInstance(this).saveUserIdSession(response.getString("user_id"));
                checkUserRole(navigationView, response.getString("role_code"));
            }

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
                navigationViewMenu.findItem(R.id.nav_clients_booked).setVisible(true);
                navigationViewMenu.findItem(R.id.nav_check_availability).setVisible(true);
                break;
        }
    }
}
