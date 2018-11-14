package com.kasksolutions.lyricist.allclasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.SeeAllAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchMoviesSeeAll extends AppCompatActivity
{

    RecyclerView recyclerView;
    SeeAllAdapter moviesSearchAdapter;
    String search_keys[]={"id","name","date"};
    ArrayList moviesArrayList;
    String data;
    public static String dateValue;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies_see_all);
        recyclerView=findViewById(R.id.searchMoviesSeeAllRcv);
        moviesArrayList=new ArrayList();

        progressBar=findViewById(R.id.spin_kit11);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        data=getIntent().getStringExtra("movieName");
        moviesAll(data);
        setTitle("Movies - "+"'"+data+"'");

    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();

    }

    protected void moviesAll(String key)
    {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ExSearchActivity.url+key,
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
                                String movie_id=e.getString("movie_id");
                                String movie_name=e.getString("movie_name");
                                String movie_release_date=e.getString("movie_release_date");
                                dateValue=movie_release_date.substring(0,4);
                                HashMap hm=new HashMap();
                                hm.put(search_keys[0],movie_id);
                                hm.put(search_keys[1],movie_name);
                                hm.put(search_keys[2],dateValue);

                                if ((movie_name.toLowerCase().contains(data)))
                                {
                                    if (!moviesArrayList.contains(hm))
                                    {
                                        moviesArrayList.add(hm);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }
                            moviesSearchAdapter=new SeeAllAdapter(SearchMoviesSeeAll.this,moviesArrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(SearchMoviesSeeAll.this,2));
                            recyclerView.setAdapter(moviesSearchAdapter);
                            recyclerView.setNestedScrollingEnabled(false);
                            moviesSearchAdapter.notifyDataSetChanged();
                        } catch (JSONException e)
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
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}
