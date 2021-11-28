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

    int a = 34;
    int b = 12;
    int answer = 0;
    int o = 0;
    int wrongAnswers = 0;
    TextView questionText;
    TextView headerText;
    EditText enteredAnswer;
    private Toast toast;

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
        toast = new Toast(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(),
                "Hi Maciej :)",
                Toast.LENGTH_LONG);
    }

    @Override
    public void onBackPressed()
    {
        toast.setText("There is no escape. :) Now answer the question.");
        toast.show();
    }

    public void checkSolution(View view)
    {
        String enteredanswer = enteredAnswer.getText().toString();
        if(enteredanswer.isEmpty())
        {
            toast.setText("Too drunk to even enter an answer...?");
            toast.show();
        }

        else{
            if(answer == Integer.parseInt(enteredanswer)){
                toast.setText("Hm. I guess you win this time. Unlocking...");
                toast.show();
                stopService(new Intent(this, CheckForAppsAndDisplayLock.class));
                finishAndRemoveTask();
            }
            else if(wrongAnswers < 2){
                toast.setText("Answer incorrect. Maybe you shouldn't text her, old man...");
                toast.show();
                wrongAnswers++;
            }
            else{
                wrongAnswers = 0;
                newQuestionAnswer();
            }
        }
    }

    public void newQuestionAnswer(){
        generateQuestionAnswer();
        toast.setText("Stop guessing. That won't work, I'll just generate a new question, idiot!");
        toast.show();
    }


    private void generateQuestionAnswer()
    {

        o = (int) (Math.random() * 4) + 1;
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
                a = (int)(Math.random() * 41) + 10;
                while(a == 10 || a == 20 || a == 30 || a == 40 || a == 50){
                    a = (int)(Math.random() * 41) + 10;
                }
                b = (int)(Math.random() * 41) + 10;
                while(b == 10 || b == 20 || b == 30 || b == 40 || b == 50){
                    a = (int)(Math.random() * 41) + 10;
                }


                questionText.setText("What is "+ a+ " * " +b + "?");
                answer = a*b;
                break;


            case 4:
                a = (int)(Math.random() * 5751) + 1014;
                b = (int)(Math.random() * 5552) + 1213;
                questionText.setText("What is "+ a+ " + " +b + "?");
                answer = a+b;
                break;

            default:
                questionText.setText("Oops - Something went wrong. Please refresh!");
                answer = 0;
        }

    }
}