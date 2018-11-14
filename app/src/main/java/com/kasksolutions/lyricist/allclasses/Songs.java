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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.SongsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Songs extends BaseActivity
{
    ArrayList songsLists;
     RecyclerView recyclerView;
    SongsListAdapter songsListAdapter;
    ImageView iv;
    public String movieId;
    public static String moviename;
    public static String dateyear;
    ProgressBar progressBar;

    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cM.getActiveNetworkInfo();
        if (activeNetworkInfo != null)
        {
        }
        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Internet Connection Required");
            builder.setCancelable(false);
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

        progressBar=findViewById(R.id.spin_kit11);
        recyclerView=findViewById(R.id.recycler_view);
        iv=findViewById(R.id.imageView);
        Toolbar toolbar=findViewById(R.id.toolbar);



        appBarLayout=findViewById(R.id.appbar);

        setSupportActionBar(toolbar);
        songsLists=new ArrayList();
        Intent i=getIntent();
        movieId=i.getStringExtra("idno");
        moviename=i.getStringExtra("moviename");
        dateyear=i.getStringExtra("date123");
        String gettingImageUrl = i.getStringExtra("BitmapImage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);
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
                    collapsingToolbar.setTitle(""+moviename);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        Glide.with(this)
                .load(gettingImageUrl)
                .into(iv);
        songDisplay();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
    protected void songDisplay()
    {
        String url="http://kasksolutions.com:90/LiriceApp/movies/"+movieId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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

                                String lyricid=e.getString("lyricId");
                                String lyricTitle=e.getString("lyricTitle");
                                String movieId=e.getString("movieId");
                                String writerName=e.getString("writerName");

                                HashMap hm=new HashMap();
                                hm.put("lyricid",lyricid);
                                hm.put("lyricTitle",lyricTitle);
                                hm.put("movieId",movieId);
                                hm.put("writerName",writerName);

                                songsLists.add(hm);
                            }

                            songsListAdapter=new SongsListAdapter(Songs.this,songsLists);

                            recyclerView.setHasFixedSize(true);
                         //   RecyclerView.LayoutManager li=new LinearLayoutManager(Songs.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Songs.this));
                            //dialog.dismiss();
                            recyclerView.setAdapter(songsListAdapter);
                            progressBar.setVisibility(View.GONE);
                            appBarLayout.setVisibility(View.VISIBLE);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(Songs.this, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
