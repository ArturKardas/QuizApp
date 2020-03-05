package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.example.quizapp.Utils.getAssetJsonData;

public class QuestActivity extends AppCompatActivity {

    Button buttonA, buttonB, buttonC, buttonD, buttonNext;
    TextView textQuest, textNumbersOfLives, textNumbersOfPunkts;
    String correct_answer = null;
    int lives = 3;
    int punkts = 0;

    List<String> keysList;
    JSONObject json, json2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String data = getAssetJsonData(getApplicationContext());

        try {
            json = new JSONObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator keysIterator = json.keys();
        keysList = new ArrayList<String>();
        while (keysIterator.hasNext()) {
            String key = (String) keysIterator.next();
            keysList.add(key);
        }


        buttonA = (Button) findViewById(R.id.button_A);
        buttonB = (Button) findViewById(R.id.button_B);
        buttonC = (Button) findViewById(R.id.button_C);
        buttonD = (Button) findViewById(R.id.button_D);
        buttonNext = (Button) findViewById(R.id.button_next);
        buttonNext.setEnabled(false);

        textQuest = (TextView) findViewById(R.id.text_quest);
        textNumbersOfLives = (TextView) findViewById(R.id.number_of_lives);
        textNumbersOfPunkts = (TextView) findViewById(R.id.number_of_punkts);

        textNumbersOfLives.setText("" + lives);
        textNumbersOfPunkts.setText("" + punkts);

        make_a_array();
        fill_the_obj();

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton("A", buttonA);
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton("B", buttonB);
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton("C", buttonC);
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton("D", buttonD);
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lives <= 0) {
                    Intent i = new Intent(QuestActivity.this, GameLossActivity.class);
                    startActivity(i);
                } else if (keysList.size() <= 0) {
                    Intent i = new Intent(QuestActivity.this, GameWinActivity.class);
                    startActivity(i);
                } else {
                    setButtonsEnabled(true);
                    fill_the_obj();
                    cleanButtons();
                }
            }
        });
    }


    void make_a_array() {
        try {
            json2 = json.getJSONObject("0");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void fill_the_obj() {
        String quest = "Q", A = "A", B = "B", C = "C", D = "D";
        if (keysList.size() < 1) {
        } else {
            try {
                int q = new Random().nextInt(keysList.size());
                String counterOfKeys = keysList.get(q);
                keysList.remove(q);

                json2 = json.getJSONObject(counterOfKeys);
                quest = (String) json2.get("quest");
                A = (String) json2.get("A");
                B = (String) json2.get("B");
                C = (String) json2.get("C");
                D = (String) json2.get("D");

                correct_answer = (String) json2.get("correct");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            textQuest.setText(quest);
            buttonA.setText(A);
            buttonB.setText(B);
            buttonC.setText(C);
            buttonD.setText(D);
        }
    }

    void onClickButton(String answear, Button button) {
        if (Objects.equals(correct_answer, answear)) {//jeśli dobra odpowiedź to dodają się punkty
            button.setBackground(getResources().getDrawable(R.drawable.btn_good_answear));
            setButtonsEnabled(false);
            punkts += 10;
            textNumbersOfPunkts.setText("" + punkts);

        } else { //jesli zła odpowiedź to odejmowane jest życie o jeden :D
            button.setBackground(getResources().getDrawable(R.drawable.btn_bad_answear));
            setButtonsEnabled(false);
            --lives;
            textNumbersOfLives.setText("" + lives);
        }

    }

    void setButtonsEnabled(boolean value) {
        buttonA.setEnabled(value);
        buttonB.setEnabled(value);
        buttonC.setEnabled(value);
        buttonD.setEnabled(value);
        buttonNext.setEnabled(!value);
    }

    private void cleanButtons() {
        buttonA.setBackground(getResources().getDrawable(R.drawable.btn_shape));
        buttonB.setBackground(getResources().getDrawable(R.drawable.btn_shape));
        buttonC.setBackground(getResources().getDrawable(R.drawable.btn_shape));
        buttonD.setBackground(getResources().getDrawable(R.drawable.btn_shape));
    }

}
