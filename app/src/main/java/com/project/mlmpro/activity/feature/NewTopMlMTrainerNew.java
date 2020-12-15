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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewTopMlMTrainerNew extends AppCompatActivity {
    String title = null, type = String.valueOf(2) , imageUrl;
    Toolbar toolbar;
    Button add;
    EditText nameEdt, addressEdtOne, addressEdtTwo, contactNumberEdt, whatsappNumberEdt,
            webLinkEdt, startingDateEdt, countryEdt, companyNameEdt, emailEdt, trainingEdt;
    String name, addressOne, addressTwo, contactNumber, whatsappNumber, webLink,
            startDate = "NA", country, email, companyName, trainning , _token;
    SessionHandler sessionHandler;
    RequestApi api;
    Loader loader;
    CircleImageView imageButton ;
    JSONObject postdata ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_top_ml_m_trainer_new);

        companyNameEdt = findViewById(R.id.company_name);
//        addressEdtOne = findViewById(R.id.address_one);
//        addressEdtTwo = findViewById(R.id.address_two);
        contactNumberEdt = findViewById(R.id.number);
        emailEdt
                = findViewById(R.id.email);
        trainingEdt = findViewById(R.id.training_inst);
//        nameEdt = findViewById(R.id.name);

        whatsappNumberEdt = findViewById(R.id.whatsapp_number);
        webLinkEdt = findViewById(R.id.website_link);
//        startingDateEdt = findViewById(R.id.start_date);
//        countryEdt = findViewById(R.id.country);
        add = findViewById(R.id.add);
//        stateEdt = findViewById(R.id.state);
//
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

        imageButton =findViewById(R.id.image_uploader);

//        calendar = Calendar.getInstance();
        setTitle("Top MLM Trainer");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyName = companyNameEdt.getText().toString().trim();
//                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                trainning = trainingEdt.getText().toString();
//                state = stateEdt.getText().toString();
                webLink = webLinkEdt.getText().toString();


                contactNumber = contactNumberEdt.getText().toString().trim();
                whatsappNumber = whatsappNumberEdt.getText().toString().trim();


                if (StringHandler.isEmpty(companyName)) {
                    companyNameEdt.setError("Required");
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

                if (StringHandler.isEmpty(trainning)) {
                    trainingEdt.setError("Required");
                    return;
                }
                if (StringHandler.isEmpty(webLink)) {
                    webLinkEdt.setError("Required");
                    return;
                }


                JSONObject a
                        = new JSONObject();
                try {
                    a.put("senderID", sessionHandler.getLoggedInUser());
                    a.put("senderName", sessionHandler.getUserName());
                    a.put("senderImage", "image urt");
                    a.put("postImage", imageUrl);
                    a.put("companyName", companyName);
                    a.put("country", "NA");
                    a.put("street1", "NA");
                    a.put("street2", "NA");
                    a.put("websiteLink", webLink);
                    a.put("phone", contactNumber);
                    a.put("whatsappContact", whatsappNumber);
                    a.put("productType", "0");
                    a.put("country", "NA");
                    a.put("startingDate", "NA");
                    a.put("planFile", "0");
                    a.put("name", "NA");
                    a.put("time", "0");
                    a.put("state", "NA");
                    a.put("email", email);
                    a.put("rank", "0");
                    a.put("trainingInstitue", trainning);
                    a.put("courierType", "0");
                    a.put("postType", type);
                    Log.d("TAG", "onClick: " + a);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                loader.show("Please wait ...");
                if (imageUrl==null){
                    Toast.makeText(getApplicationContext()   , "Image is not updated", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData(a);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), Constant.GALLERY_OPEN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.PUCHASE_ACTIVITY) {
            Log.d("TAG", "onActivityResult: " + data);
            String status = data.getStringExtra("payment");
            if (status.equals("TRUE")) {
                finish();
            }
            return;

        }
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
                    Toast.makeText(getApplicationContext() , "Image uploaded" , Toast.LENGTH_SHORT).show();
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

        Constant.CURRENT_POST_DATA = a ;
        Intent purchase = new Intent(getApplicationContext(), Subscription.class);
        purchase.putExtra("type", "3");
        startActivityForResult(purchase, Constant.PUCHASE_ACTIVITY);



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