package com.kasksolutions.lyricist.allclasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SplashScreen extends AppCompatActivity {

    //    public static String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_ex_search);
        setContentView(android.R.layout.activity_list_item);

//https://stackoverflow.com/questions/23075446/how-to-check-automatic-date-and-time-is-enabled-or-not?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        try {
            if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                // Enabled

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                String formattedDate = df.format(c);

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                intent.putExtra("time", formattedDate);
                startActivity(intent);
                finish();

                //  Toast.makeText(this, "enabled"+formattedDate, Toast.LENGTH_SHORT).show();
            } else {
                // Disabed
                //https://stackoverflow.com/questions/12369062/how-can-i-call-the-date-time-settings-screen-from-my-android-application?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
                //Toast.makeText(this, "disabled", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Required");
                adb.setCancelable(false);
                adb.setMessage("Your automatic date and time was disabled you must enabled it to use our application");
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                adb.show();
                //   Toast.makeText(this, "disabled", Toast.LENGTH_SHORT).show();


            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}