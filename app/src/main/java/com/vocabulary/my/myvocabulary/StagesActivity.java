package com.vocabulary.my.myvocabulary;

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

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 29/08/2017.
 */

public class StagesActivity extends AppCompatActivity {
    @BindView(R.id.btnStageBack) ImageView imgBack;
    @BindViews({R.id.txtStage1,R.id.txtStage2,R.id.txtStage3,R.id.txtStage4}) List<TextView> txtStage;
    @BindView(R.id.stagesLayout) LinearLayout stages;
    @BindViews({R.id.imgStageStar1, R.id.imgStageStar2, R.id.imgStageStar3,
                R.id.imgStageStar4, R.id.imgStageStar5, R.id.imgStageStar6,
                R.id.imgStageStar7, R.id.imgStageStar8, R.id.imgStageStar9,
                R.id.imgStageStar10, R.id.imgStageStar11, R.id.imgStageStar12})
    List<ImageView> stars;
    @BindViews({R.id.imgLock2, R.id.imgLock3, R.id.imgLock4}) List<ImageView> lock;
    private int level;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages);
        ButterKnife.bind(this);
        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Wicked Mouse Demo.otf");

        for(int i = 0; i < txtStage.size(); i++) {
            txtStage.get(i).setTypeface(custom_font);
            txtStage.get(i).setTextColor(Color.parseColor("#000000"));
        }
        Bundle b = getIntent().getExtras();
        level = Integer.parseInt(b.getString("level"));

        Toast.makeText(getApplicationContext(),"level: " + level, Toast.LENGTH_LONG).show();
        session = Session.getInstance(StagesActivity.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        stages.setBackgroundColor(Session.getInstance(StagesActivity.this).getBackground());
        int stage1 = session.getStarStage(level,1);
        int stage2 = session.getStarStage(level,2);
        int stage3 = session.getStarStage(level,3);
        int stage4 = session.getStarStage(level,4);

        if(stage1 >= 3){
            stars.get(0).setVisibility(View.VISIBLE);
            lock.get(0).setVisibility(View.INVISIBLE);
            txtStage.get(1).setVisibility(View.VISIBLE);
        }
        if(stage1 >= 6){
            stars.get(1).setVisibility(View.VISIBLE);
        }
        if(stage1 >= 9){
            stars.get(2).setVisibility(View.VISIBLE);
        }

        if(stage2 >= 3){
            stars.get(3).setVisibility(View.VISIBLE);
            lock.get(1).setVisibility(View.INVISIBLE);
            txtStage.get(2).setVisibility(View.VISIBLE);
        }
        if(stage2 >= 6){
            stars.get(4).setVisibility(View.VISIBLE);
        }
        if(stage2 >= 9){
            stars.get(5).setVisibility(View.VISIBLE);
        }

        if(stage3 >= 3){
            stars.get(6).setVisibility(View.VISIBLE);
            lock.get(2).setVisibility(View.INVISIBLE);
            txtStage.get(3).setVisibility(View.VISIBLE);
        }
        if(stage3 >= 6){
            stars.get(7).setVisibility(View.VISIBLE);
        }
        if(stage3 >= 9){
            stars.get(8).setVisibility(View.VISIBLE);
        }

        if(stage4 >= 3){
            stars.get(9).setVisibility(View.VISIBLE);
        }
        if(stage4 >= 6){
            stars.get(10).setVisibility(View.VISIBLE);
        }
        if(stage4 >= 9){
            stars.get(11).setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.txtStage1,R.id.txtStage2,R.id.txtStage3, R.id.txtStage4})
    public void stage(TextView t) {
        Intent i = new Intent(StagesActivity.this,GameActivity.class);
        i.putExtra("stage",t.getText());
        i.putExtra("level",level+"");
        startActivity(i);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @OnClick(R.id.btnStageBack)
    public void back(){
        finish();
    }
}
