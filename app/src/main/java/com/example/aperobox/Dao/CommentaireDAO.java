package com.example.aperobox.Dao;

import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CommentaireDAO {

    public ArrayList<Commentaire> getAllCommentaireFromBox(int boxId) throws Exception {
        ArrayList<Commentaire> commentaires = new ArrayList<>();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        URL url = new URL(Constantes.URL_API + "Commentaire/Box/" + boxId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestProperty("Content-Type", "application/json");

        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader buffer = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null)
        {
            builder.append(line);
        }
        buffer.close();
        connection.disconnect();

        String inputStringJSON = builder.toString();
        JSONArray jsonArray = new JSONArray(inputStringJSON);
        for(int i = 0; i < jsonArray.length(); i++)
        {
            commentaires.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Commentaire.class));
        }

        return commentaires;
    }



    public int ajoutCommentaire(String token, Commentaire newCommentaire)throws Exception {
        int resultCode;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String outputJsonString = gson.toJson(newCommentaire);
        URL url = new URL(Constantes.URL_API + "Commentaire");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(false);
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes);
        outputStream.flush();
        outputStream.close();
        resultCode = connection.getResponseCode();
        connection.disconnect();
        if(resultCode!=201)
            throw new HttpResultException(resultCode);
        return resultCode;
    }

}
