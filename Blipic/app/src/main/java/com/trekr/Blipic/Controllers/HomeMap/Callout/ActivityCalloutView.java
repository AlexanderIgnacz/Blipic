package com.trekr.Blipic.Controllers.HomeMap.Callout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.Common.GlideApp;
import com.trekr.R;
import com.trekr.Utils.TapOpacityHighlightLayout;
import com.trekr.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCalloutView extends RelativeLayout {

    //region UI Variables
    @BindView(R.id.titleLabel)
    TextView titleLabel;
    @BindView(R.id.descriptionLabel)
    TextView descriptionLabel;
    @BindView(R.id.imgLike)
    ImageView imgLike;
    @BindView(R.id.imgAttending)
    ImageView imgAttending;
    @BindView(R.id.hostedByImageView)
    CircleImageView hostedByImageView;
    @BindView(R.id.hostedByButton)
    TextView hostedByButton;
    @BindView(R.id.txt_friendsGoing)
    TextView txtFriendsGoing;
    @BindView(R.id.friendsGoingButton)
    TapOpacityHighlightLayout friendsGoingButton;
    @BindView(R.id.pictureImageView)
    ImageView pictureImageView;
    @BindView(R.id.activityTypeImageView)
    ImageView activityTypeImageView;
    @BindView(R.id.txtGo)
    TextView txtGo;
    //endregion

    private BLPBlip blip;
    private DestinationCalloutView.CalloutListener listener;

    public ActivityCalloutView(Context context, BLPBlip blip, DestinationCalloutView.CalloutListener listener) {
        super(context);
        this.blip = blip;
        this.listener = listener;

        initView();
    }

    public ActivityCalloutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ActivityCalloutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ActivityCalloutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        RelativeLayout view = (RelativeLayout) inflate(getContext(), R.layout.callout_activity, null);
        addView(view);

        ButterKnife.bind(this);

        updateDataSourse();
    }

    public void updateDataSourse() {
//        NSArray* arrayViews = [self.pictureImageView subviews];

//        for (UIView* view in arrayViews) {

//        [view removeFromSuperview];
//        }

        txtGo.getBackground().setLevel(1);
        titleLabel.setText(blip.name);
        if (blip.getDate() != null) {
            descriptionLabel.setText(Utils.timestampFromDate(blip.getDate().getTime(), "EEEE, MMM dd 'at' h:mm a"));
        }

        if (!TextUtils.isEmpty(blip.hostedName)) {
            hostedByButton.setText(blip.hostedName);
        } else if (blip.getCreatedByUser() != null) {
            hostedByButton.setText(blip.getCreatedByUser().name);
        }

        if (blip.attendingCount > 0) {
            if (blip.isAttending && blip.attendingCount == 1) {
                friendsGoingButton.setEnabled(false);
                txtFriendsGoing.setText("0 friends attending... yet");
            } else {
                friendsGoingButton.setEnabled(true);
                txtFriendsGoing.setText(String.format("%g %s Attending", blip.attendingCount, (blip.attendingCount == 1) ? "Friend" : "Friends"));
            }
        } else {
            friendsGoingButton.setEnabled(false);
            txtFriendsGoing.setText("Be the first to check it out");
        }

        imgLike.setImageResource(blip.getLike() ? R.drawable.pf_like : R.drawable.pf_dislike);
        imgAttending.setImageResource(blip.isAttending ? R.drawable.cll_attending : R.drawable.cll_no_attending);

        if (blip.images != null && !TextUtils.isEmpty(blip.images.get(0).url)) {
            GlideApp
                    .with(this)
                    .load(blip.images.get(0).url)
                    .fitCenter()
                    .placeholder(R.drawable.cll_no_media_placeholder)
                    .into(pictureImageView);

        } else if (blip.video != null && !TextUtils.isEmpty(blip.video.url)) {

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
//            UIImageView * imagePlayViedo = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@
//            "playVideo"]];
//            imagePlayViedo.frame = CGRectMake(0, 0, self.pictureImageView.width / 2, self.pictureImageView.width / 2);
//            imagePlayViedo.center = self.pictureImageView.center;
//            imagePlayViedo.alpha = 0.5;
//        [self.pictureImageView addSubview:imagePlayViedo];
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
//
//                if ([thumbnailImage isPanoramaImage]){
//                    imagePlayViedo.image = [UIImage imageNamed:@ "360_image"];
//                }
//
//                });
//            }];
//        });

        } else {
            pictureImageView.setImageResource(R.drawable.cll_no_media_placeholder);
        }

        if (!(blip.video != null && !TextUtils.isEmpty(blip.video.url))) {
//            self.pictureImageView.userInteractionEnabled = YES;
//
//            UITapGestureRecognizer *showImageTapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self
//            action:@selector(showImage)];
//
//        [self.pictureImageView addGestureRecognizer:showImageTapGestureRecognizer];
        }

        String activityType = blip.activityTypes.get(0);
        String blipType = "act_white_" + activityType.toLowerCase();

        activityTypeImageView.setImageResource(getContext().getResources().getIdentifier(blipType, "drawable", getContext().getPackageName()));

        if (!TextUtils.isEmpty(blip.hostedIcon)) {
            GlideApp
                    .with(this)
                    .load(blip.hostedIcon)
                    .fitCenter()
                    .placeholder(R.drawable.cll_no_media_placeholder)
                    .into(hostedByImageView);
        } else if (blip.getCreatedByUser() != null && !TextUtils.isEmpty(blip.getCreatedByUser().profilePhoto.url)) {
            GlideApp
                    .with(this)
                    .load(blip.getCreatedByUser().profilePhoto.url)
                    .fitCenter()
                    .placeholder(R.drawable.cll_no_media_placeholder)
                    .into(hostedByImageView);
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

    @OnClick(R.id.closeButton)
    public void onViewClicked() {
        listener.onClose();
    }

}
