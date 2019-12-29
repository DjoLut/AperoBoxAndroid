package com.example.aperobox.Dao;

import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Commande;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandeDAO {
    public Commande ajoutCommande(String token, Commande commande) throws Exception {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String outputJsonString = gson.toJson(commande);
        URL url = new URL(Constantes.URL_API + "Commande");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes);
        outputStream.flush();
        outputStream.close();

        if(connection.getResponseCode() == HttpURLConnection.HTTP_CREATED)
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String inputJsonString = "", line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            connection.disconnect();
            inputJsonString = stringBuilder.toString();
            return gson.fromJson(inputJsonString, Commande.class);
        } else {
            throw  new HttpResultException(connection.getResponseCode());
        }

    }



}
