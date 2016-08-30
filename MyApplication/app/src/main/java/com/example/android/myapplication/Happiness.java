package com.example.android.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

//Get a happiness score for the user
public class Happiness extends AppCompatActivity {

    final static private String APP_KEY = "7r1s4d0fpqjt5gd";
    final static private String APP_SECRET = "gpgxmzpokqw72wp";
    private DropboxAPI<AndroidAuthSession> mDBApi;
    final static private String ACCESS_TOKEN = "IYt4w6cBSQwAAAAAAAAB4d_evWPwF2suijlZ6DahejjMfLFFc8XyZTlKkyFLzUdl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happiness);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        mDBApi.getSession().setOAuth2AccessToken(ACCESS_TOKEN);
    }

    public void submit(View view){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String y = Integer.toString(year);

        int month = c.get(Calendar.MONTH) + 1;
        String m = Integer.toString(month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String d = Integer.toString(day);

        final String date = (new StringBuilder()).append(y).append("-").append(m).append("-").append(d).toString();

        //Get the rating from the rating bar
        RatingBar rate = (RatingBar)findViewById(R.id.rating);
        String s = Float.toString(rate.getRating());
        Variables.setRating(s);

        //Save it to the file
        File path = Environment.getExternalStorageDirectory();
        File myFile = new File(path + "/" + year + "-" + month + "-" + day + "-" + Variables.getuName() + ".txt");
        try {

            FileWriter writer = new FileWriter(myFile, true);
            writer.write("Username: " + Variables.getuName() + "\r\nDate: " + year + "-" + month + "-" + day +"\r\n" + "Happiness Rating(0-10):   " + Variables.getRating() + "\r\n\r\n");
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        UploadFile upload = new UploadFile(this, mDBApi, "/", myFile);
        upload.execute();

        StringRequest string = new StringRequest(Request.Method.POST, "http://apiassignment.appspot.com/score",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Toast.makeText(Happiness.this, "Please wait", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(Happiness.this, "Post error",Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            //Inputs the keys and values to be passed onto the POST request
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("happyIndex", Variables.getRating());
                params.put("date", date);
                params.put("username", Variables.getuName());
                params.put("password", Variables.getpass());
                return params;
            }
        };

        //Make the post request
        RequestQueue request = Volley.newRequestQueue(this);
        request.add(string);

        Runnable r = new Runnable(){
            @Override
        public void run(){
                String url = String.format("http://apiassignment.appspot.com/score?username=%s&password=%s", Variables.getuName(), Variables.getpass());

                StringRequest string2 = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Shows a response from the server
                                //Toast.makeText(Happiness.this, response, Toast.LENGTH_LONG).show();
                                Variables.setCurrentScore(response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Displays error message if needed
                                Toast.makeText(Happiness.this, "GET error",Toast.LENGTH_LONG).show();
                            }
                        });

                //Make the post request
                RequestQueue request2 = Volley.newRequestQueue(Happiness.this);
                request2.add(string2);


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        next();

                    }
                }, 1000);
            }
        };


        Handler h = new Handler();
        h.postDelayed(r, 1000);

        String url2 = String.format("http://apiassignment.appspot.com/user?check=yes&username=%s&password=%s", Variables.getuName(), Variables.getpass());
        //Used help from www.simplifiedcoding.net/android-volley-post-request-tutorial to get this up and running
        StringRequest string3 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        Variables.setuserID(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(Happiness.this,"getting user id error",Toast.LENGTH_LONG).show();
                        //Variables.seterror(1);
                    }
                });

        //Make the post request
        RequestQueue request3 = Volley.newRequestQueue(this);
        request3.add(string3);

    }

    public void next(){


        String url = String.format("http://apiassignment.appspot.com/user/%s/score/%s", Variables.getuserID(), Variables.getCurrentScore());

        StringRequest string2 = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Shows a response from the server
                        //Toast.makeText(Happiness.this, response, Toast.LENGTH_LONG).show();
                        //Variables.setCurrentScore(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Displays error message if needed
                        Toast.makeText(Happiness.this, "PUT error",Toast.LENGTH_LONG).show();
                    }
                });

        //Make the post request
        RequestQueue request2 = Volley.newRequestQueue(Happiness.this);
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
                RequestQueue request = Volley.newRequestQueue(Happiness.this);
                request.add(string);


                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        go_back();
                    }


                }, 1000);}
        }, 1000);

    }

    public void go_back(){
        Intent intent = new Intent(this, User.class);
        startActivity(intent);
        finish();
    }
}
