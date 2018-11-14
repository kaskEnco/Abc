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
import static android.view.View.OnTouchListener;
import static android.view.View.VISIBLE;

public class LyricDisplay extends AppCompatActivity
{
    public TextView tv2;

    RelativeLayout ll;

    Resources r;
    ArrayList al1;

    String lyricTitle,lyrisstName, movienametext, movieId;

    TextView  datetv, movieNameTextview, lyricistTextView;

    String lyricId, gettingImageUrl,writerImage;
    String xx;
    String video;
    String dateString,repWriter;
    YouTubePlayerSupportFragment frag;

    ScaleGestureDetector scaleGestureDetector;

    TableLayout tableLayout;
    public static View fragVisibility;
    Switch aSwitch;
    LinearLayout engoops;

    ProgressBar progressBar;

    LinearLayout mainEngLinearLayout;

    Button forward,backward;

    public YouTubePlayer youTubePlayer;
    public boolean isYouTubePlayerFullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lyric_display);

        progressBar=findViewById(R.id.spin_kit11);

        mainEngLinearLayout=findViewById(R.id.mainEnglishLinearLayout);


        mainEngLinearLayout.setVisibility(GONE);

        tableLayout=findViewById(R.id.table);

        forward=findViewById(R.id.forward);
        backward=findViewById(R.id.backward);

        tableLayout.setVisibility(View.GONE);
        getSupportActionBar().hide();
        tv2 = findViewById(R.id.lin1);

        aSwitch=findViewById(R.id.swith);
        fragVisibility=findViewById(R.id.youtube_view);

        engoops=findViewById(R.id.englinearoops);
      //  youLL=findViewById(R.id.youLLLL);

        movieNameTextview = findViewById(R.id.textMovieName);

        lyricistTextView = findViewById(R.id.textLyricsName);

        datetv = findViewById(R.id.dateyaer);
        ll = findViewById(R.id.cll);

//        dialog = ProgressDialog.show(LyricDisplay.this, "",
//                "", true);

        al1 = new ArrayList();

        r = getResources();
        tv2.setSelected(true);

        progressBar.setVisibility(VISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lyricId = getIntent().getStringExtra("lyricid");
        lyricTitle = getIntent().getStringExtra("lyricTitle");

        movienametext = getIntent().getStringExtra("movieName");

        lyrisstName = getIntent().getStringExtra("wrotername");

        movieId = getIntent().getStringExtra("movie_id");

        dateString = getIntent().getStringExtra("datedate");

        writerImage=getIntent().getStringExtra("writerImage");

        Log.d("writerImage",""+writerImage);
        datetv.setText(dateString);
        fragVisibility.setVisibility(View.GONE);

        gettingImageUrl = getIntent().getStringExtra("BitmapImage");

        repWriter = lyrisstName.replace("_", " ");

        SpannableString spannableMovieName = new SpannableString(movienametext);
        spannableMovieName.setSpan(new UnderlineSpan(), 0, movienametext.length(), 0);
        movieNameTextview.setText(spannableMovieName);

        SpannableString spannableWriter = new SpannableString(repWriter);
        spannableWriter.setSpan(new UnderlineSpan(), 0, repWriter.length(), 0);
        lyricistTextView.setText(spannableWriter);

          SpannableString dateS=new SpannableString(dateString);
          dateS.setSpan(new UnderlineSpan(), 0, dateString.length(), 0);
          datetv.setText(dateS);
        scaleGestureDetector = new ScaleGestureDetector(this, new simpleOnScaleGestureListener());
        tv2.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getPointerCount() == 1){
                    //stuff for 1 pointer
                }else{ //when 2 pointers are present
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

        postData();



    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

    public void postData()
    {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://kasksolutions.com:90/LiriceApp/lyrics/" + lyricId;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", ""+MainActivity.android_id);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URL, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    try
                    {
                        JSONArray jsonArray=new JSONArray(response);

                        if (jsonArray.length()==0)
                        {
                            engoops.setVisibility(VISIBLE);
                            //dialog.dismiss();
                            progressBar.setVisibility(GONE);
                            mainEngLinearLayout.setVisibility(GONE);

                        }
                        else
                        {
                            progressBar.setVisibility(GONE);
                            engoops.setVisibility(GONE);
                            mainEngLinearLayout.setVisibility(VISIBLE);
                            //dialog.dismiss();
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

                                            fragVisibility.setVisibility(VISIBLE);
                                            getSupportActionBar().hide();
                                            tv2.setText(xx);
                                            tableLayout.setVisibility(View.VISIBLE);
                                            youTube();
                                        }
                                        else
                                        {
                                            progressBar.setVisibility(GONE);
                                            fragVisibility.setVisibility(View.GONE);
                                            mainEngLinearLayout.setVisibility(VISIBLE);
                                            aSwitch.setVisibility(VISIBLE);
                                            aSwitch.setChecked(true);
                                            tv2.setText(xx);
                                            tableLayout.setVisibility(View.VISIBLE);
                                            getSupportActionBar().show();
                                            setTitle(lyricTitle + " From " + movienametext);

                                            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                                            {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton,
                                                                             boolean b)
                                                {
                                                    if (b)
                                                    {
                                                       // player.pause();
                                                        fragVisibility.setVisibility(View.GONE);
                                                        aSwitch.setVisibility(VISIBLE);
                                                        getSupportActionBar().show();
                                                        setTitle(lyricTitle+" from "+movienametext);
                                                        forward.setVisibility(GONE);
                                                        backward.setVisibility(GONE);
                                                      //  Toast.makeText(LyricDisplay.this, "Video OFF", Toast.LENGTH_SHORT).show();

                                                    }
                                                    else
                                                    {
                                                        forward.setVisibility(VISIBLE);
                                                        backward.setVisibility(VISIBLE);
                                                        //player.isPlaying();
                                                        fragVisibility.setVisibility(View.VISIBLE);
                                                        aSwitch.setVisibility(VISIBLE);
                                                        getSupportActionBar().hide();
                                                        tv2.setText(xx);
                                                        tableLayout.setVisibility(View.VISIBLE);
                                                        youTube();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        //dialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                  //  Log.e("VOLLEY", error.toString());
                   // Toast.makeText(LyricDisplay.this, "sorry"+error.toString(), Toast.LENGTH_SHORT).show();
                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError
                {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response)
                {
                    if (response.statusCode == 200)
                    {
                    } else if (response.statusCode == 401)
                    {
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            //https://stackoverflow.com/questions/22428343/android-volley-double-post-when-have-slow-request
            stringRequest.setRetryPolicy(new DefaultRetryPolicy
                    (0,-1,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void youTube()
    {

        frag = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_view);
        frag.initialize("AIzaSyBQVRjDT1pmZEV3fYPyI3AgJYOc6yIuovs", new YouTubePlayer.OnInitializedListener()
        {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                final YouTubePlayer player,
                                                boolean b)
            {
                youTubePlayer=player;
                //gesture(player);
                if (null == player)
                    return;
                if (!b)
                {
                    player.cueVideo(video);
                    aSwitch.setVisibility(VISIBLE);
                }
                else
                {
                 //   player.cueVideo(video);
                    aSwitch.setVisibility(VISIBLE);
                }
                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener()
                {
                    @Override
                    public void onAdStarted()
                    {
                       //player.play();

                       // player.seekToMillis(500);
                       //
                        //
                        // Toast.makeText(LyricDisplay.this, "add started", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(YouTubePlayer.ErrorReason arg0)
                    {
                        forward.setVisibility(GONE);
                        backward.setVisibility(GONE);
                        fragVisibility.setVisibility(GONE);
                        aSwitch.setVisibility(GONE);
                        progressBar.setVisibility(GONE);
                        getSupportActionBar().show();
                        setTitle(lyricTitle + " from " + movienametext);
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.cll), "Sorry No Video Found", Snackbar.LENGTH_LONG);
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
                    public void onPaused()
                    {
                    }
                    @Override
                    public void onPlaying()
                    {
                       // gesture(player);
                        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                        {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                            {
                                if (b)
                                {
                                    player.pause();
                                    fragVisibility.setVisibility(View.GONE);
                                    aSwitch.setVisibility(VISIBLE);
                                    getSupportActionBar().show();
                                    setTitle(lyricTitle+" from "+movienametext);
                                    Toast.makeText(LyricDisplay.this, "Video OFF", Toast.LENGTH_SHORT).show();
                                    forward.setVisibility(GONE);
                                    backward.setVisibility(GONE);


                                }
                                else
                                {

                                    fragVisibility.setVisibility(View.VISIBLE);
                                    aSwitch.setVisibility(VISIBLE);
                                    getSupportActionBar().hide();
                                    Toast.makeText(LyricDisplay.this, "Video On", Toast.LENGTH_SHORT).show();
                                    player.isPlaying();
                                    forward.setVisibility(VISIBLE);
                                    backward.setVisibility(VISIBLE);
                                }
                            }
                        });
                    }

                    @Override
                    public void onSeekTo(int arg0)
                    {
                        //      Toast.makeText(LyricDisplay.this, "seek"+arg0, Toast.LENGTH_SHORT).show();
                       // onSeekTo(1000);

                    }

                    @Override
                    public void onStopped() {
                        //    Toast.makeText(LyricDisplay.this, "stopped", Toast.LENGTH_SHORT).show();
                    }
                });
                player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener()
                {
                    @Override
                    public void onFullscreen(boolean b)
                    {
                            isYouTubePlayerFullScreen = b;
                            player.play();
                    }
                });
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (youTubePlayer != null && isYouTubePlayerFullScreen)
        {
            isYouTubePlayerFullScreen = false;
            youTubePlayer.setFullscreen(false);
        }else {
            super.onBackPressed();
        }
    }

    public void gesture(final YouTubePlayer player) {
        forward.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(LyricDisplay.this, new GestureDetector.SimpleOnGestureListener() {
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
            private GestureDetector gestureDetector2 = new GestureDetector(LyricDisplay.this, new GestureDetector.SimpleOnGestureListener() {
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
            private GestureDetector gestureDetector2= new GestureDetector(LyricDisplay.this,new GestureDetector.SimpleOnGestureListener()
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
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b)
                {

                    fragVisibility.setVisibility(View.GONE);
                    aSwitch.setVisibility(VISIBLE);
                    getSupportActionBar().show();
                    setTitle(lyricTitle+" from "+movienametext);
                    Toast.makeText(LyricDisplay.this, "Video OFF", Toast.LENGTH_SHORT).show();
                    forward.setVisibility(GONE);
                    backward.setVisibility(GONE);

                }
                else
                {
                    fragVisibility.setVisibility(View.VISIBLE);
                    aSwitch.setVisibility(VISIBLE);
                    getSupportActionBar().hide();
                    Toast.makeText(LyricDisplay.this, "Video On", Toast.LENGTH_SHORT).show();
                    forward.setVisibility(VISIBLE);
                    backward.setVisibility(VISIBLE);
                }
            }
        });
    }



    public void movieName(View v)
    {

        //finish();
        Intent intent = new Intent(LyricDisplay.this, Songs.class);
        //   intent.putExtra("idno",lyricId);
        intent.putExtra("moviename", movienametext);

        intent.putExtra("idno", movieId);

        intent.putExtra("BitmapImage", gettingImageUrl);

        intent.putExtra("date123",dateString);

        Log.d("BitmapImageLyric",""+gettingImageUrl);

        //  intent.putExtra("idno",gettingImageUrl);

        startActivity(intent);
    }

    public void writer(View v)
    {
      //  finish();
        Intent intent = new Intent(LyricDisplay.this, SingleWriter.class);
        intent.putExtra("writerName", repWriter);
        intent.putExtra("writerImage",writerImage);
        startActivity(intent);

    }
    public void dateYear(View v)
    {
        //finish();
        Intent intent = new Intent(LyricDisplay.this, SingleYear.class);
        int year=Integer.parseInt(dateString);
        intent.putExtra("yearOnly", year);
        startActivity(intent);

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

            if (product <= 100 && product >= 20 && safe < 3) {
                //tv.setText("factor= " +factor + "\n" +  "product = \n" + size + " * " + factor + " \n= " + product +"\n" + getString(R.string.hello_world));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, product);
            }

            size = tv2.getTextSize();
            Log.d("TextSizeEnd", String.valueOf(size));
            return true;
        }
    }
}







