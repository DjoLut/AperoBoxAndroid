package com.example.aperobox.Activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Dao.ProduitDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Adapter.ProductLayout.ProductPersonnaliseViewAdapter;
import com.example.aperobox.R;
import com.example.aperobox.Utility.Constantes;
import com.example.aperobox.Application.SingletonPanier;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BoxPersonnaliseFragment extends Fragment {
    private static final String SAVED_BUNDLE_TAG = "boxfragment";
    private Bundle bundle;

    private SharedPreferences preferences;
    //View
    private TextView box_name;
    private TextView box_price;
    private TextView box_description;
    private ImageView box_image;
    private TextView box_quantite;
    private Button button_ajout_panier;

    private LoadProduit loadProduit;
    private Double sommeHTVA;

    public static Map<Produit, Integer> listeProduits;
    private RecyclerView produitToDisplay;

    private View view;
    private Bundle savedInstanceState;

    private Panier panier;

    public BoxPersonnaliseFragment(){
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(bundle==null && listeProduits!=null && !listeProduits.isEmpty()) {
            bundle = new Bundle();
        }
        outState.putBundle(SAVED_BUNDLE_TAG, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!UtilDAO.isInternetAvailable(getContext()))
            setJoke();

        MaterialButton accueil = ((MainActivity)getActivity()).acceuil;
        accueil.setElevation(0);
        MaterialButton apropos = ((MainActivity)getActivity()).apropos;
        apropos.setElevation(0);
        MaterialButton compte = ((MainActivity)getActivity()).compte;
        compte.setElevation(0);
        MaterialButton option = ((MainActivity)getActivity()).option;
        option.setElevation(0);
        MaterialButton panierM = ((MainActivity)getActivity()).panier;
        panierM.setElevation(0);
        MaterialButton boxPerso = ((MainActivity)getActivity()).boxPersonnalise;
        boxPerso.setElevation(1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadProduit!=null)
            loadProduit.cancel(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AperoBoxApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setRetainInstance(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(UtilDAO.isInternetAvailable(getContext())) {
            view = inflater.inflate(R.layout.box_personnalise_fragment, container, false);
            produitToDisplay = view.findViewById(R.id.box_fragment_produit_recycler_view);

            this.savedInstanceState = savedInstanceState;

            this.box_image = view.findViewById(R.id.box_fragment_box_image);
            this.box_name = view.findViewById(R.id.box_fragment_box_name);
            this.box_price = view.findViewById(R.id.box_fragment_box_price);
            this.box_description = view.findViewById(R.id.box_fragment_box_description);
            this.button_ajout_panier = view.findViewById(R.id.box_fragment_box_button_ajout_panier);
            this.box_quantite = view.findViewById(R.id.box_fragment_box_quantite);

            if(listeProduits==null){
                loadProduit = new LoadProduit();
                loadProduit.execute();
            }

            setView();
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            view = inflater.inflate(R.layout.joke, container,false);
            setJoke();
        }

        return view;
    }

    private void setView(){
        button_ajout_panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String access_token = preferences.getString("access_token", null);
                if(access_token != null)
                {
                    Boolean isEmpty = true;
                    Map<Produit, Integer> produitsBoxPerso = new HashMap<>();

                    for (Iterator<Map.Entry<Produit, Integer>> it = listeProduits.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Produit, Integer> entry = it.next();

                        if (entry.getValue() != 0) {
                            produitsBoxPerso.put(entry.getKey(), entry.getValue());
                            isEmpty = false;
                        }
                    }

                    if (!isEmpty) {
                        //AJOUT DES PRODUITS DANS LE PANIER
                        panier = SingletonPanier.getUniquePanier();
                        panier.addProduit(produitsBoxPerso);
                        Toast.makeText(getContext(), R.string.box_fragment_produits_ajouter, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), R.string.box_fragment_box_personnalise_empty_quantite, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),R.string.box_fragment_connection_obligatoire, Toast.LENGTH_LONG).show();
                }

            }
        });


        MaterialButton menu = ((MainActivity)getActivity()).boxPersonnalise;
        menu.setOnClickListener(null);
        menu.setElevation(1);

        setViewBoxPersonnaliseBox();
        if(listeProduits!=null)
            setViewBoxPersonnaliseProduit();

        int largePadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_small);
        produitToDisplay.addItemDecoration(new BoxsGridItemDecoration(largePadding, smallPadding));

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.box_grid)
                    .setBackgroundResource(R.drawable.product_grid_background_shape);
        }
    }

    private class LoadProduit extends AsyncTask<Void, Void, Map<Produit, Integer>>
    {
        @Override
        protected Map<Produit, Integer> doInBackground(Void... params)
        {
            ProduitDAO produitDAO = new ProduitDAO();
            try {
                listeProduits = produitDAO.getAllProduitBoxPersonnalise();
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.box_fragment_erreur_load_produits) + "\n" + getString(R.string.retry), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return listeProduits;
        }

        @Override
        protected void onPostExecute(Map<Produit, Integer> produit)
        {
            setViewBoxPersonnaliseProduit();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    private void calculTotal(){
        sommeHTVA = 0.0;
        for(Produit produit: listeProduits.keySet())
            sommeHTVA += produit.getPrixUnitaireHtva() * (1+produit.getTva()) * listeProduits.get(produit);
    }

    public void affichePrix(){
        calculTotal();
        String prix;
        if(sommeHTVA!=0) {
            prix = UtilDAO.format.format(Math.round(sommeHTVA*100.0)/100.0);
        } else
            prix = getString(R.string.box_fragment_box_prix_gratuit);
        box_price.setText(prix);
    }

    private void setJoke(){
        JokeEntry jokeEntry = JokeEntry.getRandom();
        TextView textView = view.findViewById(R.id.boxs_joke);
        textView.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
    }

    private void setViewBoxPersonnaliseBox(){
        this.box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
        try{
            Glide.with(this).load(Constantes.URL_IMAGE_API + Constantes.DEFAULT_END_URL_IMAGE_API).into(this.box_image);
        } catch (Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), getString(R.string.chargement_lost_connection), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setViewBoxPersonnaliseProduit(){
        affichePrix();

        // Set up the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        produitToDisplay.setLayoutManager(layoutManager);

        final ProductPersonnaliseViewAdapter adapter = new ProductPersonnaliseViewAdapter(getContext(), box_price);
        produitToDisplay.setAdapter(adapter);

    }

}
