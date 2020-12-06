/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class NewFcs extends AppCompatActivity {
    String title = "Fast Courier Service", type = String.valueOf(7);
    Toolbar toolbar;
    Button add;
    EditText nameEdt, addressEdtOne, addressEdtTwo, contactNumberEdt, whatsappNumberEdt,
            webLinkEdt, startingDateEdt, countryEdt, emailEdt, typeEdt;
    String name, addressOne, addressTwo, contactNumber, whatsappNumber, webLink,
            startDate = "NA", country, email, typeCourier = String.valueOf(0);
    SessionHandler sessionHandler;
    RequestApi api;
    Loader loader;
    Spinner typeSelector;


    String[] types = {"State level", "District level", "International level", "Rural level"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fcs);

        nameEdt = findViewById(R.id.company_name);
        addressEdtOne = findViewById(R.id.address_one);
        addressEdtTwo = findViewById(R.id.address_two);
        contactNumberEdt = findViewById(R.id.number);
        whatsappNumberEdt = findViewById(R.id.whatsapp_number);
//        webLinkEdt = findViewById(R.id.website_link);
//        startingDateEdt = findViewById(R.id.start_date);
//        countryEdt = findViewById(R.id.country);
        add = findViewById(R.id.add);
        emailEdt = findViewById(R.id.email);
//        typeEdt = findViewById(R.id.type);
        typeSelector = findViewById(R.id.type_selector);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        typeSelector.setAdapter(aa);


        typeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "onItemSelected: " + position);
                typeCourier = String.valueOf(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


//        startingDateEdt.setOnClickListener(v -> {
//            calenderBottomSheet = new BottomSheetDialog(this);
//            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.calander_selector, null);
//            calenderBottomSheet.setContentView(view);
//            CalendarView calendarView = view.findViewById(R.id.starting_date);
//            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//                @Override
//                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                    Log.d("TAG", "onSelectedDayChange: " + year);
//                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
////                    String finalStr = outputFormat.format(inputFormat.parse(val));
//                    String temp = year + "/" + month + "/" + dayOfMonth;
//                    startDate = temp;
//                    startingDateEdt.setText(startDate);
//                    calenderBottomSheet.dismiss();
//                }
//            });
//            calenderBottomSheet.show();
//        });
        add.setOnClickListener(v -> {

            name = nameEdt.getText().toString().trim();
            addressOne = addressEdtOne.getText().toString().trim();
            addressTwo = addressEdtTwo.getText().toString().trim();
            contactNumber = contactNumberEdt.getText().toString().trim();
            whatsappNumber = whatsappNumberEdt.getText().toString().trim();
            email = emailEdt.getText().toString();


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


            JSONObject a
                    = new JSONObject();
            try {
                a.put("senderID", sessionHandler.getLoggedInUser());
                a.put("senderName", sessionHandler.getUserName());
                a.put("senderImage", "image urt");
                a.put("postImage", "NA");
                a.put("companyName", name);
//                a.put("country", country);
                a.put("street1", addressOne);
                a.put("street2", addressTwo);
                a.put("websiteLink", "NA");
                a.put("phone", contactNumber);
                a.put("whatsappContact", whatsappNumber);
                a.put("productType", typeCourier);
                a.put("country", "Na");
                a.put("startingDate", startDate);
                a.put("planFile", "0");
                a.put("name", "0");
                a.put("time", "0");
                a.put("state", "0");
                a.put("email", email);
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