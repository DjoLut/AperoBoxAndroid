package com.example.aperobox.Dao;

import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentaireDAO {

    public ArrayList<Commentaire> getAllCommentaireFromBox(Integer boxId) throws Exception {
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
        return jsonToCommentaires(inputStringJSON);
    }

    public ArrayList<Commentaire> jsonToCommentaires(String stringJson) throws Exception{
        ArrayList<Commentaire> commentaires = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(stringJson);
        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Commentaire commentaire = new Commentaire();
            commentaire.setId(jsonObject.getInt("id"));
            commentaire.setTexte(jsonObject.getString("texte"));
            commentaire.setUtilisateur(jsonObject.getInt("utilisateur"));
            commentaire.setBox(jsonObject.getInt("box"));
            String dateString = jsonObject.getString("dateCreation").substring(0,10);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(dateString);
            commentaire.setDateCreation(date);
            commentaires.add(commentaire);
        }

        return commentaires;
    }

    public ArrayList<Commentaire> getAllCommentaire(int boxId) throws Exception {
        ArrayList<Commentaire> commentaires = new ArrayList<>();
        Gson gson = new Gson();

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
