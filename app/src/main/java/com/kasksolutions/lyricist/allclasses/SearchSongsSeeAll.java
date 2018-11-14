package com.kasksolutions.lyricist.allclasses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.kasksolutions.lyricist.adapters.TrendingSeeAllRecycle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchSongsSeeAll extends AppCompatActivity
{

    RecyclerView seeAllRcv;
    TrendingSeeAllRecycle mySearchAdapter;

    String search_keys[]={"trendingmovieId","trendingname","lyricId","lyricViews","lyric_name","writerName","year"};
    ArrayList arrayList;
    String data;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_songs_see_all);
        seeAllRcv=findViewById(R.id.searchSeeAllRcv);

        progressBar=findViewById(R.id.spin_kit11);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        data=getIntent().getStringExtra("song");
        arrayList=new ArrayList();
        setTitle("Songs - "+"'"+data+"'");
        songsAll(data);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();

    }

    protected void songsAll(final String key)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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

                                String lyric_title=e.getString("lyric_title");

                                String id=e.getString("id");

                                String movie_id=e.getString("movie_id");

                                String writer_name=e.getString("writer_name");

                                String movie_name=e.getString("movie_name");

                                String movie_release_date=e.getString("movie_release_date");

                                String dateValue=movie_release_date.substring(0,4);
                                HashMap hm=new HashMap();

                                hm.put(search_keys[0],movie_id);
                                hm.put(search_keys[1],movie_name);
                                hm.put(search_keys[2],id);
                                hm.put(search_keys[4],lyric_title);
                                hm.put(search_keys[5],writer_name);
                                hm.put(search_keys[6],dateValue);

                                if ((lyric_title.toLowerCase().contains(key))||(movie_name.toLowerCase().contains(key)))
                                {
                                    if (!arrayList.contains(hm))
                                    {
                                        arrayList.add(hm);
                                        seeAllRcv.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }

                            mySearchAdapter=new TrendingSeeAllRecycle(SearchSongsSeeAll.this,arrayList);
                            seeAllRcv.setHasFixedSize(true);
                            seeAllRcv.setLayoutManager(new LinearLayoutManager(SearchSongsSeeAll.this,LinearLayoutManager.VERTICAL,false));
                            seeAllRcv.setAdapter(mySearchAdapter);
                            mySearchAdapter.notifyDataSetChanged();

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
}
