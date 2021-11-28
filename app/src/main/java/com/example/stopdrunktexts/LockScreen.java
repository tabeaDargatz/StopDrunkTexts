package com.example.stopdrunktexts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LockScreen extends AppCompatActivity
{

    int a;
    int b;
    int answer;
    int o = 0;
    TextView questionText;
    TextView headerText;
    EditText enteredAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        questionText = findViewById(R.id.txt_question);
        headerText = findViewById(R.id.txt_header);
        enteredAnswer = findViewById(R.id.editTxt_answer);
        generateQuestionAnswer();
        startService(new Intent(this, CheckForAppsAndDisplayLock.class));
    }

    @Override
    public void onBackPressed()
    {
        headerText.setText("There is no escape. :) Now answer the question.");
    }

    public void checkSolution(View view)
    {
        String enteredanswer = enteredAnswer.getText().toString();
        if(answer == Integer.parseInt(enteredanswer)){
            stopService(new Intent(this, CheckForAppsAndDisplayLock.class));
            finishAndRemoveTask();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Answer incorrect. Maybe you shouldn't text her, old man...",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void newQuestionAnswer(View view){
        generateQuestionAnswer();
        Toast toast = Toast.makeText(getApplicationContext(),
                "New question generated.",
                Toast.LENGTH_SHORT);
        toast.show();
    }


    private void generateQuestionAnswer()
    {
        a = (int)(Math.random() * 41) + 10;
        b = (int)(Math.random() * 41) + 10;
        o = (int) (Math.random() * 3) + 1;
        switch (o)
        {
            case 1:
                b = (int)(Math.random() * 5) + 2;
                questionText.setText("What is the closest prime number smaller than "+ a + " multiplied by "+b+" ?");
                answer = Calculator.nearestPrime(a) *b;
                break;


            case 2:
                a = (int)(Math.random() * 11) + 1;
                b = (int)(Math.random() * 11) + 1;
                while(a == b){
                    b = (int)(Math.random() * 11) + 1;
                }
                questionText.setText("What is the " + a + ". prime number + the " + b + ". prime number?");
                answer = Calculator.nthPrime(a) + Calculator.nthPrime(b);
                break;


            case 3:
                questionText.setText("What is "+ a+ " * " +b + "?");
                answer = a*b;
                break;


            case 4:
                questionText.setText("What is "+ a+ " + " +b + "?");
                answer = a+b;
                break;

            case 5:
                questionText.setText("What is "+ a+ " - " +b + "?");
                answer = a-b;
                break;

            default:
                questionText.setText("Oops - Something went wrong. Please refresh!");
                answer = 0;
        }

    }
}