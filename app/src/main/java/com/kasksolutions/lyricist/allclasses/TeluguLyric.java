package com.kasksolutions.lyricist.allclasses;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.kasksolutions.lyricist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class TeluguLyric extends AppCompatActivity {
    public TextView tv2;
    RelativeLayout ll;
    Resources r;
    ArrayList al1;
  //  ProgressDialog dialog;
    String lyricTitle, lyrisstName, movienametext, movieId, lyricContent;
    TextView datetv, movieNameTextview, lyricistTextView;
    String lyricId, gettingImageUrl, writerImage;
    ;
    String xx;

    String video;

    Toast toast;
    String dateString, repWriter;

    TableLayout tableLayout;
    ScaleGestureDetector scaleGestureDetector;

    YouTubePlayerSupportFragment frag;
    public static View fragVisibility;
    LinearLayout linearLayout;

    Switch aSwitchTelugu;
    RelativeLayout relativeLayouttel;

    ProgressBar progressBar;
    LinearLayout mainLinearLayout;

    Button forward,backward;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telugu_lyric);
        tableLayout = findViewById(R.id.Teltable);
        mainLinearLayout=findViewById(R.id.mainTeluguLinear);
        tableLayout.setVisibility(View.GONE);

        forward=findViewById(R.id.forwardTelugu);
        backward=findViewById(R.id.backwardTel);
        progressBar=findViewById(R.id.spin_kit11);
        forward.setVisibility(GONE);
        backward.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        relativeLayouttel=findViewById(R.id.relativeLayouttelugu);
        linearLayout = findViewById(R.id.linearoops);

        linearLayout.setVisibility(GONE);
        tv2 = findViewById(R.id.Tellin1);
        aSwitchTelugu = findViewById(R.id.swithTelugu);
        //aSwitchTelugu.setVisibility(GONE);

        fragVisibility = findViewById(R.id.telugu_youtube_view);


        movieNameTextview = findViewById(R.id.TeltextMovieName);

        lyricistTextView = findViewById(R.id.TeltextLyricsName);

        datetv = findViewById(R.id.Teldateyaer);

        //Id for Coordinator layout
        ll = findViewById(R.id.cll);


//        dialog = ProgressDialog.show(TeluguLyric.this, "",
//                "", true);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        al1 = new ArrayList();


        r = getResources();

        tv2.setSelected(true);

        lyricId = getIntent().getStringExtra("lyricid");
        lyricTitle = getIntent().getStringExtra("lyricTitle");

        movienametext = getIntent().getStringExtra("movieName");

        lyrisstName = getIntent().getStringExtra("wrotername");

        movieId = getIntent().getStringExtra("movie_id");

        dateString = getIntent().getStringExtra("datedate");

        datetv.setText(dateString);
        fragVisibility.setVisibility(View.GONE);
        gettingImageUrl = getIntent().getStringExtra("BitmapImage");
        writerImage = getIntent().getStringExtra("writerImage");
        repWriter = lyrisstName.replace("_", " ");

        SpannableString spannableMovieName = new SpannableString(movienametext);
        spannableMovieName.setSpan(new UnderlineSpan(), 0, movienametext.length(), 0);
        movieNameTextview.setText(spannableMovieName);

        SpannableString spannableWriter = new SpannableString(repWriter);
        spannableWriter.setSpan(new UnderlineSpan(), 0, repWriter.length(), 0);
        lyricistTextView.setText(spannableWriter);

        SpannableString dateS = new SpannableString(dateString);
        dateS.setSpan(new UnderlineSpan(), 0, dateString.length(), 0);
        datetv.setText(dateS);
        postData();
        //https://stackoverflow.com/questions/28529870/how-to-zoom-a-text-view-in-scroll-view
        scaleGestureDetector = new ScaleGestureDetector(this, new simpleOnScaleGestureListener());
        tv2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() == 1) {
                    //stuff for 1 pointer
                } else { //when 2 pointers are present
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            scaleGestureDetector.onTouchEvent(event);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            scaleGestureDetector.onTouchEvent(event);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                }
                return true;
            }
        });
        switchMethod();

    }

    private float safe;

    public class simpleOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            float size = tv2.getTextSize();
            Log.d("TextSizeStart", String.valueOf(size));

            //float factor = detector.getScaleFactor();
            float factor = Math.max(0.5f, Math.min(detector.getScaleFactor(), 2f));
            Log.d("Factor", String.valueOf(factor));


            float product = size * factor;
            Log.d("TextSize", String.valueOf(product));

            safe = Math.abs(product - size);

            if (product <= 60 && product >= 30 && safe < 3) {
                //tv.setText("factor= " +factor + "\n" +  "product = \n" + size + " * " + factor + " \n= " + product +"\n" + getString(R.string.hello_world));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
            }

            size = tv2.getTextSize();
            Log.d("TextSizeEnd", String.valueOf(size));
            return true;
        }
    }


    public void postData()
    {
        try
        {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://kasksolutions.com:90/LiriceApp/teluguLyrics/" + lyricId;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", "" + MainActivity.android_id);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() == 0)
                        {
                            progressBar.setVisibility(GONE);
                            linearLayout.setVisibility(VISIBLE);
                        }
                        else
                            {
                            progressBar.setVisibility(GONE);
                            linearLayout.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject e = jsonArray.getJSONObject(i);

                                final String lyricContent = e.getString("lyricContent");
                                video = e.getString("url");

                                xx = lyricContent.replaceAll("[#]+", "\n\n");

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        if (!MainActivity.videoCk)
                                        {
                                            mainLinearLayout.setVisibility(VISIBLE);
                                            fragVisibility.setVisibility(VISIBLE);
                                            forward.setVisibility(VISIBLE);
                                            backward.setVisibility(VISIBLE);
                                            getSupportActionBar().hide();
                                            tv2.setText(xx);
                                            tableLayout.setVisibility(View.VISIBLE);
                                            youTube();
                                        }
                                        else
                                        {
                                            aSwitchTelugu.setChecked(true);
                                            tv2.setText(xx);
                                            mainLinearLayout.setVisibility(VISIBLE);
                                            fragVisibility.setVisibility(View.GONE);
                                            forward.setVisibility(GONE);
                                            backward.setVisibility(GONE);
                                            tableLayout.setVisibility(View.VISIBLE);
                                            aSwitchTelugu.setVisibility(VISIBLE);
                                            getSupportActionBar().show();
                                            setTitle(lyricTitle + " From " + movienametext);

                                            aSwitchTelugu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if (b)
                                                    {
                                                        fragVisibility.setVisibility(View.GONE);
                                                        forward.setVisibility(GONE);
                                                        backward.setVisibility(GONE);
                                                        aSwitchTelugu.setVisibility(VISIBLE);
                                                        getSupportActionBar().show();
                                                        setTitle(lyricTitle + " from " + movienametext);
                                                       // Toast.makeText(TeluguLyric.this, "Video OFF", Toast.LENGTH_SHORT).show();

                                                    } else
                                                        {
                                                        fragVisibility.setVisibility(View.VISIBLE);
                                                        aSwitchTelugu.setVisibility(VISIBLE);
                                                            forward.setVisibility(VISIBLE);
                                                            backward.setVisibility(VISIBLE);
                                                        getSupportActionBar().hide();
                                                        tv2.setText(xx);
                                                        tableLayout.setVisibility(VISIBLE);
                                                        youTube();
                                                      //  Toast.makeText(TeluguLyric.this, "Video On", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
//
                                    }
                                });
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    //Log.e("VOLLEY", error.toString());
                    progressBar.setVisibility(GONE);
                    linearLayout.setVisibility(VISIBLE);
                  //  Toast.makeText(TeluguLyric.this, "sorry" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response.statusCode == 200) {

                        //do smth
                    } else if (response.statusCode == 401) {
                        //do smth else
                    }
                    return super.parseNetworkResponse(response);
                    //return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };
            //https://stackoverflow.com/questions/22428343/android-volley-double-post-when-have-slow-request

            stringRequest.setRetryPolicy(new DefaultRetryPolicy
                    (0, -1,

                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void youTube()
    {
        frag = (YouTubePlayerSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.telugu_youtube_view);
        frag.initialize("AIzaSyBQVRjDT1pmZEV3fYPyI3AgJYOc6yIuovs",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        final YouTubePlayer player, boolean b) {

                        if (null == player)
                            return;

                        // Start buffering
                        if (!b)
                        {
                            player.cueVideo(video);
                            aSwitchTelugu.setVisibility(VISIBLE);

                        }
                        else
                        {
                            aSwitchTelugu.setVisibility(VISIBLE);
                           // player.cueVideo(video);
                        }
                        player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onAdStarted() {
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason arg0) {

                                fragVisibility.setVisibility(GONE);
                                aSwitchTelugu.setVisibility(GONE);
                                forward.setVisibility(GONE);
                                backward.setVisibility(GONE);
                                //   mainLinearLayout.setVisibility(VISIBLE);
                                //    dialog.dismiss();
                                getSupportActionBar().show();

                                setTitle(lyricTitle + " from " + movienametext);
                                Snackbar snackbar = Snackbar
                                        .make(findViewById(R.id.relativeLayouttelugu), "Sorry No Video Found", Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.RED);
                                snackbar.show();

                            }

                            @Override
                            public void onLoaded(String arg0)
                            {
                                gesture(player);
                                gestureBackward(player);
                            }

                            @Override
                            public void onLoading() {
                            }

                            @Override
                            public void onVideoEnded() {
                            }

                            @Override
                            public void onVideoStarted() {
                            }
                        });


                        player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                            @Override
                            public void onBuffering(boolean arg0) {
                            }

                            @Override
                            public void onPaused() {
                                //    Toast.makeText(LyricDisplay.this, "Pause", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPlaying()
                            {
                                aSwitchTelugu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if (b)
                                        {
                                            player.pause();
                                            fragVisibility.setVisibility(View.GONE);
                                            forward.setVisibility(GONE);
                                            backward.setVisibility(GONE);
                                            aSwitchTelugu.setVisibility(VISIBLE);
                                            getSupportActionBar().show();
                                            setTitle(lyricTitle + " from " + movienametext);
                                            Toast.makeText(TeluguLyric.this, "Video OFF", Toast.LENGTH_SHORT).show();

                                        } else
                                        {
                                            player.isPlaying();
                                            fragVisibility.setVisibility(View.VISIBLE);
                                            forward.setVisibility(VISIBLE);
                                            backward.setVisibility(VISIBLE);
                                            aSwitchTelugu.setVisibility(VISIBLE);
                                            getSupportActionBar().hide();
                                            Toast.makeText(TeluguLyric.this, "Video On", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onSeekTo(int arg0) {
                                //      Toast.makeText(LyricDisplay.this, "seek"+arg0, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onStopped() {
                                //    Toast.makeText(LyricDisplay.this, "stopped", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void movieName(View v) {
        Intent intent = new Intent(TeluguLyric.this, Songs.class);
        intent.putExtra("moviename", movienametext);

        intent.putExtra("idno", movieId);

        intent.putExtra("BitmapImage", gettingImageUrl);

        intent.putExtra("date123", dateString);

        //  intent.putExtra("idno",gettingImageUrl);

        startActivity(intent);
    }

    public void writer(View v) {
        //finish();
        Intent intent = new Intent(TeluguLyric.this, SingleWriter.class);
        intent.putExtra("writerName", repWriter);
        intent.putExtra("writerImage", writerImage);
        startActivity(intent);

    }
    public void dateYear(View v) {
        Intent intent = new Intent(TeluguLyric.this, SingleYear.class);

        int yaer = Integer.parseInt(dateString);
        intent.putExtra("yearOnly", yaer);
        startActivity(intent);

    }
    public void moveToEnglish(View v)
    {
        finish();
        Intent i = new Intent(TeluguLyric.this, LyricDisplay.class);
        i.putExtra("lyricid", lyricId);

        i.putExtra("lyricTitle",lyricTitle);

        i.putExtra("wrotername",repWriter);

        i.putExtra("movieName", movienametext);

        i.putExtra("movie_id",movieId);

     //   i.putExtra("BitmapImage", writerImage);

        i.putExtra("datedate",dateString);

        i.putExtra("writerImage",writerImage);
        startActivity(i);
        //Toast.makeText(this, "Moved to English", Toast.LENGTH_SHORT).show();
    }

    public void gesture(final YouTubePlayer player) {
        forward.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(TeluguLyric.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    //Log.d("TEST", "onDoubleTap");
                    //Toast.makeText(LyricDisplay.this, "Double tapped", Toast.LENGTH_SHORT).show();
                    // player.seekToMillis(10000);
                    //    player.seekRelativeMillis(10000);
                    return super.onDoubleTap(e);
                }
                // ... // implement here other callback methods like onFling, onScroll as necessary
            });
            private GestureDetector gestureDetector2 = new GestureDetector(TeluguLyric.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    player.seekRelativeMillis(10000);

                    // Toast.makeText(LyricDisplay.this, "hi", Toast.LENGTH_SHORT).show();
                    return super.onSingleTapConfirmed(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //   Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                // gestureDetector.onTouchEvent(event);
                gestureDetector2.onTouchEvent(event);
                //  player.seekRelativeMillis(10000);
                // Toast.makeText(LyricDisplay.this, "Single tapped", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void gestureBackward(final YouTubePlayer player)
    {
        backward.setOnTouchListener(new View.OnTouchListener()
        {
            private GestureDetector gestureDetector2= new GestureDetector(TeluguLyric.this,new GestureDetector.SimpleOnGestureListener()
            {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e)
                {
                    player.seekRelativeMillis(-10000);
                    return super.onSingleTapConfirmed(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                gestureDetector2.onTouchEvent(event);
                return true;
            }
        });
    }

    public void switchMethod()
    {
        aSwitchTelugu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {
                    fragVisibility.setVisibility(View.GONE);
                    forward.setVisibility(GONE);
                    backward.setVisibility(GONE);
                    aSwitchTelugu.setVisibility(VISIBLE);
                    getSupportActionBar().show();
                    setTitle(lyricTitle + " from " + movienametext);
                    Toast.makeText(TeluguLyric.this, "Video OFF", Toast.LENGTH_SHORT).show();

                } else {
                    fragVisibility.setVisibility(View.VISIBLE);
                    aSwitchTelugu.setVisibility(VISIBLE);
                    forward.setVisibility(VISIBLE);
                    backward.setVisibility(VISIBLE);
                    getSupportActionBar().hide();
                    Toast.makeText(TeluguLyric.this, "Video On", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
