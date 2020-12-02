/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
        token = sessionHandler.getLoggedToken();
        Log.d(TAG, "RequestApi: " + token);
        queue = Volley.newRequestQueue(context);
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
}
