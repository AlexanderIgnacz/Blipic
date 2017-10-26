package com.trekr.Utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import static android.R.attr.width;

public class TapOpacityHighlightLayout extends LinearLayout {

    private boolean isSemiAlpha = false;

    public TapOpacityHighlightLayout(@NonNull Context context) {
        super(context);
    }

    public TapOpacityHighlightLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TapOpacityHighlightLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TapOpacityHighlightLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null)
            return true;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                animateAlpha(0.5f);
                isSemiAlpha = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (0 <= event.getX() && event.getX() < width && 0 <= event.getY() && event.getY() < getHeight()) {
                    if (!isSemiAlpha) {
                        animateAlpha(0.5f);
                        isSemiAlpha = true;
                    }
                } else if (isSemiAlpha) {
                    animateAlpha(1f);
                    isSemiAlpha = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isSemiAlpha) {
                    animateAlpha(1f);
                    if (isClickable())
                        performClick();
                    else
                        getChildAt(0).callOnClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isSemiAlpha) {
                    animateAlpha(1f);
                }
                break;
        }
        return true;
    }

    public void animateAlpha(Float alpha) {
        ViewCompat.animate(this).alpha(alpha).setDuration(200).start();
    }
}