package com.example.aperobox.Dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class UtilDAO {

    private static Locale locale = Locale.getDefault();
    public static NumberFormat format = NumberFormat.getCurrencyInstance(locale);

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        return network!=null && (network.getType() == ConnectivityManager.TYPE_WIFI || network.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static Double calculTotal(Map<Produit, Integer> listeProduits){
        Double sommeHTVA = 0.0;
        for(Produit produit: listeProduits.keySet())
            sommeHTVA += produit.getPrixUnitaireHtva() * (1+produit.getTva()) * listeProduits.get(produit);
        return sommeHTVA;
    }

    public static void affichePrix(Double sommeHTVA, Context context, TextView box_price){
        String prix;
        if(sommeHTVA!=0)
            prix = UtilDAO.format.format(Math.round(sommeHTVA*100.0)/100.0);
        else
            prix = context.getString(R.string.box_fragment_box_prix_gratuit);
        box_price.setText(prix);
    }
}
