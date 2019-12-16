package com.example.aperobox.Dao;

import android.content.Context;
import android.net.ConnectivityManager;

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
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = buffer.readLine();
        buffer.close();
        return line;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
