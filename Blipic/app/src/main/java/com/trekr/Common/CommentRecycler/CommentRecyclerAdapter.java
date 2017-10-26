package com.trekr.Common.CommentRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trekr.Common.Activities.CommentActivity;
import com.trekr.R;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.MyViewHolder> {

    private List<Moviex> moviesList;
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView label1, label2;
        public View rootView;

        public MyViewHolder(View view) {
            super(view);
            label1 = view.findViewById(R.id.label1);
            label2 = view.findViewById(R.id.label2);
            rootView = view.findViewById(R.id.comment_rootview);
        }
    }


    public CommentRecyclerAdapter(List<Moviex> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listadapter_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Moviex movie = moviesList.get(position);
        holder.label1.setText(movie.getLabel1());
        holder.label2.setText(movie.getLabel2());
        holder.rootView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}