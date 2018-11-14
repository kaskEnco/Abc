package com.kasksolutions.lyricist.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bumptech.glide.request.target.Target;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.Songs;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 1/29/2018.
 */

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.SeeAllViewHolder>
{
    Context c;
    ArrayList al;

    String keys[]={"id","name","date"};
    private int lastPosition = -1;

    public SeeAllAdapter(Context c, ArrayList al)
    {
        this.c = c;
        this.al = al;
    }

    @Override
    public SeeAllViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(c);
        View v1=li.inflate(R.layout.seeall_onclick_card_style,null);
       return new SeeAllViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(final SeeAllViewHolder holder, int position)
    {
        //By using Hash Map we can get position
        HashMap hm=(HashMap)al.get(position);

        final String id=(String) hm.get(keys[0]);
        final String name=(String) hm.get(keys[1]);
        final String date=(String) hm.get(keys[2]);
        holder.title.setText(name);

        //It is URL for Image baed on id
        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+id+"/"+id+".jpg";

        Glide.with(c)
                .load(imageUrl)
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
                holder.thumbnail.buildDrawingCache();
                Intent intent = new Intent(c, Songs.class);
                intent.putExtra("BitmapImage", imageUrl);
                intent.putExtra("idno",id);
                intent.putExtra("moviename",name);
                intent.putExtra("date123",date);
                Log.d("date100",""+date);

                c.startActivity(intent);

            }
        });
        holder.cardll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                holder.thumbnail.buildDrawingCache();
                Intent intent = new Intent(c, Songs.class);
                intent.putExtra("BitmapImage", imageUrl);
                intent.putExtra("idno",id);
                intent.putExtra("moviename",name);
                c.startActivity(intent);

            }
        });
        setAnimation(holder.itemView,position);

    }

    @Override
    public int getItemCount()
    {
        return al.size();
    }

    public class SeeAllViewHolder extends RecyclerView.ViewHolder
    {

        public TextView title;
        public ImageView thumbnail;
        public LinearLayout cardll;
        public ProgressBar progressBar;
        public SeeAllViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.seeAlltitle);
            thumbnail =  view.findViewById(R.id.seeAllImage);
            cardll =  view.findViewById(R.id.seeAllcardll);
            progressBar=view.findViewById(R.id.pb);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(c,R.anim.latest_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
