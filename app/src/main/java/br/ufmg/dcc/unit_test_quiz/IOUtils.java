package br.ufmg.dcc.unit_test_quiz;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static String readInputStream(InputStream inputStream) throws IOException{
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while (true){
            line = r.readLine();
            if (line == null) break;
            sb.append(line);
        }

        return sb.toString();
    }
}
