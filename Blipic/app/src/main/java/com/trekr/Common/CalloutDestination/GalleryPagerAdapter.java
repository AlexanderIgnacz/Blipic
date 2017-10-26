package com.trekr.Common.CalloutDestination;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.R;

public class GalleryPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    String[] rank;
    LayoutInflater inflater;

    public GalleryPagerAdapter(Context context, String[] rank) {
        this.context = context;
        this.rank = rank;
    }

    @Override
    public int getCount() {
        return rank.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        TextView txtrank;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.gallery_viewpager_item, container, false);

        // Locate the TextViews in viewpager_item.xml
//        txtrank = (TextView) itemView.findViewById(R.id.ranktext);

        // Capture position and set to the TextViews
//        txtrank.setText(rank[position]);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

    }
}