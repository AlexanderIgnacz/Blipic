package com.trekr.Common.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trekr.Common.SliderImageViewPagerAdapter.SliderImageViewPagerAdapter;
import com.trekr.R;


public class SliderImageActivity extends AppCompatActivity {

    ViewPager viewPager;
    int images[] = {R.drawable.wall_water_x2, R.drawable.full_map_background_x2, R.drawable.wall_mountain_x2, R.drawable.wall_snow_x2};
    SliderImageViewPagerAdapter myCustomPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_image);

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        myCustomPagerAdapter = new SliderImageViewPagerAdapter(this, images);
        viewPager.setAdapter(myCustomPagerAdapter);
    }
}
