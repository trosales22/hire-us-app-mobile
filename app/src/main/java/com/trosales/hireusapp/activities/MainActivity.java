package com.trosales.hireusapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.AndroidNetworkingShortcuts;
import com.trosales.hireusapp.classes.commons.AppSecurity;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Messages;
import com.trosales.hireusapp.classes.constants.Tags;
import com.trosales.hireusapp.fragments.BookingsFragment;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private TextView lblLoggedInFullname, lblLoggedInRole;
    private String selectedCategory;

    @BindView(R.id.card_view_talents) CardView card_view_talents;
    @BindView(R.id.card_view_news) CardView card_view_news;
    @BindView(R.id.card_view_announcements) CardView card_view_announcements;
    @BindView(R.id.card_view_feedback) CardView card_view_feedback;
    @BindView(R.id.card_view_faqs) CardView card_view_faqs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        AppSecurity.disableScreenshotRecording(this);

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

        card_view_talents.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirmation")
                    .setContentText("Are you sure you want to see talents section?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(this, TalentsActivity.class));
                        sDialog.dismissWithAnimation();
                    })
                    .setCancelText("No")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .show();
        });

        card_view_announcements.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirmation")
                    .setContentText("Are you sure you want to see announcement section?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(this, AnnouncementsActivity.class));
                        sDialog.dismissWithAnimation();
                    })
                    .setCancelText("No")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .show();
        });

        card_view_news.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirmation")
                    .setContentText("Are you sure you want to see news & updates section?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sDialog -> {
                        startActivity(new Intent(this, NewsActivity.class));
                        sDialog.dismissWithAnimation();
                    })
                    .setCancelText("No")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .show();
        });

        card_view_feedback.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops! We're sorry!")
                    .setContentText("Feedback section still an ongoing feature. Thank you.")
                    .setConfirmText("Ok, I understand!")
                    .show();
        });

        card_view_faqs.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops! We're sorry!")
                    .setContentText("FAQs section still an ongoing feature. Thank you.")
                    .setConfirmText("Ok, I understand!")
                    .show();
        });
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
            //startActivity(new Intent(this, SettingsActivity.class));
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops! We're sorry!")
                    .setContentText("Ongoing development. Thank you for your patience.")
                    .setConfirmText("Ok, I understand!")
                    .show();
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
        } else if(id == R.id.nav_search_a_talent){
            String[] items = getResources().getStringArray(R.array.categories);
            new LovelyChoiceDialog(this, R.style.TintTheme)
                    .setTopColorRes(R.color.colorPrimary)
                    .setTitle("Choose Categories")
                    .setIcon(R.drawable.ic_account_circle_white)
                    .setItemsMultiChoice(items, (categoryPositions, categoryItems) -> {
                                selectedCategory = TextUtils.join(",", categoryItems);

                                Bundle bookingsBundleArgs = new Bundle();
                                bookingsBundleArgs.putString("selectedCategory", selectedCategory);

                                Intent intent = new Intent(MainActivity.this, TalentsActivity.class);
                                intent.putExtras(bookingsBundleArgs);
                                startActivity(intent);
                            }
                    )
                    .setConfirmButtonText("Done")
                    .show();
        } else if (id == R.id.nav_booking_list) {
            startActivity(new Intent(this, BookingListActivity.class));
        } else if (id == R.id.nav_clients_booked) {
            startActivity(new Intent(this, PotentialClientsActivity.class));
        } else if (id == R.id.nav_logout) {
            showLogoutPrompt(Messages.CONFIRMATION_CAPTION, Messages.LOGOUT_PROMPT);
        } else if (id == R.id.nav_about_app) {
            new LovelyInfoDialog(this)
                    .setTopColorRes(R.color.colorPrimary)
                    .setIcon(R.drawable.ic_info_outline_white)
                    .setTitle("About Hire Us PH")
                    .setMessage("A mobile app/web based booking application that will cater market in entertainment and events industry. " +
                            "To have own payment system that allows customers/client to pay online in order to book using debit/credit card.\n")
                    .show();
        } else if (id == R.id.nav_terms_and_conditions){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops! We're sorry!")
                    .setContentText("Terms & Conditions section still an ongoing feature. Thank you.")
                    .setConfirmText("Ok, I understand!")
                    .show();
        } else if (id == R.id.nav_privacy_policy){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops! We're sorry!")
                    .setContentText("Privacy Policy section still an ongoing feature. Thank you.")
                    .setConfirmText("Ok, I understand!")
                    .show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showLogoutPrompt(String title, String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Yes")
                .setConfirmClickListener(sDialog -> {
                    SharedPrefManager.getInstance(getApplicationContext()).logoutUser();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                })
                .setCancelText("No")
                .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    public void showInfoOfLoggedInUser(NavigationView navigationView){
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage(Messages.LOADING_ACCOUNT_INFO);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String finalUrl = EndPoints.GET_PERSONAL_INFO_URL;

        if(SharedPrefManager.getInstance(this).getUserRole().equals("TALENT_MODEL")){
            finalUrl = EndPoints.GET_TALENT_PERSONAL_INFO_URL;
        }

        AndroidNetworking.get(finalUrl.concat("?username_email={username_email}"))
                .addPathParameter("username_email", SharedPrefManager.getInstance(MainActivity.this).getEmailOrUsername())
                .setTag(Tags.MAIN_ACTIVITY)
                .setPriority(Priority.HIGH)
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
                        Log.d("anError", String.valueOf(anError.getErrorCode()));
                        Snackbar.make(findViewById(android.R.id.content), anError.getErrorDetail(), Snackbar.LENGTH_LONG).show();

                        if(anError.getErrorCode() == 0){
                            SharedPrefManager.getInstance(getApplicationContext()).logoutUser();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getPersonalInfoResponse(JSONObject response, NavigationView navigationView){
        String errorMsg = null;
        try{
            if (response.has("flag") && response.has("msg")) {
                errorMsg = response.getString("msg");
            }

            if(SharedPrefManager.getInstance(this).getUserRole().equals("TALENT_MODEL")){
                SharedPrefManager.getInstance(this).saveUserIdSession(response.getString("talent_id"));
                checkUserRole(navigationView, response.getString("role_name"));

                lblLoggedInRole.setText("Talent / Model");
            }else{
                SharedPrefManager.getInstance(this).saveUserIdSession(response.getString("user_id"));
                checkUserRole(navigationView, response.getString("role_code"));

                lblLoggedInRole.setText(response.getString("role_name"));
            }

            lblLoggedInFullname.setText(response.getString("firstname") + " " + response.getString("lastname"));

        }catch (JSONException e) {
            Log.e(Tags.MAIN_ACTIVITY, e.toString());

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops! We're sorry!")
                    .setContentText(errorMsg)
                    .setConfirmClickListener(sweetAlertDialog -> {
                        SharedPrefManager.getInstance(getApplicationContext()).logoutUser();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    })
                    .show();
        }
    }

    private void checkUserRole(NavigationView navigationView, String userRole) {
        Menu navigationViewMenu = navigationView.getMenu();

        switch (userRole){
            case "CLIENT_COMPANY": case "CLIENT_INDIVIDUAL":
                card_view_announcements.setVisibility(View.GONE);
                navigationViewMenu.findItem(R.id.nav_booking_list).setVisible(true);
                navigationViewMenu.findItem(R.id.nav_search_a_talent).setVisible(true);
                break;
            default:
                card_view_talents.setVisibility(View.GONE);
                navigationViewMenu.findItem(R.id.nav_clients_booked).setVisible(true);
                navigationViewMenu.findItem(R.id.nav_search_a_talent).setVisible(false);
                break;
        }
    }
}
