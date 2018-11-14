package com.kasksolutions.lyricist.allclasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service
{
    MyBroadcastReceiver myBroadcastReceiver=new MyBroadcastReceiver();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        myBroadcastReceiver.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        myBroadcastReceiver.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
