package br.ufmg.dcc.unit_test_quiz;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewQuestionsActivity extends AppCompatActivity {
    private ListView listView;

    private View header;
    private Handler handler;
    private TextView textView;
    private Button hintButton;
    private int currentQuestion;
    private Button progressTextView;
    private TextView numberTextView;
    private List<Question> questions;
    private ArrayList<Integer> answers;
    private ArrayList<Integer> hints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        listView = (ListView) findViewById(R.id.list_view);

        Intent intent = getIntent();
        String questionsPath = intent.getStringExtra(ModulesActivity.QUESTIONS_FILE_PATH);
        final String questionsActivity = intent.getStringExtra(ModulesActivity.QUESTIONS_ACTIVITY);
        setTitle(questionsActivity);

        questions = readQuestions(questionsPath);

        answers = new ArrayList<>();
        hints = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            answers.add(-1);
            hints.add(-1);
        }

        currentQuestion = 0;

        LayoutInflater inflater = getLayoutInflater();
        header = (View) inflater.inflate(R.layout.question_header, listView, false);

        numberTextView = (TextView) header.findViewById(R.id.question_number_text_view);

        progressTextView = (Button) header.findViewById(R.id.progress_text_button);

        hintButton = (Button) header.findViewById(R.id.hint_button);

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giveAHint();
            }
        });

        ((Button) header.findViewById(R.id.next_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion = (currentQuestion + 1) % questions.size();
                updateQuestion();

                cancel.set(true);
                cancel = new AtomicBoolean();
            }
        });

        ((Button) header.findViewById(R.id.previous_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion--;
                currentQuestion = currentQuestion == -1 ? questions.size() - 1 : currentQuestion;
                updateQuestion();

                cancel.set(true);
                cancel = new AtomicBoolean();
            }
        });

        textView = (TextView) header.findViewById(R.id.text_view);

        listView.addHeaderView(header, null, false);
        updateQuestion();
    }

    private void giveAHint() {
        ArrayList<Integer> possibleHints = new ArrayList<>();

        for (int i = 0; i < questions.get(currentQuestion).getOptions().size(); i++) {
            if (questions.get(currentQuestion).getAnswer() != i) possibleHints.add(i);
        }

        int hintIndex = Math.abs(new Random().nextInt()) % possibleHints.size();
        hints.set(currentQuestion, possibleHints.get(hintIndex));
        updateQuestion();
    }

    private void updateQuestion() {
        numberTextView.setText(String.format("Questão %02d", currentQuestion + 1));

        int correct = 0;
        int answered = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (answers.get(i) != -1) {
                if (answers.get(i) == questions.get(i).getAnswer()) correct++;
                answered++;
            }
        }

        if (answers.get(currentQuestion) != -1 ||
                questions.get(currentQuestion).getOptions().size() < 3 ||
                hints.get(currentQuestion) != - 1) {
            hintButton.setEnabled(false);
        } else {
            hintButton.setEnabled(true);
        }

        progressTextView.setText(String.format("%d/%d", correct, answered));

        textView.setText(questions.get(currentQuestion).getText());

        final int correctAnswer = questions.get(currentQuestion).getAnswer();

        //listView.setEnabled(answers.get(currentQuestion) == -1);

        ArrayList<String> options = questions.get(currentQuestion).getOptions();
        for (int i = 0; i < options.size(); i++) {
            String s = options.get(i);
            options.set(i, s.substring(0, 1).toUpperCase() + s.substring(1));

        }

        listView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, options
        ) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(getColor(android.R.color.background_light));

                if (answers.get(currentQuestion) != -1) {
                    if (position == correctAnswer) {
                        view.setBackgroundColor(getColor(android.R.color.holo_green_light));
                    } else if (position == answers.get(currentQuestion)) {
                        view.setBackgroundColor(getColor(android.R.color.holo_red_light));
                    }
                }
                if (position == hints.get(currentQuestion)) {
                    view.setBackgroundColor(getColor(android.R.color.holo_orange_light));
                }

                return view;
            }

            @Override
            public boolean isEnabled(int position) {
                if (position < hints.size() && hints.get(position) != -1) return false;
                if (answers.get(currentQuestion) != -1) return false;
                return super.isEnabled(position);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = (int) l;
                answers.set(currentQuestion, index);
                updateQuestion();

                final AtomicBoolean cancelCopy = cancel;
                cancelCopy.set(false);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!cancelCopy.get()) nextUnanswered();
                    }
                }, 3000);
            }
        });

    }

    private AtomicBoolean cancel = new AtomicBoolean();

    private void nextUnanswered() {
        for (int i = 1; i <= questions.size(); i++) {
            int j = (currentQuestion + i) % questions.size();
            if (answers.get(j) == -1) {
                currentQuestion = j;
                updateQuestion();
                break;
            }
        }
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
