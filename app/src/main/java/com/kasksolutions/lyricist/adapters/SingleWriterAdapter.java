package com.kasksolutions.lyricist.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.MovieSongsByWriter;
import com.kasksolutions.lyricist.allclasses.SingleWriter;

import java.util.ArrayList;
import java.util.HashMap;

public class SingleWriterAdapter extends RecyclerView.Adapter<SingleWriterAdapter.SingleWriterViewHolder>
{
    ArrayList al;
    Context c;

    public SingleWriterAdapter(ArrayList al, Context c) {
        this.al = al;
        this.c = c;
    }

    @Override
    public SingleWriterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view=LayoutInflater.from(c).inflate(R.layout.single_writer_style,null,false);
        return new SingleWriterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleWriterViewHolder holder, int position)
    {

        HashMap hm=(HashMap)al.get(position);
        final String movieName=(String) hm.get("movieName");
        final String movieId=(String)hm.get("movieId");

        final String years=(String)hm.get("year");

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieId+"/"+movieId+".jpg";


        Glide.with(c)
                .load(imageUrl)
                .into(holder.thumnail);


        holder.lyric.setText(movieName);
        holder.lyric.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              //  ((Activity)c).finish();
                Intent i=new Intent(c,MovieSongsByWriter.class);
                i.putExtra("movieId",movieId);
                i.putExtra("movieName",movieName);
                i.putExtra("BitmapImage",imageUrl);
                i.putExtra("writerImage", SingleWriter.getWriterImage);
               // i.putExtra("BitmapImage",)
                i.putExtra("dates",years);

                Log.d("BitmapImage",""+imageUrl);

                c.startActivity(i);
            }
        });
        holder.thumnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               // ((Activity)c).finish();
                Intent i=new Intent(c,MovieSongsByWriter.class);
                i.putExtra("movieId",movieId);
                i.putExtra("movieName",movieName);
                i.putExtra("BitmapImage",imageUrl);
                i.putExtra("dates",years);
                i.putExtra("writerImage",SingleWriter.getWriterImage);
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class SingleWriterViewHolder extends RecyclerView.ViewHolder
    {

        TextView lyric;
        ImageView thumnail;

        public SingleWriterViewHolder(View itemView)
        {
            super(itemView);
            lyric=itemView.findViewById(R.id.single_songlist);
            thumnail=itemView.findViewById(R.id.imageid);

        }
    }
}
