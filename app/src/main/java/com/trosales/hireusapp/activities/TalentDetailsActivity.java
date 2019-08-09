package com.trosales.hireusapp.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.commons.SharedPrefManager;
import com.trosales.hireusapp.classes.constants.EndPoints;
import com.trosales.hireusapp.classes.constants.Tags;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalentDetailsActivity extends AppCompatActivity {
    @BindView(R.id.imgTalentProfilePicture) ImageView imgTalentProfilePicture;
    @BindView(R.id.lblTalentFullname) TextView lblTalentFullname;
    @BindView(R.id.lblTalentCategories) TextView lblTalentCategories;
    @BindView(R.id.lblTalentFee) HtmlTextView lblTalentFee;
    @BindView(R.id.lblTalentHeight) TextView lblTalentHeight;
    @BindView(R.id.lblTalentDescription) HtmlTextView lblTalentDescription;
    @BindView(R.id.lblTalentAge) TextView lblTalentAge;
    @BindView(R.id.lblTalentLocation) TextView lblTalentLocation;
    @BindView(R.id.lblTalentExperience) TextView lblTalentExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_details);
        ButterKnife.bind(this);

        getTalentDetails();
    }

    private void getTalentDetails(){
        StringBuilder sbParams = new StringBuilder();
        sbParams.append("?talent_id={talent_id}");

        AndroidNetworking
                .get(EndPoints.GET_SELECTED_TALENT_DETAILS_URL.concat(sbParams.toString()))
                .addPathParameter("talent_id", SharedPrefManager.getInstance(getApplicationContext()).getTalentId())
                .setTag(Tags.TALENT_DETAILS_ACTIVITY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("flag") && response.has("msg")){
                                Log.d("debug", response.getString("msg"));
                            }else{
                                StringBuilder sbTalentDisplayPhoto = new StringBuilder();

                                sbTalentDisplayPhoto
                                        .append(EndPoints.UPLOADS_BASE_URL)
                                        .append("talents_or_models/")
                                        .append(response.getString("talent_display_photo"));

                                Picasso.with(getApplicationContext()).load(sbTalentDisplayPhoto.toString()).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        imgTalentProfilePicture.setImageBitmap(bitmap);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) { }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {}
                                });

                                imgTalentProfilePicture.setOnClickListener(view -> {
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(TalentDetailsActivity.this);
                                    View mView = getLayoutInflater().inflate(R.layout.preview_image_dialog, null);
                                    PhotoView photoView = mView.findViewById(R.id.imgPreviewTalentDisplayPhoto);

                                    Picasso.with(getApplicationContext()).load(sbTalentDisplayPhoto.toString()).into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            photoView.setImageBitmap(bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) { }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {}
                                    });

                                    mBuilder.setView(mView);
                                    AlertDialog mDialog = mBuilder.create();
                                    mDialog.show();
                                });

                                lblTalentFullname.setText(response.getString("fullname"));
                                lblTalentCategories.setText(response.getString("category_names"));

                                String talentFeeType = null;

                                switch (response.getString("talent_fee_type")){
                                    case "HOURLY_RATE":
                                        talentFeeType = "Hourly";
                                        break;
                                    case "DAILY_RATE":
                                        talentFeeType = "Daily";
                                        break;
                                }

                                lblTalentFee.setHtml("<p>&#8369;" + response.getString("talent_fee") + "<br>" + talentFeeType + "</p>",
                                        new HtmlResImageGetter(lblTalentFee));

                                lblTalentHeight.setText(response.getString("height") + " ft.");

                                lblTalentDescription.setHtml("<p>&quot;" + (response.getString("talent_description").isEmpty() ? "No description as of the moment." : response.getString("talent_description")) + "&quot;</p>",
                                        new HtmlResImageGetter(lblTalentDescription));

                                lblTalentAge.setText(response.getString("age") + "yrs old");
                                lblTalentLocation.setText(response.getString("location"));
                                lblTalentExperience.setText(response.getString("talent_experiences"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(Tags.TALENT_DETAILS_ACTIVITY, anError.getErrorDetail());
                    }
                });
    }
}
