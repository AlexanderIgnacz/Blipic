package com.trekr.Blipic.Controllers.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.trekr.R;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    View rootView;
    View notification_listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        init();
        return rootView;
    }

    public void init() {
        notification_listview = rootView.findViewById(R.id.notification_listview);
        notification_listview.setY(0-5000);
        doDelay();
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
                .animate(notification_listview)
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
