package com.example.aperobox.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Dao.LigneProduitDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.network.JokeEntry;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.example.aperobox.Utility.Constantes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

public class BoxFragment extends Fragment {


    //View
    private TextView box_name;
    private TextView box_price;
    private TextView box_description;
    private ImageView box_image;

    private Utilisateur utilisateur;
    private LoadBox loadBoxTask;

    private Integer boxId;
    private List<Produit> produits;
    private Box selectedBox;

    private View view;
    private ViewGroup container;

    public BoxFragment(int boxId){
        this.boxId = boxId;
    }

    public BoxFragment(Box box){
        this.boxId = box.getId();
        this.selectedBox = box;
    }

    public BoxFragment(){
        this.produits = new ArrayList<>();
    }

    public BoxFragment(Utilisateur utilisateur){
        this.utilisateur = utilisateur;
    }

    public BoxFragment(int boxId, Utilisateur utilisateur){
        this.boxId = boxId;
        this.utilisateur = utilisateur;
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
        this.container = container;
        // Set up the tool bar
        setUpToolbar(view);

        this.box_image = view.findViewById(R.id.box_fragment_box_image);
        this.box_name = view.findViewById(R.id.box_fragment_box_name);
        this.box_price = view.findViewById(R.id.box_fragment_box_price);
        this.box_description = view.findViewById(R.id.box_fragment_box_description);

        //Box personnalisé
        if(boxId==null) {
            View boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
            boxPersonnalise.setOnClickListener(null);
            boxPersonnalise.setElevation(1);

            this.box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
            if(UtilDAO.isInternetAvailable(getContext()))
                Glide.with(this).load(Constantes.URL_IMAGE_API+Constantes.DEFAULT_END_URL_IMAGE_API).into(this.box_image);
        } else {
            if(selectedBox==null) {
                if(UtilDAO.isInternetAvailable(getContext())) {
                    loadBoxTask = new LoadBox();
                    loadBoxTask.execute();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    setJoke(getView());
                }
            }
        }


        return view;
    }

    /*private double calculTotal(){
        double somme = 0;
        List<Produit> prod= this.produits;
        if(boxId!=null){
            prod = new ArrayList<>();
            for(LigneProduit ligneProduit : selectedBox.getLigneProduits())
            prod.add(ligneProduit.getProduit());
        }

        for(Produit produit : produits)
            somme+= (produit.getPrixUnitaireHtva()+ produit.getPrixUnitaireHtva()*produit.getTva());

        return somme;
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
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
                ((NavigationHost) getActivity()).navigateTo(new AProposFragment(utilisateur), true);
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

        if(utilisateur!=null)
            view.findViewById(R.id.menu_compte).setVisibility(View.INVISIBLE);
        else
        view.findViewById(R.id.menu_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true);
            }
        });

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
        getContext(),
                view.findViewById(R.id.box_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }


    private class LoadBox extends AsyncTask<String, Void, Box>
    {
        private Box boxEnCours;
        private List<Produit> produitList;
        @Override
        protected Box doInBackground(String... params) {
            BoxDAO boxDAO = new BoxDAO();
            Box box = new Box();
            try {
                //String idBox = getIntent().getStringExtra("boxId");
                box = boxDAO.getBox(Integer.valueOf(boxId));
                //LoadLigneProduit loadLigneProduit = new LoadLigneProduit();
                //loadLigneProduit.execute(box);
                //box.setProduits(produitList);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erreur de chargement de la box", Toast.LENGTH_LONG).show();
            }
            return box;
        }

        @Override
        protected void onPostExecute(Box box)
        {
            if(UtilDAO.isInternetAvailable(getContext())) {
                String url = box.getPhoto();
                Glide
                        .with(BoxFragment.this)
                        .load(Constantes.URL_IMAGE_API + url)
                        .override(300, 200)
                        .error(R.drawable.ic_launcher_background)
                        .into(box_image);
            }

            view.findViewById(R.id.menu_box_personnalise);
            box_name.setText(box.getNom());
            String prix = UtilDAO.format.format(box.getPrixUnitaireHtva());
            if(box.getPromotion()!=null) {
                prix += " - " + Math.round(box.getPromotion() * 10000) / 100 + "% = ";
                prix += UtilDAO.format.format(Math.round((box.getPrixUnitaireHtva() * box.getTva() * (1 - box.getPromotion()) * 100) / 100));
            }
            box_price.setText(prix);
            box_description.setText(box.getDescription());
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        private class LoadLigneProduit extends AsyncTask<Box, Void, LigneProduit>
        {

            @Override
            protected LigneProduit doInBackground(Box... params)
            {
                LigneProduitDAO ligneProduitDAO = new LigneProduitDAO();
                LigneProduit ligneProduit = null;
                try {
                    ligneProduit = ligneProduitDAO.getLigneProduit(params[0].getId());
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Erreur de chargement de toute les boxs", Toast.LENGTH_SHORT).show();
                }
                return ligneProduit;
            }

            @Override
            protected void onPostExecute(LigneProduit ligneProduit)
            {
                LoadProduit loadProduit = new LoadProduit();
                loadProduit.execute(ligneProduit);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }


            private class LoadProduit extends AsyncTask<LigneProduit, Void, Produit>
            {
                @Override
                protected Produit doInBackground(LigneProduit... params)
                {
                    //ProduitDAO produitDAO = new LigneProduitDAO();
                    Produit produit = null;
                    try {
                        //produit = produitDAO.getProduitById(params[0].getProduit());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Erreur de chargement de toute les boxs", Toast.LENGTH_SHORT).show();
                    }
                    return produit;
                }

                @Override
                protected void onPostExecute(Produit produit)
                {
                    produitList.add(produit);
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                }

            }
        }
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
}
