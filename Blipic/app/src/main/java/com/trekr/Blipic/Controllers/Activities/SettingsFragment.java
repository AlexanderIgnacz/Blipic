package com.trekr.Blipic.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.Common.Activities.AboutActivity;
import com.trekr.Common.Activities.AccountActivity;
import com.trekr.Common.Activities.PrivacyActivity;
import com.trekr.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    View rootView;
    RelativeLayout settings_btn_accountsetting;
    RelativeLayout settings_btn_privacy;
    RelativeLayout settings_btn_about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        setHeaderOptions();
        init();
        return rootView;
    }

    public void setHeaderOptions() {
        TextView prev = rootView.findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = rootView.findViewById(R.id.fragment_header_rightbtn);
        next.setVisibility(View.GONE);
    }

    public void init() {
        settings_btn_accountsetting = rootView.findViewById(R.id.settings_btn_accountsetting);
        settings_btn_privacy = rootView.findViewById(R.id.settings_btn_privacy);
        settings_btn_about = rootView.findViewById(R.id.settings_btn_about);

        settings_btn_accountsetting.setOnClickListener(this);
        settings_btn_privacy.setOnClickListener(this);
        settings_btn_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.settings_btn_accountsetting:
                intent = new Intent(getActivity(), AccountActivity.class);
                break;
            case R.id.settings_btn_privacy:
                intent = new Intent(getActivity(), PrivacyActivity.class);
                break;
            case R.id.settings_btn_about:
                intent = new Intent(getActivity(), AboutActivity.class);
                break;
            default:
                intent = new Intent(getActivity(), AccountActivity.class);
                break;
        }
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left);
    }
}
