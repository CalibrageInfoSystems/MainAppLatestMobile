package com.cis.palm360.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitInterface {

    @GET()
    @Streaming
    Call<ResponseBody> downloadImage(@Url String fileUrl);
}
