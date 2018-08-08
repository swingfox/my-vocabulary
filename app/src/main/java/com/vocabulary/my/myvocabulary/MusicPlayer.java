package com.vocabulary.my.myvocabulary;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by David on 28/08/2017.
 */

public class MusicPlayer {
    MediaPlayer gameMedia;
    MediaPlayer homeMedia;

    private static MusicPlayer playerHome;
    private static MusicPlayer playerGame;
    private int curPosHome;
    private int curPosGame;

    private MusicPlayer(AppCompatActivity a){
        homeMedia = MediaPlayer.create(a,R.raw.game_menu);
        gameMedia = MediaPlayer.create(a,R.raw.game_puzzle);
    }

    private MusicPlayer(SoundsDialog a){
        homeMedia = MediaPlayer.create(a.getContext(),R.raw.game_menu);
        gameMedia = MediaPlayer.create(a.getContext(),R.raw.game_puzzle);
    }


    public static MusicPlayer getHomeInstance(AppCompatActivity a){
        if(playerHome == null){
            playerHome = new MusicPlayer(a);
        }
        return playerHome;
    }

    public static MusicPlayer getGameInstance(AppCompatActivity a){
        if(playerGame == null){
            playerGame = new MusicPlayer(a);
        }
        return playerGame;
    }

    public static MusicPlayer getGameInstance(SoundsDialog a){
        if(playerGame == null){
            playerGame = new MusicPlayer(a);
        }
        return playerGame;
    }

    public static MusicPlayer getHomeInstance(SoundsDialog a){
        if(playerHome == null){
            playerHome = new MusicPlayer(a);
        }
        return playerHome;
    }

    public static MediaPlayer getHomeMusic(){
        return playerHome.homeMedia;
    }

    public static MediaPlayer getPlayMusic(){
        return playerHome.gameMedia;
    }
    public void startHome(Context c){
        if(homeMedia != null){
            int v = Session.getInstance(c).getVolume();
            homeMedia.reset();
            homeMedia.stop();
            homeMedia = MediaPlayer.create(c,R.raw.game_menu);
            Toast.makeText(c.getApplicationContext(),"VOLUME: " + v, Toast.LENGTH_SHORT).show();
            homeMedia.setVolume(v,v);
            homeMedia.setLooping(true);
            homeMedia.start();
        }
    }

    public void startGame(Context c){
        if(gameMedia != null){
            int v = Session.getInstance(c).getVolume();
            gameMedia.reset();
            gameMedia.stop();
            gameMedia = MediaPlayer.create(c,R.raw.game_puzzle);
            Toast.makeText(c.getApplicationContext(),"VOLUME: " + v, Toast.LENGTH_SHORT).show();
            gameMedia.setVolume(v,v);
            gameMedia.setLooping(true);
            gameMedia.start();
        }
    }

    public void setVolume(float volume){
        if(homeMedia != null){
            homeMedia.setVolume(volume,volume);
        }
        if(gameMedia != null){
            gameMedia.setVolume(volume,volume);
        }
    }

    public void stopHome(){
        if(homeMedia != null){
            homeMedia.stop();
        }
    }

    public void stopGameMusic(){
        if(gameMedia != null){
            gameMedia.stop();
        }
    }

    public void resumeHome(){
        if(homeMedia != null){
            homeMedia.seekTo(curPosHome);
            homeMedia.start();
        }
    }

    public void resumeGameMusic(){
        if(gameMedia != null){
            gameMedia.seekTo(curPosGame);
            gameMedia.start();
        }
    }

    public boolean isPausedHomeMusic(){
        return !homeMedia.isPlaying();
    }
    public boolean isPausedGameMusic(){
        return !gameMedia.isPlaying();
    }


    public void pauseHomeMusic(){
        if(homeMedia != null){
            homeMedia.pause();
            curPosHome = homeMedia.getCurrentPosition();
        }
    }

    public void pauseGameMusic(){
        if(gameMedia != null){
            gameMedia.pause();
            curPosGame = gameMedia.getCurrentPosition();
        }
    }
}
