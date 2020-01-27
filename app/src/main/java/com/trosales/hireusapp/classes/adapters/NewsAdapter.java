package com.trosales.hireusapp.classes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.trosales.hireusapp.R;
import com.trosales.hireusapp.classes.wrappers.NewsDO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyTextView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<NewsDO> newsDOList;
    private Context context;

    public NewsAdapter(List<NewsDO> newsDOList, Context context) {
        this.newsDOList = newsDOList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list, parent, false);

        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        final NewsDO newsDO = newsDOList.get(position);

        Glide
                .with(context)
                .load(newsDO.getNewsDisplayPhoto())
                .apply(new RequestOptions().fitCenter())
                .placeholder(R.drawable.ic_no_image)
                .into(holder.imgNewsDisplayPhoto);

        holder.lblNewsCaption.setText(newsDO.getNewsCaption());
        holder.lblNewsCreator.setText(newsDO.getNewsCreator());
        holder.lblNewsCreatedDate.setText(newsDO.getNewsCreatedDate());
        holder.lblNewsDetails.setText(newsDO.getNewsDetails());

        holder.newsCardView.setOnClickListener(view -> {
            //do something
        });
    }

    @Override
    public int getItemCount() {
        return newsDOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.newsCardView) CardView newsCardView;
        @BindView(R.id.imgNewsDisplayPhoto) ImageView imgNewsDisplayPhoto;
        @BindView(R.id.lblNewsCaption) MyTextView lblNewsCaption;
        @BindView(R.id.lblNewsCreator) MyTextView lblNewsCreator;
        @BindView(R.id.lblNewsCreatedDate) MyTextView lblNewsCreatedDate;
        @BindView(R.id.lblNewsDetails) MyTextView lblNewsDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
