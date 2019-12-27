package com.example.aperobox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Dao.ProduitDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Productlayout.ProductViewAdapter;
import com.example.aperobox.R;
import com.example.aperobox.Application.SingletonPanier;
import com.example.aperobox.Utility.Constantes;
import com.google.android.material.button.MaterialButton;
import java.util.Map;

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
    private MaterialButton button_commentaire;
    private ProgressBar progressBar;

    private LoadBox loadBoxTask;
    private LoadProd loadProd;
    private Box selectedBox;
    private Box boxPersonnalise;
    private Double sommeHTVA;
    private Double promotion;


    private Integer boxId;
    private Integer quantite;
    public static Map<Produit, Integer> listeProduits;
    private RecyclerView produitToDisplay;
    private BoxDAO boxDAO;

    private View view;
    private Bundle savedInstanceState;

    private Panier panier;

    public BoxFragment(int boxId){
        this.boxId = boxId;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!UtilDAO.isInternetAvailable(getContext()))
            setJoke();
        else{
            if(selectedBox==null){
                loadBoxTask = new LoadBox();
                loadBoxTask.execute();
            } else {
                setViwBoxBox();
                if(listeProduits==null){
                    loadProd = new LoadProd();
                    loadProd.execute();
                } else{
                    setViewBoxProduit();
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loadBoxTask != null)
            loadBoxTask.cancel(true);
        if(loadProd != null)
            loadProd.cancel(true);
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

        this.quantite = 1;
        this.boxDAO = new BoxDAO();

        if(UtilDAO.isInternetAvailable(getContext())) {
            loadBoxTask = new LoadBox();
            loadBoxTask.execute();
        } else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
            setJoke();
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.box_fragment, container, false);
        this.view = view;

        setView();

        return view;
    }

    private void setView(){
        produitToDisplay = view.findViewById(R.id.box_fragment_produit_recycler_view);

        this.savedInstanceState = savedInstanceState;

        this.box_image = view.findViewById(R.id.box_fragment_box_image);
        this.box_name = view.findViewById(R.id.box_fragment_box_name);
        this.box_price = view.findViewById(R.id.box_fragment_box_price);
        this.box_description = view.findViewById(R.id.box_fragment_box_description);
        this.button_ajout_panier = view.findViewById(R.id.box_fragment_box_button_ajout_panier);
        this.button_moins = view.findViewById(R.id.box_fragment_box_quantite_moins);
        this.button_plus = view.findViewById(R.id.box_fragment_box_quantite_plus);
        this.box_quantite = view.findViewById(R.id.box_fragment_box_quantite);
        this.button_commentaire = view.findViewById(R.id.box_fragment_button_commentaire);
        this.progressBar = view.findViewById(R.id.box_fragment_progress_bar);

        this.button_moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantite = Integer.parseInt(box_quantite.getText().toString());
                if(quantite >1) {
                    quantite--;
                    box_quantite.setText(quantite.toString());
                }
                affichePrix();
            }
        });

        this.button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantite = Integer.parseInt(box_quantite.getText().toString());
                quantite++;
                box_quantite.setText(quantite.toString());
                affichePrix();
            }
        });

        this.button_commentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UtilDAO.isInternetAvailable(getContext())) {
                    ((NavigationHost) getActivity()).navigateTo(new CommentaireBoxFragment(boxId), true);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getString(R.string.connexion_fragment_erreur_connexion) + "\n" + getString(R.string.retry), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        button_ajout_panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String access_token = preferences.getString("access_token", null);
                if(access_token != null)
                {
                    if(Integer.valueOf(box_quantite.getText().toString()) != 0)
                    {

                        //AJOUT DES BOX DANS LE PANIER
                        panier = SingletonPanier.getUniquePanier();
                        panier.addBox(selectedBox, Integer.valueOf(box_quantite.getText().toString()));
                        Toast.makeText(getContext(), R.string.box_fragment_box_ajouter, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(),R.string.box_fragment_box__empty_quantite, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),R.string.box_fragment_connection_obligatoire, Toast.LENGTH_LONG).show();
                }

            }
        });

        this.box_quantite.setText(quantite.toString());


        if(selectedBox!=null){
            setViwBoxBox();
            setViewBoxProduit();
        }


        int largePadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_large);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.staggered_boxs_grid_spacing_small);
        produitToDisplay.addItemDecoration(new BoxsGridItemDecoration(largePadding, smallPadding));

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.box_grid)
                    .setBackgroundResource(R.drawable.product_grid_background_shape);
        }
    }


    private class LoadBox extends AsyncTask<Void, Void, Box>
    {
        @Override
        protected Box doInBackground(Void... params) {
            try {
                selectedBox = boxDAO.getBox(boxId);
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.box_fragment_erreur_load_box) + "\n" + getString(R.string.retry), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return selectedBox;
        }

        @Override
        protected void onPostExecute(final Box box)
        {
            loadProd= new LoadProd();
            loadProd.execute();

            setViwBoxBox();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

        private class LoadProd extends AsyncTask<Void, Void, Map<Produit,Integer>>
        {
            @Override
            protected Map<Produit, Integer> doInBackground(Void... params)
            {
                ProduitDAO produitDAO = new ProduitDAO();
                try {
                    listeProduits = produitDAO.getProduitByBoxId(boxId);
                } catch (final HttpResultException h){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getContext(), h.getMessage(), Toast.LENGTH_SHORT).show();
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
            setViewBoxProduit();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }


    private void calculTotal(){
        sommeHTVA = 0.0;
        promotion = 0.0;
        sommeHTVA = (selectedBox.getPrixUnitaireHtva()*(1+selectedBox.getTva())) * quantite;
        if(selectedBox.getPromotion()!=null)
            promotion = sommeHTVA*(1-selectedBox.getPromotion());
    }

    public void affichePrix(){
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

    private void setJoke(){
        JokeEntry jokeEntry = JokeEntry.getRandom();
        box_description.setText(jokeEntry.getBase()+"\n\n\n" + jokeEntry.getReponse());
        box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
        box_name.setText(getString(R.string.box_fragment_box_error_chargement_api_name));
    }

    private void setViwBoxBox(){
        String url = selectedBox.getPhoto();
        try{
            Glide
                    .with(BoxFragment.this)
                    .load(Constantes.URL_IMAGE_API + url)
                    .override(300, 200)
                    .error(R.drawable.ic_launcher_background)
                    .into(box_image);
        } catch (Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), getString(R.string.chargement_lost_connection), Toast.LENGTH_SHORT).show();
                }
            });
        }

        box_name.setText(selectedBox.getNom());
        affichePrix();
        box_description.setText(selectedBox.getDescription());
    }

    private void setViewBoxProduit(){
        if(listeProduits!=null) {
            affichePrix();

            progressBar.setElevation(0);

            // Set up the RecyclerView
            //RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
            produitToDisplay.setLayoutManager(gridLayoutManager);

            ProductViewAdapter adapter = new ProductViewAdapter(listeProduits, BoxFragment.this);
            produitToDisplay.setAdapter(adapter);
        } else {
            if(selectedBox==null){
                LoadBox loadBox = new LoadBox();
                loadBox.execute();
            } else {
                LoadProd loadProd= new LoadProd();
                loadProd.execute();
            }
        }
    }
}
