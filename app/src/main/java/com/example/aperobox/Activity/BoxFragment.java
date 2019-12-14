package com.example.aperobox.Activity;

import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aperobox.Dao.BoxDAO;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;

import java.util.ArrayList;
import java.util.List;

public class BoxFragment extends Fragment {

    private Utilisateur utilisateur;

    private int boxId;
    private List<Box> boxs;
    private BoxDAO boxDAO;
    public BoxFragment(int boxId){
        this.boxDAO = new BoxDAO();
        this.boxs = new ArrayList<>();
        this.boxId = boxId;
    }

    public BoxFragment(){
        this.boxDAO = new BoxDAO();
        this.boxs = new ArrayList<>();
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
        try{
            this.boxs.add(boxDAO.getBox(boxId));
            //this.boxs = boxDAO.getListBox(this.boxId);
        } catch (Exception e){
            Toast.makeText(getContext(),R.string.error_login_api,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.box_fragment, container, false);

        // Set up the tool bar
        setUpToolbar(view);

        if(this.boxs!=null){

        }


        return view;
    }

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
