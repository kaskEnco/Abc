package com.kasksolutions.lyricist.adapters;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.bumptech.glide.request.target.Target;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.LyricDisplay;
import com.kasksolutions.lyricist.allclasses.TeluguLyric;
import com.kasksolutions.lyricist.databse.MyDb;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 2/15/2018.
 */

public class RecentSeeAllAdapter extends RecyclerView.Adapter<RecentSeeAllAdapter.RecentSeeViewHolder>
{
    Context ccontext;
    ArrayList al;

    private int lastPosition = -1;

    String keys[]={"MovieId","MovieName","MovieImage","LyricName","writerName","yearDate"};

    public RecentSeeAllAdapter(Context ccontext, ArrayList al) {
        this.ccontext = ccontext;
        this.al = al;
    }

    @Override
    public RecentSeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(ccontext);
        //create a new layout for cardview
        View v1=li.inflate(R.layout.seeall_onclick_card_style,null);
        return new RecentSeeViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(final RecentSeeViewHolder holder, int position)
    {
        HashMap hm=(HashMap)al.get(position);


        final String movieId=(String) hm.get(keys[0]);
        final String MovieName=(String) hm.get(keys[1]);
        final String LyricId=(String) hm.get(keys[2]);
        final String LyricName =(String)hm.get(keys[3]);
        final String writerName=(String)hm.get(keys[4]);

        final String years=(String)hm.get(keys[5]);

        holder.title.setText(LyricName);
      //  Picasso picasso=Picasso.with(ccontext);

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieId+"/"+movieId+".jpg";
       // picasso.load(imageUrl).into(holder.thumbnail);//thaumbline is the imageview refernce

        Glide.with(ccontext)
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


        holder.cardll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ccontext);

                LayoutInflater inflater = LayoutInflater.from(ccontext);

                View content = inflater.inflate(R.layout.alert_diolg_style, null, true);

                dialog.setView(content);

                final AlertDialog alertDialog = dialog.create();

                TextView tel = content.findViewById(R.id.alerttel);
                TextView eng = content.findViewById(R.id.alerteng);

                tel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alertDialog.dismiss();

                        Intent intent = new Intent(ccontext, TeluguLyric.class);//  By using Intents we can goto Songs Activity

                        intent.putExtra("lyricid", LyricId);
                        intent.putExtra("lyricTitle", LyricName);

                        intent.putExtra("movieName", MovieName);

                        //  intent.putExtra("BitmapImage", imageUrl);


                        intent.putExtra("movie_id", movieId);

                        intent.putExtra("wrotername", writerName);

                        intent.putExtra("datedate",years);

                        MyDb myDb=new MyDb(ccontext);
                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                        ContentValues cv=new ContentValues();
                        cv.put(MyDb.COL1,movieId);
                        cv.put(MyDb.COL2,MovieName);
                        cv.put(MyDb.COL3,LyricId);
                        cv.put(MyDb.COL4,LyricName);
                        cv.put(MyDb.COL5,writerName);
                        cv.put(MyDb.COL6,years);
                        sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

                        ccontext.startActivity(intent);
                    }
                });
                eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        Intent intent = new Intent(ccontext, LyricDisplay.class);//  By using Intents we can goto Songs Activity

                        intent.putExtra("lyricid", LyricId);
                        intent.putExtra("lyricTitle", LyricName);

                        intent.putExtra("movieName", MovieName);

                        //  intent.putExtra("BitmapImage", imageUrl);


                        intent.putExtra("movie_id", movieId);

                        intent.putExtra("wrotername", writerName);

                        intent.putExtra("datedate",years);

                        MyDb myDb=new MyDb(ccontext);
                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                        ContentValues cv=new ContentValues();
                        cv.put(MyDb.COL1,movieId);
                        cv.put(MyDb.COL2,MovieName);
                        cv.put(MyDb.COL3,LyricId);
                        cv.put(MyDb.COL4,LyricName);
                        cv.put(MyDb.COL5,writerName);

                        cv.put(MyDb.COL6,years);
                        //https://stackoverflow.com/questions/26326696/how-to-prevent-to-insert-duplicate-value-in-sqlite-databse-if-duplicate-then-ov

                        // sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLi)
                        sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

                        ccontext.startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        setAnimation(holder.itemView,position);
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class RecentSeeViewHolder extends RecyclerView.ViewHolder
    {

        public TextView title;
        public ImageView thumbnail;
        public LinearLayout cardll;
        private ProgressBar progressBar;
        public RecentSeeViewHolder(View view)
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
            Animation animation = AnimationUtils.loadAnimation(ccontext,R.anim.latest_anim);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
