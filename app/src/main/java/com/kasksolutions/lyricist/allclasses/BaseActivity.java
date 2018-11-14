package com.kasksolutions.lyricist.allclasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kasksolutions.lyricist.R;

public class BaseActivity extends AppCompatActivity
{
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Menu for latest
        getMenuInflater().inflate(R.menu.latest_menu, menu);
        return true;
    }

    public void search(MenuItem menuItem)
    {
        Intent i = new Intent(this, ExSearchActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.right, R.anim.left);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void latestexit(MenuItem menuItem)
    {
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Do you want Exit this application");
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                //When Ipress OK it will close the appliction
                finishAffinity();
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

    //Menu Onclick goto main Menu

    public void latesthome(MenuItem menuItem)
    {
        //see this url
        //https://stackoverflow.com/questions/2776830/android-moving-back-to-first-activity-on-button-click

        startActivity(new Intent(BaseActivity.this,MainActivity.class));
    }

    public void latestfeedback(MenuItem menuItem)
    {
        startActivity(new Intent(this,Feedback.class));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        ConnectivityManager cM = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cM.getActiveNetworkInfo();
        if (activeNetworkInfo != null)
        {
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet Connection Required");
            builder.setCancelable(false);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    finishAffinity();
                }
            });
            builder.show();
        }
    }
}
