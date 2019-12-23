package com.example.aperobox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.PanierLayout.PanierBoxViewAdapter;
import com.example.aperobox.PanierLayout.PanierProduitViewAdapter;
import com.example.aperobox.R;
import com.example.aperobox.application.AperoBoxApplication;
import com.example.aperobox.application.SingletonPanier;
import com.google.android.material.button.MaterialButton;

public class PanierFragment extends Fragment {
    private ViewGroup container;
    private LayoutInflater inflater;
    private SharedPreferences preferences;
    private RecyclerView boxToDisplay;
    private RecyclerView produitToDisplay;
    private Panier panier = SingletonPanier.getUniquePanier();
    private TextView panierBoxTextView;
    private TextView panierBoxPersoTextView;

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

        panierBoxTextView = view.findViewById(R.id.panier_fragment_box_text_view);
        panierBoxPersoTextView = view.findViewById(R.id.panier_fragment_boxPerso_text_view);

        //BOXES
        boxToDisplay = view.findViewById(R.id.panier_fragment_box_recycler_view);
        boxToDisplay.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagerBox = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        boxToDisplay.setLayoutManager(gridLayoutManagerBox);

        PanierBoxViewAdapter adapterBox = new PanierBoxViewAdapter(panier, PanierFragment.this);
        if(adapterBox.getItemCount() == 0)
            panierBoxTextView.setText(R.string.panier_fragment_box_vide);
        boxToDisplay.setAdapter(adapterBox);

        //PRODUITS
        produitToDisplay = view.findViewById(R.id.panier_fragment_produit_recycler_view);
        produitToDisplay.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagerProduit = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        produitToDisplay.setLayoutManager(gridLayoutManagerProduit);

        PanierProduitViewAdapter adapterProduit = new PanierProduitViewAdapter(panier, PanierFragment.this);
        if(adapterProduit.getItemCount() == 0)
            panierBoxPersoTextView.setText(R.string.panier_fragment_produit_vide);
        produitToDisplay.setAdapter(adapterProduit);


        setUpToolbar(view);

        return view;
    }

    @Override
    public void onResume() { super.onResume(); }

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

/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if(menu.size()==0) {
            menuInflater.inflate(R.menu.toolbar_menu, menu);
            MenuItem icon = menu.getItem(0);
            icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    AperoBoxApplication.getInstance().setIsNightModeEnabled(!AperoBoxApplication.getInstance().isNightModeEnabled());
                    getFragmentManager().beginTransaction().detach(PanierFragment.this).attach(PanierFragment.this).commit();
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }


 */
    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.panier_app_bar);
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

        View option = view.findViewById(R.id.menu_option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new OptionFragment(),true);
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

        View panier = view.findViewById(R.id.menu_panier);
        MaterialButton compte = view.findViewById(R.id.menu_compte);
        compte.setElevation((float) 1);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = preferences.getString("access_token", null);
        if (access_token != null) {
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
                    ((NavigationHost) getActivity()).navigateTo(new PanierFragment(), true);
                }
            });
        } else {
            compte.setText(R.string.connexion_title);
            panier.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.menu_compte).setOnClickListener(null);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.panier_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }
}
