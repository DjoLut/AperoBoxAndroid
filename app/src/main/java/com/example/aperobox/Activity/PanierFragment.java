package com.example.aperobox.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aperobox.Dao.CommandeDAO;
import com.example.aperobox.Dao.LigneCommandeDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Commande;
import com.example.aperobox.Model.LigneCommande;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Adapter.PanierLayout.PanierBoxViewAdapter;
import com.example.aperobox.Adapter.PanierLayout.PanierProduitViewAdapter;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Application.SingletonPanier;
import com.google.android.material.button.MaterialButton;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class PanierFragment extends Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;
    private SharedPreferences preferences;
    private RecyclerView boxToDisplay;
    private RecyclerView produitToDisplay;
    private Panier panier = SingletonPanier.getUniquePanier();
    private TextView panierBoxTextView;
    private TextView panierBoxPersoTextView;
    private Button panierButtonAcheter;
    private TextView prixBox;
    private TextView prixProduit;
    private TextView prixTotal;
    private TextView promotionTotal;

    public PanierFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (AperoBoxApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        View view = inflater.inflate(R.layout.panier_fragment, this.container, false);

        MaterialButton menu = ((MainActivity)getActivity()).panier;
        menu.setOnClickListener(null);
        menu.setElevation(1);

        panierBoxTextView = view.findViewById(R.id.panier_fragment_box_text_view);
        panierBoxPersoTextView = view.findViewById(R.id.panier_fragment_boxPerso_text_view);
        panierButtonAcheter = view.findViewById(R.id.panier_fragment_button_acheter);
        prixTotal = view.findViewById(R.id.panier_fragment_total_prix);
        promotionTotal = view.findViewById(R.id.panier_fragment_total_promotion);
        prixBox = view.findViewById(R.id.panier_fragment_prix_box);
        prixProduit = view.findViewById(R.id.panier_fragment_prix_produit);

        //BOXES
        boxToDisplay = view.findViewById(R.id.panier_fragment_box_recycler_view);
        boxToDisplay.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagerBox = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        boxToDisplay.setLayoutManager(gridLayoutManagerBox);

        PanierBoxViewAdapter adapterBox = new PanierBoxViewAdapter(panier, PanierFragment.this, prixTotal, promotionTotal, prixBox);
        if(adapterBox.getItemCount() == 0)
            panierBoxTextView.setText(R.string.panier_fragment_box_vide);
        boxToDisplay.setAdapter(adapterBox);

        //PRODUITS
        produitToDisplay = view.findViewById(R.id.panier_fragment_produit_recycler_view);
        produitToDisplay.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagerProduit = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        produitToDisplay.setLayoutManager(gridLayoutManagerProduit);

        PanierProduitViewAdapter adapterProduit = new PanierProduitViewAdapter(panier, PanierFragment.this, prixTotal, promotionTotal, prixProduit);
        if(adapterProduit.getItemCount() == 0)
            panierBoxPersoTextView.setText(R.string.panier_fragment_produit_vide);
        produitToDisplay.setAdapter(adapterProduit);

        panierButtonAcheter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(panier.sizeBox() != 0 || panier.sizeProduit() != 0)
                {
                    Commande commande = new Commande();
                    commande.setDateCreation(new Date());
                    commande.setPromotion(panier.calculTotalPromoBox());

                    if(UtilDAO.isInternetAvailable(getContext()))
                    {
                        new AjoutCommande().execute(commande);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), R.string.panier_fragment_panier_vide, Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        MaterialButton menu = ((MainActivity)getActivity()).panier;
        menu.setOnClickListener(null);
        menu.setElevation(1);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() { super.onDestroy(); }


    public static void affichePrixTotalPromotion(TextView prixTotal, TextView promotion, @Nullable TextView prixBox, @Nullable TextView prixProduit){
        Panier panier = SingletonPanier.getUniquePanier();
        prixTotal.setText(UtilDAO.format.format(Math.round(panier.calculTotalPrixBoxEtProduit()*100.0)/100.0));
        promotion.setText(UtilDAO.format.format(Math.round(panier.calculTotalPromoBox()*100.0)/100.0));
        if(prixBox != null)
            prixBox.setText(UtilDAO.format.format(Math.round(panier.calculTotalPrixBox()*100.0)/100.0));
        if(prixProduit != null)
            prixProduit.setText(UtilDAO.format.format(Math.round(panier.calculTotalPrixProduit()*100.0)/100.0));
    }

    private class AjoutCommande extends AsyncTask<Commande, Void, Commande>
    {
        HttpResultException exception;
        @Override
        protected Commande doInBackground(Commande... newCommande) {
            CommandeDAO commandeDAO = new CommandeDAO();
            Commande commande = new Commande();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String access_token = preferences.getString("access_token", null);
            try{
                commande = commandeDAO.ajoutCommande(access_token, newCommande[0]);
            }
            catch (HttpResultException e)
            {
                exception = e;
                cancel(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return commande;
        }

        @Override
        protected void onPostExecute(Commande commande) {
            if(commande.getId() != null){

                if(panier.sizeBox() != 0)
                {
                    for(Iterator<Map.Entry<Box,Integer>> it = panier.getBox().entrySet().iterator(); it.hasNext();) {
                        Map.Entry<Box, Integer> entry = it.next();
                        LigneCommande ligneCommande = new LigneCommande();
                        ligneCommande.setQuantite(entry.getValue());
                        ligneCommande.setBox(entry.getKey().getId());
                        ligneCommande.setProduit(null);
                        ligneCommande.setCommande(commande.getId());
                        new AjoutLigneCommande().execute(ligneCommande);
                    }
                    panier.deleteAllBox();
                }

                if(panier.sizeProduit() != 0)
                {
                    for(Iterator<Map.Entry<Produit,Integer>> it = panier.getProduit().entrySet().iterator(); it.hasNext();) {
                        Map.Entry<Produit, Integer> entry = it.next();
                        LigneCommande ligneCommande = new LigneCommande();
                        ligneCommande.setQuantite(entry.getValue());
                        ligneCommande.setBox(null);
                        ligneCommande.setProduit(entry.getKey().getId());
                        ligneCommande.setCommande(commande.getId());
                        new AjoutLigneCommande().execute(ligneCommande);
                    }
                    panier.deleteAllProduit();
                }

                Toast.makeText(getContext(), R.string.panier_fragment_commande_save, Toast.LENGTH_LONG).show();
                ((NavigationHost) getActivity()).navigateTo(new PanierFragment(), true);
            }else{
                Toast.makeText(getContext(), R.string.retry, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class AjoutLigneCommande extends AsyncTask<LigneCommande, Void, Integer>
    {
        @Override
        protected Integer doInBackground(LigneCommande ...params) {
            Integer resultCode = null;
            LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAO();
            String access_token = preferences.getString("access_token", null);
            try {
                resultCode = ligneCommandeDAO.ajoutLigneCommande(access_token, params[0]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return resultCode;
        }

        @Override
        protected void onPostExecute(Integer resultCode)
        {
            if(resultCode != HttpURLConnection.HTTP_CREATED)
            {
                Toast.makeText(getContext(), R.string.panier_fragment_erreur_save, Toast.LENGTH_SHORT).show();
            }
        }
    }


}