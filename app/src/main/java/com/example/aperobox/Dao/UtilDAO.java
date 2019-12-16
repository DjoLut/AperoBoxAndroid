package com.example.aperobox.Dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UtilDAO {

    public static String getMultipleLines(String uri) throws Exception{
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);
        //connection.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null)
        {
            builder.append(line);
        }
        buffer.close();
        connection.disconnect();
        return builder.toString();
    }

    public static String getSimpleLine(String uri) throws Exception{
        URL url = new URL(uri);

        //HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);
        //connection.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null)
        {
            builder.append(line);
        }
        buffer.close();
        connection.disconnect();
        return builder.toString();
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        boolean internets = network!=null && (network.getType() == ConnectivityManager.TYPE_WIFI || network.getType() == ConnectivityManager.TYPE_MOBILE);
        return internets;
/*
        boolean internet = (cm!=null && (cm.isActiveNetworkMetered() || cm.isDefaultNetworkActive()));
        return internet;
        /*
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

         */
    }
}
