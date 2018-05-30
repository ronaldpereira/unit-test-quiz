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
import java.util.ArrayList;
import java.util.Random;

import dcc.ufmg.br.quizdetestesunidade.R;

public class MainActivity extends AppCompatActivity {

    private TextView textViewText;
    private ListView listViewOptions;
    private ImageButton imageButtonRefresh;
    private ImageButton imageButtonTip;
    private Question question;
    private boolean showingAnswer;
    private boolean showingTip;
    private int tipPosition;
    private int playerAnswerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewText = (TextView) findViewById(R.id.textViewText);
        listViewOptions = (ListView) findViewById(R.id.listViewOptions);
        imageButtonRefresh = (ImageButton) findViewById(R.id.imageButtonRefresh);
        imageButtonRefresh.setOnClickListener(imageButtonRefreshOnClickListener);

        imageButtonTip = (ImageButton) findViewById(R.id.imageButtonTip);
        imageButtonTip.setOnClickListener(imageButtonTipOnClickListener);


        ArrayList<String> vector = new ArrayList<>();
        vector.add("Rafael");
        vector.add("Ivan");
        //hidden street

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
            String[] questions = assets.list("questions");
            int randomIndex = Math.abs(new Random().nextInt()) % questions.length;
            InputStream inputStream = assets.open("questions/" + questions[randomIndex]);
            showingAnswer = false;
            showingTip = false;
            question = new Question(inputStream);
            textViewText.setText(String.format("%s\n%s", question.getTitle(), question.getText()));
            listViewOptions.setAdapter(
                    new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, question.getOptions()){
                        @NonNull
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            if (showingAnswer) {
                                if (position == playerAnswerPosition)
                                    v.setBackgroundColor(Color.RED);
                                if (position == question.getAnswer())
                                    v.setBackgroundColor(Color.GREEN);
                            }
                            if (showingTip && position == tipPosition)
                                v.setBackgroundColor(Color.RED);
                            return v;
                        }
                    }
            );
        }
        catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show(); //#TODO
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
