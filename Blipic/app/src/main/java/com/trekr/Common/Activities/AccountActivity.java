package com.trekr.Common.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.trekr.R;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        setHeaderOptions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setHeaderOptions() {
        TextView center = findViewById(R.id.fragment_header_centertitle);
        center.setText("Account");
        TextView prev = findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = findViewById(R.id.fragment_header_rightbtn);
        next.setText("Save");
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fragment_header_rightbtn:
                finish();
                overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
    }
}
