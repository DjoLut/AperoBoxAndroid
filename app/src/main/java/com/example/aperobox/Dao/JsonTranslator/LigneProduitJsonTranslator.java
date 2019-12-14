package com.example.aperobox.Dao.JsonTranslator;

import com.example.aperobox.Model.LigneProduit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LigneProduitJsonTranslator {

    public static ArrayList<LigneProduit> jsonToLigneProduits(String stringJSON) throws Exception
    {
        ArrayList<LigneProduit> ligneProduits = new ArrayList<>();
        LigneProduit ligneProduit;
        JSONArray jsonArray = new JSONArray(stringJSON);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonBox = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            ligneProduit = object.fromJson(jsonBox.toString(), LigneProduit.class);
            ligneProduits.add(ligneProduit);
        }
        return ligneProduits;
    }

    public static LigneProduit jsonToLigneProduit(String stringJSON) throws Exception
    {
        LigneProduit ligneProduit;

        JSONObject jsonBox = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        ligneProduit = object.fromJson(jsonBox.toString(), LigneProduit.class);

        return ligneProduit;
    }
}
