package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Application.JokeEntry;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements NavigationHost{
    private SharedPreferences preferences;

    private Toolbar toolbar;
    private View acceuil;
    private View boxPersonnalise;
    private View option;
    private View panier;
    private View apropos;
    private View nous_contacter;
    private MaterialButton compte;
    private NavigationIconClickListener navigationIconClickListener;
    private Boolean menuclick;

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
        acceuil.setOnClickListener(null);
        acceuil.setElevation(1);
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
        if(menuclick)
            navigationIconClickListener.onClick(toolbar.getChildAt(1));
        setUpToolbar(findViewById(android.R.id.content).getRootView());
        changeToolbar(fragment);

        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void setUpToolbar(View view) {
        menuclick = false;
        toolbar = view.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) this;
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }

        acceuil = view.findViewById(R.id.menu_acceuil);
        acceuil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuclick = true;
                navigateTo(new BoxsGridFragment(),true);
            }
        });


        boxPersonnalise = view.findViewById(R.id.menu_box_personnalise);
        boxPersonnalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuclick = true;
                navigateTo(new BoxPersonnaliseFragment(),true);
            }
        });

        option = view.findViewById(R.id.menu_option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuclick = true;
                navigateTo(new OptionFragment(),true);
            }
        });

        panier = view.findViewById(R.id.menu_panier);
        panier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuclick = true;
                navigateTo(new PanierFragment(),true);
            }
        });

        apropos = view.findViewById(R.id.menu_a_propos);
        apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuclick = true;
                navigateTo(new AProposFragment(),true);
            }
        });

        nous_contacter = view.findViewById(R.id.menu_nous_contactez);
        nous_contacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuclick = true;
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
                    Toast.makeText(MainActivity.this, "Déconnecté", Toast.LENGTH_LONG).show();
                    MainActivity.this.navigateTo(new BoxsGridFragment(), true);
                }
            });
            panier.setVisibility(View.VISIBLE);
            panier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuclick = true;
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
                    menuclick = true;
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

    private void changeToolbar(Fragment fragment){
        switch (fragment.getClass().getName()){
            case "com.example.aperobox.Activity.BoxsGridFragment":
                acceuil.setOnClickListener(null);
                acceuil.setElevation(1);
                break;
            case "com.example.aperobox.Activity.BoxPersonnaliseFragment":
                boxPersonnalise.setOnClickListener(null);
                boxPersonnalise.setElevation(1);
                break;
            case "com.example.aperobox.Activity.AProposFragment":
                apropos.setOnClickListener(null);
                apropos.setElevation(1);
                break;
            case "com.example.aperobox.Activity.InscriptionFragment":
                compte.setOnClickListener(null);
                compte.setElevation(1);
                break;
            case "com.example.aperobox.Activity.LoginFragment":
                compte.setOnClickListener(null);
                compte.setElevation(1);
                break;
            case "com.example.aperobox.Activity.OptionFragment":
                option.setOnClickListener(null);
                option.setElevation(1);
                break;
            case "com.example.aperobox.Activity.PanierFragment":
                panier.setOnClickListener(null);
                panier.setElevation(1);
                break;
        }
    }
}
