package com.example.aperobox.Dao;

import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Utility.Constantes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ProduitDAO {
    public ArrayList<Produit> getAllProduit() throws Exception {
        ArrayList<Produit> produits = new ArrayList<>();
        Gson gson = new Gson();

        URL url = new URL(Constantes.URL_API + "Box");
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
            produits.add(gson.fromJson(jsonArray.getJSONObject(i).toString(), Produit.class));
        }

        return produits;
    }

    public Produit getProduit(int id) throws Exception {
        URL url = new URL("https://aperoboxapi.azurewebsites.net/api/Produit/"+id);
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

        stringJSON = builder.toString();

        Produit produit;
        Gson object = new GsonBuilder().create();
        produit = object.fromJson(stringJSON, Produit.class);

        return produit;

    }

    public LinkedHashMap<Produit,Integer> getAllProduitBoxPersonnalise() throws Exception{
        LinkedHashMap<Produit, Integer> produits = new LinkedHashMap<>();
        Gson gson = new Gson();
        URL url = new URL("https://aperoboxapi.azurewebsites.net/api/Produit/");

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

        stringJSON = builder.toString();
        JSONArray jsonArray = new JSONArray(stringJSON);
        for(int i = 0; i < jsonArray.length(); i++)
        {
            produits.put(gson.fromJson(jsonArray.getJSONObject(i).toString(), Produit.class), 0);
        }

        return produits;
    }

    public LinkedHashMap<Produit, Integer> getProduitByBoxId(int boxId) throws Exception{
        LigneProduitDAO ligneProduitDAO = new LigneProduitDAO();
        List<LigneProduit> ligneProduits = ligneProduitDAO.getLigneProduitByBoxId(boxId);

        LinkedHashMap<Produit, Integer> produits = new LinkedHashMap<>();
        for(LigneProduit l: ligneProduits)
            produits.put(getProduit(l.getProduit()), l.getQuantite());
        /*

        LinkedHashMap<Produit, Integer> produits = new LinkedHashMap<>();
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
            LigneProduit ligneProduit = gson.fromJson(jsonArray.getJSONObject(i).toString(), LigneProduit.class);
            produits.put(getProduit(ligneProduit.getProduit()),ligneProduit.getQuantite());
        }

        return produits;

         */
        return produits;
    }
}
