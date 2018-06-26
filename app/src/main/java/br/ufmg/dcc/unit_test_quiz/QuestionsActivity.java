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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuestionsActivity extends AppCompatActivity {
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

    public static final String CORRECT_QUESTIONS = "correct";
    public static final String ANSWERED_QUESTIONS = "answered";
    private String questionsPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        listView = (ListView) findViewById(R.id.list_view);

        Intent intent = getIntent();
        questionsPath = intent.getStringExtra(ModulesActivity.QUESTIONS_FILE_PATH);
        final String questionsActivity = intent.getStringExtra(ModulesActivity.QUESTIONS_ACTIVITY);
        setTitle(questionsActivity);

        questions = readQuestions(questionsPath);

        Database.QuestionsData questionsData = Database.loadQuestionsData(this, questionsPath);

        if (questionsData == null) {

            answers = new ArrayList<>();
            hints = new ArrayList<>();
            for (int i = 0; i < questions.size(); i++) {
                answers.add(-1);
                hints.add(-1);
            }
        }
        else {
            answers = questionsData.answers;
            hints = questionsData.hints;

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
                showHint();
            }
        });

        ((Button) header.findViewById(R.id.next_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuestion = (currentQuestion + 1) % questions.size();
                updateQuestion();

                cancel.set(true);
                cancel = new AtomicBoolean();
                finishModule();
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
                finishModule();
            }
        });

        textView = (TextView) header.findViewById(R.id.text_view);

        listView.addHeaderView(header, null, false);
        updateQuestion();

        if (questionsData != null) nextUnanswered();;

    }

    private void showHint() {
        ArrayList<Integer> possibleHints = new ArrayList<>();
        for (int i = 0; i < questions.get(currentQuestion).getOptions().size(); i++) {
            if (questions.get(currentQuestion).getAnswer() != i) possibleHints.add(i);
        }

        int hintIndex = Math.abs(new Random().nextInt()) % possibleHints.size();
        hints.set(currentQuestion, possibleHints.get(hintIndex));
        writeData();

        updateQuestion();
    }

    private void writeData() {
        Database.QuestionsData data = new Database.QuestionsData();
        data.hints = hints;
        data.answers = answers;
        Database.writeQuestionsData(this, questionsPath, data);

    }

    private void updateQuestion() {
        updateQuestion(true);
    }

    private void updateQuestion(boolean setAdapter) {
        numberTextView.setText(String.format("Questão %02d", currentQuestion + 1));

        int correct = 0;
        int answered = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (answers.get(i) != -1) {
                if (answers.get(i) == questions.get(i).getAnswer()) correct++;
                answered++;
            }
        }

        Question question = questions.get(currentQuestion);
        if (answers.get(currentQuestion) != -1 ||
                question.getOptions().size() < 3 ||
                hints.get(currentQuestion) != - 1) {
            hintButton.setEnabled(false);
        } else {
            hintButton.setEnabled(true);
        }

        progressTextView.setText(String.format("%d/%d", correct, answered));

        if (!question.getTitle().trim().isEmpty()) {
            textView.setText(String.format("%s\n\n%s", question.getTitle(), question.getText()));
        } else {
            textView.setText(question.getText());
        }

        final int correctAnswer = question.getAnswer();

        //listView.setEnabled(answers.get(currentQuestion) == -1);

        ArrayList<String> options = question.getOptions();
        for (int i = 0; i < options.size(); i++) {
            String s = options.get(i);
            options.set(i, s.substring(0, 1).toUpperCase() + s.substring(1));

        }

        if (!setAdapter) {
            listView.invalidateViews();
            return;
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
                if (position < hints.size() && hints.get(currentQuestion) == position) return false;
                if (answers.get(currentQuestion) != -1) return false;
                return true;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = (int) l;
                answers.set(currentQuestion, index);
                writeData();
                updateQuestion(false);

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
        finishModule();
    }

    private void finishModule(){
        int correct = 0;
        int answered = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (answers.get(i) != -1) {
                if (answers.get(i) == questions.get(i).getAnswer()) correct++;
                answered++;
            }
        }
        if (answered != questions.size()) return;
        Intent intent = new Intent(QuestionsActivity.this, FinishingModule.class);
        intent.putExtra(CORRECT_QUESTIONS, correct);
        intent.putExtra(ANSWERED_QUESTIONS, answered);
        startActivity(intent);
        finish();
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
