package com.deepmodi.shareden.ui;

import com.deepmodi.shareden.Notifications.MyResponse;
import com.deepmodi.shareden.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key = AAAAevfvm38:APA91bHLj7n6s9UNxTGLWSC52HKyED91XYKArlmIDJW0dxugEsUyc4RofjuSG3s5KVLcH5spd1y84qOz7uykYWC3fe9szGdGlBedBMZ_maXjRzHH6DvXfv4rD7Iki4GWNEqqs86zroTW"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
