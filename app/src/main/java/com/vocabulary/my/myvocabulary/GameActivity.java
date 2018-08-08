package com.vocabulary.my.myvocabulary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.textservice.SpellCheckerSession;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by David on 19/08/2017.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.btnBack) ImageView back;
    @BindView(R.id.btnSubmit) ImageView submit;
    @BindView(R.id.txtTimer) TextView txtTimer;
    @BindView(R.id.txtWordCount) TextView txtWordCount;
    @BindView(R.id.gameLayout) LinearLayout game;
    @BindView(R.id.layoutAnswer) LinearLayout answer;
    @BindView(R.id.txtStarPoints) TextView txtStarPoints;
    @BindViews({R.id.letter1,R.id.letter2, R.id.letter3, R.id.letter4, R.id.letter5,
                R.id.letter6,R.id.letter7, R.id.letter8, R.id.letter9, R.id.letter10,
                R.id.letter11, R.id.letter12, R.id.letter13, R.id.letter14, R.id.letter15,
                R.id.letter16, R.id.letter17, R.id.letter18, R.id.letter19, R.id.letter20})
    List<TextView> txtLetters;
    public CountDownTimer timer;
    private String word;
    private SpellCheckerSession mScs;
    private int counter;
    private int level;
    private int stage;
    private int accuPoints;
    private int txtAnswerId[] = new int[]{R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7};

    private Session session;
    private List<TextView> txtAnswers = new ArrayList<>();
    private long countDownTime = 0;
    ProgressDialog pd;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        android.support.v7.app.ActionBar a = getSupportActionBar();
        if(a != null){
            a.hide();
        }

        pd = new ProgressDialog(GameActivity.this);
        pd.setMessage("Querying...");
        pd.setCancelable(false);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Wicked Mouse Demo.otf");
        for(int i = 0; i < txtLetters.size(); i++) {
            txtLetters.get(i).setTypeface(custom_font);
            txtLetters.get(i).setText("");
        }

        accuPoints = 0;
        session = Session.getInstance(GameActivity.this);
    //    Toast.makeText(getApplicationContext(), "ENTERED stage 1 pointss: " + session.getStarStage(level, stage) + " overall points: " + session.getPoints(), Toast.LENGTH_LONG).show();

        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                countDownTime = millisUntilFinished;
                txtTimer.setText((minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
            }

            public void onFinish() {
                txtTimer.setText("done!");
                timeup();
            }
        };
        txtTimer.setTextColor(Color.parseColor("#000000"));
        //txtAnswer.setTypeface(custom_font);

        generate();

        counter = 0;
        level = Integer.parseInt(getIntent().getExtras().getString("level"));
        stage = Integer.parseInt(getIntent().getExtras().getString("stage"));
        /* DYNAMIC UI ANSWER BOX */
        int textBoxes = 5;
        int answerLayoutMargin = 75;
        int textBoxSize = 115;
        int textSize = 38;
        int textTop = 0;
        int textBottom = 5;
        if(level >= 4 && level <= 6){
            textBoxes = 6;
            answerLayoutMargin = 15;
        }
        else if (level >= 7 && level <= 9){
            textBoxes = 7;
            answerLayoutMargin = 5;
            textBoxSize = 100;
            textSize = 30;
            textTop = 40;
            textBottom = 30;
        }

        session = Session.getInstance(GameActivity.this);
        answer.setRight(0);
        answer.setLeft(0);
        LinearLayout.LayoutParams answerLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        answerLayoutParam.setMargins(answerLayoutMargin,textTop,0,0);
        answer.setLayoutParams(answerLayoutParam);

        for(int i = 0; i < textBoxes; i++) {
            RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
            ImageView imageTest = new ImageView(getApplicationContext());
            imageTest.setVisibility(View.VISIBLE);
            imageTest.setImageResource(R.drawable.square);
            relativeLayout.addView(imageTest);

            LinearLayout.LayoutParams rel = new LinearLayout.LayoutParams(textBoxSize,textBoxSize);
            rel.setMargins(0, 0, 0, 5);

            relativeLayout.setLayoutParams(rel);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            llp.setMargins(55, 0, 0, textBottom); // llp.setMargins(left, top, right, bottom);

            TextView t = new TextView(getApplicationContext());
            t.setText("");
            t.setTextSize(textSize);
            t.setLeft(10);
            t.setTypeface(custom_font);
            t.setLayoutParams(llp);
            t.setId(txtAnswerId[i]);
            t.setOnClickListener(this);
            t.setTextColor(Color.GRAY);
            t.setGravity(Gravity.CENTER);

            txtAnswers.add(t);
            relativeLayout.addView(t);
            answer.addView(relativeLayout);
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(counter != 3){
            if((session.getPoints()-accuPoints) > 0) {
                session.setPoints(session.getPoints() - accuPoints);
                session.setStarStage(level, stage, session.getStarStage(level, stage) - accuPoints);
            }
            else{
                session.setPoints(0);
            }
        }
    }

    @OnClick({R.id.letter1,R.id.letter2,R.id.letter3,R.id.letter4,R.id.letter5,
              R.id.letter6,R.id.letter7,R.id.letter8,R.id.letter9,R.id.letter10,
              R.id.letter11,R.id.letter12,R.id.letter13,R.id.letter14,R.id.letter15,
              R.id.letter16,R.id.letter17,R.id.letter18,R.id.letter19,R.id.letter20})
    public void letters(View v){
        TextView t = (TextView) findViewById(v.getId());
        if(!isFullAnswer()) {
            for (int i = 0; i < txtAnswers.size(); i++) {
                if (txtAnswers.get(i).getText() == "") {
                    txtAnswers.get(i).setText(t.getText());
                    break;
                }
            }
            t.setText("");
        }
    }

    public void setGamePoints(int score){
        txtStarPoints.setText(score+"");
    }
    public void newTimer(){
        timer.cancel();
        timer = new CountDownTimer(countDownTime + 20000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                countDownTime = millisUntilFinished;
                txtTimer.setText((minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
            }

            public void onFinish() {
                txtTimer.setText("done!");
                timeup();
            }
        };
        timer.start();
    }
    @Override
    public void onResume(){
        super.onResume();
        game.setBackgroundColor(Session.getInstance(GameActivity.this).getBackground());
        timer.start();
        txtStarPoints.setText(session.getPoints()+"");
    }

    @Override
    public void onPause(){
        super.onPause();
        timer.cancel();
    }

    private void answerClear(){
        for(int i = 0; i < txtAnswers.size(); i++){
            txtAnswers.get(i).setText("");
        }
    }

    private void wordLimit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Word Limit!")
                .setMessage("You did not reach the limit!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    private void congratsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congratulations!")
                .setMessage("You finished Level " + level+ "!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();
    }

    private void emptyString(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Empty String!")
                .setMessage("The text contains empty, please try again!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
    private boolean isFullAnswer(){
        boolean ok = true;

        for(int i = 0; i < txtAnswers.size(); i++){
            if(txtAnswers.get(i).getText().equals("")){
                ok = false;
                break;
            }
        }

        return ok;
    }

    private void timeup(){
        if(settingsDialog != null) {
            settingsDialog.hide();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("")
                .setCancelable(false)
                .setMessage("Time is up!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        session.setPoints(session.getPoints()-accuPoints);
                        session.setStarStage(level,stage,session.getStarStage(level,stage) - accuPoints);
                        finish();
                    }
                });
        builder.show();
    }

    @OnClick(R.id.btnBack)
    public void back(){
        onBackPressed();
    }


    public String getAnswer(){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < txtAnswers.size(); i++){
            if(txtAnswers.get(i).getText().length() != 0)
                sb.append(Character.toLowerCase(txtAnswers.get(i).getText().charAt(0))+"");
        }
        return sb.toString();
    }
    @OnClick(R.id.btnSubmit)
    public void submit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit")
                .setMessage("Do you want to submit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String answer = getAnswer();
                      //  if() {
                            checkDictionary(answer);
                       // }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
       // Toast.makeText(getApplicationContext(),getAnswer(),Toast.LENGTH_LONG).show();
        String answer = getAnswer();
        int length = answer.length();
        if(length == 0){
            emptyString();
        } else if (level >= 1 && level <= 3 && length < 3) {
            wordLimit();
        } else if (level >= 4 && level <= 6 && length < 4) {
            wordLimit();
        } else if (level >= 7 && level <= 9 && length < 5) {
            wordLimit();
        } else if(!answer.contentEquals("")) {
            checkDictionary(answer);
        }
    }
   // @OnClick(R.id.btnGenerate)
    public void generate(){
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int N = alphabet.length();
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < txtLetters.size(); i++) {
            Random r = new Random(System.currentTimeMillis());
          //  if(txtLetters.get(i).getText().equals("")){
                txtLetters.get(i).setText(alphabet.charAt(random.nextInt(N))+"");
        //    }
            //    txtLetters.get(i).setTextColor(Color.parseColor("#000000"));
        }
    }
    private SpecialAbilityDialog settingsDialog;

    @OnClick({R.id.imgSpecialSkills})
    public void specialAbility(){
        settingsDialog = new SpecialAbilityDialog(GameActivity.this);
        Window window = settingsDialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
      //  settingsDialog.setTextSpecialPoints(session.getPoints());
        settingsDialog.show();
    }

    public void noWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!")
                .setMessage("No such word!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //answerClear();
                    }
                });
        builder.show();
    }
    private static String TAG = "GAME ACTIVITY";

    private int getAnswerLength(){
        int length = 0;
        for(int i = 0; i < txtAnswers.size(); i++){
            if(txtAnswers.get(i).getText() != ""){
                length++;
            }
            else { break;}
        }
        return length;
    }
    private boolean anotherStage = false;
    public void checkDictionary(final String word){
        pd.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    pd.dismiss();
                    if(response.equals(" []")){
                       noWord();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"CORRECT!",Toast.LENGTH_LONG).show();
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String jWord = jsonObject.getString("word").trim();
                        int length = getAnswerLength();
                        int points = session.getStarStage(level,stage);


                            if (counter < 3) {
                                int cpoints = 0;
                                ++counter;
                                if (counter != 3)
                                    txtWordCount.setText((counter + 1) + "/3");

                                if (counter == 3) {
                                    cpoints += calculatePoints(length,level,stage);
                                    accuPoints += cpoints;
                                   // Toast.makeText(getApplicationContext(), "cpoints: " + cpoints + " session get points: " + session.getPoints(), Toast.LENGTH_LONG).show();
                                    session.setPoints(session.getPoints()+cpoints);
                                    session.setStarStage(level, stage, session.getStarStage(level,stage)+cpoints);
                                    txtStarPoints.setText(session.getPoints()+"");
                                    // Toast.makeText(getApplicationContext(), "stage 1 pointss: " + session.getStarStage(level, stage) + " overall points: " + session.getPoints(), Toast.LENGTH_LONG).show();
                                  if(stage == 4)
                                    {congratsDialog();}
                                    else{finish();}
                                }
                                else {
                                    cpoints += calculatePoints(length, level, stage);
                                    accuPoints += cpoints;
                                    //    Toast.makeText(getApplicationContext(), "cpoints: " + cpoints + " session get points: " + session.getPoints(), Toast.LENGTH_LONG).show();
                                    session.setPoints(session.getPoints() + cpoints);
                                    session.setStarStage(level, stage, session.getStarStage(level, stage) + cpoints);
                                    //    Toast.makeText(getApplicationContext(), "stage 1 pointss: " + session.getStarStage(level, stage) + " overall points: " + session.getPoints(), Toast.LENGTH_LONG).show();
                             //       Toast.makeText(getApplicationContext(), "COUNTER POINTS: " + cpoints + " LENGTH: " + length, Toast.LENGTH_LONG).show();
                                    txtStarPoints.setText(session.getPoints()+"");

                                    answerClear();
                                    generate();
                                }
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
                params.put("word", word);
                params.put("query", "select_word");
                return params;
            }

        };

        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private int calculatePoints(int length,int level, int stage){
        int points = 0;

        if (level >= 1 && level <= 3) {
            if (length == 3) {
                points += 1;
            } else if (length == 4) {
                points += 2;
            } else if (length >= 5) {
                points += 3;
            } else {
                wordLimit();
            }
        } else if (level >= 4 && level <= 6) {
            if (length == 4) {
                points += 1;
            } else if (length == 5) {
                points += 2;
            } else if (length >= 6) {
                points += 3;
            } else {
                wordLimit();
            }
        } else if (level >= 7 && level <= 9) {
            if (length == 5) {
                points += 1;
            } else if (length == 6) {
                points += 2;
            } else if (length >= 7) {
                points += 3;
            } else {
                wordLimit();
            }
        }

        return points;
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) findViewById(v.getId());
        for(int i = 0; i < txtLetters.size(); i++) {
            if(txtLetters.get(i).getText().equals("")) {
                txtLetters.get(i).setText(textView.getText());
                break;
            }
        }
        textView.setText("");
    }
}
