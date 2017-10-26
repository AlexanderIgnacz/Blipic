package com.trekr.Blipic.Controllers.HomeMap.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.R;

import static com.trekr.ApiService.ApiModel.BLPBlip.BLPBlipTypes.BLPBlipTypeDestination;

public class BlipRenderer extends DefaultClusterRenderer<BLPBlip> {
    private IconGenerator mIconGenerator;
    private IconGenerator mClusterIconGenerator;
    private final ImageView mImageView;
    private final ImageView mClusterImageView;
    private Context context;

    public BlipRenderer(Context context, GoogleMap mMap, ClusterManager<BLPBlip> mClusterManager) {
        super(context, mMap, mClusterManager);
        this.context = context;
        mClusterIconGenerator = new IconGenerator(context);
        mIconGenerator = new IconGenerator(context);

        mClusterIconGenerator.setBackground(null);
        mIconGenerator.setBackground(null);

        int mDimension = (int) context.getResources().getDimension(R.dimen.custom_profile_image);

        mClusterImageView = new ImageView(context);
        mClusterImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        mClusterIconGenerator.setContentView(mClusterImageView);

        mImageView = new ImageView(context);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(BLPBlip blip, MarkerOptions markerOptions) {
        String activityType = blip.activityTypes.get(0);

        int imageResource = R.drawable.pin_destination;

        if (blip.isKindOfBlip(BLPBlipTypeDestination)) {
            imageResource = R.drawable.pin_destination_green;
        }

        if (!TextUtils.isEmpty(activityType)) {
            if (blip.isKindOfBlip(BLPBlipTypeDestination)) {
                imageResource = context.getResources().getIdentifier(String.format("pin_actg_%s", activityType.toLowerCase()), "drawable", context.getPackageName());
            } else {
                imageResource = context.getResources().getIdentifier(String.format("pin_act_%s", activityType.toLowerCase()), "drawable", context.getPackageName());
            }
        } else {

        }

        mImageView.setImageResource(imageResource);
        Bitmap icon = mIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<BLPBlip> cluster, MarkerOptions markerOptions) {

//        CCHMapClusterAnnotation *clusterAnnotation = self.annotation;

//        if ([[clusterAnnotation annotations] allObjects].count >1) {
//
//            NSMutableArray* arrayIds = [NSMutableArray new];
//            for (int i = 0; i < [[clusterAnnotation annotations] allObjects].count; i++ ) {
//                if ([[[[clusterAnnotation annotations] allObjects] objectAtIndex:i] isKindOfClass:[BLPBlip class]]) {
//                    BLPBlip* blip = [[[clusterAnnotation annotations] allObjects] objectAtIndex:i];
//                [arrayIds addObject:blip.blipId];
//                }
//            }
//            NSMutableArray* newArray = [NSMutableArray new];
//
//            for (int i = 0; i < [[clusterAnnotation annotations] allObjects].count; i++ ) {
//                if ([[[[clusterAnnotation annotations] allObjects] objectAtIndex:i] isKindOfClass:[BLPBlip class]]) {
//                    BLPBlip* blip = [[[clusterAnnotation annotations] allObjects] objectAtIndex:i];
//                    if ([arrayIds containsObject:blip.blipId]) {
//                    [arrayIds removeObject:blip.blipId];
//                    [newArray addObject:blip];
//
//                    }
//                } else {
//                [newArray addObject:[[[clusterAnnotation annotations] allObjects] objectAtIndex:i]];
//                }
//            }
//
//            if ([[clusterAnnotation annotations] allObjects].count != [newArray count] && newArray.count >0) {
//                NSSet *setAnnotations = [NSSet setWithArray:newArray];
//                clusterAnnotation.annotations = setAnnotations;
//                self.annotation = clusterAnnotation;
//            }
//        }


        int imageResource;

        int countDestinationBlips = 0;
        int countActivityBlips = 0;
        for (BLPBlip someBlip :cluster.getItems()) {
            if (someBlip.isKindOfBlip(BLPBlipTypeDestination)) {
                countDestinationBlips = countDestinationBlips + 1;
            } else {
                countActivityBlips = countActivityBlips + 1;
            }
        }

        if (countDestinationBlips == 0) {
            imageResource = R.drawable.pin_act_more;
        }else if (countActivityBlips == 0) {
            imageResource = R.drawable.pin_act_moreg;
        }else {
            imageResource = R.drawable.pin_act_more;
        }

        mClusterImageView.setImageResource(imageResource);
        Bitmap icon = mClusterIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 1;
    }
}