package com.nairaland.snakevpn.interfaces;

import com.nairaland.snakevpn.model.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerAPI {

    @GET("api/posts")
    Call<List<Api>> getApis();
}
