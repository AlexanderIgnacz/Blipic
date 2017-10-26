package com.trekr.Blipic.Controllers.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.trekr.App;
import com.trekr.Blipic.Controllers.HomeMap.Components.CategoryView;
import com.trekr.Common.Activities.MyBlipActivity;
import com.trekr.Common.Activities.MyCalendarActivity;
import com.trekr.R;

import java.util.ArrayList;
import java.util.List;

public class MyStuffFragment extends Fragment implements CategoryView.OnCategoryItemClicked, CategoryView.OnOutClicked, CategoryView.OnComingSoon {

    View rootView;
    View my_staff_category_container;
    View my_staff_black_back;
    View my_staff_label;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_stuff, container, false);
        init();
        return rootView;
    }

    public void init() {
        my_staff_category_container = rootView.findViewById(R.id.my_staff_category_container);
        ViewGroup.LayoutParams lp = my_staff_category_container.getLayoutParams();
        lp.width = App.getInstance().screenWidth - 80;
        lp.height = App.getInstance().screenWidth - 80;
        my_staff_category_container.setLayoutParams(lp);

        CategoryView category = rootView.findViewById(R.id.my_staff_category);
        category.setOnItemClickedListener(this);
        category.setOnOutClickedListener(this);
        category.setOnComingSoonListener(this);
        category.setImageResource(0, R.drawable.btn_calendar_drawable);
        category.setImageResource(1, R.drawable.btn_blip_drawable);
        //if enable
//        category.setImageResource(2, R.drawable.btn_users_drawable);
//        category.setImageResource(3, R.drawable.btn_messages_drawable);
        //else
        category.setImageResource(2, R.drawable.my_staff_users_empty);
        category.setImageResource(3, R.drawable.my_staff_messages_empty);
        category.setDanger();

        my_staff_black_back = rootView.findViewById(R.id.my_staff_black_back);
        my_staff_black_back.setVisibility(View.GONE);
        my_staff_black_back.setAlpha((float) 0.0);

        my_staff_label = rootView.findViewById(R.id.my_staff_label);
        my_staff_label.setVisibility(View.GONE);
        my_staff_label.setAlpha((float) 0.0);
    }


    @Override
    public void onCategoryItemClicked(Object object) {
        Intent intent;
        switch (object.toString()) {
            case "key-0":
                intent = new Intent(getActivity(), MyCalendarActivity.class);
                break;
            case "key-1":
                intent = new Intent(getActivity(), MyBlipActivity.class);
                break;
            case "key-2":
                intent = null;
                break;
            case "key-3":
                intent = null;
                break;
            default:
                intent = null;
                break;
        }
        if (intent != null) {
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.transition_from_bottom, R.anim.transition_to_top);
        }
    }

    @Override
    public void onOutClicked() {

    }


    @Override
    public void onComingSoon() {
        doShowAnimation();
    }

    public void doShowAnimation() {
        my_staff_black_back.setVisibility(View.VISIBLE);
        my_staff_label.setVisibility(View.VISIBLE);
        my_staff_black_back.setAlpha((float) 0.0);
        my_staff_label.setAlpha((float) 0.0);
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(my_staff_black_back)
                .withLayer()
                .alpha((float) 0.5)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());

        animatorList.add(ViewPropertyObjectAnimator
                .animate(my_staff_label)
                .withLayer()
                .alpha((float) 1.0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setUserTouchEnable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doHideAnimation();
                    }
                }, 1000);
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
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(my_staff_black_back)
                .withLayer()
                .alpha((float) 0.0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());

        animatorList.add(ViewPropertyObjectAnimator
                .animate(my_staff_label)
                .withLayer()
                .alpha((float) 0.0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setUserTouchEnable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                my_staff_black_back.setVisibility(View.GONE);
                my_staff_label.setVisibility(View.GONE);
                setUserTouchEnable(true);
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

    public void setUserTouchEnable(boolean isSet) {
        if (isSet) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
