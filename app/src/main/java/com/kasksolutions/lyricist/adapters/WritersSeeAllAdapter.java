package com.kasksolutions.lyricist.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.SingleWriter;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class WritersSeeAllAdapter extends RecyclerView.Adapter<WritersSeeAllAdapter.WritersSeeAllMyViewHolder>
{
    Context c;
    private ArrayList al;

    private int lastPosition = -1;
    public WritersSeeAllAdapter(Context c, ArrayList al)
    {
        this.c = c;
        this.al = al;
    }

    @Override
    public WritersSeeAllMyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(c);

        //create a new layout for cardview

        View v1=li.inflate(R.layout.writer_see_all_card_style,null);
        return new WritersSeeAllMyViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(final WritersSeeAllMyViewHolder holder, int position)
    {

        HashMap hm=(HashMap)al.get(position);
       final String name=(String)hm.get("123");

       final  String wri=name.replace(" ","_");
        holder.title.setText(name);

        //It is URL for Image baed on id
        final String imageUrl= "https://s3.amazonaws.com/lyricist/"+wri+".jpg";

        Glide.with(c)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform
                        (new RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.ALL)))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                      holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.thumbnail);
       holder.thumbnail.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View view)
           {
               Intent intent=new Intent(c,SingleWriter.class);
               intent.putExtra("writerName",name);
               intent.putExtra("writerImage",imageUrl);
               c.startActivity(intent);
           }
       });
       setAnimation(holder.itemView,position);


    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class WritersSeeAllMyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView title;
        private ImageView thumbnail;
        private LinearLayout cardll;
        private ProgressBar progressBar;

        public WritersSeeAllMyViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.seeAlltitle);
            thumbnail =  view.findViewById(R.id.seeAllImage);
            cardll =  view.findViewById(R.id.seeAllcardll);
            progressBar=view.findViewById(R.id.seeAllPb);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(c, R.anim.latest_anim);
            // Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
