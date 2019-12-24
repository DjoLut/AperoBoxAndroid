package com.example.aperobox.Dao;

import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LigneProduitDAO {

    public List<LigneProduit> getLigneProduitByBoxId(int boxId) throws Exception{
        ArrayList<LigneProduit> ligneProduits = new ArrayList<>();
        Gson gson = new Gson();

        URL url = new URL(Constantes.URL_API + "LigneProduit/"+boxId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);
        //connection.setRequestProperty("Authorization", "Bearer " + token);

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
            ligneProduits.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), LigneProduit.class));
        }

        return ligneProduits;
    }
}
