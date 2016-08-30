package com.example.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Goes to next activity
    public void create(View view) {

        Intent intent = new Intent(this, IntroForm.class);
        startActivity(intent);



    }

    public void login(View view) {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);



    }
}
