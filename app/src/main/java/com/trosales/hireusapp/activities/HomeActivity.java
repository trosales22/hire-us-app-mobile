package com.trosales.hireusapp.activities;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.beans.Categories;
import com.trosales.hireusapp.fragments.HomeFragment;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.listener.FilterListener;
import com.yalantis.filter.widget.Filter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements FilterListener<Categories> {
    private TextView mTextMessage;

    private int[] mColors;
    private String[] mTitles;
    private Filter<Categories> mFilter;

    private FragmentManager fm = getSupportFragmentManager();
    private Fragment homeFragment = HomeFragment.newInstance();
    private Fragment activeFragment = homeFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    fm.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mColors = getResources().getIntArray(R.array.colors);
        mTitles = getResources().getStringArray(R.array.categories);

        mFilter = findViewById(R.id.filter);
        mFilter.setAdapter(new Adapter(getTags()));
        mFilter.setListener(this);

        //the text to show when there's no selected items
        mFilter.setNoSelectedItemText(getString(R.string.str_all_selected));
        mFilter.build();

        fm.beginTransaction().add(R.id.frame_layout, homeFragment, "1").commit();
    }

    private List<Categories> getTags() {
        List<Categories> tags = new ArrayList<>();

        for (int i = 0; i < mTitles.length; ++i) {
            tags.add(new Categories(mTitles[i], mColors[i]));
        }

        return tags;
    }

    @Override
    public void onFilterDeselected(Categories categories) {

    }

    @Override
    public void onFilterSelected(Categories categories) {

    }

    @Override
    public void onFiltersSelected(@NotNull ArrayList<Categories> arrayList) {

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
            FilterItem filterItem = new FilterItem(HomeActivity.this);

            filterItem.setStrokeColor(mColors[0]);
            filterItem.setTextColor(mColors[0]);
            filterItem.setCornerRadius(14);
            filterItem.setCheckedTextColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
            filterItem.setColor(ContextCompat.getColor(HomeActivity.this, android.R.color.white));
            filterItem.setCheckedColor(mColors[position]);
            filterItem.setText(item.getText());
            filterItem.deselect();

            return filterItem;
        }
    }
}
