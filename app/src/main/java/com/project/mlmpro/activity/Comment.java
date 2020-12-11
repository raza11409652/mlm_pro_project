/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.mlmpro.R;
import com.project.mlmpro.adapter.CommentAdapter;
import com.project.mlmpro.model.CommentPost;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class Comment extends AppCompatActivity {

    SessionHandler sessionHandler;
    String TAG = Comment.class.getSimpleName();
    EditText commentsBox;
    RecyclerView commentList;
    Button commentSubmit;
    String comment;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database ;
    DatabaseReference commentWrite , commentRead ;
    ArrayList<CommentPost>posts  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionHandler = new SessionHandler(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance() ;
        commentRead = database.getReference().child("comment") ;
        commentWrite = database.getReference().child("comment") ;
        setContentView(R.layout.activity_comment);
        commentsBox = findViewById(R.id.comment_box);
        commentList = findViewById(R.id.comment_list);
        commentList.setHasFixedSize(true);
        commentSubmit = findViewById(R.id.submit);

//        getAllComments();
        commentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentsBox.getText().toString();
                if (StringHandler.isEmpty(comment)) {
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("comment", comment);
                map.put("user", sessionHandler.getUserName());
                map.put("mobile", sessionHandler.getLoggedInMobile());
                map.put("time", String.valueOf(System.currentTimeMillis()));
                map.put("post", Constant.CURRENT_POST.getId());
                commentWrite.child(Constant.CURRENT_POST.getId()).push().setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                commentsBox.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }) ;


            }
        });

        commentRead.child(Constant.CURRENT_POST.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            posts = new ArrayList<>();
                            for (DataSnapshot single : snapshot.getChildren()) {
                                Log.d("TAG", single.toString());
                                CommentPost firebaseData = single.getValue(CommentPost.class);
//                                PostParent postParent = new PostParent(single.getKey(), firebaseData, false, false);
                                posts.add(firebaseData);

                            }
                            setAdapter(posts);
                        }
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }) ; 


    }

    private void setAdapter(ArrayList<CommentPost> posts) {

        Log.d(TAG, "setAdapter: "+posts);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        CommentAdapter adapter = new CommentAdapter(posts , getApplicationContext()) ;
        adapter.notifyDataSetChanged();
        commentList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


}