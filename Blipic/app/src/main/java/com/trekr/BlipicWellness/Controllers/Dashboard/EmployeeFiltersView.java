package com.trekr.BlipicWellness.Controllers.Dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trekr.R;
import com.trekr.Utils.Utils;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeFiltersView extends LinearLayout {
    public interface FiltersDelegate {
        void tapOnFiler(Filter filter);
    }

    public enum Filter {
        week,
        month,
        quater,
        year;

        public String startDate;
        public int maxSuns;
        public int destinationMax;
        public int activityMax;

        public static Filter fromInteger(int index) {
            Filter filter;
            Date currentDate = new Date();

            switch (index) {
                case 0:
                    filter = Filter.week;
                    currentDate.setTime(currentDate.getTime() - (long)60 * 60 * 24 * 7 * 1000);
                    filter.startDate = Utils.timestampFromDate(currentDate.getTime(), null);

                    filter.maxSuns = 3500;
                    filter.destinationMax = 4;
                    filter.activityMax = 2;
                    return filter;
                case 1:
                    filter = Filter.month;
                    currentDate.setTime(currentDate.getTime() - (long)60 * 60 * 24 * 30 * 1000);
                    filter.startDate = Utils.timestampFromDate(currentDate.getTime(), null);

                    filter.maxSuns = 14000;
                    filter.destinationMax = 16;
                    filter.activityMax = 8;
                    return filter;
                case 2:
                    filter = Filter.quater;
                    currentDate.setTime(currentDate.getTime() - (long)60 * 60 * 24 * 30 * 3 * 1000);
                    filter.startDate = Utils.timestampFromDate(currentDate.getTime(), null);

                    filter.maxSuns = 42000;
                    filter.destinationMax = 48;
                    filter.activityMax = 24;
                    return filter;
                case 3:
                    filter = Filter.year;
                    currentDate.setTime(currentDate.getTime() - (long)60 * 60 * 24 * 365 * 1000);
                    filter.startDate = Utils.timestampFromDate(currentDate.getTime(), null);

                    filter.maxSuns = 168000;
                    filter.destinationMax = 192;
                    filter.activityMax = 96;
                    return filter;
            }
            return null;
        }
    }

    public FiltersDelegate delegate;
    private int[] aryButtonIndex = {R.id.btn_Week, R.id.btn_Month, R.id.btn_Quarter, R.id.btn_Year};

    //region Constructors
    public EmployeeFiltersView(Context context) {
        super(context);
        initView();
    }

    public EmployeeFiltersView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EmployeeFiltersView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmployeeFiltersView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    //endregion

    private void initView() {
        LinearLayout view = (LinearLayout) inflate(getContext(), R.layout.employee_filters_view, null);
        addView(view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_Week, R.id.btn_Month, R.id.btn_Quarter, R.id.btn_Year})
    public void onViewClicked(View view) {
        for (int buttonIndex : aryButtonIndex) {
            TextView button = (TextView) findViewById(buttonIndex);
            button.setTextColor(Color.parseColor("#88aaaaaa"));
        }

        ((TextView) view).setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

        if (delegate != null) {
            delegate.tapOnFiler(Filter.fromInteger(Integer.parseInt((String)view.getTag())));
        }

        switch (view.getId()) {
            case R.id.btn_Week:
                break;
            case R.id.btn_Month:
                break;
            case R.id.btn_Quarter:
                break;
            case R.id.btn_Year:
                break;
        }
    }

}
