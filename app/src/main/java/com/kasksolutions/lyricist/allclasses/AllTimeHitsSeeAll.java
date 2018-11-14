package com.kasksolutions.lyricist.allclasses;

import android.content.Intent;
import android.content.res.Resources;
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

public class AllTimeHitsSeeAll extends BaseActivity
{
    RecyclerView trendingRecycularView;

    String trendingkeys[]={"trendingmovieId","trendingname","lyricId","lyricViews","lyric_name","writerName","year"};
    ArrayList allTimeAl;

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
        setContentView(R.layout.activity_all_time_hits_see_all);

        appBarLayout=findViewById(R.id.all_time_trend_appbar);
        trendingRecycularView=findViewById(R.id.recycler_view);

        progressBarStyle=findViewById(R.id.spin_kit11);

        //   progressBar=findViewById(R.id.progressBarTrending);

        linearLayout1=findViewById(R.id.all_time_linearLayout);
        linearLayout2=findViewById(R.id.all_time_linearLayoutTrending1);


        linearLayout2.setVisibility(View.GONE);

        linearLayout1.setVisibility(View.GONE);

        // progressBar.setVisibility(View.VISIBLE);

        image1=findViewById(R.id.all_time_shaImage);
        image2=findViewById(R.id.all_time_shaImage1);
        image3=findViewById(R.id.all_time_shaImage2);
        image4=findViewById(R.id.all_time_shaImage3);

        Toolbar toolbar=findViewById(R.id.all_time_trend_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.all_time_collapsing_toolbar);

        collapsingToolbar.setTitle(("All Time Hits"));
        collapsingToolbar.setCollapsedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbar.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        Resources r=getResources();
        AppBarLayout appBarLayout = findViewById(R.id.all_time_trend_appbar);
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
                    collapsingToolbar.setTitle("All Time Hits");

                    isShow = true;
                } else if(isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
        allTimeAl=new ArrayList();
        allTimeHits();
    }

    private void allTimeHits()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/allTimeHits",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);
                                String movieId = e.getString("movie_id");

                                String movieName = e.getString("movieName");

                                String lyricId = e.getString("lyricId");

                                //    String lyricViews = e.getString("lyricViews");

                                String lyric_name = e.getString("lyricTitle");

                                String writerName = e.getString("writerName");

                                String releaseDate = e.getString("releaseDate");

                                String dateAlltime = getDate(Long.parseLong(releaseDate), "yyyy");

                                String writer=writerName.replace("_"," ");

                         //       Log.d("wholedata",""+movieName+"\t"+lyricId+"\t"+lyric_name+"\t"+writerName+"\t"+dateAlltime+"\t"+lyricId);

                                Picasso picasso=Picasso.with(AllTimeHitsSeeAll.this);
                                for(int j=0;j<1;j++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(j);

                                    int idaa = eee.getInt("movie_id");
                                    final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+idaa+"/"+idaa+".jpg";
                                    picasso.load(imageUrl).into(image1);
                                }
                                for(int k=1;k<2;k++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(k);
                                    int idaaa = eee.getInt("movie_id");
                                    final String imageUrl1= "https://s3.amazonaws.com/liriceapp/"+idaaa+"/"+idaaa+".jpg";
                                    picasso.load(imageUrl1).into(image2);
                                }
                                for(int j=2;j<3;j++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(j);
                                    int idaa = eee.getInt("movie_id");
                                    final String imageUrl2= "https://s3.amazonaws.com/liriceapp/"+idaa+"/"+idaa+".jpg";
                                    picasso.load(imageUrl2).into(image3);
                                }
                                for(int j=3;j<4;j++)
                                {
                                    JSONObject eee = jsonArray.getJSONObject(j);
                                    int idaa = eee.getInt("movie_id");
                                    final String imageUrl3= "https://s3.amazonaws.com/liriceapp/"+idaa+"/"+idaa+".jpg";
                                    picasso.load(imageUrl3).into(image4);
                                    linearLayout1.setVisibility(View.VISIBLE);
                                    linearLayout2.setVisibility(View.VISIBLE);
                                }
                                HashMap hm = new HashMap();
                                hm.put(trendingkeys[0], movieId);
                                hm.put(trendingkeys[1], movieName);
                                hm.put(trendingkeys[2], lyricId);
                                hm.put(trendingkeys[4],lyric_name);
                                hm.put(trendingkeys[5],writer);
                                hm.put(trendingkeys[6],dateAlltime);
                                allTimeAl.add(hm);
                                Log.d("wholedata",""+hm);

                            }

                            trendingMyRecycle=new TrendingSeeAllRecycle(AllTimeHitsSeeAll.this,allTimeAl);
                            trendingRecycularView.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllTimeHitsSeeAll.this);
                            trendingRecycularView.setLayoutManager(mLayoutManager);
                            trendingRecycularView.setAdapter(trendingMyRecycle);
                            progressBarStyle.setVisibility(View.GONE);
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
                        if (volleyError instanceof NetworkError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(AllTimeHitsSeeAll.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(AllTimeHitsSeeAll.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ParseError)
                        {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(AllTimeHitsSeeAll.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(AllTimeHitsSeeAll.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof TimeoutError)
                        {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(AllTimeHitsSeeAll.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (3000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
