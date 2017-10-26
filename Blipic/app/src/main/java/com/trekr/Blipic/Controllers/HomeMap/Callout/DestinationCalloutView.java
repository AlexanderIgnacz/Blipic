package com.trekr.Blipic.Controllers.HomeMap.Callout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.App;
import com.trekr.Common.GlideApp;
import com.trekr.R;
import com.trekr.Utils.TapOpacityHighlightLayout;
import com.trekr.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DestinationCalloutView extends RelativeLayout {

    //region UI Variables
    @BindView(R.id.titleLabel)
    TextView titleLabel;
    @BindView(R.id.descriptionLabel)
    TextView descriptionLabel;
    @BindView(R.id.createdByLabel)
    TextView createdByLabel;
    @BindView(R.id.upcomingActivitiesButton)
    TapOpacityHighlightLayout upcomingActivitiesButton;
    @BindView(R.id.txt_upcomingActivities)
    TextView txtUpcomingActivities;
    @BindView(R.id.imgLike)
    ImageView imgLike;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;
    @BindView(R.id.allowScrollImageView)
    TapOpacityHighlightLayout allowScrollImageView;
    @BindView(R.id.activityTypesScrollView)
    LinearLayout activityTypesScrollView;
    @BindView(R.id.createdByImageView)
    CircleImageView createdByImageView;
    @BindView(R.id.pictureImageView)
    ImageView pictureImageView;
    @BindView(R.id.likeButton)
    TapOpacityHighlightLayout likeButton;
    @BindView(R.id.favoriteButton)
    TapOpacityHighlightLayout favoriteButton;
    //endregion

    private BLPBlip blip;
    private CalloutListener listener;

    public interface CalloutListener {
        void onClose();
        void onShowDetails();
        void onShowComments();
    }

    //region Constructors
    public DestinationCalloutView(Context context, BLPBlip blip, CalloutListener listener) {
        super(context);
        this.blip = blip;
        this.listener = listener;

        initView();
    }

    public DestinationCalloutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DestinationCalloutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DestinationCalloutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    //endregion

    private void initView() {
        RelativeLayout view = (RelativeLayout) inflate(getContext(), R.layout.callout_destination, null);
        addView(view);

        ButterKnife.bind(this);

        reloadDataSource();
    }

    public void reloadDataSource() {
//        NSArray* arrayViews = [self.pictureImageView subviews];

//        for (UIView* view in arrayViews) {

//        [view removeFromSuperview];
//        }

        titleLabel.setText(blip.name);
        descriptionLabel.setText(blip.longDescription);

        if (blip.likesCount > 0) {
            upcomingActivitiesButton.setEnabled(true);
            if (blip.getLike() && blip.likesCount == 1) {
                txtUpcomingActivities.setText("You have liked this");
            } else {
                txtUpcomingActivities.setText(String.format("%g friend likes this", blip.likesCount));
            }
        } else {
            upcomingActivitiesButton.setEnabled(false);
            txtUpcomingActivities.setText("Be the first to check it out");
        }

        imgLike.setImageResource(blip.getLike() ? R.drawable.pf_like : R.drawable.pf_dislike);
        imgFavorite.setImageResource(blip.isFavorite ? R.drawable.cll_bn_favorites : R.drawable.cll_bn_favorite);

//        self.activityTypesScrollView.showsVerticalScrollIndicator = NO;
//        self.activityTypesScrollView.showsHorizontalScrollIndicator = NO;
//        self.activityTypesScrollView.alwaysBounceHorizontal = YES;
//    [self.activityTypesScrollView setPagingEnabled:YES];
//        self.activityTypesScrollView.alwaysBounceVertical = NO;
//
        float scrollViewWidth = 0;
        if (blip.activityTypes.size() > 3) {
            allowScrollImageView.setVisibility(View.VISIBLE);
        } else {
            allowScrollImageView.setVisibility(View.INVISIBLE);
        }

//        for (UIView * subView in[self.activityTypesScrollView subviews]){
//        [subView removeFromSuperview];
//        }
//

        for (String activityType : blip.activityTypes) {
            String blipType = "act_white_" + activityType.toLowerCase();
            blipType = blipType.replace(" ", "_");

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(getContext().getResources().getIdentifier(blipType, "drawable", getContext().getPackageName()));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getContext().getResources().getDimensionPixelSize(R.dimen.activity_size),
                    getContext().getResources().getDimensionPixelSize(R.dimen.activity_size));
            lp.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.activity_margin);

            imageView.setLayoutParams(lp);

            activityTypesScrollView.addView(imageView);
        }

        if (blip.getCreatedByUser() != null) {
//        [self.createdByImageView setImageUsingProgressViewRingWithURL:[NSURL URLWithString:
//            self.blip.createdByUser.profilePicture.url]
//            placeholderImage:[UIImage imageNamed:@ "cll-no-media-placeholder"]
//            options:
//            0
//            progress:
//            nil
//            completed:
//            nil
//            progressPrimaryColor:
//            nil
//            progressSecondaryColor:
//            nil
//            diameter:
//            25.f
//            scale:
//            YES];

            GlideApp
                    .with(this)
                    .load(blip.getCreatedByUser().profilePhoto.url)
                    .fitCenter()
                    .placeholder(R.drawable.cll_no_media_placeholder)
                    .into(createdByImageView);

            createdByLabel.setText(blip.getCreatedByUser().name);

//            [self.createdByDisclosureIndicator setHidden:NO];
        } else {
            createdByImageView.setImageResource(R.drawable.google_map_icon);
            createdByLabel.setText("Google");
//            [self.createdByDisclosureIndicator setHidden:YES];
        }

//        UIImageView * image360photo = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@ ""]];
//        image360photo.frame = CGRectMake(self.pictureImageView.center.x - 2 - self.pictureImageView.width / 4, self.pictureImageView.center.y - 2 - self.pictureImageView.width / 4, self.pictureImageView.width / 2, self.pictureImageView.width / 2);
//        image360photo.alpha = 0.5;
//    [self.pictureImageView addSubview:image360photo];
//
        if (blip.images != null && blip.images.get(0).url.length() > 0) {
            GlideApp
                    .with(this)
                    .load(blip.images.get(0).url)
                    .fitCenter()
                    .placeholder(R.drawable.cll_no_media_placeholder)
                    .into(pictureImageView);
        }
//            BLPImage * image = [self.blip.images firstObject];
//            NSURL * blipPictureURL = [NSURL URLWithString:image.url];
//
//            self.pictureImageView.userInteractionEnabled = NO;
//
//            __block UIImageView*blockImage = image360photo;
//
//        [_pictureImageView setImageUsingProgressViewRingWithURL:blipPictureURL
//            placeholderImage:[UIImage imageNamed:@ "cll-no-media-placeholder"]
//            options:
//            0
//            progress:
//            nil
//            completed:^(UIImage * image, NSError * error, SDImageCacheType
//            cacheType, NSURL * imageURL){
//
//                if ([image isPanoramaImage]){
//
//
//                    blockImage.image = [UIImage imageNamed:@ "360_image"];
//
//                }
//            }
//            progressPrimaryColor:
//            nil
//            progressSecondaryColor:
//            nil
//            diameter:
//            25.f
//            scale:
//            YES];
//
//        } else{
//
//            self.pictureImageView.image = [UIImage imageNamed:@ "cll-no-media-placeholder"];
//            image360photo.image = nil;
//
//        }
//
//        if (self.blip.video.url) {
//
//            self.pictureImageView.userInteractionEnabled = YES;
//
//            UITapGestureRecognizer * tapToPlayVideo = [[UITapGestureRecognizer alloc]initWithTarget:
//            self
//            action:
//            @selector(playBlipVideo)];
//
//        [self.pictureImageView addGestureRecognizer:tapToPlayVideo];
//
//            self.pictureImageView.image = [UIImage imageNamed:@ "cll-no-media-placeholder"];
//            image360photo.image = [UIImage imageNamed:@ "playVideo"];
//
//            __block UIImageView *imageView = [[UIImageView alloc]initWithFrame:
//            CGRectMake(0, 0, self.pictureImageView.size.width, self.pictureImageView.size.height)];
//
//            NSURL * blipVideoURL = [NSURL URLWithString:self.blip.video.url];
//            AVURLAsset * asset1 = [[AVURLAsset alloc]initWithURL:
//            blipVideoURL
//            options:
//            nil];
//            AVAssetImageGenerator * generate1 = [[AVAssetImageGenerator alloc]initWithAsset:
//            asset1];
//
//            CMTime time = CMTimeMake(0, 1);
//
//            NSValue * timeValue = [NSValue valueWithCMTime:time];
//            NSArray * times = [NSArray arrayWithObjects:timeValue, nil];
//
//            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^ {
//
//                    [generate1 generateCGImagesAsynchronouslyForTimes:
//            times completionHandler:^(CMTime requestedTime,
//                    CGImageRef image,
//                    CMTime actualTime,
//                    AVAssetImageGeneratorResult result,
//                    NSError * error){
//
//                UIImage * thumbnailImage = [[UIImage alloc]initWithCGImage:
//                image];
//
//                dispatch_async(dispatch_get_main_queue(), ^ {
//                        imageView.image = thumbnailImage;
//                self.pictureImageView.image = thumbnailImage;
//                if ([thumbnailImage isPanoramaImage]){
//
//                    image360photo.image = [UIImage imageNamed:@ "360_image"];
//                }
//                });
//            }];
//        });
//
//        }
//
//        if (!(self.blip.video && self.blip.video.url)) {
//            self.pictureImageView.userInteractionEnabled = YES;
//
//            UITapGestureRecognizer * showImageTapGestureRecognizer = [[UITapGestureRecognizer alloc]
//            initWithTarget:
//            self
//            action:
//            @selector(showImage)];
//
//        [self.pictureImageView addGestureRecognizer:showImageTapGestureRecognizer];
//        }
//
    }

    @OnClick({R.id.closeButton, R.id.btnDetails, R.id.btnComments, R.id.favoriteButton, R.id.likeButton, R.id.btnGo})
    public void onViewClicked(View view) {
        switch(view.getId()) {
            case R.id.closeButton:
                listener.onClose();
                break;
            case R.id.btnDetails:
                listener.onShowDetails();
                break;
            case R.id.btnComments:
                listener.onShowComments();
                break;
            case R.id.likeButton:
                likeBlip();
                break;
            case R.id.favoriteButton:
                favoriteButton();
                break;
            case R.id.btnGo:
                onGo();
                break;
        }
    }

    public void likeBlip() {
        Utils.showLoading((Activity) getContext());
        if (blip.getLike()) {
            App.getInstance().apiManager.dislikeBlipWithId(blip.getBlipId(),
                    new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Utils.hideLoading();

                            blip.setLike(false);

                            imgLike.setImageResource(R.drawable.pf_dislike);

                            blip.likesCount = (int)blip.likesCount - 1;

//                          [self viewDidLoad];
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Utils.hideLoading();
                        }
                    });
        } else {

            App.getInstance().apiManager.likeBlipWithId(blip.getBlipId(),
                    new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Utils.hideLoading();

                            blip.setLike(true);

                            imgLike.setImageResource(R.drawable.pf_like);

                            blip.likesCount = (int)blip.likesCount + 1;

//                          [self viewDidLoad];
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Utils.hideLoading();
                        }
                    });
        }
    }

    public void favoriteButton() {
        Utils.showLoading((Activity) getContext());

        if (blip.isFavorite) {
            App.getInstance().apiManager.removeFavoriteBlipWithId(blip.getBlipId(),
                    new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Utils.hideLoading();

                            blip.isFavorite = false;

                            imgFavorite.setImageResource(R.drawable.cll_bn_favorite);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Utils.hideLoading();
                        }
                    });
        } else {
            App.getInstance().apiManager.favoriteBlipWithId(blip.getBlipId(),
                    new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Utils.hideLoading();

                            blip.isFavorite = true;

                            imgFavorite.setImageResource(R.drawable.cll_bn_favorites);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Utils.hideLoading();
                        }
                    });
        }
    }

    public void onGo() {
        Utils.showAlertDialog((Activity)getContext(), "Error", "Could not get actual location.");



//        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+latitude1+","+longitude1+"&daddr="+latitude2+","+longitude2;
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//        startActivity(Intent.createChooser(intent, "Select an application"));
//
//        CLLocation *userLocation = [[BLPRestAPI sharedInstance].loggedUser.userLocations lastObject];
//
//        if (!userLocation) {
//            NSLog(@"Could not get user location");
//
//        [[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"KEYWORD_ERROR", nil)
//            message:NSLocalizedString(@"ERROR_NO_USER_LOCATION_MESSAGE", nil)
//            delegate:self
//            cancelButtonTitle:NSLocalizedString(@"KEYWORD_OK", nil)
//            otherButtonTitles:nil]
//            show];
//        } else {
//            //Show route to location
//            Class mapItemClass = [MKMapItem class];
//            if (mapItemClass && [mapItemClass respondsToSelector:@selector(openMapsWithItems:launchOptions:)])
//            {
//                CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake([self.blip.location.lat doubleValue], [self.blip.location.lon doubleValue]);
//                CLLocation *coordinateLocation = [[CLLocation alloc] initWithLatitude:[self.blip.location.lat doubleValue] longitude:[self.blip.location.lon doubleValue]];
//                double distanse = [userLocation distanceFromLocation:coordinateLocation];
//
//                if (distanse < 200) {
//
//                    [[[UIAlertView alloc] initWithTitle:nil message:@"This blip near you" delegate:self cancelButtonTitle:@"OK" otherButtonTitles: nil] show];
//
//                    [[BLPRestAPI sharedInstance] goFunctionalityWithBlipId:self.blip.blipId
//                    failureBlock:^(BLPServerError *error) {
//
//                    } successBlock:^(BOOL success) {
//
//                    }];
//
//                    return;
//                }
//
//                MKPlacemark *placemark = [[MKPlacemark alloc] initWithCoordinate:coordinate addressDictionary:nil];
//                MKMapItem *mapItem = [[MKMapItem alloc] initWithPlacemark:placemark];
//                [mapItem setName:self.blip.name];
//
//                NSDictionary *launchOptions = @{MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving};
//                MKMapItem *currentLocationMapItem = [MKMapItem mapItemForCurrentLocation];
//                [MKMapItem openMapsWithItems:@[currentLocationMapItem, mapItem]
//                launchOptions:launchOptions];
//            }
//
//        }
    }

}
