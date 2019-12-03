package com.example.aperobox.Dao;

import com.example.aperobox.JsonTranslator.BoxJsonTranslator;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BoxDAO {

    public ArrayList<Box> getAllBox() throws Exception {
        URL url = new URL(Constantes.URL_API + "Box");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "", line;
        while ((line = buffer.readLine()) != null)
        {
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();
        return BoxJsonTranslator.jsonToBoxes(stringJSON);
    }

    public Box getBox(int id) throws Exception {
        URL url = new URL(Constantes.URL_API+"Box/"+id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        if((line = buffer.readLine())!=null){
            return BoxJsonTranslator.jsonToBox(line);
        }
        return null;
    }

}
