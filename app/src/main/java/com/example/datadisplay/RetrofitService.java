package com.example.datadisplay;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
//    @FormUrlEncoded


    @POST("gettabledata.php")
    Call<ResponseBody>signIn(@Body LoginDTO loginDTO);
}
