package com.kasksolutions.lyricist.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kasksolutions.lyricist.R;
import com.kasksolutions.lyricist.allclasses.Latest;

/**
 * Created by home on 20-Sep-18.
 */
public class FireBaseMessagingService
        extends FirebaseMessagingService
{
    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null)
        {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        else if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
              //  scheduleJob();
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }


        if (remoteMessage.getData().size()>0)
        {
            String title,message,imgUrl;
            title=remoteMessage.getData().get("title");
            message=remoteMessage.getData().get("message");
            imgUrl=remoteMessage.getData().get("img_url");

            Log.d("values100",""+title+"\n"+message+"\n"+imgUrl);

            Intent intent=new Intent(this,Latest.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
          //  Uri uri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.horsering);
            final NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setContentIntent(pendingIntent);
            builder.setSound(soundUri);
            builder.setSmallIcon(R.mipmap.ic_launcher);

            ImageRequest imageRequest=new ImageRequest(imgUrl, new Response.Listener<Bitmap>()
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
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(imageRequest);

        }

    }



}