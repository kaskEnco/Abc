package com.kasksolutions.lyricist.adapters;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.LyricDisplay;import com.kasksolutions.lyricist.allclasses.TeluguLyric;
import com.kasksolutions.lyricist.databse.MyDb;

import java.util.ArrayList;
import java.util.HashMap;


public class  MySearchAdapter extends RecyclerView.Adapter<MySearchAdapter.MyViewHolder>

{
    Context c;

    ArrayList arrayList;
    String search_keys[]={"lyric_title","id","movie_id","writer_name","movie_release_date","movie_name"};

    public MySearchAdapter(Context c, ArrayList arrayList)
    {
        this.c = c;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.serach_card_style, parent, false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        HashMap hm=(HashMap)arrayList.get(position);

        final String songName=(String)hm.get(search_keys[0]);
        final String lyricId=(String)hm.get(search_keys[1]);
        final int movie_id=(Integer)hm.get(search_keys[2]);
        final String writerName=(String)hm.get(search_keys[3]);
        final String date=(String) hm.get(search_keys[4]);

        final String movie_name=(String)hm.get(search_keys[5]);

        final String WRITERNAME=writerName.replace(" ","_");

        final String dateYear=date.substring(0,4);

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movie_id+"/"+movie_id+".jpg";

        final String writerImage= "https://s3.amazonaws.com/lyricist/"+WRITERNAME+".jpg";

        Glide.with(c).
                load(imageUrl).
                into(holder.searchImage);

        holder.songNmae.setText(songName);

        holder.tv.setText(movie_name);

        holder.writerName.setText(writerName);

        holder.cv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                LayoutInflater inflater = LayoutInflater.from(c);
                View content = inflater.inflate(R.layout.alert_diolg_style, null, false);
                dialog.setView(content);

                final AlertDialog alertDialog = dialog.create();

                TextView tel = content.findViewById(R.id.alerttel);
                TextView eng = content.findViewById(R.id.alerteng);
                tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                        Intent i = new Intent(c, TeluguLyric.class);
                        i.putExtra("lyricid", lyricId);

                        i.putExtra("lyricTitle", songName);
                        i.putExtra("wrotername", WRITERNAME);
                        i.putExtra("movieName", movie_name);

                        i.putExtra("movie_id", "" + movie_id);

                        i.putExtra("BitmapImage", imageUrl);

                        i.putExtra("datedate", dateYear);

                        Log.d("date100","Telugu:"+dateYear);


                        i.putExtra("writerImage", writerImage);

                        MyDb myDb=new MyDb(c);

                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();

                        ContentValues cv=new ContentValues();

                        cv.put(MyDb.COL1,""+movie_id);
                        cv.put(MyDb.COL2,movie_name);
                        cv.put(MyDb.COL3,lyricId);
                        cv.put(MyDb.COL4,songName);
                        cv.put(MyDb.COL5,writerName);
                        cv.put(MyDb.COL6,dateYear);

                        sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
                        c.startActivity(i);
                    }
                });
                eng.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent i = new Intent(c, LyricDisplay.class);
                        i.putExtra("lyricid", lyricId);

                        i.putExtra("lyricTitle", songName);
                        i.putExtra("wrotername", writerName);
                        i.putExtra("movieName", movie_name);

                        i.putExtra("movie_id", "" + movie_id);

                        i.putExtra("BitmapImage", imageUrl);

                        i.putExtra("datedate", dateYear);

                        Log.d("date100","English:"+dateYear);

                        i.putExtra("writerImage", writerImage);

                        MyDb myDb=new MyDb(c);

                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();

                        ContentValues cv=new ContentValues();

                        cv.put(MyDb.COL1,""+movie_id);
                        cv.put(MyDb.COL2,movie_name);
                        cv.put(MyDb.COL3,lyricId);
                        cv.put(MyDb.COL4,songName);
                        cv.put(MyDb.COL5,writerName);
                        cv.put(MyDb.COL6,dateYear);
                        sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

                        c.startActivity(i);
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
        return Math.min(arrayList.size(),3);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv,songNmae,writerName;
        ImageView searchImage;
        CardView cv;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            cv=itemView.findViewById(R.id.card);
            tv=itemView.findViewById(R.id.search_movie_name);
            songNmae=itemView.findViewById(R.id.searchSongName);
            writerName=itemView.findViewById(R.id.writerName_writer_name);
            searchImage=itemView.findViewById(R.id.searhImage);
        }
    }
}
