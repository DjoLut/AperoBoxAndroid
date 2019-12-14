package com.example.aperobox.Dao.JsonTranslator;

import com.example.aperobox.Model.Box;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BoxJsonTranslator {

    public static ArrayList<Box> jsonToBoxes(String stringJSON) throws Exception
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


    public static Box jsonToBox(String stringJSON) throws Exception
    {
        Box box;

        JSONObject jsonBox = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        box = object.fromJson(jsonBox.toString(), Box.class);

        return box;
    }
}
