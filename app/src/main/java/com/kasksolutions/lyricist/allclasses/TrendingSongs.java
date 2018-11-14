package com.kasksolutions.lyricist.allclasses;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.TrendingSeeAllRecycle;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TrendingSongs extends BaseActivity
{
    RecyclerView trendingRecycularView;

    String trendingkeys[]={"trendingmovieId","trendingname","lyricId","lyricViews","lyric_name","writerName","year"};
    ArrayList trendingAl;

    TrendingSeeAllRecycle trendingMyRecycle;
    Intent intent;
    ImageView image1,image2,image3,image4;
    String year;
    public static String writerName;

    String movieId;
    String message = null;

    ProgressBar progressBarStyle;

    LinearLayout linearLayout1,linearLayout2;
    AppBarLayout appBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_songs);


        appBarLayout=findViewById(R.id.trend_appbar);

        trendingRecycularView=findViewById(R.id.recycler_view);

        progressBarStyle=findViewById(R.id.spin_kit11);

        linearLayout1=findViewById(R.id.linearLayoutTrending);
        linearLayout2=findViewById(R.id.linearLayoutTrending1);
        linearLayout2.setVisibility(View.GONE);

        linearLayout1.setVisibility(View.GONE);

        image1=findViewById(R.id.shaImage);
        image2=findViewById(R.id.shaImage1);
        image3=findViewById(R.id.shaImage2);
        image4=findViewById(R.id.shaImage3);
        Toolbar toolbar=findViewById(R.id.trend_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.trend_collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.trending));
        collapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbar.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        AppBarLayout appBarLayout = findViewById(R.id.trend_appbar);
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
                    collapsingToolbar.setTitle("TRENDING SONGS");

                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        trendingAl=new ArrayList();
        trending();
    }

    private void trending()
    {
       // StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.3:8080/LiriceApp/trendingAll",
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/trendingAll",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {

                            JSONArray jsonArray=new JSONArray(response);

                            //now looping through all the elements of the json array
                            for(int i=0;i<50;i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);

                                //Reading ID from url

                                movieId= e.getString("movieId");

                              //  int id=e.getInt("movieId");

                                String movieName = e.getString("movieName");

                                String lyricId = e.getString("lyricId");

                                String lyricViews = e.getString("lyricViews");

                                String lyric_name = e.getString("lyric_name");

                                String writerName = e.getString("writerName");

                                String date=e.getString("releaseDate");

                                year=getDate(Long.parseLong(date), "yyyy");

                                String writer=writerName.replace("_"," ");



                                Picasso picasso=Picasso.with(TrendingSongs.this);



                                for(int j=0;j<1;j++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(j);

                                    int idaa = eee.getInt("movieId");
                                    final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+idaa+"/"+idaa+".jpg";
                                    picasso.load(imageUrl).into(image1);
                                }
                                for(int k=1;k<2;k++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(k);
                                    int idaaa = eee.getInt("movieId");
                                    final String imageUrl1= "https://s3.amazonaws.com/liriceapp/"+idaaa+"/"+idaaa+".jpg";
                                    picasso.load(imageUrl1).into(image2);
                                }
                                for(int j=2;j<3;j++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(j);
                                    int idaa = eee.getInt("movieId");
                                    final String imageUrl2= "https://s3.amazonaws.com/liriceapp/"+idaa+"/"+idaa+".jpg";
                                    picasso.load(imageUrl2).into(image3);
                                }
                                for(int j=3;j<4;j++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(j);

                                    int idaa = eee.getInt("movieId");
                                    final String imageUrl3= "https://s3.amazonaws.com/liriceapp/"+idaa+"/"+idaa+".jpg";
                                    picasso.load(imageUrl3).into(image4);
                                   // progressBarStyle.setVisibility(View.GONE);
                                    linearLayout1.setVisibility(View.VISIBLE);
                                    linearLayout2.setVisibility(View.VISIBLE);
                                }



                                //create Hashmap obcject

                                HashMap hm = new HashMap();
                                hm.put(trendingkeys[0], movieId);
                                hm.put(trendingkeys[1], movieName);
                                hm.put(trendingkeys[2], lyricId);

                                hm.put(trendingkeys[3],lyricViews);
                                hm.put(trendingkeys[4],lyric_name);
                                hm.put(trendingkeys[5],writer);
                                hm.put(trendingkeys[6],year);

                                trendingAl.add(hm);
                                Log.d("shamu","mOvieName:"+movieName+"\nlyricsId:"+lyricId);
                            }

                            trendingMyRecycle=new TrendingSeeAllRecycle(TrendingSongs.this,trendingAl);
                            trendingRecycularView.setHasFixedSize(true);
                            //trendingRecycularView.setLayoutManager(new GridLayoutManager(TrendingSongs.this,3));
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrendingSongs.this);

                            trendingRecycularView.setLayoutManager(mLayoutManager);
                           //dialog.dismiss();

                            trendingRecycularView.setAdapter(trendingMyRecycle);
                            progressBarStyle.setVisibility(View.GONE);
                            appBarLayout.setVisibility(View.VISIBLE);
                            //relativeBgColor.setVisibility(View.VISIBLE);


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

                        //dialog.dismiss();
                        if (volleyError instanceof NetworkError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(TrendingSongs.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(TrendingSongs.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ParseError)
                        {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(TrendingSongs.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(TrendingSongs.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof TimeoutError)
                        {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(TrendingSongs.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (3000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
