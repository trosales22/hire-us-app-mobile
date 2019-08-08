package com.trosales.hireusapp.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.constants.EndPoints;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalentDetailsActivity extends AppCompatActivity {
    @BindView(R.id.imgTalentProfilePicture) ImageView imgTalentProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_details);
        ButterKnife.bind(this);

        Picasso.with(this).load(EndPoints.UPLOADS_BASE_URL + "talents_or_models/c2cb890f6182e06a361d208b442ddc22.jpg").into(new Target() {
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

            Picasso.with(this).load(EndPoints.UPLOADS_BASE_URL + "talents_or_models/c2cb890f6182e06a361d208b442ddc22.jpg").into(new Target() {
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
    }
}
