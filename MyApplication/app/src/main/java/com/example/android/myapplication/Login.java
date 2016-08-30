package com.example.android.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void submit(View view) {

        //Grabs date information to save to file
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Variables.seterror(0);

        //Pulls the values from the form fields
        EditText username = (EditText) findViewById(R.id.username);

        EditText password = (EditText) findViewById(R.id.password);


        if (username.getText().toString().matches("")) {
            Toast.makeText(Login.this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.getText().toString().matches("")) {
            Toast.makeText(Login.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Save the variables to save to file
        Variables.setuName(username.getText().toString());
        Variables.setpass(password.getText().toString());


        String url = String.format("http://apiassignment.appspot.com/user?username=%s&password=%s", Variables.getuName(), Variables.getpass());
        //Used help from www.simplifiedcoding.net/android-volley-post-request-tutorial to get this up and running
        StringRequest string = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Toast.makeText(Login.this, "Please wait", Toast.LENGTH_LONG).show();
                        Variables.setresponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(Login.this,"Error! You need a valid log in.",Toast.LENGTH_LONG).show();
                        Variables.seterror(1);
                    }
                });

        //Make the post request
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(string);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                go();
            }
            };
        Handler h = new Handler();
        h.postDelayed(r, 2000);
    }

    public void go() {
        if (Variables.geterror() == 1)
        {

            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

        else {
            finish();
            Intent intent = new Intent(this, Instruction.class);
            startActivity(intent);
        }
    }
}
