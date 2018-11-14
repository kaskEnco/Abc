package com.kasksolutions.lyricist.allclasses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.SingleWriterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SingleWriter extends BaseActivity
{
    ArrayList al;
    RecyclerView rcv;

    SingleWriterAdapter singleWriterAdapter;

    String writer;
    public static String movieName;
    public static String writerName,years;

    ImageView writerImage;
    public static String getWriterImage;

AppBarLayout appBarLayout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_writer);
        Toolbar toolbar=findViewById(R.id.single_writer_toolbar);
        setSupportActionBar(toolbar);

        writerImage=findViewById(R.id.imageView);

        appBarLayout=findViewById(R.id.single_writer_appbar);

        progressBar=findViewById(R.id.spin_kit11);


        writer=getIntent().getStringExtra("writerName");

        rcv=findViewById(R.id.writer_Content_recycler_view);

        writerName=writer.replace(" ","_");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.single_writer_collapsing_toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.single_writer_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0)
                {
                    collapsingToolbar.setTitle(writer);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        getWriterImage= getIntent().getStringExtra("writerImage");

        Glide.with(this)
                .load(getWriterImage)
                .into(writerImage);

        al=new ArrayList();
        new SingleWriterJson().execute();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    public class SingleWriterJson extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
          //  dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
            {
                URL url = new URL("http://kasksolutions.com:90/LiriceApp/writer/"+writerName);

               // URL url = new URL("http://192.168.0.5:8080/LiriceApp/writer/Devi Sri Prasad");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                JSONArray jsonArray = new JSONArray(br.readLine());

              //  Log.d("writer100",""+jsonArray);
                for (int i = 0; i < jsonArray.length(); i++)
                {

                    JSONObject e = jsonArray.getJSONObject(i);
                   String name = e.getString("movieName");
                   String id=e.getString("movieId");
                   String date=e.getString("movieReleaseDate");

                    Log.d("writer100",""+name);
                    years=getDate(Long.parseLong(date),"yyyy");
                    HashMap hm = new HashMap();
                    hm.put("movieName",name);
                    hm.put("movieId",id);
                    hm.put("year",years);
                    if (!al.contains(hm))
                    {
                        al.add(hm);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            singleWriterAdapter=new SingleWriterAdapter(al,SingleWriter.this);

            rcv.setHasFixedSize(true);
            rcv.setLayoutManager(new LinearLayoutManager(SingleWriter.this));
            rcv.setAdapter(singleWriterAdapter);

            progressBar.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);

        }
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
