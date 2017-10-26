package com.trekr.Common.LoadMoreRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trekr.R;

public class LinearHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    static ImageButton likeBtn;
    private TextView mileTextView;

    public LinearHolder(View itemView) {
        super(itemView);
        mileTextView = (TextView) itemView.findViewById(R.id.recyclerview_mile_txt);
        likeBtn = (ImageButton) itemView.findViewById(R.id.recyclerview_like_btn);
        likeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mileTextView.setText("X");
    }
}
