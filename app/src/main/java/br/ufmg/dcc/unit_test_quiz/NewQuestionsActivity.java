package br.ufmg.dcc.unit_test_quiz;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import dcc.ufmg.br.quizdetestesunidade.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NewQuestionsActivity extends AppCompatActivity {
    private ListView listView;

    private View header;
    private TextView textView;
    private int currentAnswer;
    private int currentQuestion;
    private Button progressTextView;
    private TextView numberTextView;
    private List<Question> questions;
    private ArrayList<Boolean> answered;
    private int answeredCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.list_view);

        Intent intent = getIntent();
        String questionsPath = intent.getStringExtra(ModulesActivity.QUESTIONS_FILE_PATH);
        final String questionsActivity = intent.getStringExtra(ModulesActivity.QUESTIONS_ACTIVITY);
        setTitle(questionsActivity);

        questions = readQuestions(questionsPath);

        answeredCount = 0;
        answered = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) answered.add(false);

        currentQuestion = 0;

        LayoutInflater inflater = getLayoutInflater();
        header = (View) inflater.inflate(R.layout.question_header, listView, false);

        numberTextView = (TextView) header.findViewById(R.id.question_number_text_view);

        progressTextView = (Button) header.findViewById(R.id.progress_text_button);

        ((Button) header.findViewById(R.id.next_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion = (currentQuestion + 1) % questions.size();
                updateQuestion();
            }
        });

        ((Button) header.findViewById(R.id.previous_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion--;
                currentQuestion = currentQuestion == -1 ? questions.size() - 1 : currentQuestion;
                updateQuestion();
            }
        });
        textView = (TextView) header.findViewById(R.id.text_view);

        listView.addHeaderView(header, null, false);
        updateQuestion();

    }

    private void updateQuestion() {
        updateQuestion(true);
    }

    private void updateQuestion(boolean resetAnswer) {
        numberTextView.setText(String.format("Questão %02d", currentQuestion + 1));
        progressTextView.setText(String.format("%d/%d", answeredCount, questions.size()));

        textView.setText(questions.get(currentQuestion).getText());

        final int correctAnswer = questions.get(currentQuestion).getAnswer();

        listView.setEnabled(!answered.get(currentQuestion));

        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, questions.get(currentQuestion).getOptions()
        ) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(getColor(android.R.color.background_light));
                if (answered.get(currentQuestion)) {
                    if (position == correctAnswer) {
                        view.setBackgroundColor(getColor(android.R.color.holo_green_light));
                    }
                }
                else if (currentAnswer != -1) {
                    if (position == correctAnswer) {
                        view.setBackgroundColor(getColor(android.R.color.holo_green_light));
                    } else if (position == currentAnswer) {
                        view.setBackgroundColor(getColor(android.R.color.holo_red_light));
                    }
                }
                return view;
            }
        });

        if (resetAnswer) currentAnswer = -1;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentAnswer = (int) l;
                if (currentAnswer == correctAnswer) {
                    answered.set(currentQuestion, true);
                    answeredCount++;
                }
                updateQuestion(false);
            }
        });
    }

    private List<Question> readQuestions(String path) {
        AssetManager assets = getAssets();
        List<String> files = new ArrayList<>();

        try {
            files.addAll(Arrays.asList(assets.list(path)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Collections.sort(files);

        List<Question> questions = new ArrayList<>();

        for (String fileName : files) {
            try {

                InputStream inputStream = assets.open(
                        String.format("%s/%s", path, fileName)
                );
                questions.add(new Question(inputStream));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return questions;
    }

}
