package com.example.aperobox.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.aperobox.Dao.AdresseDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.UtilisateurDAO;
import com.example.aperobox.Model.Adresse;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;


/**
 * Fragment representing the register screen for AperoBox.
 */
public class InscriptionFragment extends Fragment {
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


                if(valid)
                {
                    if(UtilDAO.isInternetAvailable(getContext()))
                    {
                        /*Adresse newAdresse = new Adresse(
                                25,
                                rueEditText.getText().toString(),
                                Integer.valueOf(numeroEditText.getText().toString()),
                                localiteEditText.getText().toString(),
                                Integer.valueOf(codePostalEditText.getText().toString()),
                                "Belgique"
                        );*/
                        //java.sql.Date sqlDate = new java.sql.Date(calendar.getTime().getTime());
                        Integer tel = null;
                        if(telephoneEditText.getText().length() != 0)
                            tel = Integer.valueOf(telephoneEditText.getText().toString());
                        Utilisateur newUser = new Utilisateur(
                                nomEditText.getText().toString(),
                                prenomEditText.getText().toString(),
                                calendar.getTime(),
                                mailEditText.getText().toString(),
                                tel,
                                Integer.valueOf(gsmEditText.getText().toString()),
                                usernameEditText.getText().toString(),
                                passwordEditText.getText().toString(),
                                1
                        );
                        //new AjoutAdresse().execute(newAdresse);
                        new Inscription().execute(newUser);
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
        return text != null && text.length() >= 3;
    }

    private boolean isUsernameLengthValid(@Nullable Editable text) {
        return text!=null && text.length() >= 3;
    }

    private boolean isConfPasswordValid(@Nullable String text, @Nullable String text2) {
        return text.equals(text2);
    }

    private boolean isDateNaissanceValid(Date date){
        return date.before(new Date());
    }


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
            if(statusCode == HttpURLConnection.HTTP_OK){
                Toast.makeText(getContext(), "Success inscription", Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
                ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), false);
            }else{
                Toast.makeText(getContext(), "Erreur Inscription Utilisateur 2 : " + statusCode, Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
            }
        }
    }


    private class AjoutAdresse extends AsyncTask<Adresse, Void, Adresse>
    {
        @Override
        protected Adresse doInBackground(Adresse ...params) {
            Adresse adresse = new Adresse();
            AdresseDAO adresseDAO = new AdresseDAO();

            try {
                adresse = adresseDAO.ajoutAdresse(params[0]);
            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), "Erreur Inscription Adresse", Toast.LENGTH_SHORT).show();
            }

            return adresse;
        }

        @Override
        protected void onPostExecute(Adresse adresse)
        {
            if(adresse.getId() != null){
                new Inscription().execute();
            }else{
                Toast.makeText(getContext(), "Erreur Inscription Adresse : ", Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
            }
        }
    }

}
