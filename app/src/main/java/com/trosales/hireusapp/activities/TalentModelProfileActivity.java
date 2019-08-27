package com.trosales.hireusapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.trosales.hireusapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import ws.wolfsoft.get_detail.Bean;
import ws.wolfsoft.get_detail.ChildAnimationExample;
import ws.wolfsoft.get_detail.ExpandableHeightListView;
import ws.wolfsoft.get_detail.JayBaseAdapter;
import ws.wolfsoft.get_detail.SliderLayout;

public class TalentModelProfileActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{
    SliderLayout mDemoSlider;

    LinearLayout linear1,showless;
    LinearLayout linear2;

    private ExpandableHeightListView listview;
    private ArrayList<ws.wolfsoft.get_detail.Bean> Bean;
    private JayBaseAdapter baseAdapter;

    private int[] IMAGE = {R.drawable.p1, R.drawable.p2, R.drawable.p3};
    private String[] TITLE = {"Best seller", "Dunt think twise for it", "Good product"};
    private String[] RATING = {"4.5 rating", "5 rating", "4 rating"};
    private String[] BY = {"by Kelly","by Emma","by Erik"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_model_profile);

        //        ********LISTVIEW***********


        listview = findViewById(R.id.listview);


        Bean = new ArrayList<>();

        for (int i= 0; i< TITLE.length; i++){

            Bean bean = new Bean(IMAGE[i], TITLE[i], RATING[i], BY[i]);
            Bean.add(bean);

        }

        baseAdapter = new JayBaseAdapter(TalentModelProfileActivity.this, Bean) {
        };

        listview.setAdapter(baseAdapter);

        //                ***********viewmore**********

        linear1 = findViewById(R.id.linear1);
        showless = findViewById(R.id.showless);

        linear2 = findViewById(R.id.linear2);

        linear1.setOnClickListener(v -> {
            linear2.setVisibility(View.VISIBLE);
            linear1.setVisibility(View.GONE);
        });

        showless.setOnClickListener(v -> {
            linear2.setVisibility(View.GONE);
            linear1.setVisibility(View.VISIBLE);


        });

//         ********Slider*********

        mDemoSlider = findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<>();
        file_maps.put("1", R.drawable.iohone1);
        file_maps.put("2",R.drawable.iphone2);
        file_maps.put("3",R.drawable.iphone3);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {

    }
}
