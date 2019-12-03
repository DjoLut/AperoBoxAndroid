package com.example.aperobox.Dao;

import com.example.aperobox.JsonTranslator.LigneProduitJsonTranslator;
import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Utility.Constantes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LigneProduitDAO {

    public ArrayList<LigneProduit> getAllLigneProduit() throws Exception {
        return  LigneProduitJsonTranslator.jsonToLigneProduits(UtilDAO.getMultipleLines(Constantes.URL_API + "LigneProduit"));
    }

    public ArrayList<LigneProduit> getAllLigneProduitFromBox(int boxId) throws Exception {
        return  LigneProduitJsonTranslator.jsonToLigneProduits(UtilDAO.getMultipleLines(Constantes.URL_API + "Box/LigneProduit/" + boxId));
    }

    public LigneProduit getLigneProduit(int id) throws Exception {
        return LigneProduitJsonTranslator.jsonToLigneProduit(UtilDAO.getSimpleLine(Constantes.URL_API+"LigneProduit/"+id));
    }
}
