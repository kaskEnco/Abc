package com.kasksolutions.lyricist.allclasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.SingleYearAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class SingleYear extends BaseActivity {
    //Recycular view reference
    RecyclerView singleYearRecycleView;

    String yearKeys[] = {"YearmovieId", "yearMovieName", "year"};


    ArrayList singleYearAl;
    Intent intent;
    SingleYearAdapter singleYearAdapter;

    String year;

    public static String writerName;
    int yy;

    ProgressBar progressBar;

    //ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_year);

        progressBar=findViewById(R.id.spin_kit11);

        singleYearRecycleView = findViewById(R.id.yearsrecv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent=getIntent();
        yy=intent.getIntExtra("yearOnly",0);

        this.setTitle(""+yy);
        singleYearAl = new ArrayList();
        singleYear();
    }
    protected void singleYear()
    {
        //StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.5:8080/LiriceApp/year/"+yy,

                StringRequest stringRequest = new StringRequest(Request.Method.GET,
                        "http://kasksolutions.com:90/LiriceApp/year/"+yy,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try {

                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() == 0)
                            {
                                Toast.makeText(SingleYear.this, "No Movies Found", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            else
                                {


                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject e = jsonArray.getJSONObject(i);

                                    //Reading ID from url

                                    String movieid = e.getString("movieId");

                                    String movieName = e.getString("movieName");

                                    String date = e.getString("movieReleaseDate");

                                    year = String.valueOf(yy);

                                    year = getDate(Long.parseLong(date), "yyyy");

                                    //create Hashmap obcject

                                    HashMap hm = new HashMap();
                                    hm.put(yearKeys[0], movieid);
                                    hm.put(yearKeys[1], movieName);

                                    hm.put(yearKeys[2], year);

                                    singleYearAl.add(hm);
                                }

                                singleYearAdapter = new SingleYearAdapter(singleYearAl, SingleYear.this);
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                                singleYearRecycleView.setLayoutManager(mLayoutManager);
                                singleYearRecycleView.setItemAnimator(new DefaultItemAnimator());

                                singleYearRecycleView.setAdapter(singleYearAdapter);
                                progressBar.setVisibility(View.GONE);

                            }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
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

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
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


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

