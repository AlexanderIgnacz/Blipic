package com.trekr.Blipic.Controllers.HomeMap.Components;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.trekr.R;

public class CategoryView extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private OnCategoryItemClicked onItemClicked;
    private OnOutClicked onOutClicked;
    private OnComingSoon onComingSoon;
    ImageView btn_mountain;
    ImageView btn_snow;
    ImageView btn_water;
    ImageView btn_wheel;
    private boolean danger = false;
    Rect rect;

    public CategoryView(Context context) {
        super(context);
        init();
    }

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setDanger() {
        danger = true;
    }

    public void unsetDanger() {
        danger = false;
    }

    public void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.home_category, this, true);

        btn_mountain = (ImageView) getRootView().findViewById(R.id.btn_mountain);
        btn_snow = (ImageView) getRootView().findViewById(R.id.btn_snow);
        btn_water = (ImageView) getRootView().findViewById(R.id.btn_water);
        btn_wheel = (ImageView) getRootView().findViewById(R.id.btn_wheel);

        btn_mountain.setOnClickListener(this);
        btn_snow.setOnClickListener(this);
        btn_water.setOnClickListener(this);
        btn_wheel.setOnClickListener(this);

        btn_mountain.setOnTouchListener(this);
        btn_snow.setOnTouchListener(this);
        btn_water.setOnTouchListener(this);
        btn_wheel.setOnTouchListener(this);
    }

    public void setImageResource(int i, int resId) {
        switch (i) {
            case 0:
                btn_mountain.setImageResource(resId);
                break;
            case 1:
                btn_snow.setImageResource(resId);
                break;
            case 2:
                btn_wheel.setImageResource(resId);
                break;
            case 3:
                btn_water.setImageResource(resId);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        onItemClicked.onCategoryItemClicked(view.getTag());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();
        float radius = view.getWidth();
        float x = 0;
        float y = 0;

        switch (id) {
            case R.id.btn_mountain:
                x = radius - motionEvent.getX();
                y = radius - motionEvent.getY();
                break;
            case R.id.btn_snow:
                x = motionEvent.getX();
                y = radius - motionEvent.getY();
                break;
            case R.id.btn_water:
                if (danger) {
                    if (motionEvent.getAction() ==MotionEvent.ACTION_DOWN) {
                        rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                        view.setAlpha((float) 0.3);
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                        assert rect != null;
                        if(!rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())){
                            view.setAlpha((float) 1.0);
                        }
                    }
                    if (motionEvent.getAction() ==MotionEvent.ACTION_UP) {
                        view.setAlpha((float) 1.0);
                        if(rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())){
                            onComingSoon.onComingSoon();
                        }
                    }
                }
                x = radius - motionEvent.getX();
                y = motionEvent.getY();
                break;
            case R.id.btn_wheel:
                if (danger) {
                    if (motionEvent.getAction() ==MotionEvent.ACTION_DOWN) {
                        rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                        view.setAlpha((float) 0.3);
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
                        assert rect != null;
                        if(!rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())){
                            view.setAlpha((float) 1.0);
                        }
                    }
                    if (motionEvent.getAction() ==MotionEvent.ACTION_UP) {
                        view.setAlpha((float) 1.0);
                        if(rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())){
                            onComingSoon.onComingSoon();
                        }
                    }
                }
                x = motionEvent.getX();
                y = motionEvent.getY();
                break;
            default:
                break;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            float distance = (float) Math.sqrt(x*x + y*y);
            if (outOfRange((float) (view.getWidth()/2.6), distance, view.getWidth())) {
                onOutClicked.onOutClicked();
                return true;
            }
        }

        return false;
    }

    public interface OnCategoryItemClicked {
        void onCategoryItemClicked(Object object);
    }

    public interface OnOutClicked {
        void onOutClicked();
    }

    public interface OnComingSoon {
        void onComingSoon();
    }

    public void setOnItemClickedListener(OnCategoryItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setOnOutClickedListener(OnOutClicked onOutClicked) {
        this.onOutClicked = onOutClicked;
    }

    public void setOnComingSoonListener(OnComingSoon onComingSoon) {
        this.onComingSoon = onComingSoon;
    }

    public boolean outOfRange(float downEnd, float value, float upEnd) {
        return value < downEnd || value > upEnd;
    }

    public void setButtonImage(int index, StateListDrawable drawable) {
        switch (index) {
            case 0:
                btn_mountain.setImageDrawable(drawable);
                break;
            case 1:
                btn_snow.setImageDrawable(drawable);
                break;
            case 2:
                btn_wheel.setImageDrawable(drawable);
                break;
            case 3:
                btn_water.setImageDrawable(drawable);
                break;
        }
    }
}