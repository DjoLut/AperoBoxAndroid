package com.example.aperobox.Dao.JsonTranslator;

import com.example.aperobox.Model.Commentaire;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentaireJsonTranslator {
    public static ArrayList<Commentaire> jsonToCommentaires(String stringJSON) throws Exception{
        ArrayList<Commentaire> commentaires = new ArrayList<>();
        Commentaire commentaire;
        JSONArray jsonArray = new JSONArray(stringJSON);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonBox = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            commentaire = object.fromJson(jsonBox.toString(), Commentaire.class);
            commentaires.add(commentaire);
        }
        return commentaires;
    }

    public static Commentaire jsonToCommentaire(String stringJSON) throws Exception
    {
        Commentaire commentaire;

        JSONObject jsonBox = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        commentaire = object.fromJson(jsonBox.toString(), Commentaire.class);

        return commentaire;
    }
}
