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
import com.kasksolutions.lyricist.adapters.WritersSeeAllAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchWritersSeeAll extends AppCompatActivity
{

    RecyclerView recyclerView;
    WritersSeeAllAdapter writersSearchAdapter;
    ArrayList arrayList;
    String data;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_writers_see_all);
        recyclerView=findViewById(R.id.searchSeeAllWritersRcv);
        arrayList=new ArrayList();

        progressBar=findViewById(R.id.spin_kit11);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);


        data=getIntent().getStringExtra("song");
        writersAll(data);
        setTitle("Writers - "+"'"+data+"'");
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    protected void writersAll(final String key)
    {
        //creating a request queue
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

                                String writer_name=e.getString("writer_name");

                                String WRITERNAME=writer_name.replace("_"," ");

                                HashMap hm=new HashMap();
                                hm.put("123",WRITERNAME);
                                if ((writer_name.toLowerCase().contains(key)))
                                {
                                    if (!arrayList.contains(hm))
                                    {
                                        arrayList.add(hm);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }

                            writersSearchAdapter=new WritersSeeAllAdapter(SearchWritersSeeAll.this,arrayList);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(SearchWritersSeeAll.this,2));
                            recyclerView.setAdapter(writersSearchAdapter);
                            //recyclerViewWriters.stopScroll();
                            recyclerView.setNestedScrollingEnabled(false);
                            writersSearchAdapter.notifyDataSetChanged();
                            //linearLayout.setVisibility(View.VISIBLE);


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


        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}
