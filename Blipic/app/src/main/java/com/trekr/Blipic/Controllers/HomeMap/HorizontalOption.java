package com.trekr.Blipic.Controllers.HomeMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.R;

public class HorizontalOption extends LinearLayout implements View.OnClickListener {

    View rootView;
    RelativeLayout v1;
    RelativeLayout v2;
    RelativeLayout v3;
    TextView t1;
    TextView t2;
    TextView t3;

    public HorizontalOption(Context context) {
        super(context);
        init();
    }

    public HorizontalOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalOption(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.horizontal_option, this, true);

        v1 = rootView.findViewById(R.id.option_left);
        v2 = rootView.findViewById(R.id.option_center);
        v3 = rootView.findViewById(R.id.option_right);

        t1 = rootView.findViewById(R.id.option_left_text);
        t2 = rootView.findViewById(R.id.option_center_text);
        t3 = rootView.findViewById(R.id.option_right_text);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);

        this.setState(0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.option_left:
                setState(0);
                break;
            case R.id.option_center:
                setState(1);
                break;
            case R.id.option_right:
                setState(2);
                break;
        }
    }

    public void setState(int state) {
        switch (state) {
            case 0:
                v1.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_left_selected));
                v2.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_center));
                v3.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_right));
                t1.setTextColor(getResources().getColor(R.color.horizontalOptionUnChosenColor));
                t2.setTextColor(getResources().getColor(R.color.horizontalOptionChosenColor));
                t3.setTextColor(getResources().getColor(R.color.horizontalOptionChosenColor));
                break;
            case 1:
                v1.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_left));
                v2.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_center_selected));
                v3.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_right));
                t1.setTextColor(getResources().getColor(R.color.horizontalOptionChosenColor));
                t2.setTextColor(getResources().getColor(R.color.horizontalOptionUnChosenColor));
                t3.setTextColor(getResources().getColor(R.color.horizontalOptionChosenColor));
                break;
            case 2:
                v1.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_left));
                v2.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_center));
                v3.setBackground(getResources().getDrawable(R.drawable.border_horizontal_option_right_selected));
                t1.setTextColor(getResources().getColor(R.color.horizontalOptionChosenColor));
                t2.setTextColor(getResources().getColor(R.color.horizontalOptionChosenColor));
                t3.setTextColor(getResources().getColor(R.color.horizontalOptionUnChosenColor));
                break;
        }
    }
}
