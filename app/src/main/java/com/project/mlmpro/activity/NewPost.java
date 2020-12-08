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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPost extends AppCompatActivity {

    SessionHandler sessionHandler;
    Toolbar toolbar;
    String imageUrl = "NA", postData = null, sender, senderName, senderImage = "N/A";
    EditText postEdt;
    Button post;
    String TAG = NewPost.class.getSimpleName();
    RequestApi api;
    Loader loader;

    ImageView imageWrapper;

    Button selectorGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        toolbar = findViewById(R.id.toolbar);
        post = findViewById(R.id.post_btn);
        postEdt = findViewById(R.id.post_data);
        selectorGallery = findViewById(R.id.selector_gallery);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.new_post_heading));

        sessionHandler = new SessionHandler(this);
        api = new RequestApi(this);
        sender = sessionHandler.getLoggedInUser();
        senderName = sessionHandler.getUserName();
//        senderImage = null;
        loader = new Loader(this);
        imageWrapper = findViewById(R.id.image_wrapper);


        post.setOnClickListener(v -> {
            postData = postEdt.getText().toString().trim();
//            Log.d(TAG, "onCreate: " + postData);
            if (StringHandler.isEmpty(postData)) {
                Toast.makeText(getApplicationContext(), "Please write something", Toast.LENGTH_SHORT).show();
                return;
            }


//            Log.d(TAG, "onCreate: ");
            try {
                JSONObject object = new JSONObject();
                object.put("senderID", sender);
                object.put("senderName", senderName);
                object.put("postText", postData);
                object.put("postImage", imageUrl);
                object.put("senderImage", senderImage);
                Log.d(TAG, "onCreate: " + object);
                postCreate(object);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

        selectorGallery.setOnClickListener(v -> {
            selectImage();

        });
        imageWrapper.setOnClickListener(v -> selectImage());


    }


    private void selectImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery.setType("*/*");
//            gallery.setAction(Intent.ACTION_PICK);
//            gallery.seAC
        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), Constant.GALLERY_IMAGE_VIDEO);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.GALLERY_IMAGE_VIDEO) {
            try {
                Uri uri = data.getData();
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("image")) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    Log.d(TAG, "onActivityResult: image selected");

                    InputStream is = getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));


                    //handle image
                } else if (selectedMediaUri.toString().contains("video")) {
                    Log.d(TAG, "onActivityResult: video selected");
                    //handle video
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(byte[] bytes) {
        selectorGallery.setText("Image is being uploaded");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        double random  = Math.random() ;
        String fileName = random +"_"+System.currentTimeMillis() +".jpg";
        Log.e(TAG, "uploadImage: " +fileName );
        MultipartBody.Part body = MultipartBody.Part.createFormData("photos", fileName, requestFile);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().getStatus());
                    Log.d(TAG, "onResponse: " + response.body().getData().getLocation());
                    Log.d(TAG, "onResponse: " + response.message());
                    imageUrl = response.body().getData().getLocation();
                    selectorGallery.setVisibility(View.GONE);
                    imageWrapper.setVisibility(View.VISIBLE);
                    Picasso.get().load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(imageWrapper);
                } else {
                    selectorGallery.setText("Upload");
                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
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

    private void postCreate(JSONObject object) {
        loader.show(getString(R.string.loading));
        api.postRequest(object, response -> {
            Log.d(TAG, "postCreate: " + response);
            loader.dismiss();

            Toast.makeText(getApplicationContext() , "Post Success" , Toast.LENGTH_SHORT).show();
            finish();
        }, Server.NEW_POST);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}