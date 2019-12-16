package com.example.aperobox.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.network.JokeEntry;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.LigneProduit;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.example.aperobox.Utility.Constantes;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class BoxFragment extends Fragment {


    //View
    private TextView box_name;
    private TextView box_price;
    private TextView box_description;
    private ImageView box_image;
    private final Context context;

    private Utilisateur utilisateur;
    private LoadBox loadBoxTask;

    private Integer boxId;
    private List<Box> listBoxs;
    private List<Produit> produits;
    private Box selectedBox;
    private BoxDAO boxDAO;
    public BoxFragment(int boxId, Context context){
        this.boxDAO = new BoxDAO();
        this.boxId = boxId;
        this.context = context;
    }

    public BoxFragment(Context context){
        this.boxDAO = new BoxDAO();
        this.produits = new ArrayList<>();
        this.context = context;
    }

    public BoxFragment(Utilisateur utilisateur, Context context){
        this.context = context;
        this.utilisateur = utilisateur;
    }

    public BoxFragment(int boxId, Utilisateur utilisateur, Context context){
        this.boxId = boxId;
        this.utilisateur = utilisateur;
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
/*
    private AsyncTask<Integer,null,Box> getBox(Integer id){

    }


 */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.box_fragment, container, false);

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
            boxPersonnalise.setElevation((float)1);

            this.box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
            Glide.with(this).load(Constantes.URL_IMAGE_API+Constantes.DEFAULT_END_URL_IMAGE_API).into(this.box_image);
        } else {
            loadBoxTask = new LoadBox();
            loadBoxTask.execute();
        }


        return view;
    }

    private double calculTotal(){
        double somme = 0;
        List<Produit> prod= this.produits;
        if(boxId!=null){
            prod = new ArrayList<>();
            for(LigneProduit ligneProduit : selectedBox.getLigneProduits())
            prod.add(ligneProduit.getProduit());
        }

        for(Produit produit : produits)
            somme+= (produit.getPrix()+ produit.getPrix()*produit.getTva());

        return somme;
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
        boxPersonnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new BoxFragment(getContext()), true);
            }
        });

        View panier = view.findViewById(R.id.menu_panier);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new PanierFragment(), true);
            }
        });

        View options = view.findViewById(R.id.menu_options);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new OptionFragment(), true);
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

        view.findViewById(R.id.menu_compte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utilisateur == null)
                    ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), true);
                else
                    ((NavigationHost) getActivity()).navigateTo(new CompteFragment(), true);
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
        @Override
        protected Box doInBackground(String... params) {
            BoxDAO boxDAO = new BoxDAO();
            Box box = new Box();
            try {
                //String idBox = getIntent().getStringExtra("boxId");
                box = boxDAO.getBox(Integer.valueOf(boxId));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erreur de chargement de la box", Toast.LENGTH_SHORT).show();
            }
            return box;
        }

        @Override
        protected void onPostExecute(Box box)
        {
            String url = box.getPhoto();
            Glide
                    .with(BoxFragment.this)
                    .load(Constantes.URL_IMAGE_API+url)
                    .override(300, 200)
                    .error(R.drawable.ic_launcher_background)
                    .into(box_image);
            box_name.setText(box.getNom());
            box_price.setText(UtilDAO.format.format(box.getPrixUnitaireHtva()));
            box_description.setText(box.getDescription());
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(boxId!=null) {
            loadBoxTask = new LoadBox();
            loadBoxTask.execute();
        }
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
