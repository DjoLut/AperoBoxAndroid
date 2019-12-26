package com.example.aperobox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Dao.ProduitDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Productlayout.ProductPersonnaliseViewAdapter;
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

    private BoxPersonnaliseFragment.LoadProduit loadProduit;
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
        if(bundle==null && listeProduits!=null) {
            bundle = new Bundle();
        }
        outState.putBundle(SAVED_BUNDLE_TAG, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!UtilDAO.isInternetAvailable(getContext()))
            setJoke();
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

        if(UtilDAO.isInternetAvailable(getContext())) {
            loadProduit = new LoadProduit();
            loadProduit.execute();
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            setJoke();
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.box_personnalise_fragment, container, false);
        this.view = view;
        produitToDisplay = view.findViewById(R.id.box_fragment_produit_recycler_view);

        savedInstanceState = savedInstanceState;

        // Set up the tool bar
        setUpToolbar(view);

        this.box_image = view.findViewById(R.id.box_fragment_box_image);
        this.box_name = view.findViewById(R.id.box_fragment_box_name);
        this.box_price = view.findViewById(R.id.box_fragment_box_price);
        this.box_description = view.findViewById(R.id.box_fragment_box_description);
        this.button_ajout_panier = view.findViewById(R.id.box_fragment_box_button_ajout_panier);
        this.box_quantite = view.findViewById(R.id.box_fragment_box_quantite);

        setView();

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
            listeProduits = new HashMap<>();
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
        box_description.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
        box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
        box_name.setText(getString(R.string.box_fragment_box_error_chargement_api_name));
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.box_app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        View acceuil = view.findViewById(R.id.menu_acceuil);
        acceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new BoxsGridFragment(), true);
            }
        });

        View boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
        boxPersonnalise.setElevation(1);
        boxPersonnalise.setOnClickListener(null);

        View option = view.findViewById(R.id.menu_option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new OptionFragment(),true);
            }
        });

        View panier = view.findViewById(R.id.menu_panier);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new PanierFragment(), true);
            }
        });

        view.findViewById(R.id.menu_a_propos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new AProposFragment(), true);
            }
        });

        view.findViewById(R.id.menu_nous_contactez).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.contact_mail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.contact_mail_sujet);
                startActivity(Intent.createChooser(intent, getString(R.string.contact_mail_chooser)));
            }
        });

        // Compte onclick listener
        MaterialButton compte = view.findViewById(R.id.menu_compte);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = preferences.getString("access_token", null);
        if(access_token!=null) {
            compte.setVisibility(View.VISIBLE);
            compte.setText(R.string.deconnection_title);
            compte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(getContext(), "Déconnecté", Toast.LENGTH_LONG).show();
                    ((NavigationHost) getActivity()).navigateTo(new BoxsGridFragment(), true);
                }
            });
            panier.setVisibility(View.VISIBLE);
            panier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost)getActivity()).navigateTo(new PanierFragment(), true);
                }
            });
        }
        else {
            compte.setText(R.string.connexion_title);
            panier.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.menu_compte).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true);
                }
            });
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.box_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }

    private void setViewBoxPersonnaliseBox(){
        View boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
        boxPersonnalise.setOnClickListener(null);
        boxPersonnalise.setElevation(1);

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        produitToDisplay.setLayoutManager(gridLayoutManager);

        final ProductPersonnaliseViewAdapter adapter = new ProductPersonnaliseViewAdapter(getContext(), box_price);
        produitToDisplay.setAdapter(adapter);
    }

}
