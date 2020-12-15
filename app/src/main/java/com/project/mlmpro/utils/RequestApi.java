/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
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
            public Map<String, String> getHeaders() {
//                Log.d(TAG, "getHeaders: " + token);
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

//    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }


    public void uploadImage(Bitmap bitmap, Response.Listener<JSONObject> success) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject();
//            String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
//            jsonObject.put("name", imgname);
//            jsonObject.put("photos", encodedImage);
//            // jsonObject.put("aa", "aa");
//        } catch (JSONException e) {
//            Log.e("JSONObject Here", e.toString());
//        }
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Server.UPLOAD_PHOTO,
//                jsonObject,
//                success, volleyError -> Log.e("aaaaaaa", volleyError.toString())){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String ,String>map = new HashMap<>() ;
//                map.put("photos"  , encodedImage) ;
//                return map;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("authorization", token);
//                return map;
//            }
//
//        };
//
//        queue.add(jsonObjectRequest);



    }
}
