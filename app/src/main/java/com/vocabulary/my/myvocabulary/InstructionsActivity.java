package com.vocabulary.my.myvocabulary;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 17/09/2017.
 */

public class InstructionsActivity extends AppCompatActivity {
    @BindView(R.id.txtHelp) TextView txtHelp;
    @BindView(R.id.txtHelp1) TextView txtHelp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);

        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Wicked Mouse Demo.otf");
        txtHelp1.setTypeface(custom_font);
        txtHelp.setText(getString(R.string.txtHelp));
    }
}
