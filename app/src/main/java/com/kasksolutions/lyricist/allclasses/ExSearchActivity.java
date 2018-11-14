package com.kasksolutions.lyricist.allclasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.kasksolutions.lyricist.adapters.MoviesSearchAdapter;
import com.kasksolutions.lyricist.adapters.MySearchAdapter;
import com.kasksolutions.lyricist.adapters.WritersSearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ExSearchActivity extends AppCompatActivity
{
    MySearchAdapter mySearchAdapter;

    MoviesSearchAdapter moviesSearchAdapter;

    WritersSearchAdapter writersSearchAdapter;

    ArrayList songsAl,moviesAl,writersAl;

    RecyclerView recyclerViewSongs,recyclerViewMovies,recyclerViewWriters;

    String search_keys[]={"lyric_title","id","movie_id","writer_name","movie_release_date","movie_name"};

  //  public static String dateValue;

    String writer_name;

    TextView songSeeAll,moviesSeeAll,writersSeeAll;

    ArrayList searchList;

    String message=null;

    String lyric_title="";

    RelativeLayout progressBarRL,seachMainPageRl;

    LinearLayout searchImageRL;

    public static String url="http://kasksolutions.com:90/LiriceApp/search/";

    LinearLayout songsLinearLayout,moviesLinearLayout,writersLinearLayout;

    ProgressBar progressBar;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_search);

        searchView=findViewById(R.id.sea);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));


        recyclerViewSongs=findViewById(R.id.searchSongsRecyclerView);

        recyclerViewMovies=findViewById(R.id.searchMovieRecyclerView);

        recyclerViewWriters=findViewById(R.id.serachWriterRecyclerView);

        searchImageRL=findViewById(R.id.searchImageRl);
        progressBarRL=findViewById(R.id.progressBarRL);
        seachMainPageRl=findViewById(R.id.searchMainPageRL);

        songSeeAll=findViewById(R.id.songsSeeAll);
        moviesSeeAll=findViewById(R.id.moviesSeeAll);
        writersSeeAll=findViewById(R.id.writersSeeAll);

        songsLinearLayout=findViewById(R.id.songsLinearL);
        moviesLinearLayout=findViewById(R.id.moviesLinearL);
        writersLinearLayout=findViewById(R.id.writersLinearL);

        progressBar=findViewById(R.id.search_progressBar);
        progressBarRL.setVisibility(View.GONE);
        seachMainPageRl.setVisibility(View.GONE);

        moviesLinearLayout.setVisibility(View.GONE);
        songsLinearLayout.setVisibility(View.GONE);
        writersLinearLayout.setVisibility(View.GONE);

        setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        songsAl=new ArrayList();

        moviesAl=new ArrayList();

        writersAl=new ArrayList();

        searchList=new ArrayList();

        songsAl.clear();

    //    if(p)



        searchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchView.onActionViewExpanded();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            searchView.setRevealOnFocusHint(true);
        }
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setQueryHint("Search songs,movies,writers...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                query=query.trim().toLowerCase();
                latestAll(query);
                movies(query);
                writers(query);
                searchView.clearFocus();
                seachMainPageRl.setVisibility(View.VISIBLE);
                searchImageRL.setVisibility(View.GONE);
//                if (searchImageRL.getVisibility()==View.VISIBLE)
//                {
//                    seachMainPageRl.setVisibility(View.GONE);
//                    Toast.makeText(ExSearchActivity.this, "Go", Toast.LENGTH_SHORT).show();
//                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {

                newText.trim().toLowerCase();
                if (newText.length()>2)
                {
                    latestAll(newText);
                    movies(newText);
                    writers(newText);
                    seachMainPageRl.setVisibility(View.VISIBLE);
                    searchImageRL.setVisibility(View.GONE);
                }
                else if (newText.length()==0)
                {
                    seachMainPageRl.setVisibility(View.GONE);
                    songsLinearLayout.setVisibility(View.GONE);
                    moviesLinearLayout.setVisibility(View.GONE);
                    writersLinearLayout.setVisibility(View.GONE);
                    searchImageRL.setVisibility(View.VISIBLE);
                    progressBarRL.setVisibility(View.GONE);
                    //isNotShowing();
                }
                return false;
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.final_search, menu);
//        MenuItem menuItem = menu.findItem(R.id.searchMenu);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        menuItem.expandActionView();
//        searchView.setQueryHint("Search songs,movies,writers...");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//                query=query.trim().toLowerCase();
//                latestAll(query);
//                movies(query);
//                writers(query);
//                searchView.clearFocus();
//                seachMainPageRl.setVisibility(View.VISIBLE);
//                searchImageRL.setVisibility(View.GONE);
//                return false;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                newText.trim().toLowerCase();
//                if (newText.length()>2)
//                {
//                    latestAll(newText);
//                    movies(newText);
//                    writers(newText);
//                    seachMainPageRl.setVisibility(View.VISIBLE);
//                    searchImageRL.setVisibility(View.GONE);
//                   // showRetry(7000,progressBar);
//                }
//                else if (newText.length()==0)
//                {
//                    seachMainPageRl.setVisibility(View.GONE);
//                    songsLinearLayout.setVisibility(View.GONE);
//                    moviesLinearLayout.setVisibility(View.GONE);
//                    writersLinearLayout.setVisibility(View.GONE);
//                    searchImageRL.setVisibility(View.VISIBLE);
//                    isNotShowing();
//                }
//                return false;
//            }
//        });
//        return  true;
//    }

    protected void latestAll(final String key)
    {
        isShowing();
        moviesLinearLayout.setVisibility(View.GONE);
        songsLinearLayout.setVisibility(View.GONE);
        writersLinearLayout.setVisibility(View.GONE);
        songsAl.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+key,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            final JSONArray jsonArray=new JSONArray(response);

                            if (jsonArray.length()==0)
                            {
                                Toast.makeText(ExSearchActivity.this, "Make sure enter correct query", Toast.LENGTH_SHORT).show();
                                progressBarRL.setVisibility(View.GONE);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                searchImageRL.setVisibility(View.VISIBLE);
                                seachMainPageRl.setVisibility(View.GONE);
                            }
                            else
                                {
                                    searchImageRL.setVisibility(View.GONE);
                                    progressBarRL.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jsonArray.length(); i++)
                                    {
                                        songSeeAll.setOnClickListener(new View.OnClickListener()
                                        {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            Intent intent = new Intent(ExSearchActivity.this, SearchSongsSeeAll.class);
                                            intent.putExtra("song", key);
                                            startActivity(intent);
                                        }
                                    });
                                    JSONObject e = jsonArray.getJSONObject(i);
                                    lyric_title = e.getString("lyric_title");
                                    String id = e.getString("id");

                                    int movie_id = e.getInt("movie_id");

                                    writer_name = e.getString("writer_name");

                                    String movie_name = e.getString("movie_name");

                                    String movie_release_date = e.getString("movie_release_date");

                                    String dateValue = movie_release_date.substring(0, 4);

                                    String WRITERNAME = writer_name.replace("_", " ");

                                    HashMap hm = new HashMap();
                                    hm.put(search_keys[0], lyric_title);
                                    hm.put(search_keys[1], id);
                                    hm.put(search_keys[2], movie_id);

                                    hm.put(search_keys[3], WRITERNAME);
                                    hm.put(search_keys[4], dateValue);
                                    hm.put(search_keys[5], movie_name);
                                    if ((lyric_title.toLowerCase().contains(key)) || (movie_name.toLowerCase().contains(key)))
                                    {
                                        if (!songsAl.contains(hm))
                                        {
                                            songsAl.add(hm);
                                            songsLinearLayout.setVisibility(View.VISIBLE);
                                            progressBarRL.setVisibility(View.GONE);
                                            //isNotShowing();
                                        }
                                    }

                                }
                            }

                            mySearchAdapter=new MySearchAdapter(ExSearchActivity.this,songsAl);
                            recyclerViewSongs.setHasFixedSize(true);

                            recyclerViewSongs.setLayoutManager(new LinearLayoutManager(ExSearchActivity.this,LinearLayoutManager.VERTICAL,false));

                            recyclerViewSongs.setAdapter(mySearchAdapter);

                            mySearchAdapter.notifyDataSetChanged();

                            recyclerViewSongs.setNestedScrollingEnabled(false);

                            progressBarRL.setVisibility(View.GONE);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(ExSearchActivity.this, "123", Toast.LENGTH_SHORT).show();
                        }
                        if (response==null)
                        {
                            Toast.makeText(ExSearchActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        progressBarRL.setVisibility(View.GONE);
                        //  progressBar.setVisibility(View.GONE);
                        if (volleyError instanceof NetworkError)
                        {
                            message = "Sorry no data found";
                            Toast.makeText(ExSearchActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                            Toast.makeText(ExSearchActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(ExSearchActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(ExSearchActivity.this, message, Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder adb=new AlertDialog.Builder(ExSearchActivity.this);
                            View view= LayoutInflater.from(ExSearchActivity.this).inflate(R.layout.retry_button,null,false);
                            adb.setView(view);
                            adb.setCancelable(false);
                            final AlertDialog alertDialog=adb.create();
                            Button button=view.findViewById(R.id.button2);
                            button.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    Intent intent = getIntent();
                                    overridePendingTransition(0, 0);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(ExSearchActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        // dismissProgressDialog();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
    protected void movies(final String key)
    {

        isShowing();
        moviesLinearLayout.setVisibility(View.GONE);
        songsLinearLayout.setVisibility(View.GONE);
        writersLinearLayout.setVisibility(View.GONE);
        moviesAl.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+key,
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
                                moviesSeeAll.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent intent=new Intent(ExSearchActivity.this,SearchMoviesSeeAll.class);
                                        intent.putExtra("movieName",key);
                                        startActivity(intent);
                                    }
                                });
                                JSONObject e = jsonArray.getJSONObject(i);
                                int movie_id=e.getInt("movie_id");
                                String movie_name=e.getString("movie_name");
                                String movie_release_date=e.getString("movie_release_date");
                                String dateValue=movie_release_date.substring(0,4);

                                HashMap hm=new HashMap();
                                hm.put(search_keys[2],movie_id);
                                hm.put(search_keys[4],dateValue);
                                hm.put(search_keys[5],movie_name);
                                //movie_name.contains(key);

                                if ((movie_name.toLowerCase().contains(key)))
                                {
                                    if (!moviesAl.contains(hm))
                                    {
                                        moviesAl.add(hm);
                                        moviesLinearLayout.setVisibility(View.VISIBLE);
                                        progressBarRL.setVisibility(View.GONE);
                                      //  isNotShowing();
                                      //  progressBarRL.setVisibility(View.GONE);
                                    }
                                }
                            }
                            moviesSearchAdapter=new MoviesSearchAdapter(ExSearchActivity.this,moviesAl);
                            recyclerViewMovies.setHasFixedSize(true);
                            recyclerViewMovies.setLayoutManager(new LinearLayoutManager(ExSearchActivity.this,LinearLayoutManager.VERTICAL,false));
                            recyclerViewMovies.setAdapter(moviesSearchAdapter);
                            recyclerViewMovies.setNestedScrollingEnabled(false);
                            moviesSearchAdapter.notifyDataSetChanged();
                            progressBarRL.setVisibility(View.GONE);
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
    protected void writers(final String key)
    {
        isShowing();
        moviesLinearLayout.setVisibility(View.GONE);
        songsLinearLayout.setVisibility(View.GONE);
        writersLinearLayout.setVisibility(View.GONE);
        writersAl.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+key,
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

                                writersSeeAll.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        Intent intent=new Intent(ExSearchActivity.this,SearchWritersSeeAll.class);
                                        intent.putExtra("song",key);
                                        startActivity(intent);
                                    }
                                });

                                JSONObject e = jsonArray.getJSONObject(i);

                                writer_name=e.getString("writer_name");

                                String witer=writer_name.replace("_"," ");

                                HashMap hm=new HashMap();

                                hm.put("100",witer);

                                if ((witer.toLowerCase().contains(key)))
                                {
                                    if (!writersAl.contains(hm))
                                    {
                                        writersAl.add(hm);
                                        writersLinearLayout.setVisibility(View.VISIBLE);
                                        progressBarRL.setVisibility(View.GONE);
                                        //isNotShowing();

                                    }
                                }
                            }
                            writersSearchAdapter=new WritersSearchAdapter(ExSearchActivity.this,writersAl);
                            recyclerViewWriters.setHasFixedSize(true);
                            recyclerViewWriters.setLayoutManager(new LinearLayoutManager(ExSearchActivity.this,LinearLayoutManager.VERTICAL,false));
                            recyclerViewWriters.setAdapter(writersSearchAdapter);
                            recyclerViewWriters.setNestedScrollingEnabled(false);
                            writersSearchAdapter.notifyDataSetChanged();
                            progressBarRL.setVisibility(View.GONE);



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
    @Override
    public boolean onSupportNavigateUp()
    {

        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(R.anim.one, R.anim.two);
        super.onBackPressed();
    }
    private void isShowing()
    {
        if (progressBar == null)
        {
            songsLinearLayout.setVisibility(View.GONE);
            moviesLinearLayout.setVisibility(View.GONE);
            writersLinearLayout.setVisibility(View.GONE);
        }
        progressBarRL.setVisibility(View.VISIBLE);
    }
//    private void isNotShowing()
//    {
//        if (progressBar != null)
//        {
//            progressBarRL.setVisibility(View.GONE);
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100)
        {
            new Intent(ExSearchActivity.this,MainActivity.class);
        }
    }

    public void showRetry(long time, final ProgressBar progressBar)
    {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ExSearchActivity.this, "prograss bar invisible", Toast.LENGTH_SHORT).show();
            }
        },time);
    }
}
