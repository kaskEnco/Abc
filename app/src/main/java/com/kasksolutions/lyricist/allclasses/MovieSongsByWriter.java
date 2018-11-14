package com.kasksolutions.lyricist.allclasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.MovieSongsByWriterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MovieSongsByWriter extends BaseActivity
{
    ArrayList songsLists;
    private RecyclerView recyclerView;
    MovieSongsByWriterAdapter songsListAdapter;

    ImageView iv;
    String movieId;
    public static String movieName;
    public static String dateYear;
    public static String gettingImageUrl;

    String message = null;
    ProgressBar progressBar;
    AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_songs_by_writer);

        progressBar=findViewById(R.id.spin_kit11);
        appBarLayout=findViewById(R.id.ws_appbar);

        ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = cM.getActiveNetworkInfo();
        if (activeNetworkInfo != null)
        {
        }
        else
        {

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Internet Connection Required");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    finish();

                }
            });
            builder.show();
        }

        recyclerView=findViewById(R.id.recycler_view);
        iv=findViewById(R.id.ws_imageView);

        Toolbar toolbar=findViewById(R.id.ws_toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.ws_collapsing_toolbar);

        songsLists=new ArrayList();
        Intent i=getIntent();
        movieId=i.getStringExtra("movieId");
        movieName=i.getStringExtra("movieName");
        dateYear=i.getStringExtra("dates");


         gettingImageUrl= i.getStringExtra("BitmapImage");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // collapsingToolbar.setTitle(movieName);

        Glide.with(this)
                .load(gettingImageUrl)
                .into(iv);

      //  AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.ws_appbar);
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
                    collapsingToolbar.setTitle(movieName);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


        writerSongs();

    }
    protected  void writerSongs()
    {
        Log.d("name100",""+movieId+SingleWriter.writerName);
        //StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.5:8080/LiriceApp/lyrics/"+movieId+"/"+SingleWriter.aaa,
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://kasksolutions.com:90/LiriceApp/lyrics/"+movieId+"/"+SingleWriter.writerName,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {
                            JSONArray jsonArray=new JSONArray(response);

                            //now looping through all the elements of the json array
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);

                                HashMap hm=new HashMap();

                                hm.put("lyricid",e.getString("lyricId"));
                                hm.put("lyricTitle",e.getString("lyricTitle"));
                                hm.put("movieId",e.getString("movieId"));
                                hm.put("writerName",e.getString("writerName"));
                                //ADDED INTO ARRAYLIST NAMED AS SONGS LIST

                                songsLists.add(hm);
                            }

                            songsListAdapter=new MovieSongsByWriterAdapter(MovieSongsByWriter.this,songsLists);

                            RecyclerView.LayoutManager li=new LinearLayoutManager(MovieSongsByWriter.this);
                            recyclerView.setLayoutManager(li);
                          //  dialog.dismiss();
                            recyclerView.setAdapter(songsListAdapter);
                            progressBar.setVisibility(View.GONE);
                            appBarLayout.setVisibility(View.VISIBLE);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                      //  dialog.dismiss();
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(MovieSongsByWriter.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(MovieSongsByWriter.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(MovieSongsByWriter.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(MovieSongsByWriter.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(MovieSongsByWriter.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
