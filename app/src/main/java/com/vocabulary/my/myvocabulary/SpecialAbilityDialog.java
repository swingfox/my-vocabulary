package com.vocabulary.my.myvocabulary;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 24/09/2017.
 */

public class SpecialAbilityDialog extends Dialog {
    @BindView(R.id.btnSpecialOK) Button ok;
    @BindView(R.id.btnSpecialCancel) Button cancel;
    @BindView(R.id.txtSpecialPoints) TextView txtPoints;
    public GameActivity c;
    private Session session;


    public SpecialAbilityDialog(GameActivity context) {
        super(context);
        this.c = context;
        this.setCancelable(false);
        //txtPoints.setText(Session.getInstance(c).getPoints()+"");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings);
        ButterKnife.bind(this);
        session = Session.getInstance(c.getApplicationContext());
        txtPoints.setText(Session.getInstance(c).getPoints()+"");
    }

    @OnClick({R.id.btnSpecialOK, R.id.btnSpecialCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSpecialOK:
            case R.id.btnSpecialCancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.imgBuyReset)
    public void buyReset(View view) {
        int points = Session.getInstance(c.getApplicationContext()).getPoints();
        int m = points-10;
        if(m>=0){
            Session.getInstance(c.getApplicationContext()).setPoints(m);
            c.generate();
            c.setGamePoints(m);
        }
        else{
             Toast.makeText(c.getApplicationContext(),"NOT ENOUGH POINTS!",Toast.LENGTH_LONG).show();
        }
        dismiss();
    }


    @OnClick(R.id.imgBuyTime)
    public void buyTime(View view){
        int points = Session.getInstance(c.getApplicationContext()).getPoints();
        int m = points-10;
        if(m>=0){
            Session.getInstance(c.getApplicationContext()).setPoints(m);
            c.newTimer();
            c.setGamePoints(m);
        }
        else{
            Toast.makeText(c.getApplicationContext(),"NOT ENOUGH POINTS!",Toast.LENGTH_LONG).show();
        }
        dismiss();
    }
}
