package com.kasksolutions.lyricist.allclasses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.io.File;
import java.util.Calendar;

public class MyBroadcastReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        deleteCache(context);
//        Notification notification = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Cache")
//                .setContentText("Your Chache deleted succesfully")
//                .build();
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(1, notification);
//
//        // Put here YOUR code.
//        Toast.makeText(context, "Wait next 24hrs", Toast.LENGTH_LONG).show(); // For example

        wl.release();
    }


    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MyBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        //am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),60000,pi);
        // am.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),1000*60,pi);
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7 , pi); // Millisec * Second * Minute


    }
    public static void deleteCache(Context context)
    {
        try
        {
            File dir = context.getCacheDir();
            deleteDir(dir);
            //   Toast.makeText(context, "delete cache", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
