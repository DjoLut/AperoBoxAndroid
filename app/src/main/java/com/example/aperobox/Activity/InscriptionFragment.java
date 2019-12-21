package com.example.aperobox.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.aperobox.Dao.AdresseDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.UtilisateurDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Adresse;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;


/**
 * Fragment representing the register screen for AperoBox.
 */
public class InscriptionFragment extends Fragment {

    private Utilisateur newUser;
    private Adresse newAdresse;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inscription_fragment, container, false);

        final TextInputLayout nomTextInput = view.findViewById(R.id.inscription_nom_text_input);
        final TextInputEditText nomEditText = view.findViewById(R.id.inscription_nom_edit_text);

        final TextInputLayout prenomTextInput = view.findViewById(R.id.inscription_prenom_text_input);
        final TextInputEditText prenomEditText = view.findViewById(R.id.inscription_prenom_edit_text);

        final TextInputLayout dateNaissanceTextInput = view.findViewById(R.id.inscription_date_naissance_input);
        final DatePicker dateNaissanceEditText = view.findViewById(R.id.inscription_date_naissance_datePicker1);

        final TextInputLayout mailTextInput = view.findViewById(R.id.inscription_mail_text_input);
        final TextInputEditText mailEditText = view.findViewById(R.id.inscription_mail_edit_text);

        final TextInputLayout telephoneTextInput = view.findViewById(R.id.inscription_telephone_text_input);
        final TextInputEditText telephoneEditText = view.findViewById(R.id.inscription_telephone_edit_text);

        final TextInputLayout gsmTextInput = view.findViewById(R.id.inscription_gsm_text_input);
        final TextInputEditText gsmEditText = view.findViewById(R.id.inscription_gsm_edit_text);

        final TextInputLayout usernameTextInput = view.findViewById(R.id.inscription_username_text_input);
        final TextInputEditText usernameEditText = view.findViewById(R.id.inscription_username_edit_text);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.inscription_password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.inscription_password_edit_text);

        final TextInputLayout confPasswordTextInput = view.findViewById(R.id.inscription_conf_password_text_input);
        final TextInputEditText confPasswordEditText = view.findViewById(R.id.inscription_conf_password_edit_text);

        final TextInputLayout rueTextInput = view.findViewById(R.id.inscription_rue_text_input);
        final TextInputEditText rueEditText = view.findViewById(R.id.inscription_rue_edit_text);

        final TextInputLayout numeroTextInput = view.findViewById(R.id.inscription_numero_text_input);
        final TextInputEditText numeroEditText = view.findViewById(R.id.inscription_numero_edit_text);

        final TextInputLayout localiteTextInput = view.findViewById(R.id.inscription_localite_text_input);
        final TextInputEditText localiteEditText = view.findViewById(R.id.inscription_localite_edit_text);

        final TextInputLayout codePostalTextInput = view.findViewById(R.id.inscription_code_postal_text_input);
        final TextInputEditText codePostalEditText = view.findViewById(R.id.inscription_code_postal_edit_text);

        MaterialButton inscriptionButton = view.findViewById(R.id.inscription_button_inscription);

        // Set up the toolbar
        setUpToolbar(view);

        // Set an error if the password is less than 8 characters.
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;
                if (!isPasswordLengthValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.invalid_password_length));
                    valid = false;
                } else {
                    passwordTextInput.setError(null); // Clear the error
                }

                if(!isConfPasswordValid(confPasswordEditText.getText().toString(), passwordEditText.getText().toString())) {
                    confPasswordTextInput.setError(getString(R.string.invalid_conf_password));
                    valid = false;
                } else{
                    confPasswordTextInput.setError(null);
                }

                if(!isUsernameLengthValid(usernameEditText.getText())){
                    usernameTextInput.setError(getString(R.string.invalid_username_length));
                    valid = false;
                } else {
                    usernameTextInput.setError(null);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.set(dateNaissanceEditText.getYear(), dateNaissanceEditText.getMonth(), dateNaissanceEditText.getDayOfMonth());
                if(!isDateNaissanceValid(calendar.getTime())){
                    dateNaissanceTextInput.setError(getString(R.string.invalid_date_naissance));
                    valid = false;
                } else
                    dateNaissanceTextInput.setError(null);

                if(!isNomValid(nomEditText.getText())){
                    nomTextInput.setError(getString(R.string.invalid_nom));
                    valid = false;
                } else {
                    nomTextInput.setError(null);
                }

                if(!isPrenomValid(prenomEditText.getText())){
                    prenomTextInput.setError(getString(R.string.invalid_prenom));
                    valid = false;
                } else {
                    prenomTextInput.setError(null);
                }

                if(!isMailValid(mailEditText.getText())){
                    mailTextInput.setError(getString(R.string.invalid_mail));
                    valid = false;
                } else {
                    mailTextInput.setError(null);
                }

                if(!isGsmValid(gsmEditText.getText())){
                    gsmTextInput.setError(getString(R.string.invalid_gsm));
                    valid = false;
                } else {
                    gsmTextInput.setError(null);
                }

                if(!isTelephoneValid(telephoneEditText.getText())){
                    telephoneTextInput.setError(getString(R.string.invalid_telephone));
                    valid = false;
                } else {
                    telephoneTextInput.setError(null);
                }

                if(!isRueValid(rueEditText.getText())){
                    rueTextInput.setError(getString(R.string.invalid_rue));
                    valid = false;
                } else {
                    rueTextInput.setError(null);
                }

                if(!isNumeroValid(numeroEditText.getText())){
                    numeroTextInput.setError(getString(R.string.invalid_numero));
                    valid = false;
                } else {
                    numeroTextInput.setError(null);
                }

                if(!isLocaliteValid(localiteEditText.getText())){
                    localiteTextInput.setError(getString(R.string.invalid_localite));
                    valid = false;
                } else {
                    localiteTextInput.setError(null);
                }

                if(!isCodePostalValid(codePostalEditText.getText())){
                    codePostalTextInput.setError(getString(R.string.invalid_codePostal));
                    valid = false;
                } else {
                    codePostalTextInput.setError(null);
                }

                if(valid)
                {
                    if(UtilDAO.isInternetAvailable(getContext()))
                    {
                        newAdresse = new Adresse(
                                rueEditText.getText().toString(),
                                Integer.valueOf(numeroEditText.getText().toString()),
                                localiteEditText.getText().toString(),
                                Integer.valueOf(codePostalEditText.getText().toString()),
                                "Belgique"
                        );

                        Long tel = null;
                        if(telephoneEditText.getText().length() != 0)
                            tel = Long.valueOf(telephoneEditText.getText().toString());
                        newUser = new Utilisateur(
                                nomEditText.getText().toString(),
                                prenomEditText.getText().toString(),
                                calendar.getTime(),
                                mailEditText.getText().toString(),
                                tel,
                                Long.valueOf(gsmEditText.getText().toString()),
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString()
                        );
                        new AjoutAdresse().execute(newAdresse);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Erreur connexion lost", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // Clear the error once more than 8 characters are typed.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordLengthValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
        usernameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(isUsernameLengthValid(usernameEditText.getText()))
                    usernameTextInput.setError(null);
                return false;
            }
        });

        confPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(isConfPasswordValid(confPasswordEditText.getText().toString(), passwordEditText.getText().toString()))
                    confPasswordTextInput.setError(null);
                return false;
            }
        });
        return view;
    }

    private boolean isPasswordLengthValid(@Nullable Editable text) {
        return text != null && text.length() >= 3 && text.length() < 20;
    }

    private boolean isUsernameLengthValid(@Nullable Editable text) {
        return text!=null && text.length() >= 3 && text.length() < 30;
    }

    private boolean isConfPasswordValid(@Nullable String text, @Nullable String text2) {
        return text.equals(text2);
    }

    private boolean isDateNaissanceValid(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -13);
        return date.before(calendar.getTime());
    }

    private boolean isNomValid(@NonNull Editable nom) {
        return nom!=null && nom.length() >= 2 && nom.length() < 50;
    }

    private boolean isPrenomValid(@NonNull Editable prenom) {
        return prenom!=null && prenom.length() >= 2 && prenom.length() < 50;
    }

    private boolean isMailValid(@NonNull Editable mail) {
        return mail!=null && mail.length() >= 2 && mail.length() < 50;
    }

    private boolean isTelephoneValid(@NonNull Editable telephone) {
        return telephone.length() == 0 || telephone.length() == 9;
    }

    private boolean isGsmValid(@NonNull Editable gsm) {
        return gsm!=null && gsm.length() == 10;
    }

    private boolean isRueValid(@NonNull Editable rue) {
        return rue!=null && rue.length() >= 2 && rue.length() < 100;
    }

    private boolean isNumeroValid(@NonNull Editable numero) {
        return numero!=null && numero.length() >= 1 && numero.length() < 6;
    }

    private boolean isLocaliteValid(@NonNull Editable text) {
        return text!=null && text.length() >= 2 && text.length() < 20;
    }

    private boolean isCodePostalValid(@NonNull Editable numero) {
        return numero!=null && numero.length() >= 1 && numero.length() < 8;
    }


    //APPELS DES DAOS
    private class Inscription extends AsyncTask<Utilisateur, Void, Integer>
    {
        @Override
        protected Integer doInBackground(Utilisateur ...params) {
            Integer statusCodeUtilisateur = null;
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

            try {
                statusCodeUtilisateur = utilisateurDAO.inscription(params[0]);
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), "Erreur Inscription Utilisateur 1", Toast.LENGTH_SHORT).show();
            }

            return statusCodeUtilisateur;
        }

        @Override
        protected void onPostExecute(Integer statusCode)
        {
            if(statusCode == 201){
                Toast.makeText(getContext(), "Success inscription", Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
                ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), false);
            }else{
                Toast.makeText(getContext(), "Erreur Inscription Utilisateur 2 : " + statusCode, Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
            }
        }
    }

    private class AjoutAdresse extends AsyncTask<Adresse, Void, Adresse>
    {
        HttpResultException exception;
        @Override
        protected Adresse doInBackground(Adresse ...params) {
            Adresse adresse = new Adresse();
            AdresseDAO adresseDAO = new AdresseDAO();

            try {
                adresse = adresseDAO.ajoutAdresse(params[0]);
            }
            catch (HttpResultException e)
            {
                exception = e;
                cancel(true);
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), "Erreur Inscription Adresse : ", Toast.LENGTH_SHORT).show();
            }

            newUser.setAdresse(adresse.getId());
            return adresse;
        }

        @Override
        protected void onPostExecute(Adresse adresse)
        {
            if(adresse.getId() != null){
                new Inscription().execute(newUser);
            }else{
                Toast.makeText(getContext(), "Erreur Inscription Adresse : ", Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
            }
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
        Toolbar toolbar = view.findViewById(R.id.inscription_app_bar);
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
                ((NavigationHost)getActivity()).navigateTo(new BoxFragment(),true);
            }
        });

        view.findViewById(R.id.menu_a_propos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost)getActivity()).navigateTo(new AProposFragment(),true);
            }
        });

        view.findViewById(R.id.menu_nous_contactez).setOnClickListener(new View.OnClickListener() {
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

        View panier = view.findViewById(R.id.menu_panier);
        MaterialButton compte = view.findViewById(R.id.menu_compte);
        compte.setElevation((float)1);
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
            view.findViewById(R.id.menu_compte).setOnClickListener(null);
        }

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(
                getContext(),
                view.findViewById(R.id.inscription_grid),
                new AccelerateDecelerateInterpolator(),
                getContext().getResources().getDrawable(R.drawable.branded_menu), // Menu open icon
                getContext().getResources().getDrawable(R.drawable.close_menu))); // Menu close icon
    }
}
