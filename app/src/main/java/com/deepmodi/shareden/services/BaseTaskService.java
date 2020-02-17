package com.deepmodi.shareden.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.deepmodi.shareden.R;

public class BaseTaskService extends Service {
    private static final String CHANNEL_ID_DEFAULT = "uploadDefaultChannel";

    static final int PROGRESS_NOTIFICATION_ID = 0;
    static final int FINISHED_NOTIFICATION_ID = 1;

    private static final String TAG ="BaseTaskService";
    private int numberTask = 0;

    public void taskStarted()
    {
        changeNumberOfTask(1);
    }

    public void taskCompleted()
    {
        changeNumberOfTask(-1);
    }

    private synchronized void changeNumberOfTask(int delta)
    {
        Log.d(TAG,"ChangeNumberOfTask :"+numberTask+":"+delta);
        numberTask+=delta;

        if(numberTask <=0)
        {
            Log.d(TAG,"stopping task");
            stopSelf();
        }
    }

    private void createDefaultChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DEFAULT,"uploadDefaultChannel",NotificationManager.IMPORTANCE_DEFAULT);
        }
    }

    protected void showProgressNotification(String caption,long completedUnits,long totalUnits)
    {
        int percentComplete = 0;
        if(totalUnits > 0)
        {
            percentComplete = (int)(100 * completedUnits / totalUnits);
        }
        createDefaultChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setProgress(100,percentComplete,false)
                .setContentText(caption)
                .setOngoing(true)
                .setAutoCancel(false);
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        manager.notify(PROGRESS_NOTIFICATION_ID,builder.build());
    }

    protected void showFinishedNotification(String caption, Intent intent,boolean success)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        createDefaultChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(caption)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(FINISHED_NOTIFICATION_ID,builder.build());
    }

    protected void dismissProgressNotification()
    {
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(PROGRESS_NOTIFICATION_ID);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
