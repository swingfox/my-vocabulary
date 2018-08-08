package com.vocabulary.my.myvocabulary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 28/08/2017.
 */

public class LevelActivity extends AppCompatActivity {
    @BindView(R.id.btnLevelBack) ImageView imgBack;
    @BindViews({R.id.txtLevel1,R.id.txtLevel2,R.id.txtLevel3,
                R.id.txtLevel4,R.id.txtLevel5,R.id.txtLevel6,
                R.id.txtLevel7,R.id.txtLevel8,R.id.txtLevel9})
    List<TextView> txtLevel;

    @BindViews({R.id.levelLock2,R.id.levelLock3,R.id.levelLock4,
            R.id.levelLock5,R.id.levelLock6,R.id.levelLock7,
            R.id.levelLock8,R.id.levelLock9})
    List<ImageView> imgLock;

    @BindView(R.id.levelLayout) LinearLayout level;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Wicked Mouse Demo.otf");
        for(int i = 0; i < txtLevel.size(); i++) {
            txtLevel.get(i).setTypeface(custom_font);
            txtLevel.get(i).setTextColor(Color.parseColor("#000000"));
        }

        session = Session.getInstance(LevelActivity.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        level.setBackgroundColor(Session.getInstance(LevelActivity.this).getBackground());
        List<Integer> levels = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            levels.add(session.getStarStage(i+1,4));
            if(levels.get(i) >= 3 && i != 9){
                txtLevel.get(i+1).setVisibility(View.VISIBLE);
                imgLock.get(i).setVisibility(View.INVISIBLE);
            }
        }

    }

    @OnClick(R.id.btnLevelBack)
    public void back(){
        finish();
    }

    @OnClick({R.id.txtLevel1,R.id.txtLevel2,R.id.txtLevel3,
              R.id.txtLevel4,R.id.txtLevel5,R.id.txtLevel6,
              R.id.txtLevel7,R.id.txtLevel8,R.id.txtLevel9})
    public void text(TextView t) {
        Intent i = new Intent(LevelActivity.this,StagesActivity.class);
        i.putExtra("level",t.getText()+"");
        startActivity(i);
    }

}
