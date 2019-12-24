package com.example.aperobox.Application;

import android.content.res.Resources;
import android.util.Log;
import com.example.aperobox.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeEntry {
    private static final String TAG = JokeEntry.class.getSimpleName();

    private final String base;
    private final String reponse;
    private static List<JokeEntry> list;

    private JokeEntry(String base, String reponse){
        this.base = base;
        this.reponse = reponse;
    }

    public static void initJokeEntryList(Resources resources) {
        if(list==null) {
            InputStream inputStream = resources.openRawResource(R.raw.joke);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                int pointer;
                while ((pointer = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, pointer);
                }
            } catch (IOException exception) {
                Log.e(TAG, "Error writing/reading from the JSON file.", exception);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    Log.e(TAG, "Error closing the input stream.", exception);
                }
            }
            String jsonProductsString = writer.toString();
            Gson gson = new Gson();
            Type jokeListType = new TypeToken<ArrayList<JokeEntry>>() {
            }.getType();
            list = gson.fromJson(jsonProductsString, jokeListType);
        }
    }

    public String getBase() {
        return base;
    }

    public String getReponse() {
        return reponse;
    }

    public static JokeEntry getRandom(){
        return list.get(new Random().nextInt(list.size()));
    }
}
