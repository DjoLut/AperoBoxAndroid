package com.example.aperobox.Dao;

import com.example.aperobox.Dao.JsonTranslator.CommentaireJsonTranslator;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.Utility.Constantes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CommentaireDAO {

    public ArrayList<Commentaire> getAllCommentaireFromBox(int boxId) throws Exception {
        URL url = new URL(Constantes.URL_API + "Commentaire/Box/" + boxId);
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
        return CommentaireJsonTranslator.jsonToCommentaires(stringJSON);
    }

    public Commentaire getCommentaire(int id) throws Exception {
        URL url = new URL(Constantes.URL_API+"Commentaire/Box/Commentaire/"+id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        if((line = buffer.readLine())!=null){
            return CommentaireJsonTranslator.jsonToCommentaire(line);
        }
        return null;
    }

}
