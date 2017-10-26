package com.trekr.Blipic.Controllers.HomeMap;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.R;

public class RepetitionOption extends LinearLayout implements View.OnClickListener {

    View rootView;
    RelativeLayout v1;
    RelativeLayout v2;
    RelativeLayout v3;
    RelativeLayout v4;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    private boolean componentEnabled;

    private static RepetitionOption instance;
    public static RepetitionOption getInstance() {
        return instance;
    }

    public RepetitionOption(Context context) {
        super(context);
        init();
    }

    public RepetitionOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RepetitionOption(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setComponentEnable(boolean b) {
        this.componentEnabled = b;
        if (b) {
            v1.setClickable(true);
            v2.setClickable(true);
            v3.setClickable(true);
            v4.setClickable(true);
        } else {
            v1.setClickable(false);
            v2.setClickable(false);
            v3.setClickable(false);
            v4.setClickable(false);
        }
    }

    public void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = layoutInflater.inflate(R.layout.repetition_option, this, true);

        v1 = rootView.findViewById(R.id.option_left);
        v2 = rootView.findViewById(R.id.option_center_l);
        v3 = rootView.findViewById(R.id.option_center_r);
        v4 = rootView.findViewById(R.id.option_right);

        t1 = rootView.findViewById(R.id.option_left_text);
        t2 = rootView.findViewById(R.id.option_center_l_text);
        t3 = rootView.findViewById(R.id.option_center_r_text);
        t4 = rootView.findViewById(R.id.option_right_text);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
        v3.setOnClickListener(this);
        v4.setOnClickListener(this);

        this.setState(0);
        componentEnabled = true;
        instance = this;
    }

    @Override
    public void onClick(View view) {
        if (!componentEnabled) {
            return;
        }
        int id = view.getId();
        switch (id) {
            case R.id.option_left:
                setState(0);
                break;
            case R.id.option_center_l:
                setState(1);
                break;
            case R.id.option_center_r:
                setState(2);
                break;
            case R.id.option_right:
                setState(3);
                break;
        }
    }

    public void setState(int state) {
        switch (state) {
            case 0:
                v1.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_left_selected));
                v2.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center));
                v3.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center));
                v4.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_right));
                t1.setTextColor(getResources().getColor(R.color.repetitionOptionUnChosenColor));
                t2.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t3.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t4.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                break;
            case 1:
                v1.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_left));
                v2.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center_selected));
                v3.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center));
                v4.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_right));
                t1.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t2.setTextColor(getResources().getColor(R.color.repetitionOptionUnChosenColor));
                t3.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t4.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                break;
            case 2:
                v1.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_left));
                v2.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center));
                v3.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center_selected));
                v4.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_right));
                t1.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t2.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t3.setTextColor(getResources().getColor(R.color.repetitionOptionUnChosenColor));
                t4.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                break;
            case 3:
                v1.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_left));
                v2.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center));
                v3.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_center));
                v4.setBackground(getResources().getDrawable(R.drawable.border_repetition_option_right_selected));
                t1.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t2.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t3.setTextColor(getResources().getColor(R.color.repetitionOptionChosenColor));
                t4.setTextColor(getResources().getColor(R.color.repetitionOptionUnChosenColor));
                break;
        }
    }
}