package com.trekr.Common.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout settings_btn_license;
    RelativeLayout settings_btn_terms;
    RelativeLayout settings_btn_pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setHeaderOptions();

        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setHeaderOptions() {
        TextView center = findViewById(R.id.fragment_header_centertitle);
        center.setText("About");
        TextView prev = findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = findViewById(R.id.fragment_header_rightbtn);
        next.setVisibility(View.GONE);
    }

    public void init() {
        settings_btn_license = findViewById(R.id.settings_btn_license);
        settings_btn_terms = findViewById(R.id.settings_btn_terms);
        settings_btn_pp = findViewById(R.id.settings_btn_pp);

        settings_btn_license.setOnClickListener(this);
        settings_btn_terms.setOnClickListener(this);
        settings_btn_pp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.settings_btn_license:
                intent = new Intent(this, LicenseActivity.class);
                break;
            case R.id.settings_btn_terms:
                intent = new Intent(this, TermsActivity.class);
                break;
            case R.id.settings_btn_pp:
                intent = new Intent(this, PrivacyPolicyActivity.class);
                break;
            default:
                intent = new Intent(this, LicenseActivity.class);
                break;
        }
        startActivity(intent);
        overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
    }
}
