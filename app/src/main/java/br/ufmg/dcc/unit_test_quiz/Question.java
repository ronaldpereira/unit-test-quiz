package br.ufmg.dcc.unit_test_quiz;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Question {
    private String title;
    private String text;
    private ArrayList<String> options;
    private int answer;

    public Question(InputStream inputStream) throws Exception {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while (true){
            line = r.readLine();
            if (line == null) break;
            sb.append(line);
        }
        JSONObject object = new JSONObject(sb.toString());
        title = object.getString("title");
        text = object.getString("text");
        JSONArray jsonOptions = object.getJSONArray("options");
        options = new ArrayList<String>();
        for (int i = 0; i < jsonOptions.length(); i++) {
            options.add(jsonOptions.getString(i));
        }
        answer = object.getInt("answer");
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int getAnswer() {
        return answer;
    }
}
