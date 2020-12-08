/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;


import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {

    @Multipart
    @POST("upload/photo")

    @Headers({"authorization:Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6Ijk4NzY1NDMyMTEiLCJpYXQiOjE2MDc0NDg5ODd9.T7Mflyj6weVGRYiwca_3H7v3dpLh-gTiB6iHZ2ln3aU"})
    Call<ResultResponse> uploadImage(@Part MultipartBody.Part image);

}