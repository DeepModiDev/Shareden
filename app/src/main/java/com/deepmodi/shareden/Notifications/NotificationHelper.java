package com.deepmodi.shareden.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.deepmodi.shareden.R;
import java.lang.annotation.Target;

public class NotificationHelper extends ContextWrapper {

    public static final String NOTIFICATION_ID_1 = "NOTIFICATION_1";
    public static final String NOTIFICATION_CHANNEL_NAME_1 = "CHANNEL_1";

    public static final String NOTIFICATION_ID_2 = "NOTIFICATION_2";
    public static final String NOTIFICATION_CHANNEL_NAME_2 = "CHANNEL_2";

    private NotificationManager manager;

        public NotificationHelper(Context base) {
            super(base);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                createNotification();
            }
        }


        @TargetApi(Build.VERSION_CODES.O)
        private void createNotification()
        {
            NotificationChannel channel1 = new NotificationChannel(NOTIFICATION_ID_1,NOTIFICATION_CHANNEL_NAME_1, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.enableVibration(true);
            channel1.enableLights(true);
            channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel1);

            NotificationChannel channel2 = new NotificationChannel(NOTIFICATION_ID_2,NOTIFICATION_CHANNEL_NAME_2, NotificationManager.IMPORTANCE_DEFAULT);
            channel2.enableVibration(true);
            channel2.enableLights(true);
            channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(channel2);

        }

        public NotificationManager getManager()
        {
            if(manager == null)
            {
                manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            return manager;
        }

        public NotificationCompat.Builder getChannel1Builder(String title,String message)
        {
            return new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_ID_1)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher);
        }

        public NotificationCompat.Builder getChannel2Builder(String title,String message)
        {
            return new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_ID_2)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_camera);
        }
}
