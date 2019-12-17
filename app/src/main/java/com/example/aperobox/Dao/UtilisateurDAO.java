package com.example.aperobox.Dao;

import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.JwtToken;
import com.example.aperobox.Model.LoginModel;
import com.example.aperobox.Model.Utilisateur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilisateurDAO {

    public JwtToken connection(LoginModel loginModel)throws Exception{

        Gson gson = new Gson();
        String outputJsonString = gson.toJson(loginModel);
        URL url = new URL("https://aperoboxapi.azurewebsites.net/api/Jwt");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        byte[] outputBytes = outputJsonString.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(outputBytes);
        outputStream.flush();
        outputStream.close();

        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String inputJsonString = "", line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            connection.disconnect();
            inputJsonString = stringBuilder.toString();
            return gson.fromJson(inputJsonString, JwtToken.class);
        }else {
            throw new HttpResultException(connection.getResponseCode());
        }
    }

    public int inscription(Utilisateur newUser)throws Exception {
        int resultCode;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String outputJsonString = gson.toJson(newUser);
        URL url = new URL("https://aperoboxapi.azurewebsites.net/api/Utilisateur/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(false);
        connection.setDoOutput(true);
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
