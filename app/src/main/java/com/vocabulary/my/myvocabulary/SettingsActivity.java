package com.vocabulary.my.myvocabulary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 19/08/2017.
 */

public class SettingsActivity extends AppCompatActivity{
    @BindView(R.id.btnSettingsBack) ImageView btnBack;
    @BindView(R.id.btnSound) ImageView btnSound;
    @BindView(R.id.btnTheme) ImageView btnTheme;
    @BindView(R.id.btnHelp) ImageView btnHelp;
    @BindView(R.id.settingsLayout) LinearLayout settings;
    private Session session = Session.getInstance(SettingsActivity.this);
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }
        ButterKnife.bind(this);

        pd = new ProgressDialog(SettingsActivity.this);
        pd.setMessage("Clearing Vocabulary...");
        pd.setCancelable(false);
    }

    @Override
    public void onResume(){
        super.onResume();
        settings.setBackgroundColor(session.getBackground());
    }

    @OnClick(R.id.btnSound)
    public void sounds(){
        SoundsDialog settingsDialog = new SoundsDialog(SettingsActivity.this,SettingsActivity.this);
        Window window = settingsDialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        settingsDialog.show();
    }
    @OnClick(R.id.btnTheme)
    public void theme(){
        int currentBackgroundColor = 0;
        Drawable background = settings.getBackground();
        if(background instanceof ColorDrawable){
            currentBackgroundColor = ((ColorDrawable)background).getColor();
        }

        ColorPickerDialogBuilder
                .with(SettingsActivity.this)
                .setTitle("Choose color")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    //    Toast.makeText(getApplicationContext(),"onColorSelected: 0x" + Integer.toHexString(selectedColor),Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        settings.setBackgroundColor(selectedColor);
                        session.setBackground(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @OnClick(R.id.btnSettingsBack)
    public void back(){
        finish();
    }

    @OnClick(R.id.btnHelp)
    public void help(){
        Intent i = new Intent(SettingsActivity.this, InstructionsActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.btnReset)
    public void reset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset!")
                .setMessage("Which one you would like to reset?")
                .setPositiveButton("LEVEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Session.getInstance(SettingsActivity.this).clear();
                        Toast.makeText(getApplicationContext(),"LEVEL SUCCESSFULLY RESET!",Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("VOCABULARY",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Session.getInstance(SettingsActivity.this).clear();
                clearVocabulary();
            }
        });
        builder.show();
    }
    public String TAG = "CLEAR VOCABULARY: ";
    public void clearVocabulary(){
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                pd.dismiss();
                if(response.equals(" []")){
                    Toast.makeText(getApplicationContext(), "EMPTY RESPONSE!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"VOCABULARY SUCCESSFULLY RESET!",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                final Map<String, String> params = new HashMap<String, String>();
                params.put("query", "clear_vocabulary");
                return params;
            }

        };

        strReq.setShouldCache(false);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }
}
