package com.trekr.Common.LoadMoreRecycler;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.trekr.R;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    List<DataView> dataViews;
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    public LinearAdapter(List<DataView> dataViews) {
        this.dataViews = dataViews;
    }

    public void addData(List<DataView> dataViews) {
        this.dataViews.addAll(dataViews);
        notifyDataSetChanged();
    }

    public DataView getItemAtPosition(int position) {
        return dataViews.get(position);
    }

    public void addLoadingView() {
        //add loading item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dataViews.add(null);
                notifyItemInserted(dataViews.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        //Remove loading item
        dataViews.remove(dataViews.size() - 1);
        notifyItemRemoved(dataViews.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_list_item, parent, false);
            return new LinearHolder(view);
        } else if(viewType==VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_recycler_progress, parent, false);
            return new LoadingHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataViews == null ? 0 : dataViews.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataViews.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.recyclerview_like_btn:
                v.setBackgroundResource(R.drawable.bn_like);
                break;
        }
    }
}