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
        Boolean boxEstPresent = false;
        int oldQuantite;
        for (Iterator<Map.Entry<Box, Integer>> it = boxes.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<Box, Integer> entry = it.next();
            if(entry.getKey().getId() == box.getId())
            {
                oldQuantite = entry.getValue();
                it.remove();
                if((quantite + oldQuantite) < 25)
                    boxes.put(box, quantite + oldQuantite);
                else
                    boxes.put(box, 25);
                boxEstPresent = true;
                break;
            }
        }

        if(!boxEstPresent)
            boxes.put(box, quantite);
    }

    public void addProduit(Map<Produit, Integer> listeProduit)
    {
        for (Iterator<Map.Entry<Produit, Integer>> itListeProduit = listeProduit.entrySet().iterator(); itListeProduit.hasNext();)
        {
            Map.Entry<Produit, Integer> entryListeProduit = itListeProduit.next();
            int oldQuantite;
            Boolean produitEstPresent = false;
            for (Iterator<Map.Entry<Produit, Integer>> itProduits = produits.entrySet().iterator(); itProduits.hasNext();)
            {
                Map.Entry<Produit, Integer> entryProduits = itProduits.next();
                if(entryProduits.getKey().getId() == entryListeProduit.getKey().getId())
                {
                    oldQuantite = entryProduits.getValue();
                    itProduits.remove();
                    if((entryListeProduit.getValue() + oldQuantite) < 25)
                        produits.put(entryListeProduit.getKey(), entryListeProduit.getValue() + oldQuantite);
                    else
                        produits.put(entryListeProduit.getKey(), 25);
                    produitEstPresent = true;
                    break;
                }
            }

            if(!produitEstPresent)
                produits.put(entryListeProduit.getKey(), entryListeProduit.getValue());

        }
    }

    public int sizeBox()
    {
        return this.boxes.size();
    }

    public int sizeProduit()
    {
        return this.produits.size();
    }

    public void deleteBox(Box box)
    {
        for(Iterator<Map.Entry<Box,Integer>> it = boxes.entrySet().iterator(); it.hasNext();){
            Map.Entry<Box, Integer> entry = it.next();
            if (entry.getKey().getId() == box.getId())
            {
                it.remove();
                break;
            }
        }
    }

    public void deleteProduit(Produit produit)
    {
        for(Iterator<Map.Entry<Produit,Integer>> it = produits.entrySet().iterator(); it.hasNext();){
            Map.Entry<Produit, Integer> entry = it.next();
            if (entry.getKey().getId() == produit.getId())
            {
                it.remove();
                break;
            }
        }
    }

    public double calculTotalPrixBoxEtProduit()
    {
        return calculTotalPrixBox() + calculTotalPrixProduit();
    }

    public double calculTotalPrixBox()
    {
        double totalPrice = 0;
        for(Iterator<Map.Entry<Box,Integer>> it = boxes.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<Box, Integer> entry = it.next();
            totalPrice += (entry.getKey().getPrixUnitaireHtva() * (1+entry.getKey().getTva()) * entry.getValue());
        }
        return totalPrice;
    }

    public double calculTotalPrixProduit()
    {
        double totalPrice = 0;
        for(Iterator<Map.Entry<Produit,Integer>> it = produits.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<Produit, Integer> entry = it.next();
            totalPrice += (entry.getKey().getPrixUnitaireHtva() * (1+entry.getKey().getTva()) * entry.getValue());
        }
        return totalPrice;
    }

    public double calculTotalPromoBox()
    {
        double totalPromo = 0;
        for(Iterator<Map.Entry<Box,Integer>> it = boxes.entrySet().iterator(); it.hasNext();){
            Map.Entry<Box, Integer> entry = it.next();
            if(entry.getKey().getPromotion() != null)
                totalPromo += (entry.getKey().getPromotion()/100*(entry.getKey().getPrixUnitaireHtva() * (1+entry.getKey().getTva()) * entry.getValue()));
        }
        return totalPromo;
    }

    public void modifQuantiteBox(Box box, int quantite)
    {
        for(Iterator<Map.Entry<Box,Integer>> it = boxes.entrySet().iterator(); it.hasNext();){
            Map.Entry<Box, Integer> entry = it.next();
            if (entry.getKey().getId() == box.getId())
            {
                it.remove();
                if(quantite < 25)
                    boxes.put(entry.getKey(), quantite);
                else
                    boxes.put(entry.getKey(), 25);
                break;
            }
        }
    }

    public void modifQuantiteProduit(Produit produit, int quantite)
    {
        for(Iterator<Map.Entry<Produit,Integer>> it = produits.entrySet().iterator(); it.hasNext();){
            Map.Entry<Produit, Integer> entry = it.next();
            if (entry.getKey().getId() == produit.getId())
            {
                it.remove();
                if(quantite < 25)
                    produits.put(entry.getKey(), quantite);
                else
                    produits.put(entry.getKey(), 25);
                break;
            }
        }
    }

}
