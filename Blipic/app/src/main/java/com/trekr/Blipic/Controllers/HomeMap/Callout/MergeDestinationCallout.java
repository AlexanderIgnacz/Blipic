package com.trekr.Blipic.Controllers.HomeMap.Callout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.ApiService.ApiModel.BlipModel;
import com.trekr.App;
import com.trekr.R;
import com.trekr.Utils.TapOpacityHighlightLayout;
import com.trekr.Utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MergeDestinationCallout extends RelativeLayout {

    @BindView(R.id.containerView)
    ViewPager containerView;
    @BindView(R.id.btn_leftArrow)
    TapOpacityHighlightLayout leftButton;
    @BindView(R.id.btn_rightArrow)
    TapOpacityHighlightLayout rightButton;

    private ArrayList<BLPBlip> mergeBlips;
    private BLPBlip pointBlip;
    private DestinationCalloutView.CalloutListener listener;

    //region Constructors
    public MergeDestinationCallout(Context context, BLPBlip pointBlip, DestinationCalloutView.CalloutListener listener) {
        super(context);

        this.pointBlip = pointBlip;
        this.listener = listener;
        initView();
    }

    public MergeDestinationCallout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MergeDestinationCallout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MergeDestinationCallout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    //endregion

    private void initView() {
        LinearLayout view = (LinearLayout) inflate(getContext(), R.layout.callout_merge_destination, null);
        addView(view);

        ButterKnife.bind(this);

        mergeBlips = new ArrayList<>();
        mergeBlips.add(pointBlip);

        getMergeBlipsForPoint(pointBlip);
    }

    public void getMergeBlipsForPoint(BLPBlip blipPoint) {
        Utils.showLoading((Activity)getContext());

        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);

        App.getInstance().apiManager.getMergeBlipWithPointId(blipPoint.getPointId(),
                new Callback<BlipModel>() {
                    @Override
                    public void onResponse(Call<BlipModel> call, Response<BlipModel> response) {
                        Utils.hideLoading();

                        mergeBlips = (ArrayList<BLPBlip>)response.body().data;
                        createPageViewController();

                        if (mergeBlips.size() > 1) {
                            leftButton.setVisibility(View.VISIBLE);
                            rightButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<BlipModel> call, Throwable t) {
                        Utils.hideLoading();

                        createPageViewController();
                    }
                });

    }

    public void createPageViewController() {
        containerView.setAdapter(new ViewPagerAdapter(getContext()));
    }


    @OnClick({R.id.btn_leftArrow, R.id.btn_rightArrow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_leftArrow:
                containerView.setCurrentItem(containerView.getCurrentItem() - 1);
                break;
            case R.id.btn_rightArrow:
                containerView.setCurrentItem(containerView.getCurrentItem() + 1);
                break;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;

        public ViewPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            BLPBlip blip = mergeBlips.get(position);

            View layout = new DestinationCalloutView(mContext, blip, listener);

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public int getCount() {
            return mergeBlips.size();
        }
    }
}
