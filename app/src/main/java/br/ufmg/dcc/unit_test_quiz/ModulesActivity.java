package br.ufmg.dcc.unit_test_quiz;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import dcc.ufmg.br.quizdetestesunidade.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModulesActivity extends AppCompatActivity {

    public enum ItemType {
        MODULE, ACTIVITY
    }

    public static class ListItem {
        public ItemType type;
        public String text;

        public ListItem(ItemType type, String text) {
            this.type = type;
            this.text = text;

        }

        @Override
        public String toString() {
            return text;
        }
    }

    public class ItemAdapter extends ArrayAdapter<ListItem> {
        public ItemAdapter(@NonNull Context context, @NonNull List<ListItem> objects) {
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

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        item.type == ItemType.MODULE ? R.layout.module_list_item : R.layout.activities_list_item,
                        parent, false
                );
            }

            ((TextView) convertView).setText(item.text);

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ListView lv = (ListView) findViewById(R.id.activities_list_view);

        ArrayList<ListItem> items = new ArrayList<>();
        items.add(new ListItem(ItemType.MODULE, "Conceitos básicos"));
        items.add(new ListItem(ItemType.ACTIVITY, "Testes automatizados"));
        items.add(new ListItem(ItemType.ACTIVITY, "Classes de equivalência e comportamento"));

        items.add(new ListItem(ItemType.MODULE, "Testes de unidade"));
        items.add(new ListItem(ItemType.ACTIVITY, "Controladores e cotos"));
        items.add(new ListItem(ItemType.ACTIVITY, "Testando métodos privados"));

        items.add(new ListItem(ItemType.MODULE, "Outros testes"));
        items.add(new ListItem(ItemType.ACTIVITY, "Teste de desempenho"));

        ItemAdapter adapter = new ItemAdapter(this, items);


        lv.setAdapter(adapter);

        // Performatividade de trabalho
    }

}
