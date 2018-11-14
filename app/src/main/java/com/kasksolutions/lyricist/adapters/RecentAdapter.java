package com.kasksolutions.lyricist.adapters;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.LyricDisplay;
import com.kasksolutions.lyricist.allclasses.TeluguLyric;
import com.kasksolutions.lyricist.databse.MyDb;

import java.util.ArrayList;
import java.util.HashMap;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentViewHolder>
{
    Context ccontext;
    ArrayList al;

    String keys[]={"MovieId","MovieName","MovieImage","LyricName","writerName","yearDate"};

    public RecentAdapter(Context ccontext, ArrayList al) {
        this.ccontext = ccontext;
        this.al = al;
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater li=LayoutInflater.from(ccontext);
        //create a new layout for cardview
        View v1=li.inflate(R.layout.mycard_style,null);
        return new RecentViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, int position)
    {

        HashMap hm=(HashMap)al.get(position);

        final String movieId=(String) hm.get(keys[0]);
        final String MovieName=(String) hm.get(keys[1]);
        final String LyricId=(String) hm.get(keys[2]);
        final String LyricName =(String)hm.get(keys[3]);
        final String writerName=(String)hm.get(keys[4]);

        final String years=(String)hm.get(keys[5]);

        holder.title.setText(LyricName);

        final String writerImage= "https://s3.amazonaws.com/lyricist/"+writerName+".jpg";

        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieId+"/"+movieId+".jpg";

        Glide.with(ccontext)
                .load(imageUrl)
                .into(holder.thumbnail);

        holder.cardll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(ccontext);

                LayoutInflater inflater = LayoutInflater.from(ccontext);

                final View content = inflater.inflate(R.layout.alert_diolg_style, null,true);

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

                        Intent intent = new Intent(ccontext, TeluguLyric.class);//  By using Intents we can goto Songs Activity

                        intent.putExtra("lyricid", LyricId);
                        intent.putExtra("lyricTitle", LyricName);
                        intent.putExtra("movieName", MovieName);
                        intent.putExtra("movie_id", movieId);
                        intent.putExtra("wrotername", writerName);
                        intent.putExtra("datedate",years);
                        intent.putExtra("writerImage",writerImage);
                        intent.putExtra("BitmapImage",imageUrl);

                        MyDb myDb=new MyDb(ccontext);
                        SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                        ContentValues cv=new ContentValues();
                        cv.put(MyDb.COL1,movieId);
                        cv.put(MyDb.COL2,MovieName);
                        cv.put(MyDb.COL3,LyricId);
                        cv.put(MyDb.COL4,LyricName);
                        cv.put(MyDb.COL5,writerName);
                        cv.put(MyDb.COL6,years);
                        // sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLi)
                        sqLiteDatabase.insertWithOnConflict(MyDb.TABLENAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);


                        ccontext.startActivity(intent);
                    }
                });
                eng.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        alertDialog.dismiss();

                        Intent intent = new Intent(ccontext, LyricDisplay.class);//  By using Intents we can goto Songs Activity

                        intent.putExtra("lyricid", LyricId);
                        intent.putExtra("lyricTitle", LyricName);

                        intent.putExtra("movieName", MovieName);

                        //  intent.putExtra("BitmapImage", imageUrl);


                        intent.putExtra("movie_id", movieId);

                        intent.putExtra("wrotername", writerName);
                        intent.putExtra("datedate",years);

                        intent.putExtra("writerImage",writerImage);
                        intent.putExtra("BitmapImage",imageUrl);

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

    public class RecentViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout cardll;
        public RecentViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            thumbnail =  view.findViewById(R.id.thumbnail);
            cardll =  view.findViewById(R.id.cardll);
        }
    }
}
