package com.kasksolutions.lyricist.allclasses;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.adapters.RecentAdapter;
import com.kasksolutions.lyricist.adapters.TrendingMyRecycle;
import com.kasksolutions.lyricist.adapters.WriterRecycleAdapter;
import com.kasksolutions.lyricist.adapters.YearsRecAdapter;
import com.kasksolutions.lyricist.databse.MyDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    String ketys[] = {"id", "name", "date"};  //used for Hash Map

    String writkey[] = {"writer"};

    String trendingkeys[] = {"trendingmovieId", "trendingname", "lyricId", "lyricViews", "lyric_name", "writerName", "releaseDate"};

    String recentkeys[] = {"MovieId", "MovieName", "MovieImage", "LyricName", "writerName", "yearDate"};

    RecyclerView recyclerView, trendingRecycularView, yearsRecycularView, writersRecycularView, recentRecyclerView,allTimeHitsRecyclerView;//Declare RecycularView Reference variable

    ArrayList recentAll;

    ArrayList al1, trendingAl, yearsAl, writersAl,allTimeAl;

    MyRecycle myRecycle;//Adapter class reference

    RecentAdapter recentAdapter;

    TrendingMyRecycle trendingMyRecycle;

    WriterRecycleAdapter writerRecycleAdapter;

    YearsRecAdapter yearsRecAdapter;

    Intent intent;

    ProgressDialog pDialog;

    static String date;

    String trendingDate;
    String message = null;
    android.support.v7.widget.SearchView searchView = null;
    String yearsKeys[] = {"yearId", "lyircYear"};

    public static boolean videoCk;

    LinearLayout trendingLL, latestLL, writersLL, yearLL, recentLL,allTimeLL;

    String lll;

    String todayDate;

    public static String android_id;

    //public static String newToken;

   //TextView trendingSeeAll,latestSeeAll;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        FirebaseApp.initializeApp(this);


        //dismissProgressDialog();

        getIntent().getLongExtra("time",1);

        lll=getIntent().getStringExtra("time");

        trendingLL = findViewById(R.id.fisrtLLTrending);
        latestLL = findViewById(R.id.fisrtLLLatest);
        writersLL = findViewById(R.id.fisrtLLWriter);
        yearLL = findViewById(R.id.yearLL);
        recentLL = findViewById(R.id.recentLL);
        allTimeLL=findViewById(R.id.allTimeLL);
        latestLL.setVisibility(View.GONE);
        trendingLL.setVisibility(View.GONE);
        writersLL.setVisibility(View.GONE);
        yearLL.setVisibility(View.GONE);
        allTimeLL.setVisibility(View.GONE);

       // latestSeeAll=findViewById(R.id.latestSeeAll);
        //trendingSeeAll=findViewById(R.id.trendingSeeAll);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//        {
//            // If we're running on Honeycomb or newer, then we can use the Theme's
//            // selectableItemBackground to ensure that the View has a pressed state
//            TypedValue outValue = new TypedValue();
//            this.getTheme().resolveAttribute(R.drawable.ripple_boarder, outValue, true);
//            latestSeeAll.setBackgroundResource(outValue.resourceId);
//        }
//        else
//        {
//
//        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("123", false))
        {
            startService(new Intent(MainActivity.this,MyService.class));
            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("123", true);
            sendNotification();
           // makeJsonObjReq();
            editor.commit();
         //   Toast.makeText(this, "device Id send", Toast.LENGTH_SHORT).show();
        }




        recyclerView = findViewById(R.id.recyclerView);

        trendingRecycularView = findViewById(R.id.trendingrecyclerView);

        writersRecycularView = findViewById(R.id.writersrecyclerView);

        recentRecyclerView = findViewById(R.id.recentRecyclerView);

        yearsRecycularView = findViewById(R.id.yearRecyclerView);

        allTimeHitsRecyclerView=findViewById(R.id.allTimeRecyclerView);

        recentAll = new ArrayList();
        yearsAl = new ArrayList();
        allTimeAl=new ArrayList();

        intent = new Intent();

        al1 = new ArrayList();
        trendingAl = new ArrayList();
        yearsAl = new ArrayList();
        writersAl = new ArrayList();

        trending();
        latestMovies();
        writerJson();
        yearJson();
        allTimeHits();
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recentRecyclerView);

        MyDb myDb = new MyDb(this);

        SQLiteDatabase sql = myDb.getReadableDatabase();

        Cursor cursor=sql.query(MyDb.TABLENAME,null,null,null,null,null,null);
        if(cursor.getCount()>0)
        {
            if (pDialog.isShowing())
            {
                recentLL.setVisibility(View.GONE);

            }
            else
            {
                recentLL.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            recentLL.setVisibility(View.GONE);
        }
        //sendNotification();
        //makeJsonObjReq();
        //Log.d("refes100",""+)
     //   Toast.makeText(this, ""+ FireBaseInstanceIDService.refreshedToken, Toast.LENGTH_SHORT).show();

        //Log.d("data1741",""+ FirebaseInstanceId.getInstance().getToken());
//
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,
//                new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                newToken = instanceIdResult.getToken();
//                Log.e("newToken",newToken);
//
//            }
//        });
    }
    public void sendNotification()
    {
     //   try
      //  {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                    new OnSuccessListener<InstanceIdResult>()
                    {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult)
                        {

                            String newToken=instanceIdResult.getToken();
                            try
                            {
                                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                                String URL = "http://kasksolutions.com:90/LiriceApp/deviceId";
                                String localURL="http://192.168.0.3:8080/LiriceApp/deviceId";
                                JSONObject jsonBody = new JSONObject();
                                jsonBody.put("id", ""+android_id);
                                jsonBody.put("fcmToken", ""+newToken);
                                Log.e("newToken","Token Number"+newToken);
                                Log.e("newToken","id Number"+android_id);
                                final String requestBody = jsonBody.toString();

                                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                URL,
                                new Response.Listener<String>()
                                {
                                @Override
                                    public void onResponse(String response)
                                    {
                                        Log.d("VOLLEY1100", response);
                                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                     }
                            }, new Response.ErrorListener()
                            {
                             @Override
                             public void onErrorResponse(VolleyError error)
                             {
                                Log.d("VOLLEY", error.toString());
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                    }) {
                         @Override
                         public String getBodyContentType()
                         {
                             return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError
                        {
                            try
                            {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            }
                            catch (UnsupportedEncodingException uee)
                            {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response)
                        {
                             if (response.statusCode == 200)
                            {

                                //do smth
                            } else if (response.statusCode == 401)
                            {
                                //do smth else
                            }
                            return super.parseNetworkResponse(response);
                         }

                  };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                        e.printStackTrace();
                     }
                        }
                    });
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String URL = "http://kasksolutions.com:90/LiriceApp/deviceId";
//            String localURL="http://192.168.0.3:8080/LiriceApp/deviceId";
//            JSONObject jsonBody = new JSONObject();
//            jsonBody.put("id", ""+android_id);
//            jsonBody.put("fcmToken", ""+newToken);
//            Log.e("newToken","Token Number"+newToken);
//            Log.e("newToken","id Number"+android_id);
//            final String requestBody = jsonBody.toString();
//
//            StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                    localURL,
//                    new Response.Listener<String>()
//                    {
//                        @Override
//                        public void onResponse(String response)
//                        {
//                            Log.d("VOLLEY1100", response);
//                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                        }
//                    }, new Response.ErrorListener()
//            {
//                @Override
//                public void onErrorResponse(VolleyError error)
//                {
//                    Log.d("VOLLEY", error.toString());
//                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return requestBody == null ? null : requestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    if (response.statusCode == 200)
//                    {
//
//                        //do smth
//                    } else if (response.statusCode == 401)
//                    {
//                        //do smth else
//                    }
//                    return super.parseNetworkResponse(response);
//                }
//
//            };
//
//            requestQueue.add(stringRequest);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
    private void makeJsonObjReq()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("id", "100");

        //http://kasksolutions.com:8080/LiriceApp/mail
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "http://kasksolutions.com:90/LiriceApp/deviceId", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Log.d("tag123", response.toString());
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d("tag123", "Error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cM.getActiveNetworkInfo();

        if (activeNetworkInfo != null)
        {
            recentLL.setVisibility(View.GONE);
        }
        else
        {

            recentLL.setVisibility(View.GONE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet Connection Required");
            builder.setCancelable(false);

            builder.setPositiveButton("Back to Online", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    // finish();
                    finishAffinity();

                }
            });
            builder.show();
        }
    }


    @Override
    protected void onResume()
    {
        //https://stackoverflow.com/questions/41305148/how-to-stop-the-items-duplication-in-recyclerview-android

        super.onResume();
        recentAll.clear();

        MyDb myDb = new MyDb(this);

        SQLiteDatabase sql = myDb.getReadableDatabase();

        Cursor c = sql.rawQuery("select * from " + MyDb.TABLENAME, null);

        boolean res = c.moveToLast();

        if (res)
        {
            if (pDialog.isShowing())
            {
                recentLL.setVisibility(View.GONE);

            }
            else
            {
                 recentLL.setVisibility(View.VISIBLE);
            }
            do {
                String movie_id = c.getString(0);
                String movie_name = c.getString(1);
                String lyric_id = c.getString(2);
                String lyric_name = c.getString(3);
                String writer_Name = c.getString(4);
                String date = c.getString(5);

                HashMap hm = new HashMap();
                hm.put(recentkeys[0], movie_id);
                hm.put(recentkeys[1], movie_name);
                hm.put(recentkeys[2], lyric_id);
                hm.put(recentkeys[3], lyric_name);
                hm.put(recentkeys[4], writer_Name);
                hm.put(recentkeys[5], date);

                recentAll.add(hm);

            }
            while (c.moveToPrevious());
        }
        else
        {
            recentLL.setVisibility(View.GONE);
        }
        if (pDialog.isShowing())
        {
            recentLL.setVisibility(View.GONE);
        }
        else
        {
            Cursor cursor=sql.query(MyDb.TABLENAME,null,null,null,null,null,null);
            if(cursor.getCount()>0)
            {
                recentLL.setVisibility(View.VISIBLE);
            }
            else
            {
                recentLL.setVisibility(View.GONE);
            }
        }
        recentAdapter = new RecentAdapter(this, recentAll);

        recentRecyclerView.setHasFixedSize(true);
        recentRecyclerView.setLayoutManager(new CenterZoomLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recentRecyclerView.setAdapter(recentAdapter);
    }


    private void latestMovies()
    {
        showProgressDialog();
        //StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.5:8080/LiriceApp/latestMovies",
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://kasksolutions.com:90/LiriceApp/latestMovies",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);
                                String id = e.getString("movieId");
                                String name = e.getString("movieName");
                                String tim = e.getString("movieReleaseDate");
                                date = getDate(Long.parseLong(tim), "yyyy");

                                HashMap hm = new HashMap();
                                hm.put(ketys[0], id);
                                hm.put(ketys[1], name);
                                hm.put(ketys[2], date);
                                al1.add(hm);
                                //dismissProgressDialog();
                            }

                            myRecycle = new MyRecycle(MainActivity.this, al1);
                            recyclerView.setHasFixedSize(true);

                            recyclerView.setLayoutManager(new CenterZoomLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            SnapHelper snapHelper = new LinearSnapHelper();
                            snapHelper.attachToRecyclerView(recyclerView);
                            recyclerView.setAdapter(myRecycle);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                       //dismissProgressDialog();
                    }

                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void trending()
    {
        showProgressDialog();
      ///  StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.6:8080/LiriceApp/allTimeHits",
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/trending",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try
                        {

                            JSONArray jsonArray = new JSONArray(response);

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);

                                //Reading ID from url

                                String id = e.getString("movieId");

                                String name = e.getString("movieName");

                                String lyricId = e.getString("lyricId");

                                String lyricViews = e.getString("lyricViews");

                                String lyric_name = e.getString("lyric_name");

                                String writerName = e.getString("writerName");

                                String releaseDate = e.getString("releaseDate");

                                trendingDate = getDate(Long.parseLong(releaseDate), "yyyy");

                                HashMap hm = new HashMap();
                                hm.put(trendingkeys[0], id);
                                hm.put(trendingkeys[1], name);
                                hm.put(trendingkeys[2], lyricId);
                                hm.put(trendingkeys[3], lyricViews);
                                hm.put(trendingkeys[4], lyric_name);
                                hm.put(trendingkeys[5], writerName);
                                hm.put(trendingkeys[6], trendingDate);

                                trendingAl.add(hm);
                                //     trendingLL.setVisibility(View.VISIBLE);
                            }

                            trendingMyRecycle = new TrendingMyRecycle(MainActivity.this, trendingAl);
                            trendingRecycularView.setHasFixedSize(true);
                            trendingRecycularView.setLayoutManager(new CenterZoomLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            SnapHelper snapHelper = new LinearSnapHelper();
                            snapHelper.attachToRecyclerView(trendingRecycularView);
                            trendingRecycularView.setAdapter(trendingMyRecycle);
                         //   dismissProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void writerJson()
    {
        showProgressDialog();
        //StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.5:8080/LiriceApp/writer",
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/writer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                           // Log.d("resres",""+response);
                            // JSONArray jsonArray=new JSONArray(response);

                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("writers");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < 15; i++)
                            {
                                String nameee = jsonArray.getString(i);
                                String ss = nameee.replace("_", " ");

                                HashMap hm = new HashMap();
                                hm.put(writkey[0], ss);
                                writersAl.add(hm);

                            }

                            writerRecycleAdapter = new WriterRecycleAdapter(MainActivity.this, writersAl);

                            // writerRecycleAdapter=new WriterRecycleAdapter(MainActivity.this,writersAl);
                            writersRecycularView.setHasFixedSize(true);

                            writersRecycularView.setLayoutManager(new CenterZoomLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            //writersRecycularView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                            SnapHelper snapHelper = new LinearSnapHelper();
                            snapHelper.attachToRecyclerView(writersRecycularView);

                            writersRecycularView.setAdapter(writerRecycleAdapter);
                            //dismissProgressDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void yearJson()
    {
        final long yearTime=Long.parseLong(lll);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/years",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {
                            JSONArray jsonArray=new JSONArray(response);

                            for (int i=jsonArray.length()-1;i>=50;i--)
                           // for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);
                                int yearsId=e.getInt("yearId");
                                int lyricYears=e.getInt("lyircYear");
                                HashMap hm = new HashMap();

                          //      if (lyricYearsi)
                                if(lyricYears<=(yearTime))
                                {
                                    hm.put(yearsKeys[0], yearsId);

                                    hm.put(yearsKeys[1], lyricYears);

                                    yearsAl.add(hm);
                                    trendingLL.setVisibility(View.VISIBLE);
                                    latestLL.setVisibility(View.VISIBLE);
                                    yearLL.setVisibility(View.VISIBLE);
                                    writersLL.setVisibility(View.VISIBLE);
                                    allTimeLL.setVisibility(View.VISIBLE);
                                    dismissProgressDialog();
                                    //recentLL.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        yearsRecAdapter = new YearsRecAdapter(yearsAl,MainActivity.this);
                        yearsRecycularView.setHasFixedSize(true);
                        yearsRecycularView.setLayoutManager(new CenterZoomLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        // yearsRecycularView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                        SnapHelper snapHelper=new LinearSnapHelper();
                        snapHelper.attachToRecyclerView(yearsRecycularView);

                        yearsRecycularView.setAdapter(yearsRecAdapter);

                        MyDb myDb = new MyDb(MainActivity.this);

                        SQLiteDatabase sql = myDb.getReadableDatabase();
                        Cursor cursor=sql.query(MyDb.TABLENAME,null,null,null,null,null,null);
                        if(cursor.getCount()>0)
                        {
                            recentLL.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            recentLL.setVisibility(View.GONE);
                            //Toast.makeText(MainActivity.this, "NO data", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        recentLL.setVisibility(View.GONE);
                        if (volleyError instanceof NetworkError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                            View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.retry_button,null,false);
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
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            getLayoutInflater().inflate(R.layout.mycard_style,null,false);
                            //View v=get
                        }
                        else if (volleyError instanceof ServerError)
                        {
                            message = "The server could not be found. Please try again after some time!!";
                            AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                            View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.retry_button,null,false);
                            adb.setView(view);
                       //     adb.setCancelable(false);
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

                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof ParseError)
                        {
                            message = "Parsing error! Please try again after some time!!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof NoConnectionError)
                        {
                            message = "Cannot connect to Internet...Please check your connection!";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else if (volleyError instanceof TimeoutError)
                        {
                            message = "Connection TimeOut! Please check your internet connection.";
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void allTimeHits()
    {
        showProgressDialog();
        ///  StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.6:8080/LiriceApp/allTimeHits",
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://kasksolutions.com:90/LiriceApp/allTimeHits",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try
                        {

                            JSONArray jsonArray = new JSONArray(response);

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);

                                //Reading ID from url

                                String movieId = e.getString("movie_id");

                                String name = e.getString("movieName");

                                String lyricId = e.getString("lyricId");

                            //    String lyricViews = e.getString("lyricViews");

                                String lyric_name = e.getString("lyricTitle");

                                String writerName = e.getString("writerName");

                                String releaseDate = e.getString("releaseDate");

                                String dateAlltime = getDate(Long.parseLong(releaseDate), "yyyy");

                                HashMap hm = new HashMap();
                                hm.put(trendingkeys[0], movieId);
                                hm.put(trendingkeys[1], name);
                                hm.put(trendingkeys[2], lyricId);
                               // hm.put(trendingkeys[3], lyricViews);
                                hm.put(trendingkeys[4], lyric_name);
                                hm.put(trendingkeys[5], writerName);
                                hm.put(trendingkeys[6], dateAlltime);
                                allTimeAl.add(hm);
                            }
                            trendingMyRecycle = new TrendingMyRecycle(MainActivity.this, allTimeAl);
                            allTimeHitsRecyclerView.setHasFixedSize(true);
                            allTimeHitsRecyclerView.setLayoutManager(new CenterZoomLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            SnapHelper snapHelper = new LinearSnapHelper();
                            snapHelper.attachToRecyclerView(allTimeHitsRecyclerView);
                            allTimeHitsRecyclerView.setAdapter(trendingMyRecycle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy
                (5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent)
    {
        // Get search query and create object of class AsyncFetch
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchView != null) {
                searchView.clearFocus();
            }
        }
    }
    public void yearsSeeAll(View v)
    {

        startActivity(new Intent(MainActivity.this,YearsSeeAll.class));
    }
    public void recentSeeAll(View v)
    {
        startActivity(new Intent(this,RecentSeeAll.class));
    }
    public void latestSeeAll(View v)
    {
        startActivity(new Intent(this, Latest.class));
    }
    public void trendingSeeAll(View v)
    {
        startActivity(new Intent(this, TrendingSongs.class));
    }
    public void writerSeeAll(View v)
    {
        startActivity(new Intent(this,WritersCollection.class));
    }
    public void allTimeSeeAll(View v)
    {
        startActivity(new Intent(this,AllTimeHitsSeeAll.class));
    }

//https://stackoverflow.com/questions/13667966/how-to-get-date-from-milliseconds-in-android
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
    public void onBackPressed()
    {
        //It will show an Alert Dialog

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Do you want Exit this application");
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //When Ipress OK it will close the appliction

                MainActivity.super.onBackPressed();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //When Ipress Cancel  it will close the Alert Dilog

                dialogInterface.dismiss();
            }
        });
        ad.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search, menu);
        SharedPreferences settings = getSharedPreferences("settings", 0);
        videoCk = settings.getBoolean("checkbox", false);
        MenuItem item = menu.findItem(R.id.menu2);
        item.setChecked(videoCk);
        menu.findItem(R.id.search);
        return true;
    }
    public void search(MenuItem menuItem)
    {
        Intent i = new Intent(this, ExSearchActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right, R.anim.left);
    }
    //https://stackoverflow.com/questions/29122447/checkbox-item-state-on-menu-android
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu2:
                if (item.isChecked())
                {
                    item.setChecked(false);
                    videoCk = false;
                    SharedPreferences settings = getSharedPreferences("settings", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("checkbox", item.isChecked());
                    editor.commit();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(this, "Your video Mode is Enabled", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    item.setChecked(true);
                    videoCk = true;
                    SharedPreferences settings = getSharedPreferences("settings", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("checkbox", item.isChecked());
                    editor.commit();
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(this, "Your video Mode is Disabled", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void exit(MenuItem menuItem)
    {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Do you want Exit this application");
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finishAffinity();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });
        ad.show();
    }
    public void clearDb(MenuItem menuItem)
    {
        removeAll();
        finish();
        startActivity(getIntent());
        Toast.makeText(this, "Database cleared successfully", Toast.LENGTH_SHORT).show();
    }
    public void share(MenuItem menuItem)
    {
        notifications();
    }

    public void feedback(MenuItem menuItem)
    {
        startActivity(new Intent(this,Feedback.class));
    }
    private void showProgressDialog()
    {
        if (pDialog == null)
        {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            recentLL.setVisibility(View.GONE);
        }
        pDialog.show();
    }
    private void dismissProgressDialog()
    {
        if (pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }
    public void removeAll()
    {
        MyDb myDb=new MyDb(this);
        SQLiteDatabase sqLiteDatabase=myDb.getReadableDatabase();
        sqLiteDatabase.delete(MyDb.TABLENAME,null,null);
    }
    public void notifications()
    {
        String url="http://192.168.0.3:8080/LiriceApp/newSongs";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONArray jsonArray=jsonObject.getJSONArray("Added new songs");
                    //    Toast.makeText(MainActivity.this, ""+name, Toast.LENGTH_SHORT).show();

                    for(int i=0;i<1;i++)
                    {
                        JSONObject e = jsonArray.getJSONObject(i);

                        //   Log.d("jajaja",""+e);

                        String id=e.getString("id");
                        String lyricname=e.getString("lyricname");
                        String moviename=e.getString("moviename");
                        String movieId=e.getString("movieId");
                        String writerName=e.getString("writerName");
                        String date=e.getString("movieReleaseDate");

                        String dateYaer = getDate(Long.parseLong(date), "yyyy");
                        final String imageUrl= "https://s3.amazonaws.com/liriceapp/"+movieId+"/"+movieId+".jpg";

                        Intent intent=new Intent(MainActivity.this,Songs.class);
                        intent.putExtra("idno",movieId);
                        intent.putExtra("moviename",moviename);
                        intent.putExtra("date123",dateYaer);
                        intent.putExtra("BitmapImage",imageUrl);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        PendingIntent pendingIntent= PendingIntent.getActivity
                                (MainActivity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        final NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this);
                        builder.setContentTitle("New Songs Added");
                        builder.setContentText(moviename);
                        builder.setContentIntent(pendingIntent);
                        builder.setSound(soundUri);
                        builder.setSmallIcon(R.mipmap.ic_launcher);

                        ImageRequest imageRequest=new ImageRequest(imageUrl, new Response.Listener<Bitmap>()
                        {
                            @Override
                            public void onResponse(Bitmap response)
                            {
                                Log.d("imageResponc",""+response);

                                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                                NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.notify(0,builder.build());
                            }
                        },0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {
                                //Toast.makeText(FireBaseMessagingService.this, ""+error, Toast.LENGTH_SHORT).show();
                                Log.d("errr",""+error);

                            }
                        });
                        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
                        requestQueue.add(imageRequest);

//                        Glide.with(MainActivity.this)
//                                .load(imageUrl)
//                                .into(holder.thumnail);

//                        Intent intent=new Intent(MainActivity.this,MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        PendingIntent pendingIntent= PendingIntent.getActivity
//                                (MainActivity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//                        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        final NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this);
//                        builder.setContentTitle("New Songs Added");
//                        builder.setContentText(moviename);
//                        builder.setContentIntent(pendingIntent);
//                        builder.setSound(soundUri);
//                        builder.setSmallIcon(R.mipmap.ic_launcher);
//
//
//
//                        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(movieId));
//                        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                        notificationManager.notify(0,builder.build());

                        // Log.d("res100",name+""+id+"\n"+lyricname+"\n"+moviename+"\n"+movieId+"\n"+writerName+"\n"+date);



                        //  songsLists.add(hm);
                    }
                }
                catch (Exception e)
                {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        Toast.makeText(MainActivity.this, "Err"+error, Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}


