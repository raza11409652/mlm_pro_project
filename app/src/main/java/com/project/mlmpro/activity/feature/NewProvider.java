/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.activity.Subscription;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.FileUploadService;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.ResultResponse;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;
import com.project.mlmpro.utils.TimeDiff;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewProvider extends AppCompatActivity {
    String title = "Product Service Provider", type = String.valueOf(8) , imageUrl;
    Toolbar toolbar;
    Button add;
    EditText nameEdt, addressEdtOne, addressEdtTwo, contactNumberEdt, whatsappNumberEdt,
            webLinkEdt, startingDateEdt, countryEdt, emailEdt, productEdt;
    String name, addressOne, addressTwo, contactNumber, whatsappNumber, webLink,
            startDate = "NA", country, productType, email;
    SessionHandler sessionHandler;
    RequestApi api;
    Loader loader;

    String _token ;
    ImageButton imageButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_provider);

        //New Provider

        nameEdt = findViewById(R.id.company_name);
        addressEdtOne = findViewById(R.id.address_one);
        addressEdtTwo = findViewById(R.id.address_two);
        contactNumberEdt = findViewById(R.id.number);
        whatsappNumberEdt = findViewById(R.id.whatsapp_number);
        emailEdt = findViewById(R.id.email);
        productEdt = findViewById(R.id.product_type);
//        startingDateEdt = findViewById(R.id.start_date);
        countryEdt = findViewById(R.id.country);
        add = findViewById(R.id.add);

//        title = getIntent().getStringExtra("title");
//        type = getIntent().getStringExtra("type");
        sessionHandler = new SessionHandler(this);
        loader = new Loader(this);

        _token = "Bearer " + sessionHandler.getLoggedToken();
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        api = new RequestApi(this);

        imageButton = findViewById(R.id.image_uploader) ;
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), Constant.GALLERY_OPEN);
            }
        });

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
            email = emailEdt.getText().toString().trim();
            productType = productEdt.getText().toString().trim();
//            webLink = webLinkEdt.getText().toString().trim();
//            country = countryEdt.getText().toString().trim();


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
            if (StringHandler.isEmailValid(email) == false) return;
//            if ()
//            if (StringHandler.isEmpty(webLink)) {
//                webLinkEdt.setError("Required");
//                return;
//            }
            //Now validate WEbsite
//            if (StringHandler.isEmpty(country)) {
//                countryEdt.setError("Reuqired");
//                return;
//            }

            JSONObject a
                    = new JSONObject();
            try {
                a.put("senderID", sessionHandler.getLoggedInUser());
                a.put("senderName", sessionHandler.getUserName());
                a.put("senderImage", "image urt");
                a.put("postImage", imageUrl);
                a.put("companyName", name);
                a.put("country", "NA");
                a.put("street1", addressOne);
                a.put("street2", addressTwo);
                a.put("websiteLink", "NA");
                a.put("phone", contactNumber);
                a.put("whatsappContact", whatsappNumber);
                a.put("productType", productType);
//                a.put("country", country);
                a.put("startingDate", "NA");
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

//            loader.show("Please wait ...");
            saveData(a);


        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
//            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                //displaying selected image to image view
                imageButton.setImageBitmap(bitmap);
//                uploadBitmap(bitmap);

                InputStream is = getContentResolver().openInputStream(data.getData());

                uploadImage(getBytes(is));


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
//            Log.d(TAG, "onActivityResult: user cancel this action");
        }
    }
    private void uploadImage(byte[] bytes) {
//        selectorGallery.setText("Image is being uploaded");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        double random = Math.random();
        String fileName = random + "_profile_" + System.currentTimeMillis() + ".jpg";
//        Log.e(TAG, "uploadImage: " + fileName);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photos", fileName, requestFile);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token , body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    imageUrl = response.body().getData().getLocation();
                } else {
//                    selectorGallery.setText("Upload");
                    Toast.makeText(getApplicationContext(), "Error in Image Upload", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

//                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
//                selectorGallery.setText("Upload");
            }
        });


    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }
    private void saveData(JSONObject a) {

        Log.d("TAG", "saveData: "  + Constant.PURCHASED_SUBSCRIPTION_TYPE);
        Log.d("TAG", "saveData: "  + Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON);
        String expiry = TimeDiff.convertMongoDate(Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON) ;
        if (Constant.PURCHASED_SUBSCRIPTION_TYPE!=null){
            String diff  =TimeDiff.diffO(Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON) ;
            Log.d("TAG", "saveData: "  +diff);
            if (diff==null){
                Intent purchase = new Intent(getApplicationContext()  , Subscription.class) ;
                startActivity(purchase)  ;
                return;
            }
            if (diff.equals("0") ||diff==null){
                Intent purchase = new Intent(getApplicationContext()  , Subscription.class) ;
                startActivity(purchase)  ;
                return;
            }else{
                saveInit(a);
            }
        }else if(Constant.PURCHASED_SUBSCRIPTION_TYPE==null){
            Intent purchase = new Intent(getApplicationContext()  , Subscription.class) ;
            startActivity(purchase)  ;
            return;
        }else{
//            saveInit(a);
//            loader.show("Please wait..");
//            api.postRequest(a, response -> {
//                Log.d("TAG", "saveData: " + response);
//                loader.dismiss();
//                try {
//                    String msg = response.getString("message");
//                    int status = response.getInt("status");
//                    if (status == 200) {
//                        Toast.makeText(getApplicationContext(), msg,
//                                Toast.LENGTH_SHORT).show();
//                        finish();
//                        return;
//                    } else {
//                        Toast.makeText(getApplicationContext(), msg
//                                , Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }, Server.GET_FEATURE);
        }




    }
    private void saveInit(JSONObject a) {
        loader.show("Please wait..");
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