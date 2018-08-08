package com.vocabulary.my.myvocabulary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vocabulary.my.myvocabulary.R;
import com.vocabulary.my.myvocabulary.Session;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 05/03/2017.
 */

public class SoundsDialog  extends Dialog implements View.OnClickListener{
    public Activity c;
    private Session session;
    @BindView(R.id.btnOK) Button ok;
    @BindView(R.id.btnCancel) Button cancel;
    @BindView(R.id.seekAudio) SeekBar seekVolume;
    @BindView(R.id.txtVolume) TextView txtVolume;
    private MusicPlayer player;
    public SoundsDialog(Activity context, SettingsActivity s) {
        super(context);
        this.c = context;
        this.player = MusicPlayer.getHomeInstance(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sound);

        ButterKnife.bind(this);

        session = Session.getInstance(SoundsDialog.this.getContext());

        txtVolume.setText("Volume: " + session.getVolume());
        seekVolume.setProgress(session.getVolume());

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                session.setVolume(seekBar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtVolume.setText("Volume: " + progress);
            }
        });
    }

    @OnClick({R.id.btnOK, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
                player.setVolume(session.getVolume());
                MusicPlayer.getGameInstance(SoundsDialog.this).setVolume(0);
                MusicPlayer.getHomeInstance(SoundsDialog.this).setVolume(0);
                MusicPlayer.getHomeInstance(SoundsDialog.this).startHome(c.getApplicationContext());

                Toast.makeText(getContext(),"Music Volume: " + session.getVolume(),Toast.LENGTH_LONG).show();

                dismiss();
                break;
            case R.id.btnCancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}
