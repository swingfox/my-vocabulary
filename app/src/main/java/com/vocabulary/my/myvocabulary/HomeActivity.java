package com.vocabulary.my.myvocabulary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.btnPlay) ImageView play;
    @BindView(R.id.btnVocabulary) ImageView vocabulary;
    @BindView(R.id.btnSettings) ImageView settings;
    @BindView(R.id.btnExit) ImageView exit;
    @BindView(R.id.homeLayout) LinearLayout home;
    private MusicPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        
        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        home.setBackgroundColor(Session.getInstance(HomeActivity.this).getBackground());
        mp = MusicPlayer.getHomeInstance(HomeActivity.this);
        mp.startHome(getApplicationContext());
        mp.stopGameMusic();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onBackPressed(){

    }

    @OnClick(R.id.btnPlay)
    public void play() {
        Intent i = new Intent(HomeActivity.this,LevelActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnVocabulary)
    public void vocabulary() {
        Intent i = new Intent(HomeActivity.this,VocabularyActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnSettings)
    public void settings(){
        Intent i = new Intent(HomeActivity.this,SettingsActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnExit)
    public void exit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
