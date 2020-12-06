/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class NewTopCompanyNetword extends AppCompatActivity {
    String title = "Top Network Company", type = String.valueOf(1);
    Toolbar toolbar;
    Button add;
    EditText companyNameEdt, addressEdtOne, addressEdtTwo, contactNumberEdt, whatsappNumberEdt,
            webLinkEdt, startingDateEdt, countryEdt, nameEdt, stateEdt, emailEdt;
    String name, companyName, addressOne, addressTwo, contactNumber, whatsappNumber, webLink,
            startDate = "NA", country, state, email;
    SessionHandler sessionHandler;
    RequestApi api;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_top_company_netword);
        companyNameEdt = findViewById(R.id.company_name);
//        addressEdtOne = findViewById(R.id.address_one);
//        addressEdtTwo = findViewById(R.id.address_two);
        contactNumberEdt = findViewById(R.id.number);
        emailEdt
                = findViewById(R.id.email);
        nameEdt = findViewById(R.id.name);

        whatsappNumberEdt = findViewById(R.id.whatsapp_number);
//        webLinkEdt = findViewById(R.id.website_link);
//        startingDateEdt = findViewById(R.id.start_date);
//        countryEdt = findViewById(R.id.country);
        add = findViewById(R.id.add);
        stateEdt = findViewById(R.id.state);
//
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
        setTitle("Top Network Company");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyName = companyNameEdt.getText().toString().trim();
                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                state = stateEdt.getText().toString();


                contactNumber = contactNumberEdt.getText().toString().trim();
                whatsappNumber = whatsappNumberEdt.getText().toString().trim();


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
                if (StringHandler.isEmailValid(email) == false) {
                    emailEdt.setError("Invalid");
                    return;
                }


                JSONObject a
                        = new JSONObject();
                try {
                    a.put("senderID", sessionHandler.getLoggedInUser());
                    a.put("senderName", sessionHandler.getUserName());
                    a.put("senderImage", "image urt");
                    a.put("postImage", "NA");
                    a.put("companyName", companyName);
                    a.put("country", "NA");
                    a.put("street1", "NA");
                    a.put("street2", "NA");
                    a.put("websiteLink", "NA");
                    a.put("phone", contactNumber);
                    a.put("whatsappContact", whatsappNumber);
                    a.put("productType", "0");
                    a.put("country", "NA");
                    a.put("startingDate", "NA");
                    a.put("planFile", "0");
                    a.put("name", name);
                    a.put("time", "0");
                    a.put("state", state);
                    a.put("email", email);
                    a.put("rank", "0");
                    a.put("trainingInstitue", "0");
                    a.put("courierType", "0");
                    a.put("postType", type);
                    Log.d("TAG", "onClick: " + a);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loader.show("Please wait ...");
                saveData(a);
            }
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}