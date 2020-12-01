package com.project.mlmpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.project.mlmpro.activity.Login;

/**
 * This will work as splash screen
 * and entry point for the App
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent login = new Intent(getApplicationContext() , Login.class) ;
        startActivity(login);

    }



   private void updateScreen(Intent intent){
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

   }
}