package com.kasksolutions.lyricist.adapters;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class TrendingSeeAllRecycle
        extends RecyclerView.Adapter<TrendingSeeAllRecycle.TrendingSeeAllMyviewHolder>
        implements FastScrollRecyclerView.SectionedAdapter
{
    Context c;
    private ArrayList al;
    int mExpandedPosition=-1;
    int previousExpandedPosition=-1;


    private int lastPosition = -1;


    String trendingkeys[]={"trendingmovieId","trendingname","lyricId","lyricViews","lyric_name","writerName","year"};

    public TrendingSeeAllRecycle(Context c, ArrayList al)
    {
        this.c = c;
        this.al = al;
    }

    @Override
    public TrendingSeeAllMyviewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(c);

        //create a new layout for cardview

        View v1=li.inflate(R.layout.trending_all_songs_list_style,null,false);
        TrendingSeeAllMyviewHolder trendingSeeAllMyviewHolder=new TrendingSeeAllMyviewHolder(v1);



        return trendingSeeAllMyviewHolder;
    }

    @Override
    public void onBindViewHolder(final TrendingSeeAllMyviewHolder holder, final int position)
    {

        HashMap hm=(HashMap)al.get(position);
       // final int movieid=(Integer)hm.get(trendingkeys[0]);
        final String movieid=(String) hm.get(trendingkeys[0]);
        final String movieNAme=(String) hm.get(trendingkeys[1]);
        final String lyricId=(String) hm.get(trendingkeys[2]);
      //  final String lyricViews=(String) hm.get(trendingkeys[3]);
        final String lyricName=(String) hm.get(trendingkeys[4]);
        final String writerName=(String) hm.get(trendingkeys[5]);
        final String dd=(String)hm.get(trendingkeys[6]);
//        holder.textname.setText(movieNAme);

        holder.songlist.setText(lyricName);

        holder.trending_movie_name.setText(movieNAme);

        holder.trending_writer_name.setText(writerName);

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieid+"/"+movieid+".jpg";

        final String writrImage= "https://s3.amazonaws.com/lyricist/"+writerName+".jpg";

        Glide.with(c)
                .load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                    {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.trending_image);

        final boolean isExpanded = position==mExpandedPosition;

        //TABLE LAYOUT IS THE REFERENCE VARIABLE OF TABLE LAYOUT

       holder.tablelayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);


        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            }
        });


        //IT IS TELUGU ONCLICK

        holder.telugu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent(c, TeluguLyric.class);

                i.putExtra("lyricid",lyricId);
                i.putExtra("lyricTitle",lyricName);
                i.putExtra("wrotername",writerName);
                i.putExtra("movieName",movieNAme);

                i.putExtra("movie_id",movieid);

                i.putExtra("BitmapImage", imageUrl);
                i.putExtra("datedate",dd);

                i.putExtra("writerImage",writrImage);


                MyDb myDb=new MyDb(c);
                SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put(MyDb.COL1,movieid);
                cv.put(MyDb.COL2,movieNAme);
                cv.put(MyDb.COL3,lyricId);
                cv.put(MyDb.COL4,lyricName);
                cv.put(MyDb.COL5,writerName);
                cv.put(MyDb.COL6,dd);

                long l=sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                if (l!=0)
                {
                }
                c.startActivity(i);
    }
});

        //IT IS FOR ENGLISH ONCLICK
        holder.english.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(c,LyricDisplay.class);
                i.putExtra("lyricid",lyricId);
                i.putExtra("lyricTitle",lyricName);
                i.putExtra("wrotername",writerName);
                i.putExtra("movieName",movieNAme);

                i.putExtra("movie_id",movieid);

                i.putExtra("BitmapImage", imageUrl);
                i.putExtra("datedate",dd);
                i.putExtra("writerImage",writrImage);



                MyDb myDb=new MyDb(c);
                SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put(MyDb.COL1,movieid);
                cv.put(MyDb.COL2,movieNAme);
                cv.put(MyDb.COL3,lyricId);
                cv.put(MyDb.COL4,lyricName);
                cv.put(MyDb.COL5,writerName);
                cv.put(MyDb.COL6,dd);

                long l=sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                //cv.put(MyDb.COL6,getDateTime());
               // long l=sqLiteDatabase.insert(MyDb.TABLENAME,null,cv);
                if (l!=0)
                {
                    //Toast.makeText(c, "Inserted Sucessfully", Toast.LENGTH_SHORT).show();
                }
                c.startActivity(i);
            }
        });

        setAnimation(holder.itemView,position);

    }

    @Override
    public int getItemCount()
    {
        return al.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position)
    {
        //return null;
        return String.valueOf(al.get(position));
    }

    public class TrendingSeeAllMyviewHolder extends RecyclerView.ViewHolder
    {

        public ProgressBar pb;
        CardView cv;
        public TextView songlist,telugu,english,trending_movie_name,trending_writer_name;
        public LinearLayout tablelayout;
        public ImageView trending_image;
        public TrendingSeeAllMyviewHolder(View view)
        {
            super(view);
            songlist=view.findViewById(R.id.trend_songlist);
            tablelayout=view.findViewById(R.id.tlll);
            trending_image=view.findViewById(R.id.trending_image);
            trending_movie_name=view.findViewById(R.id.trending_movie_name);
            trending_writer_name=view.findViewById(R.id.trending_writer_name);
            cv=view.findViewById(R.id.trend_card_view);
            telugu=view.findViewById(R.id.one);
            english=view.findViewById(R.id.two);

            pb=view.findViewById(R.id.trending_pb);
        }
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(c, R.anim.right);
            // Animation animation = AnimationUtils.loadAnimation(c, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
