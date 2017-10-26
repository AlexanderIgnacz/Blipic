package com.trekr.BlipicWellness.Controllers.Dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.theartofdev.edmodo.cropper.CropImage;
import com.trekr.ApiService.ApiModel.GeoActivitiesModel;
import com.trekr.App;
import com.trekr.BlipicWellness.Controllers.Dashboard.EmployeeFiltersView.Filter;
import com.trekr.BlipicWellness.Controllers.HomeActivity;
import com.trekr.Common.BaseFragment;
import com.trekr.Common.GlideApp;
import com.trekr.R;
import com.trekr.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EmployeeDashboardFragment extends BaseFragment implements EmployeeFiltersView.FiltersDelegate, ActionSheet.ActionSheetListener {
    //region Variables
    @BindView(R.id.userImageView)
    ImageView userImageView;
    @BindView(R.id.userNameLabel)
    TextView userNameLabel;
    @BindView(R.id.locationLabel)
    TextView locationLabel;
    @BindView(R.id.img_background)
    ImageView backgroundImageView;
    @BindView(R.id.filterView)
    EmployeeFiltersView filterView;
    @BindView(R.id.sunpointsLabel)
    TextView sunpointsLabel;
    @BindView(R.id.activityPointsLabel)
    TextView activityPointsLabel;
    @BindView(R.id.destinationPointsLabel)
    TextView destinationPointsLabel;
    @BindView(R.id.sunShineCircleView)
    SeekArc sunShineCircleView;
    @BindView(R.id.activityCircleView)
    SeekArc activityCircleView;
    @BindView(R.id.destinationCircleView)
    SeekArc destinationCircleView;

    Unbinder unbinder;
    //endregion

    private Filter filter = Filter.fromInteger(0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_dashboard_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        ((HomeActivity)getActivity()).setStatusBar(true, 0);

        init();

        return view;
    }

    private void init() {
        userNameLabel.setText(App.getInstance().loggedUser.name);
        filterView.delegate = this;

        // Set width same as height
        userImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams layout = userImageView.getLayoutParams();
                layout.width = userImageView.getHeight();
                userImageView.setLayoutParams(layout);
                userImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        GlideApp
                .with(this)
                .load(App.getInstance().loggedUser.profilePhoto.url)
                .fitCenter()
                .placeholder(R.drawable.empty_profile_photo)
                .into(userImageView);

        GlideApp.with(getActivity())
                .asBitmap()
                .load(App.getInstance().loggedUser.getCoverPhoto().url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (backgroundImageView != null)
                            backgroundImageView.setImageBitmap(Utils.fastblur(resource, 0.1f, 1));
                    }
                });
        loadPoints();
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
        loadPoints();
    }

//    @IBAction
//    func moreAction(_ sender:Any) {
//
//        self.changePhoto(profilePhoto:{
//            (profileImage) in
//
//            BLPRestAPI.sharedInstance().loggedUser.uploadFile(photoType:
//            UserPhotoType.profilePhoto(image:profileImage),
//            handler:
//            {
//                (result) in
//
//                self.refreshUser()
//
//                if let controller = self.slideMenuController() ?.leftViewController as?
//                MenuViewController {
//
//                controller.refreshUserData()
//            }
//
//            })
//
//        }){
//            (backgroundImage) in
//
//            BLPRestAPI.sharedInstance().loggedUser.uploadFile(photoType:
//            UserPhotoType.backgroundPhoto(image:backgroundImage),
//            handler:
//            {
//                (result) in
//                self.refreshUser()
//            })
//
//        }
//
//    }

    private void loadPoints() {
        App.getInstance().apiManager.geofenceActivites(filter.startDate,
                new Callback<GeoActivitiesModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GeoActivitiesModel> call, @NonNull Response<GeoActivitiesModel> response) {
                        GeoActivitiesModel model= response.body();
                        configureCircles(model.data);
                    }

                    @Override
                    public void onFailure(@NonNull Call<GeoActivitiesModel> call, @NonNull Throwable t) {
                        Utils.hideLoading();
                        Utils.showAlertDialog(getActivity(), getString(R.string.REQUEST_UNPERFORMED), t.getMessage());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void tapOnFiler(Filter filter) {
        setFilter(filter);
    }

    private void configureCircles(GeoActivitiesModel.DataGeoActivities geoActivity) {
        if (sunpointsLabel == null) {
            return;
        }
        sunpointsLabel.setText(Integer.toString(geoActivity.sunshinePoints));
        destinationPointsLabel.setText(Integer.toString(geoActivity.destinationPoints));
        activityPointsLabel.setText(Integer.toString(geoActivity.activityPoints));

        sunShineCircleView.setMax(filter.maxSuns);
        destinationCircleView.setMax(filter.destinationMax);
        activityCircleView.setMax(filter.activityMax);

        sunShineCircleView.setProgress(geoActivity.sunshinePoints);
        destinationCircleView.setProgress(geoActivity.destinationPoints);
        activityCircleView.setProgress(geoActivity.activityPoints);
    }

    @OnClick({R.id.btn_userImageView, R.id.moreButton, R.id.logItButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_userImageView:
            case R.id.moreButton:
                ActionSheet.createBuilder(getActivity(), getFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Edit Profile Photo", "Edit Background Photo")
                        .setCancelableOnTouchOutside(true)
                        .setListener(this).show();
                break;
        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        switch(index) {
            case 0:
                CropImage.activity()
                        .start(getContext(), this);
                break;
            case 1:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
