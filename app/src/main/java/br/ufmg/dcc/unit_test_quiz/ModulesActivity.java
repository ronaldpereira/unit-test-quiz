package br.ufmg.dcc.unit_test_quiz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dcc.ufmg.br.quizdetestesunidade.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModulesActivity extends AppCompatActivity {
    public enum ItemType {
        MODULE, ACTIVITY
    }

    public static final String QUESTIONS_FILE_PATH = "questionsFilePath";
    public static final String QUESTIONS_ACTIVITY = "questionsActivity";

    public static class ListItem {
        public ItemType type;
        public String text;
        public String questionsFilePath;

        ListItem(ItemType type, String text) {
            this.type = type;
            this.text = text;
            this.questionsFilePath = null;

        }

        public void setQuestionsFilePath(String questionsFilePath) {
            this.questionsFilePath = questionsFilePath;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public class ItemAdapter extends ArrayAdapter<ListItem> {
        ItemAdapter(@NonNull Context context, @NonNull List<ListItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public int getItemViewType(int position) {
            return Objects.requireNonNull(getItem(position)).type.ordinal();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ListItem item = getItem(position);

            assert item != null;

            if (item.type == ItemType.MODULE) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.module_list_item,
                        parent, false
                );

                convertView.setClickable(false);
                convertView.setOnClickListener(null);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.activities_list_item,
                        parent, false
                );
            }


            ((TextView) convertView).setText(item.text);

            return convertView;
        }
    }

    private ArrayList<ListItem> readActivities() {

        ArrayList<ListItem> results = new ArrayList<>();
        AssetManager assets = getAssets();
        InputStream inputStream;
        String jsonString = null;
        JSONArray jsonArray = null;
        try {
            inputStream = assets.open("activities.json");
            jsonString = IOUtils.readInputStream(inputStream);
        } catch (IOException ioex) {
            ioex.getStackTrace();
            System.exit(1);
        }

        try {
            jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject module = jsonArray.getJSONObject(i);
                results.add(new ListItem(
                        ItemType.MODULE,
                        module.getString("name"))
                );

                JSONArray activities = module.getJSONArray("activities");

                for (int i1 = 0; i1 < activities.length(); i1++) {
                    JSONObject activity = activities.getJSONObject(i1);
                    ListItem item = new ListItem(
                            ItemType.ACTIVITY,
                            activity.getString("name"));
                    item.setQuestionsFilePath(activity.getString(QUESTIONS_FILE_PATH));
                    results.add(item);
                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.activities_list_view);

        final ArrayList<ListItem> items = readActivities();
        ItemAdapter adapter = new ItemAdapter(this, items);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ModulesActivity.this, QuestionsActivity.class);
                        ListItem item = items.get(position);
                        intent.putExtra(QUESTIONS_FILE_PATH, item.questionsFilePath);
                        intent.putExtra(QUESTIONS_ACTIVITY, item.text);
                        startActivity(intent);
                    }
                }
        );

        listView.setAdapter(adapter);
    }


}
