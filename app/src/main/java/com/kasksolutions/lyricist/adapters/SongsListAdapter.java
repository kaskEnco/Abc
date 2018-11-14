package com.kasksolutions.lyricist.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.LyricDisplay;
import com.kasksolutions.lyricist.allclasses.Songs;
import com.kasksolutions.lyricist.allclasses.TeluguLyric;
import com.kasksolutions.lyricist.databse.MyDb;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by home on 13-Dec-17.
 */

 public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MySongsListViewHolder>
{
    private Context c;
    ArrayList albumList;

    //REFERENCE VARIABLE FOR HIDING CARD VIEWS 
    
    int mExpandedPosition=-1;
    int previousExpandedPosition=-1;

    public SongsListAdapter(Context c, ArrayList albumList)
    {
        this.c = c;
        this.albumList = albumList;
    }

    @Override
    public MySongsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //CREATE A NEW LAYOUT NMAED AS SEE ALL CARD STYLE

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.see_all_card_style, parent, false);

        return new MySongsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MySongsListViewHolder holder, final int position)
    {
        HashMap hm=(HashMap)albumList.get(position);
        final String lyricid=(String) hm.get("lyricid");
        final String lyricTitle=(String) hm.get("lyricTitle");
        final String writerNmae=(String)hm.get("writerName");
        final String movieId=(String)hm.get("movieId");

        holder.songlist.setText(lyricTitle);

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieId+"/"+movieId+".jpg";

        final String writerImage= "https://s3.amazonaws.com/lyricist/"+writerNmae+".jpg";


        final boolean isExpanded = position==mExpandedPosition;


        holder.tablelayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
        {
            previousExpandedPosition = position;
            holder.songlist.setTextColor(c.getResources().getColor(R.color.collapse));
        }
        else
        {
            holder.songlist.setTextColor(c.getResources().getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //holder.songlist.setTextColor(c.getResources().getColor(R.color.collapse));
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
               // ((Activity)c).finish();

                Intent i = new Intent(c, TeluguLyric.class);
                i.putExtra("lyricid", lyricid);

                i.putExtra("lyricTitle",lyricTitle);

                i.putExtra("wrotername",writerNmae);

                i.putExtra("movieName", Songs.moviename);

                i.putExtra("movie_id",movieId);

                i.putExtra("BitmapImage", imageUrl);

                i.putExtra("datedate",Songs.dateyear);

                i.putExtra("writerImage",writerImage);

                MyDb myDb=new MyDb(c);

                SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put(MyDb.COL1,movieId);
                cv.put(MyDb.COL2,Songs.moviename);
                cv.put(MyDb.COL3,lyricid);
                cv.put(MyDb.COL4,lyricTitle);
                cv.put(MyDb.COL5,writerNmae);
                cv.put(MyDb.COL6,Songs.dateyear);

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
                //https://stackoverflow.com/questions/7951936/how-to-finish-an-activity-from-an-adapter
                //BY SUING INTENTS WE CAN GO TO LYRIC DISPLAY ACTIVITY
                // ((Activity)c).finish();
                Intent i = new Intent(c, LyricDisplay.class);
                i.putExtra("lyricid", lyricid);

                i.putExtra("lyricTitle",lyricTitle);
                i.putExtra("wrotername",writerNmae);
                i.putExtra("movieName",Songs.moviename);

                i.putExtra("movie_id",movieId);

                i.putExtra("BitmapImage", imageUrl);

                i.putExtra("datedate",Songs.dateyear);

                i.putExtra("writerImage",writerImage);

                MyDb myDb=new MyDb(c);
                SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                ContentValues cv=new ContentValues();
                cv.put(MyDb.COL1,movieId);
                cv.put(MyDb.COL2,Songs.moviename);
                cv.put(MyDb.COL3,lyricid);
                cv.put(MyDb.COL4,lyricTitle);
                cv.put(MyDb.COL5,writerNmae);
                cv.put(MyDb.COL6,Songs.dateyear);
                sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

                c.startActivity(i);
            }

        });


    }

    @Override
    public int getItemCount()
    {
        return albumList.size();

    }

    public class MySongsListViewHolder extends RecyclerView.ViewHolder
    {
        public TextView songlist,telugu,english;
        public LinearLayout tablelayout;
        public CardView cardView;


        public MySongsListViewHolder(View view)
        {
            super(view);
            songlist=view.findViewById(R.id.songlist);
            tablelayout=view.findViewById(R.id.tlll);
            telugu=view.findViewById(R.id.one);
            english=view.findViewById(R.id.two);
            cardView=view.findViewById(R.id.card_view);

        }
    }
}
