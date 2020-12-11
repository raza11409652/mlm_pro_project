/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
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
import com.project.mlmpro.utils.FilePath;
import com.project.mlmpro.utils.FileUploadService;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.ResultResponse;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.TimeDiff;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class NewPlanDetails extends AppCompatActivity {

    String imageUrl = null, planFile, videoUrl;
    Toolbar toolbar;
    RequestApi requestApi;

    SessionHandler sessionHandler;
    ImageButton imageButton;
    Button pdfUpload, videoUpload;
    String mediaPath, mediaPath1;
    //    ImageView imgView;
    String[] mediaColumns = {MediaStore.Video.Media._ID};
    //    ProgressDialog progressDialog;
//    TextViewtView str1, str2;
    Loader loader;
    String _token ;
//    EditText search_bar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan_details);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        sessionHandler = new SessionHandler(this);
        loader = new Loader(this);
        requestApi = new RequestApi(this);

        _token = "Bearer " + sessionHandler.getLoggedToken();

        setTitle("Company Plan Details");
        imageButton = findViewById(R.id.image_uploader);
        pdfUpload = findViewById(R.id.upload_pdf);
        videoUpload = findViewById(R.id.upload_video);
        checkForSubs();

//        search_bar  =findViewById(R.id.search_bar);

        videoUpload.setOnClickListener(v -> {


            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, Constant.VIDEO_OPEN);
        });

        imageButton.setOnClickListener(v -> {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), Constant.GALLERY_OPEN);
        });

        pdfUpload.setOnClickListener(v -> {
            Intent intent = new Intent();

            intent.setType("application/pdf");

            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), Constant.OPEN_FOR_PDF);
        });


    }

    private void checkForSubs() {
        Log.d("TAG", "saveData: " + Constant.PURCHASED_SUBSCRIPTION_TYPE);
        Log.d("TAG", "saveData: " + Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON);
        String expiry = TimeDiff.convertMongoDate(Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON);
        if (Constant.PURCHASED_SUBSCRIPTION_TYPE != null) {
            String diff = TimeDiff.diffO(Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON);
            Log.d("TAG", "saveData: " + diff);

            if (diff == null) {
                Intent purchase = new Intent(getApplicationContext(), Subscription.class);
                startActivity(purchase);
                return;
            }
            if (diff.equals("0") || diff == null) {
                Intent purchase = new Intent(getApplicationContext(), Subscription.class);
                startActivity(purchase);
                return;
            } else {

            }
        } else {
            Intent purchase = new Intent(getApplicationContext(), Subscription.class);
            startActivity(purchase);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
//            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            switch (requestCode) {
                case Constant.GALLERY_OPEN:
                    try {
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                        //displaying selected image to image view
                        imageButton.setImageBitmap(bitmap);

                        InputStream is = getContentResolver().openInputStream(data.getData());

                        uploadImage(getBytes(is));


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.OPEN_FOR_PDF:

                    Uri uri = data.getData();
                    Log.d("TAG", "onActivityResult: " + uri);
                    uploadPdf(uri);

                    break;

                case Constant.VIDEO_OPEN:

                    Uri selectedVideo = data.getData();
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    mediaPath1 = cursor.getString(columnIndex);
//                    str2.setText(mediaPath1);
                    // Set the Video Thumb in ImageView Previewing the Media
//                    imgView.setImageBitmap(getThumbnailPathForLocalFile(MainActivity.this, selectedVideo));
                    cursor.close();
                    uploadVideo(mediaPath1);
                    break;

            }


        } else {
//            Log.d(TAG, "onActivityResult: user cancel this action");
        }
    }

    private void uploadVideo(String path) {
        loader.show("Please wait...");
        File file = new File(path);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photos", file.getName(), requestBody);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token  ,fileToUpload);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    videoUrl = response.body().getData().getLocation();
                    Log.w("TA", "onResponse: " + videoUrl);
                    saveData();


                } else {
//                    selectorGallery.setText("Upload");
                    Toast.makeText(getApplicationContext(), "Error in Video Upload", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {

                Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
//                selectorGallery.setText("Upload");
            }
        });
    }

    private void saveData() {
        JSONObject a
                = new JSONObject();
        try {
            a.put("senderID", sessionHandler.getLoggedInUser());
            a.put("senderName", sessionHandler.getUserName());
            a.put("senderImage", sessionHandler.getProfileImage());
            a.put("postImage", imageUrl);
            a.put("companyName", videoUrl);
            a.put("country", "Na");
            a.put("street1", "NA");
            a.put("street2", "NA");
            a.put("websiteLink", "Na");
            a.put("phone", "NA");
            a.put("whatsappContact", "Na");
            a.put("productType", "NA");
            a.put("country", "NA");
            a.put("startingDate", "startDate");
            a.put("planFile", planFile);
            a.put("name", "0");
            a.put("time", "0");
            a.put("state", "0");
            a.put("email", "0");
            a.put("rank", "0");
            a.put("trainingInstitue", "0");
            a.put("courierType", "0");
            a.put("postType", "5");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TAG", "onCreate: " + a);
        requestApi.postRequest(a, response -> {

            Log.d("TAG", "saveData: " + response);
            loader.dismiss();
            finish();
        }, Server.GET_FEATURE);


    }

    private void uploadPdf(Uri uri) {

        String path = FilePath.getPath(getApplicationContext(), uri);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), path);
        double random = Math.random();
        String fileName = random + "_plan_pdf_" + System.currentTimeMillis() + ".pdf";
        MultipartBody.Part body = MultipartBody.Part.createFormData("photos", fileName, requestFile);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token , body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    planFile = response.body().getData().getLocation();
                    Log.w("TA", "onResponse: " + planFile);
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

    private void uploadImage(byte[] bytes) {
//        selectorGallery.setText("Image is being uploaded");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
        double random = Math.random();
        String fileName = random + "_comp_plan_logo_" + System.currentTimeMillis() + ".jpg";
//        Log.e(TAG, "uploadImage: " + fileName);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photos", fileName, requestFile);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token , body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    imageUrl = response.body().getData().getLocation();
                    Log.w("TA", "onResponse: " + imageUrl);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}