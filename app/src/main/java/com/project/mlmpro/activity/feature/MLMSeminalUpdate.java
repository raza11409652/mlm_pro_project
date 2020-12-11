/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.adapter.MlmSeminarUpdateAdapter;
import com.project.mlmpro.model.FeaturePost;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//Trainning seminar update

public class MLMSeminalUpdate extends AppCompatActivity {

    Toolbar toolbar;
    RequestApi requestApi;

    ArrayList<FeaturePost> list = new ArrayList<>();
    MlmSeminarUpdateAdapter adapter;
    RecyclerView listView;

    EditText search_bar;
    String limit = String.valueOf(20), query = null, skip = String.valueOf(0);

    LinearLayoutManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_l_m_seminal_update);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.mlm_seminar_update));
        requestApi = new RequestApi(this);

        search_bar = findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() < 1) {
                    query = null;
                } else {
                    query = s.toString();
                }
                fetch(limit, skip, query);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        fetch(limit, skip, query);

        listView = findViewById(R.id.list_view);

        dataManager = new LinearLayoutManager(this);
        listView.setLayoutManager(dataManager);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = dataManager.findLastCompletelyVisibleItemPosition();
                if (lastItem == list.size() - 1) {
                    Log.d("TAG", "onScrolled: Reach last item");
                    int skipVal = Integer.parseInt(skip);
                    skipVal = skipVal + lastItem;
                    Log.d("TAG", "onScrolled: " + skipVal);
                    if (query == null) {
                        fetch(limit, String.valueOf(skipVal), query);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetch(limit, skip, query);
    }

    private void fetch(String limit, String skip, String query) {
//
        String url = null;
        if (query == null || query.length() < 1) {
            url = Server.GET_FEATURE + "?postType=4&limit=" + limit + "&skip=" + skip;
        } else {
            url = Server.GET_FEATURE + "?postType=4&searchTxt=" + query + "&limit=" + limit + "&skip=" + skip;
        }
        requestApi.getRequest(url, response -> {
//            Log.d(TAG, "fetch: " + response);
            try {
                JSONObject object = new JSONObject(response);
                String message = object.getString("message");
                int status = object.getInt("status");
                if (status == 200) {
//                    Toast.makeText()
                    JSONObject data = object.getJSONObject("data");
                    JSONArray array = data.getJSONArray("posts");
                    if (array.length() < 1) {
                        Toast.makeText(getApplicationContext(), "No list found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(skip.equals("0")){
                        list = new ArrayList<>();
                    }
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
                    adapter = new MlmSeminarUpdateAdapter(list, this);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.new_post_menu) {
//            open Add Men list
            Intent newF = new Intent(getApplicationContext(), NewTrainingUpdate.class);
//            newF.putExtra("title", getString(R.string.mlm_company_list));
//            newF.putExtra("type", String.valueOf(0));
            startActivity(newF);

        }
        return super.onOptionsItemSelected(item);

    }
}