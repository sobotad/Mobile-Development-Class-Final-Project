package com.example.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//Provides instructions for the app
public class Instruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
    }

    //Next activity
    public void go_next(View view){
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
        finish();
    }
}
