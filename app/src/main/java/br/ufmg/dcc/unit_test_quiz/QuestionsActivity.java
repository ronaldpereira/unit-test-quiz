package br.ufmg.dcc.unit_test_quiz;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Random;

import android.os.Handler;

import dcc.ufmg.br.quizdetestesunidade.R;

public class QuestionsActivity extends AppCompatActivity {

    String questionFilePath;
    private TextView textViewText;
    private ListView listViewOptions;
    private ImageButton imageButtonRefresh;
    private ImageButton imageButtonTip;
    private Question question;
    private boolean showingAnswer;
    private boolean showingTip;
    private int tipPosition;
    private int playerAnswerPosition;

    private int playerPoints;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        playerPoints = 0;

        handler = new Handler();

        questionFilePath = getIntent().getStringExtra(ModulesActivity.QUESTIONS_FILE_PATH);
        textViewText = (TextView) findViewById(R.id.textViewText);
        listViewOptions = (ListView) findViewById(R.id.listViewOptions);
        imageButtonRefresh = (ImageButton) findViewById(R.id.imageButtonRefresh);
        imageButtonRefresh.setOnClickListener(imageButtonRefreshOnClickListener);

        imageButtonTip = (ImageButton) findViewById(R.id.imageButtonTip);
        imageButtonTip.setOnClickListener(imageButtonTipOnClickListener);

        listViewOptions.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (showingAnswer) {
                    playerPoints++;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showRandomQuestion();
                        }
                    }, 1000);

                    TextView textView = (TextView) findViewById(R.id.playerPoints);
                    textView.setText(Integer.toString(playerPoints));
                }
            }
        });

        listViewOptions.setOnItemClickListener(listViewOptionsOnItemClickListener);
        showRandomQuestion();
    }


    private AdapterView.OnItemClickListener listViewOptionsOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (showingAnswer)
                return;
            showingAnswer = true;
            playerAnswerPosition = position;
            ((BaseAdapter) listViewOptions.getAdapter()).notifyDataSetChanged();
        }
    };

    private void showRandomQuestion(){
        try{
            AssetManager assets = getAssets();
            String[] questions = assets.list(questionFilePath);
            int randomIndex = Math.abs(new Random().nextInt()) % questions.length;
            InputStream inputStream = assets.open(questionFilePath + "/"+ questions[randomIndex]);
            showingAnswer = false;
            showingTip = false;
            question = new Question(inputStream);
            textViewText.setText(String.format("%s\n%s", question.getTitle(), question.getText()));
            listViewOptions.setAdapter(
                new ArrayAdapter<String>(QuestionsActivity.this, android.R.layout.simple_list_item_1, question.getOptions()){
                    @NonNull
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        if (showingAnswer) {
                            if (position == playerAnswerPosition){
                                v.setBackgroundColor(Color.RED);
                            }
                            if (position == question.getAnswer()){
                                v.setBackgroundColor(Color.GREEN);
                            }

                        }
                        if (showingTip && position == tipPosition){
                            v.setBackgroundColor(Color.RED);
                        }
                        return v;
                    }
                }
            );
        }
        catch (Exception ex) {
            Toast.makeText(QuestionsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show(); //#TODO
        }
    }

    private View.OnClickListener imageButtonRefreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showRandomQuestion();
        }
    };

    private View.OnClickListener imageButtonTipOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (showingAnswer) return;
            if (showingTip) return;
            showingTip = true;
            do {
                tipPosition = Math.abs(new Random().nextInt()) % question.getOptions().size();
            } while (tipPosition== question.getAnswer());
            ((BaseAdapter) listViewOptions.getAdapter()).notifyDataSetChanged();
        }
    };
}
