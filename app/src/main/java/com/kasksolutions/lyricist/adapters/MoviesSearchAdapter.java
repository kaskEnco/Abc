package com.kasksolutions.lyricist.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.Songs;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 28-Mar-18.
 */

public class MoviesSearchAdapter extends RecyclerView.Adapter<MoviesSearchAdapter.MoviesSearchViewHolder>
{

    Context moviesContext;
    ArrayList moviesAl;
    String search_keys[]={"lyric_title","id","movie_id","writer_name","movie_release_date","movie_name"};

    public MoviesSearchAdapter(Context moviesContext, ArrayList moviesAl) {
        this.moviesContext = moviesContext;
        this.moviesAl = moviesAl;
    }

    @Override
    public MoviesSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_movie_name_card_style, parent, false);
        return  new MoviesSearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MoviesSearchViewHolder holder, int position)
    {
        HashMap hm=(HashMap)moviesAl.get(position);
        final int movie_id=(Integer)hm.get(search_keys[2]);
        final String movie_name=(String)hm.get(search_keys[5]);
        String date=(String)hm.get(search_keys[4]);

        final String dateYear=date.substring(0,4);
        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movie_id+"/"+movie_id+".jpg";

        Glide.with(moviesContext).
                load(imageUrl).
                into(holder.movieImage);

        holder.movieName.setText(movie_name);

        holder.movieCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(moviesContext,Songs.class);

                intent.putExtra("idno", ""+movie_id);

                intent.putExtra("moviename",movie_name);

                intent.putExtra("BitmapImage", imageUrl);

                intent.putExtra("date123", dateYear);

                Log.d("year100","'movieDate"+dateYear);

                moviesContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        //https://stackoverflow.com/questions/49540001/limit-recyclerview-items-to-only-10-without-paginating-with-filter?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        //return Math.min(moviesAl.size(), 5);
        return Math.min(moviesAl.size(),3);
    }

    public class MoviesSearchViewHolder extends RecyclerView.ViewHolder
    {
        private TextView movieName;
        private ImageView movieImage;
        private CardView movieCard;
        public MoviesSearchViewHolder(View itemView)
        {
            super(itemView);
            movieName=itemView.findViewById(R.id.search_movie_name);
            movieImage=itemView.findViewById(R.id.search_movie_imageView);
            movieCard=itemView.findViewById(R.id.search_movie_card);
        }
    }
}
