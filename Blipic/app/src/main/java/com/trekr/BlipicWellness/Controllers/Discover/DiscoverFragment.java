package com.trekr.BlipicWellness.Controllers.Discover;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.trekr.App;
import com.trekr.Blipic.Controllers.HomeMap.MapView;
import com.trekr.Blipic.Controllers.LocalFeed.LocalFeedView;
import com.trekr.BlipicWellness.Controllers.HomeActivity;
import com.trekr.Common.BaseFragment;
import com.trekr.R;

public class DiscoverFragment extends BaseFragment implements HomeActivity.OnBackPressedListener, View.OnClickListener {

    RelativeLayout relativeLayout;

    RelativeLayout view1;
    RelativeLayout view2;
    RelativeLayout view3;

    private boolean animating = false;

    private int state = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);

        ((HomeActivity) getActivity()).setStatusBar(false, getResources().getColor(android.R.color.black));
        ((HomeActivity)getActivity()).OnBackPressedListener = this;

        relativeLayout = (RelativeLayout) view.findViewById(R.id.random_splitcontainer);

        init();
        view.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        ((HomeActivity)getActivity()).OnBackPressedListener = null;
        super.onDestroyView();
    }

    public void init() {
        int deviceWidth = App.getInstance().screenWidth;
        int deviceHeight = App.getInstance().screenHeight - ((HomeActivity) getActivity()).getStatusBarHeight();

        view1 = new RelativeLayout(getActivity());
        view2 = new RelativeLayout(getActivity());
        view3 = new RelativeLayout(getActivity());

        relativeLayout.addView(view1);
        relativeLayout.addView(view2);
        relativeLayout.addView(view3);

        RelativeLayout.LayoutParams view1_layoutparams = (RelativeLayout.LayoutParams) view1.getLayoutParams();
        view1_layoutparams.width = deviceWidth;
        view1_layoutparams.height = (deviceHeight - 40) / 3;
        view1_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        view1.setLayoutParams(view1_layoutparams);

        view1.addView(new LocalFeedView(getActivity()));
        view1.addView(new RelativeLayout(getActivity()));

        view1.getChildAt(1).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams view2_layoutparams = (RelativeLayout.LayoutParams) view2.getLayoutParams();
        view2_layoutparams.width = deviceWidth;
        view2_layoutparams.height = (deviceHeight - 40) / 3;
        view2_layoutparams.addRule(RelativeLayout.CENTER_IN_PARENT);
        view2.setLayoutParams(view2_layoutparams);

        view2.addView(new MapView(getActivity()));
        view2.addView(new RelativeLayout(getActivity()));

        view2.getChildAt(1).setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams view3_layoutparams = (RelativeLayout.LayoutParams) view3.getLayoutParams();
        view3_layoutparams.width = deviceWidth;
        view3_layoutparams.height = (deviceHeight - 40) / 3;
        view3_layoutparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view3.setLayoutParams(view3_layoutparams);

        view1.getChildAt(1).setOnClickListener(this);
        view2.getChildAt(1).setOnClickListener(this);
        view3.setOnClickListener(this);

        view3.setBackgroundColor(Color.GRAY);
        setState(0);
    }

    public void setState(int s) {
        state = s;
    }

    @Override
    public void doBack() {
        if (state > 0) {
            switch (state) {
                case 1:
                    unexpand(1);
                    break;
                case 2:
                    unexpand(2);
                    break;
                case 3:
                    unexpand(3);
                    break;
            }
        } else {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onClick(View clickedView) {
        View view = clickedView;

        if (animating) {
            return;
        }

        if (clickedView == view1.getChildAt(1)) {
            view = view1;
            setState(1);
        }
        if (clickedView == view2.getChildAt(1)) {
            view = view2;
            setState(2);
        }
        if (clickedView == view3) {
            setState(3);
        }

        final View contentView = view;

        int deviceHeight = App.getInstance().screenHeight - ((HomeActivity) getActivity()).getStatusBarHeight();

        contentView.bringToFront();

        animating = true;
        ValueAnimator anim = ValueAnimator.ofInt(contentView.getMeasuredHeight(), deviceHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.height = val;
                contentView.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(400);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                switch (state) {
                    case 1:
                        view1.getChildAt(1).setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        view2.getChildAt(1).setVisibility(View.INVISIBLE);
                        break;
                }
                animating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        anim.start();
    }

    public void unexpand(final int state) {

        if (animating) {
            return;
        }

        View v = null;
        switch (state) {
            case 1:
                v = view1;
                break;
            case 2:
                v = view2;
                break;
            case 3:
                v = view3;
                break;
        }
        int deviceHeight = App.getInstance().screenHeight - ((HomeActivity) getActivity()).getStatusBarHeight();

        animating = true;
        ValueAnimator anim = ValueAnimator.ofInt(v.getMeasuredHeight(), (deviceHeight - 40) / 3);
        final View finalV = v;
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = finalV.getLayoutParams();
                layoutParams.height = val;
                finalV.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(400);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                switch(state) {
                    case 1:
                        view1.getChildAt(1).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        view2.getChildAt(1).setVisibility(View.VISIBLE);
                        break;
                }
                setState(0);
                animating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        anim.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((MapView)view2.getChildAt(0)).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
