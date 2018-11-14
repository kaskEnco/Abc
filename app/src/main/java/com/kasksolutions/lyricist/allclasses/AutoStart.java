package com.kasksolutions.lyricist.allclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AutoStart extends BroadcastReceiver {

    MyBroadcastReceiver myBroadcastReceiver=new MyBroadcastReceiver();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            myBroadcastReceiver.setAlarm(context);
            Toast.makeText(context, "Repeat Action when boot", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Not at all", Toast.LENGTH_SHORT).show();
        }
    }
}
