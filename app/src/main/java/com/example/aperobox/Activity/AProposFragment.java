package com.example.aperobox.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;

public class AProposFragment extends Fragment {

    private Utilisateur utilisateur;
    private ViewGroup container;

    public AProposFragment(@Nullable Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apropos_fragment, container, false);
        this.container = container;

        setUpToolbar(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.apropos_app_bar);
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


        //Delete listener for menu_a_propos
        View a_propos = view.findViewById(R.id.menu_a_propos);
        a_propos.setOnClickListener(null);
        a_propos.setElevation((float)1);



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
                view.findViewById(R.id.apropos_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
