package com.kasksolutions.lyricist.adapters;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.kasksolutions.lyricist.allclasses.LyricDisplay;
import com.kasksolutions.lyricist.allclasses.TeluguLyric;
import com.kasksolutions.lyricist.databse.MyDb;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 1/25/2018.
 */

public class TrendingMyRecycle extends RecyclerView.Adapter<TrendingMyRecycle.MyTrendingViewHolder>
{
    Context c;
    ArrayList al;

    String trendingkeys[]={"trendingmovieId","trendingname","lyricId","lyricViews","lyric_name","writerName","releaseDate"};

    public TrendingMyRecycle(Context c, ArrayList al)
    {
        this.c = c;
        this.al = al;
    }

    @Override
    public MyTrendingViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(c);

        //create a new layout for cardview

        View v1=li.inflate(R.layout.mycard_style,null);

        MyTrendingViewHolder myTrendingViewHolder=new MyTrendingViewHolder(v1);

        return myTrendingViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyTrendingViewHolder holder, int position)
    {

        //By using Hash Map we can get position
        HashMap hm=(HashMap)al.get(position);

        final String movie_id=(String) hm.get(trendingkeys[0]);
        final String moviename=(String) hm.get(trendingkeys[1]);
        final String lyricId=(String) hm.get(trendingkeys[2]);
        final String lyricviews=(String) hm.get(trendingkeys[3]);
        final String lyricname=(String) hm.get(trendingkeys[4]);
        final String writername=(String)hm.get(trendingkeys[5]);
        final String releaseDAte=(String)hm.get(trendingkeys[6]);
     //   final String date=(String) hm.get(keys[2]);
        holder.title.setText(lyricname);


        //It is URL for Image baed on id
        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movie_id+"/"+movie_id+".jpg";
      //  Toast.makeText(c, ""+imageUrl, Toast.LENGTH_SHORT).show();
        Log.d("mine100",""+imageUrl);


        final String writerImage= "https://s3.amazonaws.com/lyricist/"+writername+".jpg";

        Glide.with(c)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {
                        holder.progressBar.setVisibility(View.GONE);
                        if (holder.progressBar.isShown())
                        {

                        }
                        else {
                            holder.thumbnail.setImageResource(R.drawable.noimage);
                        }

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

        //By click on image Onclick

        holder.thumbnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                LayoutInflater inflater = LayoutInflater.from(c);
                View content = inflater.inflate(R.layout.alert_diolg_style, null,false);
                dialog.setView(content);

                final AlertDialog alertDialog=dialog.create();

                TextView tel =  content.findViewById(R.id.alerttel);
                TextView eng =  content.findViewById(R.id.alerteng);

                tel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alertDialog.dismiss();

                        holder.thumbnail.buildDrawingCache();

                        Intent intent = new Intent(c, TeluguLyric.class);//  By using Intents we can goto Songs Activity



                        intent.putExtra("lyricid",lyricId);
                        intent.putExtra("lyricTitle",lyricname);

                        intent.putExtra("wrotername",writername);
                        intent.putExtra("movieName",moviename);

                        Log.d("writerNmae",""+writername);

                        intent.putExtra("movie_id",movie_id);

                        intent.putExtra("BitmapImage", imageUrl);

                        intent.putExtra("datedate",releaseDAte);

                        intent.putExtra("writerImage",writerImage);

                        MyDb myDb=new MyDb(c);
                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                        ContentValues cv=new ContentValues();
                        cv.put(MyDb.COL1,movie_id);
                        cv.put(MyDb.COL2,moviename);
                        cv.put(MyDb.COL3,lyricId);
                        cv.put(MyDb.COL4,lyricname);
                        cv.put(MyDb.COL5,writername);
                        cv.put(MyDb.COL6,releaseDAte);
                        long l=sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                        if (l!=0)
                        {
                        }
                        c.startActivity(intent);
                    }
                });
                eng.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alertDialog.dismiss();
                        Intent intent = new Intent(c, LyricDisplay.class);//  By using Intents we can goto Songs Activity
                        intent.putExtra("lyricid",lyricId);
                        intent.putExtra("lyricTitle",lyricname);

                        intent.putExtra("wrotername",writername);
                        intent.putExtra("movieName",moviename);

                        Log.d("writerNmae",""+writername);

                        intent.putExtra("movie_id",movie_id);

                        intent.putExtra("BitmapImage", imageUrl);

                        intent.putExtra("datedate",releaseDAte);

                        intent.putExtra("writerImage",writerImage);

                        Log.d("images123",""+writerImage+"\n"+imageUrl);


                        MyDb myDb=new MyDb(c);
                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                        ContentValues cv=new ContentValues();
                        cv.put(MyDb.COL1,movie_id);
                        cv.put(MyDb.COL2,moviename);
                        cv.put(MyDb.COL3,lyricId);
                        cv.put(MyDb.COL4,lyricname);
                        cv.put(MyDb.COL5,writername);
                        cv.put(MyDb.COL6,releaseDAte);

                        Log.d("date100",""+releaseDAte);
                        long l=sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

                        if (l!=0)
                        {
                        }
                        c.startActivity(intent);
                    }
                });
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return al.size();
    }

    public class MyTrendingViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout cardll;
        public TextView title;
        public ImageView thumbnail;
        public ProgressBar progressBar;

        public MyTrendingViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail =  view.findViewById(R.id.thumbnail);
            cardll =  view.findViewById(R.id.cardll);
            progressBar=view.findViewById(R.id.indeterminateBar);
        }
    }
}
