package br.ufmg.dcc.unit_test_quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import dcc.ufmg.br.quizdetestesunidade.R;

public class FinishingModule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishing_module);
        Intent intent = getIntent();
        int correct = (Integer) intent.getIntExtra(QuestionsActivity.CORRECT_QUESTIONS, -1);
        int answered = (Integer) intent.getIntExtra(QuestionsActivity.ANSWERED_QUESTIONS, -1);

        ((TextView) findViewById(R.id.textViewRate)).append(String.valueOf(correct) + " de " + String.valueOf(answered) + " questões");
    }
}
