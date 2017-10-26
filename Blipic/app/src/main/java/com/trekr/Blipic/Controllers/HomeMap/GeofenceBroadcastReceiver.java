package com.trekr.Blipic.Controllers.HomeMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.trekr.R;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;

        switch (event.getGeofenceTransition()) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                 notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Geofence action")
                    .setContentText("You entered to region")
                    .setTicker("Geofence action")
                    .build();
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:

                break;
        }

        nm.notify(0, notification);
    }
}
