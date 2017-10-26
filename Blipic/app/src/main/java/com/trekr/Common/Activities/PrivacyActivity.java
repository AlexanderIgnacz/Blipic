package com.trekr.Common.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.trekr.App;
import com.trekr.R;
import com.trekr.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.trekr.Utils.Constants.kBLPSubCategoryDuration;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    View dlg;
    View dlg_back;
    TextView dlg_close_tv;
    boolean isAnimating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        setHeaderOptions();
        init();
        setupDlg();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setHeaderOptions() {
        TextView center = findViewById(R.id.fragment_header_centertitle);
        center.setText("Privacy");
        TextView prev = findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = findViewById(R.id.fragment_header_rightbtn);
        next.setVisibility(View.GONE);
    }

    public void init() {
        RelativeLayout privacy_locationaccess_btn = findViewById(R.id.privacy_locationaccess_btn);
        privacy_locationaccess_btn.setOnClickListener(this);
        RelativeLayout privacy_camera_btn = findViewById(R.id.privacy_camera_btn);
        privacy_camera_btn.setOnClickListener(this);
        RelativeLayout privacy_media_btn = findViewById(R.id.privacy_media_btn);
        privacy_media_btn.setOnClickListener(this);
        RelativeLayout privacy_microphone_btn = findViewById(R.id.privacy_microphone_btn);
        privacy_microphone_btn.setOnClickListener(this);
        RelativeLayout privacy_notifications_btn = findViewById(R.id.privacy_notifications_btn);
        privacy_notifications_btn.setOnClickListener(this);

        RelativeLayout location_state_0 = findViewById(R.id.location_state_0);
        RelativeLayout location_state_1 = findViewById(R.id.location_state_1);
        RelativeLayout camera_state_0 = findViewById(R.id.camera_state_0);
        RelativeLayout camera_state_1 = findViewById(R.id.camera_state_1);
        RelativeLayout media_state_0 = findViewById(R.id.media_state_0);
        RelativeLayout media_state_1 = findViewById(R.id.media_state_1);
        RelativeLayout microphone_state_0 = findViewById(R.id.microphone_state_0);
        RelativeLayout microphone_state_1 = findViewById(R.id.microphone_state_1);
        RelativeLayout notifications_state_0 = findViewById(R.id.notifications_state_0);
        RelativeLayout notifications_state_1 = findViewById(R.id.notifications_state_1);

        location_state_0.setVisibility(View.GONE);
        location_state_1.setVisibility(View.VISIBLE);
        camera_state_0.setVisibility(View.GONE);
        camera_state_1.setVisibility(View.VISIBLE);
        media_state_0.setVisibility(View.VISIBLE);
        media_state_1.setVisibility(View.GONE);
        microphone_state_0.setVisibility(View.GONE);
        microphone_state_1.setVisibility(View.VISIBLE);
        notifications_state_0.setVisibility(View.GONE);
        notifications_state_1.setVisibility(View.VISIBLE);

        dlg_close_tv = findViewById(R.id.dlg_close_tv);
        dlg_close_tv.setOnClickListener(this);

        isAnimating = false;
    }

    @Override
    public void onClick(View view) {
        if (isAnimating) {
            return;
        }
        int id = view.getId();
        switch (id) {
            case R.id.privacy_locationaccess_btn:
                break;
            case R.id.privacy_camera_btn:
                break;
            case R.id.privacy_media_btn:
                doShowAnimation();
                break;
            case R.id.privacy_microphone_btn:
                break;
            case R.id.privacy_notifications_btn:
                break;
            case R.id.licence_dlg:
                break;
            case R.id.dlg_back:
                doHideAnimation();
                break;
            case R.id.dlg_close_tv:
                doHideAnimation();
                break;
        }
    }

    public void setupDlg() {
        dlg = findViewById(R.id.licence_dlg);
        dlg.setOnClickListener(this);
        dlg_back = findViewById(R.id.dlg_back);
        dlg_back.setAlpha((float) 0.0);
        dlg_back.setVisibility(View.GONE);
        dlg.setY(0-2000);
        dlg_back.setOnClickListener(this);
    }

    public void doShowAnimation() {
        isAnimating = true;
        dlg_back.setVisibility(View.VISIBLE);
        dlg_back.setAlpha((float) 0.0);
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(dlg)
                .withLayer()
                .y((App.getInstance().screenHeight) / 2 - dlg.getHeight() / 2)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .get());
        animatorList.add(ViewPropertyObjectAnimator
                .animate(dlg_back)
                .withLayer()
                .alpha((float) 0.8)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();
    }

    public void doHideAnimation() {
        isAnimating = true;
        dlg_back.setVisibility(View.VISIBLE);
        dlg_back.setAlpha((float) 0.8);
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(dlg)
                .withLayer()
                .y(2000)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .get());
        animatorList.add(ViewPropertyObjectAnimator
                .animate(dlg_back)
                .withLayer()
                .alpha((float) 0.0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dlg_back.setVisibility(View.GONE);
                dlg.setY(0-2000);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
    }
}
