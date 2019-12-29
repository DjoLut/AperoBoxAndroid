package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationHost{
    private SharedPreferences preferences;

    private Toolbar toolbar;
    protected MaterialButton acceuil;
    protected MaterialButton boxPersonnalise;
    protected MaterialButton option;
    protected MaterialButton panier;
    protected MaterialButton apropos;
    protected MaterialButton nous_contacter;
    protected MaterialButton compte;
    private NavigationIconClickListener navigationIconClickListener;
    private Boolean internetAvaillable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JokeEntry.initJokeEntryList(getResources());

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new BoxsGridFragment())
                    .commit();
        }
        if(!UtilDAO.isInternetAvailable(getBaseContext()))
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_LONG).show();

        setUpToolbar(findViewById(android.R.id.content).getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!UtilDAO.isInternetAvailable(getBaseContext()))
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!UtilDAO.isInternetAvailable(getBaseContext()))
            Toast.makeText(this,R.string.error_no_internet,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(AperoBoxApplication.token == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(AperoBoxApplication.token == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
        }*/
    }

    /**
     * Navigate to the given fragment.
     *
     * @param fragment       Fragment to navigate to.
     * @param addToBackstack Whether or not the current fragment should be added to the backstack.
     */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        if(navigationIconClickListener.getBackDropShown())
            navigationIconClickListener.onClick(toolbar.getChildAt(1));
        setUpToolbar(findViewById(android.R.id.content).getRootView());

        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.getClass().getName());

        if (addToBackstack) {
            transaction.addToBackStack(fragment.getClass().getName());
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(navigationIconClickListener.getBackDropShown())
            navigationIconClickListener.onClick(toolbar.getChildAt(1));
    }

    private void setUpToolbar(View view) {
        internetAvaillable = UtilDAO.isInternetAvailable(view.getContext());
        toolbar = view.findViewById(R.id.app_bar);
        final AppCompatActivity activity = (AppCompatActivity) this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        acceuil = view.findViewById(R.id.menu_acceuil);
        acceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acceuil.getElevation() == 0)
                    navigateTo(new BoxsGridFragment(), true);
            }
        });


        boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
        boxPersonnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxPersonnalise.getElevation() == 0)
                    navigateTo(new BoxPersonnaliseFragment(),true);
            }
        });

        option = view.findViewById(R.id.menu_option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(option.getElevation() == 0)
                    navigateTo(new OptionFragment(),true);
            }
        });

        panier = view.findViewById(R.id.menu_panier);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(panier.getElevation() == 0)
                    navigateTo(new PanierFragment(),true);
            }
        });

        apropos = view.findViewById(R.id.menu_a_propos);
        apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(apropos.getElevation() == 0)
                    navigateTo(new AProposFragment(),true);
            }
        });

        nous_contacter = view.findViewById(R.id.menu_nous_contactez);
        nous_contacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.contact_mail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.contact_mail_sujet);
                startActivity(Intent.createChooser(intent, getString(R.string.contact_mail_chooser)));
            }
        });

        // Compte onclick listener
        compte = view.findViewById(R.id.menu_compte);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                    AperoBoxApplication.getInstance().deconnexion();
                    Toast.makeText(MainActivity.this, R.string.deconnecte, Toast.LENGTH_LONG).show();
                    MainActivity.this.navigateTo(new BoxsGridFragment(), true);
                }
            });
            panier.setVisibility(View.VISIBLE);
            panier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(panier.getElevation() == 0)
                        MainActivity.this.navigateTo(new PanierFragment(), true);
                }
            });
        }
        else {
            compte.setText(R.string.connexion_title);
            panier.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.menu_compte).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(compte.getElevation() == 0)
                        MainActivity.this.navigateTo(new LoginFragment(), true);
                }
            });
        }

        acceuil.setElevation(0);
        boxPersonnalise.setElevation(0);
        apropos.setElevation(0);
        compte.setElevation(0);
        option.setElevation(0);
        panier.setElevation(0);

        navigationIconClickListener = new NavigationIconClickListener(
                this,
                view.findViewById(R.id.container),
                new AccelerateDecelerateInterpolator(),
                this.getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                this.getResources().getDrawable(R.drawable.close_menu));

        toolbar.setNavigationOnClickListener(navigationIconClickListener); // Menu close icon
    }
}