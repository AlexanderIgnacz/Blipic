package com.trekr.Common.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trekr.App;
import com.trekr.Common.CommentRecycler.CommentRecyclerAdapter;
import com.trekr.Common.CommentRecycler.Moviex;
import com.trekr.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CommentActivity extends AppCompatActivity implements View.OnClickListener, CommentRecyclerAdapter.OnItemClicked {

    View comment_addnewcommenttextview;
    RecyclerView mainListView;
    private CommentRecyclerAdapter mAdapter;
    private List<Moviex> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        setHeaderOptions();
        setViews();
    }

    public void setHeaderOptions() {
        TextView center = findViewById(R.id.fragment_header_centertitle);
        center.setText("Bell Harbor Marina");
        TextView prev = findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = findViewById(R.id.fragment_header_rightbtn);
        next.setVisibility(View.GONE);
    }

    public void setViews() {
        comment_addnewcommenttextview = findViewById(R.id.comment_addnewcommenttextview);
        comment_addnewcommenttextview.setOnClickListener(this);

        mainListView = findViewById(R.id.comment_listview);
        mAdapter = new CommentRecyclerAdapter(movieList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mainListView.setLayoutManager(mLayoutManager);
        mainListView.setItemAnimator(new DefaultItemAnimator());
        mainListView.setAdapter(mAdapter);

        mAdapter.setOnClick(this);

//        prepareMovieData();
    }

    private void prepareMovieData() {
        Moviex movie = new Moviex("Mad Max: Fury Road", "Action & Adventure");
        movieList.add(movie);

        movie = new Moviex("Guardians of the Galaxy", "Science Fiction & Fantasy");
        movieList.add(movie);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_top, R.anim.transition_to_bottom);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.comment_addnewcommenttextview:
                Intent intent;
                intent = new Intent(this, NewCommentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Objects.equals(App.getInstance().post_comment, "")) {
            App.getInstance().post_comment = "";

        }
    }

    @Override
    public void onItemClick(int position) {
        Log.w("~~~~~~", String.valueOf(position));
        Intent intent;
        intent = new Intent(this, SliderImageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_from_bottom, R.anim.transition_to_top);
    }
}
