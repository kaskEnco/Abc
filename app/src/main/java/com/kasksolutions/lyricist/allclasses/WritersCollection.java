package com.kasksolutions.lyricist.allclasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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

public class WritersCollection extends BaseActivity
{

    //Recycular view reference
    RecyclerView recyclerView;


    ArrayList al1;
    Intent intent;
    //ProgressDialog dialog;

    ProgressBar progressBar;

    WritersSeeAllAdapter writersSeeAllAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writers_collection);
        recyclerView=findViewById(R.id.writersCollRcv);

        progressBar=findViewById(R.id.spin_kit11);
        al1=new ArrayList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        writer();

      //  new MyJson().execute();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();;
        return super.onSupportNavigateUp();

    }

    private void writer()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
       // StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://192.168.0.5:8080/LiriceApp/writer",
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/writer",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("writers");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                String e=jsonArray.getString(i);
                                String rep=e.replace("_"," ");
                                HashMap hm=new HashMap();

                                hm.put("123", rep);

                                al1.add(hm);
                                //   Collections.sort(writersAl);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        writersSeeAllAdapter=new WritersSeeAllAdapter(WritersCollection.this,al1);
                        recyclerView.setHasFixedSize(true);

                        //Recyclular view elements displaying vertically

                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);

                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        recyclerView.setAdapter(writersSeeAllAdapter);
                        progressBar.setVisibility(View.GONE);
                      //  dialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });
        requestQueue.add(stringRequest);

    }


}
