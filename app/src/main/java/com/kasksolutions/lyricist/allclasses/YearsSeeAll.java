package com.kasksolutions.lyricist.allclasses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.YearsSeeAllAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class YearsSeeAll extends BaseActivity
{
    String yearsKeys[]={"yearId","lyircYear"};
    ArrayList yearsAl;
    YearsSeeAllAdapter yearsSeeAllAdapter;

    RecyclerView yearsRCV;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_years_see_all);
        yearsRCV=findViewById(R.id.yearsSeeAllRCV);
        progressBar=findViewById(R.id.spin_kit11);
        yearsAl=new ArrayList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        new YearsJson().execute();
    }
    public class YearsJson extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
          // dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                //URL for latest movies
                URL url = new URL("http://kasksolutions.com:90/LiriceApp/years");

               // URL url = new URL("http://192.168.0.5:8080/LiriceApp/years");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                JSONArray jsonArray = new JSONArray(br.readLine());

                for (int i=jsonArray.length()-1;i>=0;i--)
                {
                    JSONObject e = jsonArray.getJSONObject(i);

                    int yearsId=e.getInt("yearId");
                    int lyricYears=e.getInt("lyircYear");

                    HashMap hm = new HashMap();
                    if(lyricYears<=(2018))
                    {
                        hm.put(yearsKeys[0], yearsId);

                        hm.put(yearsKeys[1], lyricYears);

                        yearsAl.add(hm);
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);

            yearsSeeAllAdapter = new YearsSeeAllAdapter(yearsAl,YearsSeeAll.this);
            yearsRCV.setHasFixedSize(true);
            yearsRCV.setLayoutManager(new GridLayoutManager(YearsSeeAll.this,2));

            yearsRCV.setAdapter(yearsSeeAllAdapter);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;

    }
}
