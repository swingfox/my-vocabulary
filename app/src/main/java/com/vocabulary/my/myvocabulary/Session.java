package com.vocabulary.my.myvocabulary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by David on 14/03/2017.
 */

public class Session {
    private SharedPreferences sharedPreferences;
    private static Session session;

    private Session(Context activity){
        sharedPreferences = activity.getSharedPreferences(activity.getClass().toString(),Context.MODE_PRIVATE);
    }

    public static Session getInstance(Context activity){
        if(session == null){
            session = new Session(activity);
        }
        return session;
    }

    public void setBackground(int color){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("background", color);
        editor.commit();
    }

    public int getBackground(){
        int defaultValue = Color.rgb(98,230,255);
        return sharedPreferences.getInt("background", defaultValue);
    }

    public void setVolume(int volume){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("volume", volume);
        editor.commit();
    }

    public void setAutoplay(int autoplay){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("autoplay", autoplay);
        editor.commit();
    }

    public void setStars(int stars){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("stars", stars);
        editor.commit();
    }

    public void setPoints(int points){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("points", points);
        editor.commit();
    }

    public int getPoints(){
        int defaultValue =  0;
        return sharedPreferences.getInt("points", defaultValue);
    }

    public int getStars(){
        int defaultValue =  0;
        return sharedPreferences.getInt("stars", defaultValue);
    }
    public void setStarStage(int level, int stage,int points){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("starsl"+level+"s"+stage, points);
        editor.commit();
    }
    public int getStarStage(int level, int stage){
        int defaultValue =  0;
        return sharedPreferences.getInt("starsl"+level+"s"+stage, defaultValue);
    }

    public int getAutoplay(){
        int defaultValue =  0;
        return sharedPreferences.getInt("autoplay", defaultValue);
    }

    public int getVolume(){
        int defaultValue =  100;
        return sharedPreferences.getInt("volume", defaultValue);
    }

    public void clear(){
        sharedPreferences.edit().clear().commit();
    }
}
