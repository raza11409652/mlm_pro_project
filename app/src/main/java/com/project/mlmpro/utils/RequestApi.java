/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.mlmpro.model.ImageDataPart;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/*
Request Utility for Making API call
POST , GET , PUT
 */
public class RequestApi {
    String token = null;
    Context context;
    SessionHandler sessionHandler;
    String TAG = RequestApi.class.getSimpleName();
    RequestQueue queue;

    public RequestApi(Context context) {
        this.context = context;
        sessionHandler = new SessionHandler(context);
        token = "Bearer " + sessionHandler.getLoggedToken();
        Log.d(TAG, "RequestApi: " + token);
        queue = Volley.newRequestQueue(context);
    }

    public void validateSession(Response.Listener<String> s) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.TOKEN_LOGIN, s, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("authorization", token);
                return map;
            }
        };
        queue.add(stringRequest);
    }

    public void postRequest(JSONObject object, Response.Listener<JSONObject> success, String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                success, error -> Log.d(TAG, "onErrorResponse: " + error.getMessage())) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("authorization", token);
                return map;
            }
        };

        queue.add(request);
    }

    public void getRequest(String url, Response.Listener<String> success) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, success, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("authorization", token);
                return map;
            }
        };
        queue.add(stringRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void uploadImage(Bitmap bitmap, Response.Listener<NetworkResponse> success) {
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, Server.UPLOAD_PHOTO, success, error -> {
            Log.d(TAG, "uploadImage: " + error.getMessage());

        }) {
            @Override
            protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
                return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
//                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> map = new HashMap<>();
                map.put("authorization", token);
                return map;
            }

            @Override
            protected Map<String, ImageDataPart> getByteData() {
                Map<String, ImageDataPart> params = new HashMap<>();
                long imageName = System.currentTimeMillis();
                params.put("photos", new ImageDataPart(imageName + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }
}
