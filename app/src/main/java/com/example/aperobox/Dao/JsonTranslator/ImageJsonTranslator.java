package com.example.aperobox.Dao.JsonTranslator;

import android.media.Image;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public class ImageJsonTranslator {


    public static Image jsonToLigneProduit(String stringJSON) throws Exception
    {
        Image image;

        JSONObject jsonBox = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        image = object.fromJson(jsonBox.toString(), Image.class);

        return image;
    }
}
