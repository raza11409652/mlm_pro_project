/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.common.primitives.Bytes;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.FileUploadService;
import com.project.mlmpro.utils.FileUtils;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.ResultResponse;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;
import com.sendbird.android.OpenChannel;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
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
    String imageUrl = "NA", postData = "NA", sender, senderName, senderImage = "N/A";
    EditText postEdt;
    Button post;
    String TAG = NewPost.class.getSimpleName();
    RequestApi api;
    Loader loader;
    FirebaseStorage storage;
    StorageReference storageReference;
    CircleImageView imageView;
    TextView nameProfile ;

    ImageView imageWrapper;
    String _token = null;

    VideoView videoView;
    Button selectorGallery;
    String videoPath;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //    SessionHandler sessionHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        toolbar = findViewById(R.id.toolbar);
        post = findViewById(R.id.post_btn);
        postEdt = findViewById(R.id.post_data);
        selectorGallery = findViewById(R.id.selector_gallery);
        imageView = findViewById(R.id.profile_image) ;
        nameProfile = findViewById(R.id.name);

        sessionHandler = new SessionHandler(this);
        senderImage = sessionHandler.getProfileImage();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//        SendBird.init(getString(R.string.sendbird_id), NewPost.this);
        _token = "Bearer " + sessionHandler.getLoggedToken();

//        SendBird.connect(sessionHandler.getLoggedInMobile(), new SendBird.ConnectHandler() {
//            @Override
//            public void onConnected(User user, SendBirdException e) {
//                if (e != null) {
//                    Log.w(TAG, "onConnected: " + e.toString());
//                }
//                Log.w(TAG, "onConnected: " + user.getFriendName());
//            }
//        });


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.new_post_heading));


        api = new RequestApi(this);
        sender = sessionHandler.getLoggedInUser();
        senderName = sessionHandler.getUserName();
//        senderImage = null;
        loader = new Loader(this);
        imageWrapper = findViewById(R.id.image_wrapper);
        videoView = findViewById(R.id.video_wrapper);
        Picasso.get().load(sessionHandler.getProfileImage())
                .error(R.drawable.logo_circle)
                .placeholder(R.drawable.logo_circle)
                .into(imageView);
        nameProfile.setText(sessionHandler.getUserName() ==null?"NAME":sessionHandler.getUserName());

//        verifyStoragePermissions(this , 0);

        post.setOnClickListener(v -> {
            postData = postEdt.getText().toString().trim();

//            Log.d(TAG, "onCreate: " + postData);
            if (StringHandler.isEmpty(postData) && imageUrl.equals("NA")) {
                Toast.makeText(getApplicationContext(), "Please write something", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringHandler.isEmpty(postData)) {
                postData = "NA";
            }
//            OpenChannel.createChannel((openChannel, e) -> {
//                if (e != null) {
//                    Log.w(TAG, "onCreate: " + e.toString());
//                }
//
//
////                Log.d(TAG, "onCreate: " + openChannel.getName());
////                Log.d(TAG, "onCreate: " + openChannel.getCoverUrl());
////                Log.d(TAG, "onCreate: " + openChannel.getUrl());
//
//            });


//            Log.d(TAG, "onCreate: ");
            try {
                JSONObject object = new JSONObject();
                object.put("sendBirdGroupId", "NOT FOUN");
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
            verifyStoragePermissions(NewPost.this, 1);
//            selectImage();

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

    public void verifyStoragePermissions(Activity activity, int process) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    Constant.PERMISSION
            );
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "onRequestPermissionsResult: Permission available");
            selectImage();
        } else {
            Toast.makeText(getApplicationContext(), "Permission not given", Toast.LENGTH_SHORT).show();
        }
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
                    Uri selectedVideo = data.getData();
//                    log
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String mediaPath1 = null;
                    mediaPath1 = cursor.getString(columnIndex);
//                    str2.setText(mediaPath1);
                    // Set the Video Thumb in ImageView Previewing the Media
//                    imgView.setImageBitmap(getThumbnailPathForLocalFile(MainActivity.this, selectedVideo));
                    cursor.close();
                    uploadVideo(selectedVideo);
                } else {
                    Toast.makeText(getApplicationContext(), "Only Image Or video can be uploaded", Toast.LENGTH_SHORT).show();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadVideo(Uri uri) throws FileNotFoundException {
        post.setEnabled(false);
        InputStream is = getContentResolver().openInputStream(uri);
        byte[] arr = new byte[0];
        try {
             arr = getBytes(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectorGallery.setText("Video is being uploaded");

        String fileName = "MLM_PRO_VIDEO" + System.currentTimeMillis();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("video/mp4"),
                        arr
                );
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photos", fileName+".3gp"  , requestFile);
        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token  ,fileToUpload);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                Log.d("TAG", "onResponse: " + response.isSuccessful());
                Log.d("Upload vide khalid", "onResponse: " + response.message());
                if (response.isSuccessful()) {
                    post.setEnabled(true);
                   String videoUrl = response.body().getData().getLocation();
                    Log.w("TA", "onResponse: " + videoUrl);
//                    saveData();
                    imageUrl  =videoUrl ;
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(imageUrl);
                    videoView.start();
                    selectorGallery.setText("Upload");
                    selectorGallery.setVisibility(View.GONE);


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

//        StorageReference ref = storageReference.child(fileName);
//        ref.putFile(uri)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
////                        Log.d(TAG, "uploadVideo: " + task.getResult().get);
//
//                       ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                           @Override
//                           public void onSuccess(Uri uri) {
//                               Log.d(TAG, "onSuccess: "  +uri);
//                               imageUrl = uri.toString();
//                               selectorGallery.setVisibility(View.GONE);
//                               imageWrapper.setVisibility(View.GONE);
//                               videoView.setVisibility(View.VISIBLE);
//                               videoView.setVideoPath(imageUrl);
//                               videoView.start();
//                           }
//                       });
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Error while uploading", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//
//                }).addOnFailureListener(e -> {
//            Log.d(TAG, "uploadVideo: " + e.getLocalizedMessage());
//
//        });
    }

    private void uploadImage(byte[] bytes) {
        post.setEnabled(false);
        selectorGallery.setText("Image is being uploaded");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Server.ROOT_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService retrofitInterface = retrofit.create(FileUploadService.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);

        double random = Math.random();
        String fileName = random + "_" + System.currentTimeMillis() + ".jpg";
        Log.e(TAG, "uploadImage: " + fileName);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photos", fileName, requestFile);

        Call<ResultResponse> responseCall = retrofitInterface.uploadImage(_token,
                body);

        responseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (response.isSuccessful()) {
                    post.setEnabled(true);
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
                selectorGallery.setText("Upload");
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

            Toast.makeText(getApplicationContext(), "Post Success", Toast.LENGTH_SHORT).show();
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