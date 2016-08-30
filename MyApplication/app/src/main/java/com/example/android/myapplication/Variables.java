package com.example.android.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DavidSobota on 7/31/16.
 */

public class Variables extends AppCompatActivity{


    //User variables to be stored and referenced
    public static String Name;
    public static String getName() {
        return Name;
    }
    public static void setName(String uname) {
        Name = uname;
    }

    public static String Location;
    public static String getLoc() {
        return Location;
    }
    public static void setLoc(String place) {
        Location = place;
    }

    public static String Work;
    public static String getWork() {
        return Work;
    }
    public static void setWork(String job) {
        Work = job;
    }

    public static String rating;
    public static void setRating(String rate) {rating = rate;}
    public static String getRating() {return rating;}

    public static String username;
    public static String getuName() {
        return username;
    }
    public static void setuName(String name) {
        username = name;
    }

    public static String password;
    public static String getpass() {
        return password;
    }
    public static void setpass(String pass) {
        password = pass;
    }

    public static String response;
    public static void setresponse(String obj) { response = obj;}
    public static String getresponse(){return response;}

    public static int error = 0;
    public static int geterror() {
        return error;
    }
    public static void seterror(int err) {
        error = err;
    }

    public static String deletescore;
    public static void setdeletescore(String score) {deletescore = score;}
    public static String getdeletescore() {return deletescore;}

    public static String viewscore;
    public static void setviewscore(String score) {viewscore = score;}
    public static String getviewscore() {return viewscore;}

    public static String userID;
    public static void setuserID(String ID) {userID = ID;}
    public static String getuserID() {return userID;}

    public static String currentScore;
    public static void setCurrentScore(String score) { currentScore = score;}
    public static String getCurrentScore() {return currentScore;}

    public static String scoreResponse;
    public static void setScoreResponse(String obj) { scoreResponse = obj;}
    public static String getScoreResponse(){return scoreResponse;}


}