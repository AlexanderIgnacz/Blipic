package com.trekr.Common.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.trekr.R;

import java.io.IOException;
import java.io.InputStream;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        setHeaderOptions();

        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setHeaderOptions() {
        TextView center = findViewById(R.id.fragment_header_centertitle);
        center.setText("License Agreement");
        TextView prev = findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = findViewById(R.id.fragment_header_rightbtn);
        next.setVisibility(View.GONE);
    }

    public void init() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
    }

}
