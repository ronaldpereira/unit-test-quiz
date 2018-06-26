package br.ufmg.dcc.unit_test_quiz;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class Util {

    public static int getAssetJsonSize() {

        AssetManager assets = getInstrumentation().getTargetContext().getResources().getAssets();
        InputStream inputStream;
        String        jsonString = null;
        JSONArray jsonArray;

        try {
            inputStream = assets.open("activities.json");
            jsonString  = IOUtils.readInputStream(inputStream);
        } catch (IOException ioex) {
            System.out.println(ioex.getMessage());
        }

        int count = 0;

        try {

            jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject module = jsonArray.getJSONObject(i);
                JSONArray activities = module.getJSONArray("activities");

                count += activities.length() + 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return count;
    }

}
