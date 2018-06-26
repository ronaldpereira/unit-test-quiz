package br.ufmg.dcc.unit_test_quiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;

import dcc.ufmg.br.quizdetestesunidade.R;

public class TestWizActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    boolean init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wiz);

        getSupportActionBar().setTitle(null);

        imageSwitcher = ((ImageSwitcher) findViewById(R.id.imageSwitcher));
        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSwitcher.showNext();
                Handler handler = new Handler();
                if (!init) return;
                init = false;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(TestWizActivity.this, ModulesActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
            }
        });

    }
}
