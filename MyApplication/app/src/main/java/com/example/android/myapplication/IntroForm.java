package com.example.android.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//This is where users will input their data
public class IntroForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_form);


    }

    //When the user submits
    public void submit(View view){


        //Pulls the values from the form fields
        EditText username = (EditText) findViewById(R.id.username);

        EditText password = (EditText) findViewById(R.id.password);

        EditText name = (EditText) findViewById(R.id.enteredName);

        EditText work = (EditText) findViewById(R.id.enteredWork);

        EditText location = (EditText) findViewById(R.id.enteredLocation);

        //Error checking for any fields left blank
        if(name.getText().toString().matches("")) {
            Toast.makeText(IntroForm.this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(work.getText().toString().matches("")) {
            Toast.makeText(IntroForm.this, "Please enter valid job", Toast.LENGTH_SHORT).show();
            return;
        }

        if(location.getText().toString().matches("")) {
            Toast.makeText(IntroForm.this, "Please enter a location", Toast.LENGTH_SHORT).show();
            return;
        }

        if(username.getText().toString().matches("")) {
            Toast.makeText(IntroForm.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.getText().toString().matches("")) {
            Toast.makeText(IntroForm.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Save the variables to save to file
        Variables.setName(name.getText().toString());
        Variables.setWork(work.getText().toString());
        Variables.setLoc(location.getText().toString());
        Variables.setuName(username.getText().toString());
        Variables.setpass(password.getText().toString());


        //Used help from www.simplifiedcoding.net/android-volley-post-request-tutorial to get this up and running
        StringRequest string = new StringRequest(Request.Method.POST, "http://apiassignment.appspot.com/user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Toast.makeText(IntroForm.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(IntroForm.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            //Inputs the keys and values to be passed onto the POST request
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Variables.getuName());
                params.put("password", Variables.getpass());
                params.put("name", Variables.getName());
                params.put("location", Variables.getLoc());
                params.put("job", Variables.getWork());
                return params;
            }
        };

        //Make the post request
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(string);

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();

    }

}
