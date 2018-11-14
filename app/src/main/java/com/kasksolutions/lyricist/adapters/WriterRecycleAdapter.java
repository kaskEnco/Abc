package com.kasksolutions.lyricist.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kasksolutions.lyricist.allclasses.SingleWriter;

import java.util.ArrayList;
import java.util.HashMap;


public class WriterRecycleAdapter extends RecyclerView.Adapter<WriterRecycleAdapter.WriterMyViewHolder>
{
    Context c;
    ArrayList al;

    String keys[]={"writer"};

    public WriterRecycleAdapter(Context c, ArrayList al) {
        this.c = c;
        this.al = al;
    }

    @Override
    public WriterMyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(c);

        //create a new layout for cardview

        View v1=li.inflate(R.layout.mycard_style,null);

        return new WriterMyViewHolder(v1);


    }

    @Override
    public void onBindViewHolder(final WriterMyViewHolder holder, int position)
    {
        HashMap hm=(HashMap)al.get(position);

        final String writersname=(String) hm.get(keys[0]);

        holder.title.setText(writersname);

        final String name=writersname.replace(" ","_");

        //It is URL for Image baed on id
        final String imageUrl= "https://s3.amazonaws.com/lyricist/"+name+".jpg";

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
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                    {

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
                Intent i=new Intent(c,SingleWriter.class);
                i.putExtra("writerName",writersname);
                i.putExtra("writerImage",imageUrl);
                c.startActivity(i);

            }
        });


    }
    @Override
    public int getItemCount()
    {
        return al.size();
    }

    public class WriterMyViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout cardll;
        public TextView title;
        public ImageView thumbnail;
        public ProgressBar progressBar;


        public WriterMyViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail =  view.findViewById(R.id.thumbnail);
            cardll =  view.findViewById(R.id.cardll);
            progressBar=view.findViewById(R.id.indeterminateBar);

        }
    }
}
