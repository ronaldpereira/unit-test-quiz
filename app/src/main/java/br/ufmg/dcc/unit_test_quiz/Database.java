package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database {


    public static final String VERSION = "0";
    public static final String FILE = "db" + VERSION + ".json";
    public static final String PROGRESS_ = "progress_";

    public static class QuestionsData {
        public ArrayList<Integer> answers;
        public ArrayList<Integer> hints;
    }

    private static JSONObject load(Context context) {
        FileInputStream fileInputStream;

        try {
            fileInputStream = context.openFileInput(
                    FILE
            );
        } catch (FileNotFoundException e) {
            return new JSONObject();
        }

        BufferedReader r = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (true){
            try {
                line = r.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            if (line == null) break;
            sb.append(line);
        }
        try {
            return new JSONObject(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private static void store(Context context, JSONObject o) {
        FileOutputStream stream;

        try {
            stream = context.openFileOutput(FILE, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        String s = o.toString();
        try {
            stream.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void writeQuestionsData(Context context, String questionsPath, QuestionsData data) {
        String hash = IOUtils.md5(questionsPath).substring(0, 10);
        String key = String.format("%s_%s", questionsPath.replace('/', '_'), hash);

        JSONObject db = load(context);

        JSONArray a = new JSONArray();

        try {
            int total = data.answers.size();
            int answered = 0;

            for (int i = 0; i < data.answers.size(); i++) {
                JSONObject o = new JSONObject();
                o.put("a", data.answers.get(i));
                o.put("h", data.hints.get(i));
                a.put(o);

                if (data.answers.get(i) != -1) answered++;
            }
            assert db != null;
            db.put(key, a);

            db.put(PROGRESS_ + questionsPath, (double) answered / total);
        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(1);
        }

        store(context, db);
    }

    public static HashMap<String, Double> getProgress(Context context) {
        JSONObject db = load(context);

        assert db != null;
        Iterator<String> iter = db.keys();
        HashMap<String, Double> result = new HashMap<>();
        while (iter.hasNext()) {
            String key = iter.next();
            if (!key.startsWith(PROGRESS_)) continue;;

            String kp = key.substring(PROGRESS_.length());

            try {
                Double value = db.getDouble(key);
                result.put(kp, value);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }

        return result;
    }

    public static QuestionsData loadQuestionsData(Context context, String questionsPath) {
        String hash = IOUtils.md5(questionsPath).substring(0, 10);
        String key = String.format("%s_%s", questionsPath.replace('/', '_'), hash);

        JSONObject db = load(context);
        JSONArray array;
        try {
            assert db != null;
            array = db.getJSONArray(key);
        } catch (JSONException e) {
            array = null;
        }

        try {
            if (array != null) {
                QuestionsData result = new QuestionsData();
                result.answers = new ArrayList<>();
                result.hints = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = array.getJSONObject(i);
                    int a = jsonObject.getInt("a");
                    int h = jsonObject.getInt("h");
                    result.answers.add(a);
                    result.hints.add(h);
                }
                return result;
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
