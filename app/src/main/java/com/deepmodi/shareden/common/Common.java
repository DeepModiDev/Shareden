package com.deepmodi.shareden.common;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    public static final String USER_FINAL_NUMBER = "userNumber";
    public static final String USER_AUTH_ID = "userAuthId";
    public static final String USER_FINAL_PASSWORD = "userPassword";
    public static final String USER_IMAGE_LINK = "userImage";
    public static final String NOTIFICATION_SHAREDEN_NEWS_ID = "2";

    public static boolean checkInternetConnenction(Context context)
    {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null)
        {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if(info!=null)
            {
                for(int i=0; i<info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
