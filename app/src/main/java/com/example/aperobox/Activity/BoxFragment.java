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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.HttpException;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Dao.LigneProduitDAO;
import com.example.aperobox.Dao.ProduitDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.network.JokeEntry;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.Productlayout.ProductPersonnaliseViewAdapter;
import com.example.aperobox.Productlayout.ProductViewAdapter;
import com.example.aperobox.R;
import com.example.aperobox.Utility.Constantes;
import com.google.android.material.button.MaterialButton;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.zip.Inflater;

public class BoxFragment extends Fragment {

    private SharedPreferences preferences;
    //View
    private TextView box_name;
    private TextView box_price;
    private TextView box_description;
    private ImageView box_image;
    private TextView box_quantite;
    private Button button_ajout_panier;
    private Button button_plus;
    private Button button_moins;

    private LoadBox loadBoxTask;
    private Box selectedBox;
    private Double sommeHTVA;
    private Double promotion;

    private LayoutInflater inflater;
    private ViewGroup container;
    private Integer boxId;
    private Integer quantite;
    public static LinkedHashMap<Produit, Integer> listeProduits;
    private RecyclerView produitToDisplay;

    private View view;

    public BoxFragment(int boxId){
        this.boxId = boxId;
    }

    public BoxFragment(){
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        container.removeView(view);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.view = inflater.inflate(R.layout.box_fragment_landscape, container, false);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            this.view = inflater.inflate(R.layout.box_fragment, container, false);
        }

        /*if(boxId==null)
            setViewPersonnalise();
        else
            setView();*/

        container.addView(view);
    }


    private void setJoke(View view){
        JokeEntry jokeEntry = JokeEntry.getRandom();
        box_description.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
        box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
        box_name.setText(getString(R.string.box_fragment_box_error_chargement_api_name));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!UtilDAO.isInternetAvailable(getContext()))
            setJoke(getView());
        /*if(boxId!=null) {
            if(selectedBox!=null) {
                if(UtilDAO.isInternetAvailable(getContext())) {
                    loadBoxTask = new LoadBox();
                    loadBoxTask.execute();
                } else
                    Toast.makeText(getContext(),getString(R.string.error_no_internet),Toast.LENGTH_SHORT).show();
            }
        }

         */
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
    public void onDestroy() {
        super.onDestroy();
        if(loadBoxTask != null)
            loadBoxTask.cancel(true);
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.box_fragment, container, false);
        this.view = view;
        this.inflater = inflater;
        this.container = container;
        produitToDisplay = view.findViewById(R.id.box_fragment_produit_recycler_view);

        // Set up the tool bar
        setUpToolbar(view);

        this.box_image = view.findViewById(R.id.box_fragment_box_image);
        this.box_name = view.findViewById(R.id.box_fragment_box_name);
        this.box_price = view.findViewById(R.id.box_fragment_box_price);
        this.box_description = view.findViewById(R.id.box_fragment_box_description);
        this.button_ajout_panier = view.findViewById(R.id.box_fragment_box_button_ajout_panier);
        this.button_moins = view.findViewById(R.id.box_fragment_box_quantite_moins);
        this.button_plus = view.findViewById(R.id.box_fragment_box_quantite_plus);
        this.box_quantite = view.findViewById(R.id.box_fragment_box_quantite);
        this.quantite = 1;
        this.box_quantite.setText(quantite.toString());


        this.button_moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantite >1) {
                    quantite--;
                    box_quantite.setText(quantite.toString());
                }
            }
        });

        this.button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantite++;
                box_quantite.setText(quantite.toString());
            }
        });

        //Box personnalisé
        if(boxId==null) {
            View boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
            boxPersonnalise.setOnClickListener(null);
            boxPersonnalise.setElevation(1);

            this.box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
            if(UtilDAO.isInternetAvailable(getContext())) {
                Glide.with(this).load(Constantes.URL_IMAGE_API + Constantes.DEFAULT_END_URL_IMAGE_API).into(this.box_image);
                LoadProduit loadProduit = new LoadProduit();
                loadProduit.execute();

            }
        } else {
            if(UtilDAO.isInternetAvailable(getContext())) {
                loadBoxTask = new LoadBox();
                loadBoxTask.execute();
            } else {
                Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                setJoke(getView());
            }
        }

        int largePadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_small);
        produitToDisplay.addItemDecoration(new BoxsGridItemDecoration(largePadding, smallPadding));

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.box_grid)
                    .setBackgroundResource(R.drawable.product_grid_background_shape);
        }

        return view;
    }

    private void calculTotal(){
        sommeHTVA = 0.0;
        promotion = 0.0;
        if(boxId!=null){
            sommeHTVA = (selectedBox.getPrixUnitaireHtva()*(1+selectedBox.getTva())) * quantite;
            if(selectedBox.getPromotion()!=null)
                promotion = sommeHTVA*(1-selectedBox.getPromotion());
        } else {
            for(Produit produit: listeProduits.keySet()) {
                sommeHTVA += produit.getPrixUnitaireHtva() * (1+produit.getTva()) * listeProduits.get(produit) * quantite;
            }
        }
    }

    private class LoadBox extends AsyncTask<String, Void, Box>
    {
        @Override
        protected Box doInBackground(String... params) {
            BoxDAO boxDAO = new BoxDAO();
            Box box = new Box();
            try {
                box = boxDAO.getBox(Integer.valueOf(boxId));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erreur de chargement de la box", Toast.LENGTH_LONG).show();
            }
            return box;
        }

        @Override
        protected void onPostExecute(Box box)
        {
            selectedBox = box;
            LoadProd loadProd= new LoadProd();
            loadProd.execute();

            if(UtilDAO.isInternetAvailable(getContext())) {
                String url = box.getPhoto();
                Glide
                        .with(BoxFragment.this)
                        .load(Constantes.URL_IMAGE_API + url)
                        .override(300, 200)
                        .error(R.drawable.ic_launcher_background)
                        .into(box_image);
            }

            box_name.setText(box.getNom());
            box_description.setText(box.getDescription());
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        private class LoadProd extends AsyncTask<Void, Void, LinkedHashMap<Produit,Integer>>
        {
            @Override
            protected LinkedHashMap<Produit, Integer> doInBackground(Void... params)
            {
                ProduitDAO produitDAO = new ProduitDAO();
                try {
                    listeProduits = produitDAO.getProduitByBoxId(boxId);
                } catch (HttpResultException h){
                    Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Erreur de chargement de toute les boxs", Toast.LENGTH_SHORT).show();
                }
                return listeProduits;
            }

            @Override
            protected void onPostExecute(LinkedHashMap<Produit, Integer> produit)
            {

                setView();
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

        }
    }

    private void affichePrix(){
        calculTotal();
        String prix;
        if(sommeHTVA!=0) {
            prix = UtilDAO.format.format(Math.round(sommeHTVA*100.0)/100.0);
            if (promotion != 0) {
                prix += " - " +UtilDAO.format.format(Math.round(promotion*100.0)/100.0);
                prix += " = " + UtilDAO.format.format(Math.round((sommeHTVA-promotion)*100.0)/100.0);
            }
        } else
            prix = getString(R.string.box_fragment_box_prix_gratuit);
        box_price.setText(prix);
    }

    private void setView(){
        affichePrix();

        // Set up the RecyclerView
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        produitToDisplay.setLayoutManager(gridLayoutManager);

        ProductViewAdapter adapter = new ProductViewAdapter(listeProduits, BoxFragment.this);
        produitToDisplay.setAdapter(adapter);
    }

    private void setViewPersonnalise(){
        affichePrix();

        // Set up the RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        produitToDisplay.setLayoutManager(gridLayoutManager);

        final ProductPersonnaliseViewAdapter adapter = new ProductPersonnaliseViewAdapter();
        produitToDisplay.setAdapter(adapter);

        button_ajout_panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isempty = true;
                for(Integer value : listeProduits.values()) {
                    if (value != 0)
                        isempty = false;
                }

                if(!isempty) {
                    //Liste bien mise à jour
                } else{
                    Toast.makeText(getContext(),R.string.box_fragment_box_personnalise_empty_quantite, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class LoadProduit extends AsyncTask<Void, Void, LinkedHashMap<Produit, Integer>>
    {
        @Override
        protected LinkedHashMap<Produit, Integer> doInBackground(Void... params)
        {
            ProduitDAO produitDAO = new ProduitDAO();
            listeProduits = new LinkedHashMap<>();
            try {
                listeProduits = produitDAO.getAllProduitBoxPersonnalise();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erreur de chargement de toute les boxs", Toast.LENGTH_SHORT).show();
            }
            return listeProduits;
        }

        @Override
        protected void onPostExecute(LinkedHashMap<Produit, Integer> produit)
        {
            setViewPersonnalise();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if(menu.size()==0) {
            menuInflater.inflate(R.menu.toolbar_menu, menu);
            MenuItem icon = menu.getItem(0);
            icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        return true;
                    } else if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        return true;
                    }
                    return false;
                }
            });
        }
        super.onCreateOptionsMenu(menu, menuInflater);
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
        if(boxId==null)
            boxPersonnalise.setOnClickListener(null);
        else{
            boxPersonnalise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost) getActivity()).navigateTo(new BoxFragment(), true);
                }
            });
        }

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
                    //  Add logout;
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
}
