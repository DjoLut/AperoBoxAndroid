package com.example.aperobox.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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

    private Utilisateur utilisateur;

    private Integer boxId;
    private List<Box> listBoxs;
    private List<Produit> produits;
    private Box selectedBox;
    private BoxDAO boxDAO;
    public BoxFragment(int boxId){
        this.boxDAO = new BoxDAO();
        this.listBoxs = new ArrayList<>();
        this.boxId = boxId;
    }

    public BoxFragment(){
        this.boxDAO = new BoxDAO();
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

        // Set up the tool bar
        setUpToolbar(view);

        this.box_image = view.findViewById(R.id.box_fragment_box_image);
        this.box_name = view.findViewById(R.id.box_fragment_box_name);
        this.box_price = view.findViewById(R.id.box_fragment_box_price);
        this.box_description = view.findViewById(R.id.box_fragment_box_description);

        Locale locale = Locale.getDefault();
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        //Box présélectionné;
        if(boxId!=null) {
            try {
                this.selectedBox = boxDAO.getBox(boxId);
                Glide.with(this).load(Constantes.URL_IMAGE_API+this.selectedBox.getImage()).into(this.box_image);
                this.box_name.setText(this.selectedBox.getNom());
                this.box_price.setText(format.format(this.selectedBox.getPrix()));
                this.box_description.setText(format.format(this.selectedBox.getDescription()));
            } catch (Exception e) {
                Toast.makeText(getContext(), R.string.error_login_api, Toast.LENGTH_LONG).show();
                Glide.with(this).load(Constantes.URL_IMAGE_API+Constantes.DEFAULT_END_URL_IMAGE_API).into(this.box_image);
                this.box_name.setText(getString(R.string.box_fragment_box_error_chargement_api_name));
                this.box_price.setText(getString(R.string.box_fragment_box_prix_gratuit));
                List<JokeEntry> jokeEntry = JokeEntry.initJokeEntryList(getResources());
                JokeEntry joke = jokeEntry.get(new Random().nextInt(jokeEntry.size()));
                this.box_description.setText(getString(R.string.box_fragment_box_error_chargement_api_description) + "\n\n\n" + joke.getBase() + "\n\n\n\n" + joke.getReponse());

            }
        }
        //Box personnalisé
        else{


            this.box_price.setText(format.format(32.24));
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

    /*
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_2:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_4:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_6:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_8:
                if (checked)
                    // Ninjas rule
                    break;
            case R.id.radio_10:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }
     */

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
                ((NavigationHost) getActivity()).navigateTo(new BoxFragment(), true);
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

}
