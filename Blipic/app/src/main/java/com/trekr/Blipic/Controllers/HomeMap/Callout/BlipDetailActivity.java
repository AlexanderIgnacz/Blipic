package com.trekr.Blipic.Controllers.HomeMap.Callout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.App;
import com.trekr.Common.CalloutDestination.GalleryPagerAdapter;
import com.trekr.Common.GlideApp;
import com.trekr.R;

public class BlipDetailActivity extends AppCompatActivity {
    private BLPBlip blip;

    TextView blip_detail_title;
    TextView blip_detail_long_description;
    TextView blip_detail_like_count;
    ImageView blip_detail_picture_view;
    Button attendingButton;
    ViewPager gallery_pager;
    GalleryPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blip_detail);

        blip = App.getInstance().selectedBlip;

        setViews();
        setDatas();
    }

    public void setViews() {
        blip_detail_title = findViewById(R.id.blip_detail_title);
        blip_detail_long_description = findViewById(R.id.blip_detail_long_description);
        blip_detail_picture_view = findViewById(R.id.blip_detail_picture_view);
        blip_detail_like_count = findViewById(R.id.blip_detail_like_count);
        attendingButton = findViewById(R.id.attendingButton);
        gallery_pager = findViewById(R.id.gallery_pager);
    }

    public void setDatas() {
        blip_detail_title.setText(blip.name);
        blip_detail_long_description.setText(blip.longDescription);

        if (blip.images != null && blip.images.get(0).url.length() > 0) {
            GlideApp
                .with(this)
                .load(blip.images.get(0).url)
                .fitCenter()
                .placeholder(R.drawable.cll_no_media_placeholder)
                .into(blip_detail_picture_view);
        }

        String suffix;
        if (blip.likesCount < 2) {
            suffix = " like";
        } else {
            suffix = " likes";
        }
        blip_detail_like_count.setText(String.valueOf((int) blip.likesCount) + suffix);

        //
        if (!blip.getType().equals("location")) {

            String peopleAttendingCaption;

            if (blip.attendingCount > 0) {

                peopleAttendingCaption = blip.attendingCount + "People Attending";
                if (blip.attendingCount == 1)
                    peopleAttendingCaption = blip.attendingCount + "Person Attending";

            } else {

                peopleAttendingCaption = "no attendees yet";

            }

            attendingButton.setText(peopleAttendingCaption);

            attendingButton.setVisibility(View.VISIBLE);
        } else {

            attendingButton.setVisibility(View.GONE);
        }

        String[] rank = {"", ""};
        adapter = new GalleryPagerAdapter(getBaseContext(), rank);
        gallery_pager.setAdapter(adapter);
    }
}
