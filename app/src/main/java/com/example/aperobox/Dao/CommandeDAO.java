package com.example.aperobox.Dao;

import com.example.aperobox.Model.Commande;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class CommandeDAO {

    public int ajoutCommande(String token, Commande commande) throws Exception {
        int resultCode;
        commande.setDateCreation(new Date());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String outputJsonString = gson.toJson(commande);
        URL url = new URL(Constantes.URL_API + "Commande");
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
        return resultCode;
    }


}
