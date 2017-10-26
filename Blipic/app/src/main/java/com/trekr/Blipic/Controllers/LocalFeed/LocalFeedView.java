package com.trekr.Blipic.Controllers.LocalFeed;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.trekr.BlipicWellness.Controllers.HomeActivity;
import com.trekr.Common.LoadMoreRecycler.DataView;
import com.trekr.Common.LoadMoreRecycler.LinearAdapter;
import com.trekr.Common.LoadMoreRecycler.OnLoadMoreListener;
import com.trekr.Common.LoadMoreRecycler.RecyclerViewLoadMoreScroll;
import com.trekr.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocalFeedView extends RelativeLayout {

    private RecyclerView recyclerview;
    private LinearAdapter linearAdapter;
    private RecyclerViewLoadMoreScroll scrollListener;
    private int loadedCount;

    Unbinder unbinder;
    private Context context;

    //region Constructors
    public LocalFeedView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public LocalFeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LocalFeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LocalFeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    //endregion

    public void initView(){
        RelativeLayout view = (RelativeLayout) inflate(getContext(), R.layout.view_local_feed, null);;
        addView(view);

        unbinder = ButterKnife.bind(this);

        ((HomeActivity) context).setStatusBar(false, getResources().getColor(android.R.color.black));
        //region recyclerview

        loadedCount = 0;
        recyclerview=(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        linearAdapter=new LinearAdapter(DataView.getDataViews(5, loadedCount));
        recyclerview.setAdapter(linearAdapter);
        loadedCount = 5;

        scrollListener=new RecyclerViewLoadMoreScroll(linearLayoutManager);
        scrollListener.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadMoreData();
            }
        });

        recyclerview.addOnScrollListener(scrollListener);

        //endregion
    }

    private void LoadMoreData() {
        linearAdapter.addLoadingView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<DataView> dataViews=DataView.getDataViews(3, loadedCount);
                linearAdapter.removeLoadingView();
                linearAdapter.addData(dataViews);
                linearAdapter.notifyDataSetChanged();
                scrollListener.setLoaded();
                loadedCount += 3;
            }
        },2000);

    }
}
