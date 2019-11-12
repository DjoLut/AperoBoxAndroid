package com.example.aperobox.Dao;

import com.example.aperobox.Model.Box;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BoxDAO {

    public ArrayList<Box> getAllBox() throws Exception {
        URL url = new URL("https://example.com/allBox");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null)
        {
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();
        return jsonToBoxes(stringJSON);
    }

    public Box getBox(int id) throws Exception {
        URL url = new URL("https://example.com/allBox/"+id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "";

        return jsonToBox(stringJSON);

    }

    private ArrayList<Box> jsonToBoxes(String stringJSON) throws Exception
    {
        ArrayList<Box> boxes = new ArrayList<>();
        Box box;
        JSONArray jsonArray = new JSONArray(stringJSON);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonBox = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            box = object.fromJson(jsonBox.toString(), Box.class);
            boxes.add(box);
        }
        return boxes;
    }


    private Box jsonToBox(String stringJSON) throws Exception
    {
        Box box;

        JSONObject jsonBox = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        box = object.fromJson(jsonBox.toString(), Box.class);

        return box;
    }

}
