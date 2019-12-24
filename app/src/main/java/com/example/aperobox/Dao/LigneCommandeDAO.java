package com.example.aperobox.Dao;

import com.example.aperobox.Model.LigneCommande;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LigneCommandeDAO {

    public int ajoutLigneCommande(String token, LigneCommande ligneCommande) throws Exception {
        int resultCode;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String outputJsonString = gson.toJson(ligneCommande);
        URL url = new URL(Constantes.URL_API + "LigneCommande");
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
