/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewGrowthCompany extends AppCompatActivity {
    String title = "Current Growth Company", type = String.valueOf(6);
    Toolbar toolbar;
    Button add;
    EditText nameEdt, addressEdtOne, addressEdtTwo, contactNumberEdt, whatsappNumberEdt,
            webLinkEdt, startingDateEdt, countryEdt;
    String name, addressOne, addressTwo, contactNumber, whatsappNumber, webLink,
            startDate = "NA", country;
    SessionHandler sessionHandler;
    RequestApi api;
    Loader loader;

    Calendar calendar;
    BottomSheetDialog calenderBottomSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_growth_company);
        nameEdt = findViewById(R.id.company_name);
        addressEdtOne = findViewById(R.id.address_one);
        addressEdtTwo = findViewById(R.id.address_two);
        contactNumberEdt = findViewById(R.id.number);
        whatsappNumberEdt = findViewById(R.id.whatsapp_number);
        webLinkEdt = findViewById(R.id.website_link);
        startingDateEdt = findViewById(R.id.start_date);
        countryEdt = findViewById(R.id.country);
        add = findViewById(R.id.add);

        sessionHandler = new SessionHandler(this);
        loader = new Loader(this);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        api = new RequestApi(this);


        calendar = Calendar.getInstance();
        setTitle(title);


        startingDateEdt.setOnClickListener(v -> {
            calenderBottomSheet = new BottomSheetDialog(this);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.calander_selector, null);
            calenderBottomSheet.setContentView(view);
            CalendarView calendarView = view.findViewById(R.id.starting_date);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    Log.d("TAG", "onSelectedDayChange: " + year);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
//                    String finalStr = outputFormat.format(inputFormat.parse(val));
                    String temp = year + "/" + month + "/" + dayOfMonth;
                    startDate = temp;
                    startingDateEdt.setText(startDate);
                    calenderBottomSheet.dismiss();
                }
            });
            calenderBottomSheet.show();
        });
        add.setOnClickListener(v -> {

            name = nameEdt.getText().toString().trim();
            addressOne = addressEdtOne.getText().toString().trim();
            addressTwo = addressEdtTwo.getText().toString().trim();
            contactNumber = contactNumberEdt.getText().toString().trim();
            whatsappNumber = whatsappNumberEdt.getText().toString().trim();
            webLink = webLinkEdt.getText().toString().trim();
            country = countryEdt.getText().toString().trim();


            if (StringHandler.isEmpty(name)) {
                nameEdt.setError("Required");
                return;
            }
            if (StringHandler.isEmpty(addressOne)) {
                addressEdtOne.setError("Required");
                return;
            }
            if (StringHandler.isEmpty(addressTwo)) {
                addressEdtTwo.setError("Required");
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
            if (StringHandler.isEmpty(webLink)) {
                webLinkEdt.setError("Required");
                return;
            }
            //Now validate WEbsite
            if (StringHandler.isEmpty(country)) {
                countryEdt.setError("Reuqired");
                return;
            }

            JSONObject a
                    = new JSONObject();
            try {
                a.put("senderID", sessionHandler.getLoggedInUser());
                a.put("senderName", sessionHandler.getUserName());
                a.put("senderImage", "image urt");
                a.put("postImage", "NA");
                a.put("companyName", name);
                a.put("country", country);
                a.put("street1", addressOne);
                a.put("street2", addressTwo);
                a.put("websiteLink", webLink);
                a.put("phone", contactNumber);
                a.put("whatsappContact", whatsappNumber);
                a.put("productType", "0");
//                a.put("country", country);
                a.put("startingDate", startDate);
                a.put("planFile", "0");
                a.put("name", "0");
                a.put("time", "0");
                a.put("state", "0");
                a.put("email", "0");
                a.put("rank", "0");
                a.put("trainingInstitue", "0");
                a.put("courierType", "0");
                a.put("postType", type);


            } catch (JSONException e) {
                e.printStackTrace();
            }

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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}