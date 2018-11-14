package com.kasksolutions.lyricist.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
 * Created by home on 27-Feb-18.
 */

public class SingleYearAdapter extends RecyclerView.Adapter<SingleYearAdapter.SingleYearViewHolder>
{
    ArrayList yearAl;
    Context contextYear;
    String yearKeys[] = {"YearmovieId", "yearMovieName", "year"};

    public SingleYearAdapter(ArrayList yearAl, Context contextYear)
    {
        this.yearAl = yearAl;
        this.contextYear = contextYear;
    }

    @Override
    public SingleYearViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(contextYear);

        View v1=li.inflate(R.layout.seeall_onclick_card_style,null,false);

        return new SingleYearViewHolder(v1);

    }

    @Override
    public void onBindViewHolder(final SingleYearViewHolder holder, int position)
    {

        HashMap hm=(HashMap)yearAl.get(position);
        final String movieid=(String) hm.get(yearKeys[0]);
        final String movieNAme=(String) hm.get(yearKeys[1]);
        final String year=(String) hm.get(yearKeys[2]);

        holder.title.setText(movieNAme);

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieid+"/"+movieid+".jpg";

        Glide.with(contextYear)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.thumbnail);

        holder.cardll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intent=new Intent(contextYear,Songs.class);
                intent.putExtra("idno",movieid);
                intent.putExtra("BitmapImage",imageUrl);
                intent.putExtra("moviename",movieNAme);
                intent.putExtra("date123",year);


                contextYear.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return yearAl.size();
    }

    public class SingleYearViewHolder extends RecyclerView.ViewHolder
    {

        public TextView title;
        public ImageView thumbnail;
        public CardView cardll;
        public ProgressBar pb;

        public SingleYearViewHolder(View view)
        {
            super(view);

            title = view.findViewById(R.id.seeAlltitle);
            thumbnail =  view.findViewById(R.id.seeAllImage);
            cardll =  view.findViewById(R.id.card_view);
            pb=view.findViewById(R.id.pb);

        }
    }
}
