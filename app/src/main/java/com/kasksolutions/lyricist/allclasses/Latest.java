package com.kasksolutions.lyricist.allclasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.SeeAllAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Latest extends BaseActivity
{

    String ketys[]={"id","name","date"};
    //Recycular view reference
    RecyclerView recyclerView;
    ArrayList al1;
    Intent intent;
  //  ProgressBar progressBar;
    SeeAllAdapter myRecycle;
    private ShimmerFrameLayout mShimmerViewContainer;

    String message=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);
        recyclerView=findViewById(R.id.recv);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        //progressBar = findViewById(R.id.spin_kit11);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        intent=new Intent();

        al1=new ArrayList();
        latestAll();

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
    //Status bar onclick

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;

    }

    protected void latestAll()
    {

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.5:8080/LiriceApp/latestMoviesAll",

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://kasksolutions.com:90/LiriceApp/latestMoviesAll",
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
                                String id=e.getString("movieId");

                                String name=e.getString("movieName");

                                String tim=e.getString("movieReleaseDate");

                                MainActivity.date=getDate(Long.parseLong(tim),"yyyy");

                                //

                                HashMap hm=new HashMap();
                                hm.put(ketys[0],id);
                                hm.put(ketys[1],name);
                               // hm.put(ketys[2], MainActivity.date);
                                hm.put(ketys[2], MainActivity.date);
                                al1.add(hm);
                            }

                            myRecycle=new SeeAllAdapter(Latest.this,al1);
                            recyclerView.setHasFixedSize(true);

                            //Recyclular view elements displaying vertically

                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(myRecycle);
                            mShimmerViewContainer.setVisibility(View.GONE);
                           // dialog.dismiss();


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
                            Toast.makeText(Latest.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(Latest.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ParseError)
                        {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(Latest.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(Latest.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof TimeoutError)
                        {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(Latest.this, message, Toast.LENGTH_SHORT).show();
                        }
                        //progressBar.setVisibility(View.GONE);
                     //  dialog.dismiss();
                        //dismissProgressDialog();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


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
}