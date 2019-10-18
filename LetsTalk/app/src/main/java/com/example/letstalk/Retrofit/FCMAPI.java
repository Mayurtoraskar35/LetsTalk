package com.example.letstalk.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMAPI {

    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAAZqKoG58:APA91bGabR6V10jnCaG3KDmiMKAC00mwbHY9xFrGVuNC76TmhPNmbuMKbkyeMJctJMG6mumLf2qxybDq70zuVKr2S8MQyy37-I3mlDFC853CqE1jiCYcFQ0O2ljukKa0ETQZEmvwrnup"
    })
    @POST("/fcm/send")
    Call<ResponseBody> sendMessage(@Body MessageEntity messageEntity);
}
