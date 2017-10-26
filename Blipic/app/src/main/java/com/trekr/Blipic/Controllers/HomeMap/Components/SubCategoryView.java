package com.trekr.Blipic.Controllers.HomeMap.Components;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trekr.R;

public class SubCategoryView extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private OnSubCategoryItemClicked onItemClicked;
    private CategoryView.OnOutClicked onOutClicked;

    ImageView first_category;
    ImageView second_category;
    ImageView third_category;
    ImageView fourth_category;
    ImageView fifth_category;
    ImageView first_category_second;
    ImageView second_category_second;
    ImageView third_category_second;
    ImageView fourth_category_second;
    ImageView fifth_category_second;

    LinearLayout first_container;
    LinearLayout second_container;

    public SubCategoryView(Context context) {
        super(context);
        init();
    }

    public SubCategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubCategoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.home_sub_category, this, true);

        first_category = (ImageView) getRootView().findViewById(R.id.firstCategory);
        second_category = (ImageView) getRootView().findViewById(R.id.secondCategory);
        third_category = (ImageView) getRootView().findViewById(R.id.thirdCategory);
        fourth_category = (ImageView) getRootView().findViewById(R.id.fourthCategory);
        fifth_category = (ImageView) getRootView().findViewById(R.id.fifthCategory);
        first_category_second = (ImageView) getRootView().findViewById(R.id.firstCategorySecond);
        second_category_second = (ImageView) getRootView().findViewById(R.id.secondCategorySecond);
        third_category_second = (ImageView) getRootView().findViewById(R.id.thirdCategorySecond);
        fourth_category_second = (ImageView) getRootView().findViewById(R.id.fourthCategorySecond);
        fifth_category_second = (ImageView) getRootView().findViewById(R.id.fifthCategorySecond);

        first_container = (LinearLayout) getRootView().findViewById(R.id.home_first_container);
        second_container = (LinearLayout) getRootView().findViewById(R.id.home_second_container);

        first_category.setOnClickListener(this);
        second_category.setOnClickListener(this);
        third_category.setOnClickListener(this);
        fourth_category.setOnClickListener(this);
        fifth_category.setOnClickListener(this);
        first_category_second.setOnClickListener(this);
        second_category_second.setOnClickListener(this);
        third_category_second.setOnClickListener(this);
        fourth_category_second.setOnClickListener(this);
        fifth_category_second.setOnClickListener(this);

        first_container.setOnTouchListener(this);
        second_container.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemClicked.OnSubCategoryItemClicked(view.getTag());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int id = view.getId();
        float radius = view.getWidth();

        float pos_x = motionEvent.getX();
        float pos_y = motionEvent.getY();

        if (id == R.id.home_first_container || id == R.id.home_second_container) {

            float x = Math.abs(radius / 2 - pos_x);
            float y = Math.abs(radius / 2 - pos_y);

            float distance = (float) Math.sqrt(x*x + y*y);

            if (inOfRange((float) (view.getWidth()/2/2.6), distance, view.getWidth() / 2)) {
                return false;
            } else {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    onOutClicked.onOutClicked();
                }
            }

            return true;
        }

        return false;
    }

    public interface OnSubCategoryItemClicked {
        void OnSubCategoryItemClicked(Object object);
    }

    public void setOnItemClickedListener(OnSubCategoryItemClicked onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setOnOutClickedListener(CategoryView.OnOutClicked onOutClicked) {
        this.onOutClicked = onOutClicked;
    }


    public boolean inOfRange(float downEnd, float value, float upEnd) {
        return value > downEnd && value <= upEnd;
    }


    public void setButtonImage(int index, StateListDrawable drawable) {
        switch (index) {
            case 0:
                first_category.setImageDrawable(drawable);
                break;
            case 1:
                second_category.setImageDrawable(drawable);
                break;
            case 2:
                third_category.setImageDrawable(drawable);
                break;
            case 3:
                fourth_category.setImageDrawable(drawable);
                break;
            case 4:
                fifth_category.setImageDrawable(drawable);
                break;
            case 5:
                first_category_second.setImageDrawable(drawable);
                break;
            case 6:
                second_category_second.setImageDrawable(drawable);
                break;
            case 7:
                third_category_second.setImageDrawable(drawable);
                break;
            case 8:
                fourth_category_second.setImageDrawable(drawable);
                break;
            case 9:
                fifth_category_second.setImageDrawable(drawable);
                break;
        }
    }
}