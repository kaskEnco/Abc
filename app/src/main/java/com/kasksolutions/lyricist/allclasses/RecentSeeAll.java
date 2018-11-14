package com.kasksolutions.lyricist.allclasses;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.RecentSeeAllAdapter;
import com.kasksolutions.lyricist.databse.MyDb;

import java.util.ArrayList;
import java.util.HashMap;

public class RecentSeeAll extends BaseActivity {

    ArrayList al;

    String recentkeys[] = {"MovieId", "MovieName", "MovieImage", "LyricName", "writerName", "yearDate"};

    RecentSeeAllAdapter recentSeeAllAdapter;

    RecyclerView recentRecyclerView;
    Button clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_see_all);

        recentRecyclerView = findViewById(R.id.recentSeeAllRecv);

        clear = findViewById(R.id.clear);

        clear.setVisibility(View.VISIBLE);

        al = new ArrayList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    public void clearDb(View v) {
        removeAll();
        finish();
        //startActivity(getIntent());
        Toast.makeText(this, "Recently viewed data cleared successfully", Toast.LENGTH_SHORT).show();
    }

    public void removeAll() {
        MyDb myDb = new MyDb(this);
        SQLiteDatabase sqLiteDatabase = myDb.getReadableDatabase();
        sqLiteDatabase.delete(MyDb.TABLENAME, null, null);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        ;
        return super.onSupportNavigateUp();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //It is used to clear the array list
        al.clear();
        MyDb myDb = new MyDb(this);

        SQLiteDatabase sql = myDb.getReadableDatabase();

        Cursor c = sql.rawQuery("select * from " + MyDb.TABLENAME, null);
        boolean res = c.moveToLast();

        if (res) {
            do {
                String movie_id = c.getString(0);
                String movie_name = c.getString(1);
                String lyric_id = c.getString(2);
                String lyric_name = c.getString(3);
                String writer_Name = c.getString(4);
                String date = c.getString(5);

                HashMap hm = new HashMap();
                hm.put(recentkeys[0], movie_id);
                hm.put(recentkeys[1], movie_name);
                hm.put(recentkeys[2], lyric_id);
                hm.put(recentkeys[3], lyric_name);
                hm.put(recentkeys[4], writer_Name);
                hm.put(recentkeys[5], date);

//                Log.d("name11",movie_name);
                al.add(hm);
                clear.setVisibility(View.VISIBLE);

            }
            while (c.moveToPrevious());
        }

        recentSeeAllAdapter = new RecentSeeAllAdapter(this, al);
        recentRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recentRecyclerView.setLayoutManager(mLayoutManager);
        recentRecyclerView.setAdapter(recentSeeAllAdapter);
        clear.setVisibility(View.VISIBLE);

    }
}
