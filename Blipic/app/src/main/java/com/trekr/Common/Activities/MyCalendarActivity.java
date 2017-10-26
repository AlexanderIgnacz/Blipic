package com.trekr.Common.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.trekr.R;

import java.util.ArrayList;
import java.util.List;

public class MyCalendarActivity extends AppCompatActivity implements View.OnClickListener {

    View my_calendar_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void init() {
        my_calendar_listview = findViewById(R.id.my_calendar_listview);
        my_calendar_listview.setY(0-5000);
        doDelay();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_top, R.anim.transition_to_bottom);
    }

    public void doDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doAnimation();
            }
        }, 1500);
    }

    public void doAnimation() {
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(my_calendar_listview)
                .withLayer()
                .y(60)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(500)
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

}
