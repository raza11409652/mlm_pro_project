/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.flashbar.Flashbar;
import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.AlertFlash;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.FileUploadService;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.ResultResponse;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

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

public class CompleteProfile extends AppCompatActivity {
    ImageButton uploadImage;
    String TAG = CompleteProfile.class.getSimpleName(), profileImage = null;
    RequestApi api;
    Button createButton;
    EditText whatsAppEdt, companyEdt, stateEdt, countryEdt;
    String whatsAppNumber, company, state, country;
    Loader loader;
    Flashbar flashbar = null;
    AlertFlash alertFlash;
    SessionHandler sessionHandler;
    String _token ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);


        uploadImage = findViewById(R.id.image_uploader);
        whatsAppEdt = findViewById(R.id.whatsapp_number);
        companyEdt = findViewById(R.id.company_name);
        stateEdt = findViewById(R.id.state);
        countryEdt = findViewById(R.id.country);
        createButton = findViewById(R.id.create_btn);


        api = new RequestApi(this);
        sessionHandler = new SessionHandler(this);
        _token = "Bearer " + sessionHandler.getLoggedToken();
        loader = new Loader(this);

        alertFlash = new AlertFlash(this, this);

        uploadImage.setOnClickListener(v -> {
            Log.d(TAG, "onCreate:Image button Clicked ");

            //Intent for gallery selector

            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), Constant.GALLERY_OPEN);
        });

        createButton.setOnClickListener(v -> {
            whatsAppNumber = whatsAppEdt.getText().toString();
            company = companyEdt.getText().toString();
            state = stateEdt.getText().toString();
            country = countryEdt.getText().toString();
            if (StringHandler.isEmpty(whatsAppNumber)) {
                Log.d(TAG, "onCreate: Mobile number is required");
                return;
            }
            if (!StringHandler.isValidMobile(whatsAppNumber)) {
                return;
            }
            if (StringHandler.isEmpty(company)) {
                return;
            }
            if (StringHandler.isEmpty(state)) {
                return;
            }
            if (StringHandler.isEmpty(country)) {
                return;
            }
            JSONObject object = new JSONObject();
            try {
                object.put("imageStr", profileImage);
                object.put("whatsappContact", whatsAppNumber);
                object.put("companyName", company);
                object.put("state", state);
                object.put("country", country);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> saveProfile(object));


        });


    }

    private void saveProfile(JSONObject object) {
        loader.show(getString(R.string.loading));
        api.postRequest(object, response -> {
            loader.dismiss();
            Log.d(TAG, "saveProfile: " + response);
            try {
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //ok
                    sessionHandler.setLogin(true);
                    JSONObject data = response.getJSONObject("data");
                    String imaeg = response.getString("imageStr");
                    sessionHandler.setProfileImage(imaeg);

                    //Update screen set user loggedIn
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    updateScreen(intent);


                } else {
                    if (flashbar != null) {
                        flashbar = null;
                    }
                    flashbar = alertFlash.alertError(message + "");
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Server.UPDATE_PROFILE);
    }

    private void updateScreen(Intent intent) {

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
                uploadImage.setImageBitmap(bitmap);
//                uploadBitmap(bitmap);

                InputStream is = getContentResolver().openInputStream(data.getData());

                uploadImage(getBytes(is));


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Log.d(TAG, "onActivityResult: user cancel this action");
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
        Log.e(TAG, "uploadImage: " + fileName);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photos", fileName, requestFile);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token , body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().getStatus());
                    Log.d(TAG, "onResponse: " + response.body().getData().getLocation());
                    Log.d(TAG, "onResponse: " + response.message());
                    profileImage = response.body().getData().getLocation();
//                    selectorGallery.setVisibility(View.GONE);
//                    imageWrapper.setVisibility(View.VISIBLE);
//                    Picasso.get().load(imageUrl)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.placeholder)
//                            .into(imageWrapper);
                } else {
//                    selectorGallery.setText("Upload");
                    Toast.makeText(getApplicationContext(), "Error in Image Upload", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
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
}