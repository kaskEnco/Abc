package com.kasksolutions.lyricist.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.LyricDisplay;
import com.kasksolutions.lyricist.allclasses.MovieSongsByWriter;
import com.kasksolutions.lyricist.allclasses.TeluguLyric;
import com.kasksolutions.lyricist.databse.MyDb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by home on 2/6/2018.
 */

public class MovieSongsByWriterAdapter extends RecyclerView.Adapter<MovieSongsByWriterAdapter.MySongsViewHolder>
{
    private Context c;
    ArrayList albumList;

    //REFERENCE VARIABLE FOR HIDING CARD VIEWS 

    int mExpandedPosition=-1;
    int previousExpandedPosition=-1;

    public MovieSongsByWriterAdapter(Context c, ArrayList albumList)
    {
        this.c = c;
        this.albumList = albumList;
    }

    @Override
    public MySongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.see_all_card_style, parent, false);
        return new MySongsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MySongsViewHolder holder, final int position)
    {
        HashMap hm=(HashMap)albumList.get(position);
        final String lyricid=(String) hm.get("lyricid");
        final String lyricTitle=(String) hm.get("lyricTitle");
        final String writerNmae=(String)hm.get("writerName");
        final String movieId=(String)hm.get("movieId");

        holder.songlist.setText(lyricTitle);

        final String MovieimageUrl= "https://s3.amazonaws.com/liriceapp/"+movieId+"/"+movieId+".jpg";


        final String imageUrl= "https://s3.amazonaws.com/lyricist/"+writerNmae+".jpg";
        final boolean isExpanded = position==mExpandedPosition;

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
                //https://stackoverflow.com/questions/7951936/how-to-finish-an-activity-from-an-adapter
                //BY SUING INTENTS WE CAN GO TO LYRIC DISPLAY ACTIVITY
                //((Activity)c).finish();
                Intent i = new Intent(c, TeluguLyric.class);
                i.putExtra("lyricid", lyricid);
                i.putExtra("lyricTitle",lyricTitle);
                i.putExtra("wrotername",writerNmae);
                i.putExtra("movieName", MovieSongsByWriter.movieName);
                i.putExtra("movie_id",movieId);

                i.putExtra("writerImage", imageUrl);

                i.putExtra("BitmapImage",MovieimageUrl);

                i.putExtra("datedate",MovieSongsByWriter.dateYear);

                MyDb myDb=new MyDb(c);

                SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();

                ContentValues cv=new ContentValues();

                cv.put(MyDb.COL1,movieId);
                cv.put(MyDb.COL2,MovieSongsByWriter.movieName);
                cv.put(MyDb.COL3,lyricid);
                cv.put(MyDb.COL4,lyricTitle);
                cv.put(MyDb.COL5,writerNmae);

                cv.put(MyDb.COL6,MovieSongsByWriter.dateYear);
               // cv.put(MyDb.COL6,getDateTime());
                long l=sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

                if (l!=0) {}
                c.startActivity(i);
            }

        });

        //IT IS FOR ENGLISH ONCLICK
        holder.english.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
             //   ((Activity)c).finish();
                Intent i=new Intent(c,LyricDisplay.class);
                i.putExtra("lyricid", lyricid);
                i.putExtra("lyricTitle",lyricTitle);
                i.putExtra("wrotername",writerNmae);
                i.putExtra("movieName",MovieSongsByWriter.movieName);
                i.putExtra("movie_id",movieId);

                i.putExtra("writerImage", imageUrl);

                i.putExtra("BitmapImage",MovieimageUrl);

                i.putExtra("datedate",MovieSongsByWriter.dateYear);

                MyDb myDb=new MyDb(c);
                SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put(MyDb.COL1,movieId);
                cv.put(MyDb.COL2,MovieSongsByWriter.movieName);
                cv.put(MyDb.COL3,lyricid);
                cv.put(MyDb.COL4,lyricTitle);
                cv.put(MyDb.COL5,writerNmae);
                cv.put(MyDb.COL6,MovieSongsByWriter.dateYear);
                long l=sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
              //// cv.put(MyDb.COL6,getDateTime());
               // long l=sqLiteDatabase.insert(MyDb.TABLENAME,null,cv);
                if (l!=0)
                {
                    // Toast.makeText(c, "Inserted Sucessfully", Toast.LENGTH_SHORT).show();
                }

                c.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return albumList.size();
    }

    public class MySongsViewHolder extends RecyclerView.ViewHolder
    {

        public TextView songlist,telugu,english;
        public LinearLayout tablelayout;


        public MySongsViewHolder(View view) 
        {
            super(view);
            songlist=view.findViewById(R.id.songlist);
            tablelayout=view.findViewById(R.id.tlll);
            telugu=view.findViewById(R.id.one);
            english=view.findViewById(R.id.two);
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
