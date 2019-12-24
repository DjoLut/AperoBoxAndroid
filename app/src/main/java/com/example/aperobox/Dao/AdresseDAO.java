package com.example.aperobox.Dao;

import com.bumptech.glide.load.HttpException;
import com.example.aperobox.Model.Adresse;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdresseDAO {

    public Adresse ajoutAdresse(Adresse newAdresse) throws Exception{
        URL url =  new URL(Constantes.URL_API + "Adresse");

        Gson gson = new Gson();
        String newAdresseString = gson.toJson(newAdresse);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        byte[] output = newAdresseString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(output);
        outputStream.flush();
        outputStream.close();

        if(connection.getResponseCode() == 201)
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
            return gson.fromJson(inputJsonString, Adresse.class);
        }
        else {
            throw new HttpException(connection.getResponseCode());
        }

    }


}
