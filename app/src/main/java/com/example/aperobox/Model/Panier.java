package com.example.aperobox.Model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Panier {
    private Map<Box, Integer> boxes;
    private Map<Produit, Integer> produits;

    public Panier()
    {
        boxes = new HashMap<>();
        produits = new HashMap<>();
    }

    public Map<Box, Integer> getBox()
    {
        return boxes;
    }

    public Map<Produit, Integer> getProduit()
    {
        return produits;
    }

    public void setBox(Map<Box, Integer> box)
    {
        this.boxes = box;
    }

    public void setProduit(Map<Produit, Integer> produit)
    {
        this.produits = produit;
    }

    public void deleteAllBox()
    {
        boxes.clear();
    }

    public void deleteAllProduit()
    {
        produits.clear();
    }

    public void addBox(Box box, int quantite)
    {
        //TEST IF EXIST .... PLUS TARD
        /*for (Iterator<Map.Entry<Box, Integer>> it = boxes.entrySet().iterator(); it.hasNext();)
        {
            ;
        }*/
        boxes.put(box, quantite);
    }

    public void addProduit(Map<Produit, Integer> listeProduit)
    {
        //TEST IF EXIST .....
        produits.putAll(listeProduit);
    }
}
