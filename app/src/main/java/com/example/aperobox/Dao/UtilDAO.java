package com.example.aperobox.Dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilDAO {

    public static String getMultipleLines(String uri) throws Exception{
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null)
        {
            builder.append(line);
        }
        buffer.close();
        return builder.toString();
    }

    public static String getSimpleLine(String uri) throws Exception{
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = buffer.readLine();
        buffer.close();
        return line;
    }
}
