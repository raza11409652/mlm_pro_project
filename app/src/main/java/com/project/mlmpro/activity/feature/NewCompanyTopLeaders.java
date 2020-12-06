/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class NewCompanyTopLeaders extends AppCompatActivity {
    String title = "Company Top Leaders", type = String.valueOf(3);
    Toolbar toolbar;
    Button add;
    EditText nameEdt, addressEdtOne, addressEdtTwo, contactNumberEdt, whatsappNumberEdt,
            webLinkEdt, startingDateEdt, countryEdt, timeEdt, emailEdt, rankEdt;
    String name, addressOne, addressTwo, contactNumber, whatsappNumber, webLink,
            startDate = "NA", country, time, email, rank;
    SessionHandler sessionHandler;
    RequestApi api;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_company_top_leaders);

        nameEdt = findViewById(R.id.name);
        addressEdtOne = findViewById(R.id.address_one);
        addressEdtTwo = findViewById(R.id.address_two);
        contactNumberEdt = findViewById(R.id.number);
        whatsappNumberEdt = findViewById(R.id.whatsapp_number);
        webLinkEdt = findViewById(R.id.website_link);
        emailEdt = findViewById(R.id.email);
        timeEdt = findViewById(R.id.time);

        rankEdt = findViewById(R.id.rank);
//        startingDateEdt = findViewById(R.id.start_date);
        countryEdt = findViewById(R.id.country);
        add = findViewById(R.id.add);

//        title = getIntent().getStringExtra("title");
//        type = getIntent().getStringExtra("type");
        sessionHandler = new SessionHandler(this);
        loader = new Loader(this);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        api = new RequestApi(this);


//        calendar = Calendar.getInstance();
        setTitle(title);


        add.setOnClickListener(v -> {

            name = nameEdt.getText().toString().trim();
//            addressOne = addressEdtOne.getText().toString().trim();
//            addressTwo = addressEdtTwo.getText().toString().trim();
            contactNumber = contactNumberEdt.getText().toString().trim();
            whatsappNumber = whatsappNumberEdt.getText().toString().trim();
            webLink = webLinkEdt.getText().toString().trim();
            country = countryEdt.getText().toString().trim();
            time = timeEdt.getText().toString().trim();
            rank = rankEdt.getText().toString();

            email = emailEdt.getText().toString();

            if (StringHandler.isEmpty(name)) {
                nameEdt.setError("Required");
                return;
            }


            if (!StringHandler.isValidMobile(contactNumber)) {
                contactNumberEdt.setError("Invalid");
                return;
            }
            if (!StringHandler.isValidMobile(whatsappNumber)) {
                whatsappNumberEdt.setError("Invalid");
                return;
            }

            //Now validate WEbsite
            if (StringHandler.isEmpty(country)) {
                countryEdt.setError("Required");
                return;
            }

            JSONObject a
                    = new JSONObject();
            try {
                a.put("senderID", sessionHandler.getLoggedInUser());
                a.put("senderName", sessionHandler.getUserName());
                a.put("senderImage", "image urt");
                a.put("postImage", "NA");
                a.put("companyName", "NA");
                a.put("country", "NA");
                a.put("street1", "NA");
                a.put("street2", "NA");
                a.put("websiteLink", webLink);
                a.put("phone", contactNumber);
                a.put("whatsappContact", whatsappNumber);
                a.put("productType", "0");
                a.put("country", country);
                a.put("startingDate", "NA");
                a.put("planFile", "0");
                a.put("name", name);
                a.put("time", time);
                a.put("state", "0");
                a.put("email", email);
                a.put("rank", rank);
                a.put("trainingInstitue", "0");
                a.put("courierType", "0");
                a.put("postType", type);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("TAG", "onCreate: " + a);
            loader.show("Please wait ...");
            saveData(a);


        });

    }

    private void saveData(JSONObject a) {
        api.postRequest(a, response -> {
            Log.d("TAG", "saveData: " + response);
            loader.dismiss();
            try {
                String msg = response.getString("message");
                int status = response.getInt("status");
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), msg,
                            Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), msg
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Server.GET_FEATURE);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}