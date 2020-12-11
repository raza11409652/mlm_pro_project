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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.FileUploadService;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.ResultResponse;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateProfile extends AppCompatActivity {
    SessionHandler sessionHandler;
    Button update;
    CircleImageView profileImage;
    EditText nameEdt, emailEdt, companyName;
    String name, email, companyNameStr, image;
    RequestApi api;
    String _token;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        sessionHandler = new SessionHandler(this);
        profileImage = findViewById(R.id.profile_image);
        nameEdt = findViewById(R.id.name);
        emailEdt = findViewById(R.id.email);
        companyName = findViewById(R.id.company_name);
        update = findViewById(R.id.update_profile);
        loader = new Loader(this);

        _token = "Bearer " + sessionHandler.getLoggedToken();


        api = new RequestApi(this);
        nameEdt.setText(sessionHandler.getUserName());
        emailEdt.setText(sessionHandler.getUserEmail());
        companyName.setText(sessionHandler.getCompanyName());
        Picasso.get().load(sessionHandler.getProfileImage())
                .error(R.drawable.profile)
                .placeholder(R.drawable.placeholder)
                .into(profileImage);
        profileImage.setOnClickListener(v -> {
            //Open intent for image selector
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), Constant.GALLERY_OPEN);
        });
        update.setOnClickListener(v -> {
            name = nameEdt.getText().toString();
            email = emailEdt.getText().toString();
            companyNameStr = companyName.getText().toString();
            if (StringHandler.isEmpty(name)) {
                nameEdt.setError("Required");
                return;
            }
            if (!StringHandler.isEmailValid(email)) {
                emailEdt.setError("Invalid");
                return;
            }
            JSONObject object = new JSONObject();
            try {
                object.put("fullName", name);
                object.put("email", email);
                object.put("companyName", companyNameStr);
                if (image != null) {
                    object.put("imageStr", image);
                }

//                object.put("state", state);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            api.postRequest(object, response -> {
                Log.d("TAG", "onCreate: " + response);
                try {
//                    sessionHandler.setProfileImage();
                    int status = response.getInt("status");
                    String message = response.getString("message");
                    if (status == 200) {
                        JSONObject data = response.getJSONObject("data");
                        String _email = data.getString("email");
                        String _companyName = data.getString("companyName");
                        String _image = data.getString("imageStr");
                        String _name = data.getString("fullName");
                        sessionHandler.setProfileImage(_image);
                        sessionHandler.setCompanyName(_companyName);
                        sessionHandler.setLoggedInEmail(_email);
                        sessionHandler.setUserName(_name);
                        Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        Intent home = new Intent(getApplicationContext(), Home.class);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(home);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, Server.UPDATE_PROFILE);

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
                profileImage.setImageBitmap(bitmap);
//                uploadBitmap(bitmap);

                InputStream is = getContentResolver().openInputStream(data.getData());

                uploadImage(getBytes(is));


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Log.d("TAG", "onActivityResult: user cancel this action");
        }
    }

    private void uploadImage(byte[] bytes) {
//        selectorGallery.setText("Image is being uploaded");
        loader.show("Please wait..");

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
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token, body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
//                    Log.d(TAG, "onResponse: " + response.body().getStatus());
//                    Log.d(TAG, "onResponse: " + response.body().getData().getLocation());
//                    Log.d(TAG, "onResponse: " + response.message());
                    image = response.body().getData().getLocation();
                    Log.d("TAG", "onResponse: " + image);
                    loader.dismiss();

                } else {
                    loader.dismiss();

//                    selectorGallery.setText("Upload");
                    Toast.makeText(getApplicationContext(), "Error in Image Upload", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                loader.dismiss();

                Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
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