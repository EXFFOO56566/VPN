package com.nairaland.snakevpn.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAVdPNnXk:APA91bG8xx77K_dR1_1e34sLGuRuy1J61h8HBF5gbwhiuNegy3oYXE2Gqca0hLdGtZaq7z9ywEG5Z8Syk1UAyJ_dHAQgWO2iRERz27rNuh0VthYVWxoZaw0igN6NOqZLrLVYrFNEz5xH"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
