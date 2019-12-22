package com.example.aperobox.application;

import com.example.aperobox.Model.Panier;

public class SingletonPanier {

    private static Panier uniquePanier;

    private SingletonPanier() { };

    public static Panier getUniquePanier() {
        if(uniquePanier == null)
        {
            uniquePanier = new Panier();
        }
        return uniquePanier;
    }
}
