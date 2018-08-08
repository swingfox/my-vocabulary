package com.vocabulary.my.myvocabulary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vocabulary.my.myvocabulary.Model.Vocabulary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by David on 19/08/2017.
 */

public class VocabularyActivity extends AppCompatActivity {
    @BindView(R.id.vocabularyLayout) LinearLayout vocabulary;
    @BindView(R.id.wordListLayout) LinearLayout wordListLayout;
    @BindView(R.id.txtWordsCollected) TextView wordsCollected;

    private List<Vocabulary> list = new ArrayList<>();
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }

        ButterKnife.bind(this);

        pd = new ProgressDialog(VocabularyActivity.this);
        pd.setMessage("Querying Vocabulary...");
        pd.setCancelable(false);
        queryVocabulary();
    }

    @Override
    public void onResume(){
        super.onResume();
        vocabulary.setBackgroundColor(Session.getInstance(VocabularyActivity.this).getBackground());
    }

    public class VocabularyView extends LinearLayout{
        TextView word;
        TextView description;
        private Vocabulary v;
        public VocabularyView(Context context,Vocabulary v){

            super(context);
            this.v = v;

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(30,0,0,20);
            LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams2.setMargins(60,0,0,20);


            setOrientation(LinearLayout.VERTICAL);
            setVisibility(View.VISIBLE);
            word = new TextView(context);
            word.setVisibility(View.VISIBLE);
            word.setText(v.getName());
            word.setTextSize(30);
            word.setLayoutParams(layoutParams);
            word.setTextColor(Color.BLACK);

            description = new TextView(context);
            description.setTextSize(16);
            description.setVisibility(View.VISIBLE);
            description.setText(v.getDefinition());
            description.setLayoutParams(layoutParams2);
            description.setTextColor(Color.BLACK);
            description.setRight(30);
            // description.setTextColor(0);

            addView(word);
            addView(description);
        }
    }
    private static String TAG = "VOCABULARY ACTIVITY";
    public void queryVocabulary(){
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    pd.dismiss();
                    if(response.equals(" []")){
                        Toast.makeText(getApplicationContext(), "EMPTY RESPONSE!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        JSONArray jsonArray = new JSONArray(response);
                        wordsCollected.setText("Words Collected: " + jsonArray.length());
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Vocabulary v = new Vocabulary();
                            v.setId(jsonObject.getInt("id"));
                            v.setWord(jsonObject.getString("word"));
                            v.setDefinition(jsonObject.getString("definition"));
                            wordListLayout.addView(new VocabularyView(getApplicationContext(),v));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
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
                params.put("query", "select_vocabulary");
                return params;
            }

        };

        strReq.setShouldCache(false);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


}
