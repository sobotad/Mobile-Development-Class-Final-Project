package com.example.android.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class User extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView text = (TextView) findViewById(R.id.response);
        text.setText(Variables.getresponse());


    }

    public void add(View view)
    {
        Intent intent = new Intent(User.this, Happiness.class);
        startActivity(intent);

    }

    public void select(View view)
    {

        EditText text = (EditText) findViewById(R.id.selectscore);
        String value = text.getText().toString();
        Variables.setviewscore(value);

        String url = String.format("http://apiassignment.appspot.com/score/%s", Variables.getviewscore());
        //Used help from www.simplifiedcoding.net/android-volley-post-request-tutorial to get this up and running
        StringRequest string = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Toast.makeText(User.this, "please wait", Toast.LENGTH_LONG).show();
                        Variables.setScoreResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(User.this,"Error! You need a valid log in.",Toast.LENGTH_LONG).show();
                        //Variables.seterror(1);
                    }
                });

        //Make the post request
        RequestQueue request = Volley.newRequestQueue(User.this);
        request.add(string);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(User.this);
                builder.setMessage(Variables.getScoreResponse());
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }


        }, 2000);



    }


    public void delete(View view){

        EditText text = (EditText) findViewById(R.id.deletescore);
        String value = text.getText().toString();
        Variables.setdeletescore(value);

        String url = String.format("http://apiassignment.appspot.com/score/%s", Variables.getdeletescore());

        StringRequest string2 = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Toast.makeText(User.this, "Score deleted", Toast.LENGTH_LONG).show();
                        //Variables.setCurrentScore(response);
                        //quit();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(User.this, "DELETE error", Toast.LENGTH_LONG).show();
                    }
                });

        //Make the post request
        RequestQueue request2 = Volley.newRequestQueue(User.this);
        request2.add(string2);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                String url = String.format("http://apiassignment.appspot.com/user?username=%s&password=%s", Variables.getuName(), Variables.getpass());
                //Used help from www.simplifiedcoding.net/android-volley-post-request-tutorial to get this up and running
                StringRequest string = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Shows a response from the server
                                //Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                                Variables.setresponse(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Displays error message if needed
                                //Toast.makeText(Happiness.this,"Error! You need a valid log in.",Toast.LENGTH_LONG).show();
                                Variables.seterror(1);
                            }
                        });

                //Make the post request
                RequestQueue request = Volley.newRequestQueue(User.this);
                request.add(string);


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(User.this, User.class);
                        startActivity(intent);
                        finish();
                    }


                }, 1000);
            }
        }, 1000);
    }

    public void deleteUser(View view){

        String url = String.format("http://apiassignment.appspot.com/user/%s", Variables.getuserID());

        StringRequest string2 = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Toast.makeText(User.this, "User deleted", Toast.LENGTH_LONG).show();
                        //Variables.setCurrentScore(response);
                        quit();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(User.this, "PUT error", Toast.LENGTH_LONG).show();
                    }
                });

        //Make the post request
        RequestQueue request2 = Volley.newRequestQueue(User.this);
        request2.add(string2);

    }

    public void quit(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
