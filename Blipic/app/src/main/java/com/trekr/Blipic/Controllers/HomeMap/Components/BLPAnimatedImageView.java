package com.trekr.Blipic.Controllers.HomeMap.Components;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.trekr.R;

public class BLPAnimatedImageView extends LinearLayout {

    private boolean animatings = false;
    ImageView searchbarSideLogo;

    public BLPAnimatedImageView(Context context) {
        super(context);
        init();
    }

    public BLPAnimatedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BLPAnimatedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.blp, this, true);
        searchbarSideLogo = (ImageView) getRootView().findViewById(R.id.blp_logo);
        stopAnimation();
    }

    public void startAnimation() {
        if (!animatings) {
            animatings = true;
            doAnimation();
        }
    }

    public void stopAnimation() {
        animatings = false;
    }

    public void doAnimation() {
        float targetAlpha = 0;
        if (searchbarSideLogo.getAlpha() == 1) {
            targetAlpha = 0;
        } else {
            targetAlpha = 1;
        }
        ValueAnimator anim = ValueAnimator.ofFloat(searchbarSideLogo.getAlpha(), targetAlpha);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (float) valueAnimator.getAnimatedValue();
                searchbarSideLogo.setAlpha(val);
            }
        });
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animatings) {
                    doAnimation();
                } else {
                    searchbarSideLogo.setAlpha((float) 1);
                }
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
}
