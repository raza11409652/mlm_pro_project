/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.adapter.CurrentGrowthAdapter;
import com.project.mlmpro.model.FeaturePost;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class VerifiedCompany extends Fragment {
    RequestApi api;
    EditText search_bar;
    String limit = String.valueOf(20), query = null, skip = String.valueOf(0);

//    RequestApi api;

    RecyclerView listView;
    ArrayList<FeaturePost> list = new ArrayList<>();
    CurrentGrowthAdapter adapter;
    LinearLayoutManager dataManager;

    public VerifiedCompany() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new RequestApi(getContext());
        fetch(limit , skip , query);


    }

    private void fetch(String limit , String skip , String query) {
        String url = null;
        if (query == null || query.length() < 1) {
            url = Server.GET_FEATURE + "?postType=6&limit="+limit+"&skip="+skip;
        } else {
            url = Server.GET_FEATURE + "?postType=7&searchTxt=" + query+"&limit="+limit+"&skip="+skip;
        }
        api.getRequest(url, response -> {
            Log.d("TAG", "fetch: " + response);
            try {
                JSONObject object = new JSONObject(response);
                String message = object.getString("message");
                int status = object.getInt("status");
                if (status == 200) {
//                    Toast.makeText()
//                    JSONObject data = object.getJSONObject("data");
//                    JSONArray array = object.getJSONArray("data");
                    JSONObject data = object.getJSONObject("data");
                    JSONArray array = data.getJSONArray("posts");
                    if (array.length() < 1) {
//                        Toast.makeText(getApplicationContext(), "No list found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (skip.equals("0")) {
                        list = new ArrayList<>();
                    }
//                    list = new ArrayList<>();
                    Log.d("TAG", "fetch: " + array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject single = array.getJSONObject(i);
                        String senderID = single.getString("senderID");
                        String senderName = single.getString("senderName");
                        String image = single.getString("senderImage");
                        String postImage = single.getString("postImage");
                        String companyName = single.getString("companyName");
                        String planFile = single.getString("planFile");
                        String name = single.getString("name").toString();
                        String websiteLike = single.getString("websiteLink");
                        String startingDate = single.getString("startingDate");
                        String phone = single.getString("phone");
                        String email = single.getString("email");
                        String rank = single.getString("rank");
                        String time = single.getString("time");
                        String trainingInstitue = single.getString("trainingInstitue");
                        String productType = single.getString("productType");
                        String courierType = single.getString("courierType");
                        String street1 = single.getString("street1");
                        String street2 = single.getString("street2");
                        String state = single.getString("state");
                        String country = single.getString("country");
                        String postType = single.getString("postType");
                        String whatsappContact = single.getString("whatsappContact");
                        String statusP = single.getString("status");
                        String createdAt = single.getString("createdAt");
                        FeaturePost featurePost = new FeaturePost(senderID, senderName, image, postImage, companyName, planFile, name,
                                websiteLike, startingDate, phone, email, rank, time, trainingInstitue, productType,
                                courierType, street1, street2, state, country, postType, whatsappContact, statusP, createdAt);
                        list.add(featurePost);
                    }
                    adapter = new CurrentGrowthAdapter(list, getContext());
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
//                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view);
        listView.setHasFixedSize(true);
        search_bar = view.findViewById(R.id.search_bar);
//        listView.setHasFixedSize(true);

        dataManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(dataManager) ;

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItem = dataManager.findLastCompletelyVisibleItemPosition();
                Log.d("TAG", "onScrolled: " + lastItem);
                if (lastItem ==list.size()-1){
                    Log.d("TAG", "onScrolled: Reach last item" );
                    int skipVal = Integer.parseInt(skip);
                    skipVal = skipVal + list.size()  ;
//                    Log.d(TAG, "onScrolled: "  +skipVal);
                    if(query==null){
                        fetch(limit , String.valueOf(skipVal), query );
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                query = s.toString();
                fetch(limit , skip ,query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verified_company, container, false);
    }
}