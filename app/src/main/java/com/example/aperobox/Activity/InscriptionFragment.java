package com.example.aperobox.Activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;
import com.example.aperobox.Dao.AdresseDAO;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.UtilisateurDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.Adresse;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.example.aperobox.Application.AperoBoxApplication;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionFragment extends Fragment {

    private Utilisateur newUser;
    private Adresse newAdresse;
    private Inscription inscriptionTask;
    private AjoutAdresse ajoutAdresseTask;


    private TextInputLayout nomTextInput;
    private TextInputEditText nomEditText;

    private TextInputLayout prenomTextInput;
    private TextInputEditText prenomEditText;

    private TextInputLayout dateNaissanceTextInput;
    private DatePicker dateNaissanceEditText;

    private TextInputLayout mailTextInput;
    private TextInputEditText mailEditText;

    private TextInputLayout telephoneTextInput;
    private TextInputEditText telephoneEditText;

    private TextInputLayout gsmTextInput;
    private TextInputEditText gsmEditText;

    private TextInputLayout usernameTextInput;
    private TextInputEditText usernameEditText;

    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;

    private TextInputLayout confPasswordTextInput;
    private TextInputEditText confPasswordEditText;

    private TextInputLayout rueTextInput;
    private TextInputEditText rueEditText;

    private TextInputLayout numeroTextInput;
    private TextInputEditText numeroEditText;

    private TextInputLayout localiteTextInput;
    private TextInputEditText localiteEditText;

    private TextInputLayout codePostalTextInput;
    private TextInputEditText codePostalEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AperoBoxApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MaterialButton accueil = ((MainActivity)getActivity()).accueil;
        accueil.setElevation(0);
        MaterialButton apropos = ((MainActivity)getActivity()).apropos;
        apropos.setElevation(0);
        MaterialButton compte = ((MainActivity)getActivity()).compte;
        compte.setElevation(1);
        MaterialButton option = ((MainActivity)getActivity()).option;
        option.setElevation(0);
        MaterialButton panierM = ((MainActivity)getActivity()).panier;
        panierM.setElevation(0);
        MaterialButton boxPerso = ((MainActivity)getActivity()).boxPersonnalise;
        boxPerso.setElevation(0);

    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inscription_fragment, container, false);

        MaterialButton menu = ((MainActivity)getActivity()).compte;
        menu.setElevation(1);


        nomTextInput = view.findViewById(R.id.inscription_nom_text_input);
        nomEditText = view.findViewById(R.id.inscription_nom_edit_text);

        prenomTextInput = view.findViewById(R.id.inscription_prenom_text_input);
        prenomEditText = view.findViewById(R.id.inscription_prenom_edit_text);

        dateNaissanceTextInput = view.findViewById(R.id.inscription_date_naissance_input);
        dateNaissanceEditText = view.findViewById(R.id.inscription_date_naissance_datePicker1);

        mailTextInput = view.findViewById(R.id.inscription_mail_text_input);
        mailEditText = view.findViewById(R.id.inscription_mail_edit_text);

        telephoneTextInput = view.findViewById(R.id.inscription_telephone_text_input);
        telephoneEditText = view.findViewById(R.id.inscription_telephone_edit_text);

        gsmTextInput = view.findViewById(R.id.inscription_gsm_text_input);
        gsmEditText = view.findViewById(R.id.inscription_gsm_edit_text);

        usernameTextInput = view.findViewById(R.id.inscription_username_text_input);
        usernameEditText = view.findViewById(R.id.inscription_username_edit_text);

        passwordTextInput = view.findViewById(R.id.inscription_password_text_input);
        passwordEditText = view.findViewById(R.id.inscription_password_edit_text);

        confPasswordTextInput = view.findViewById(R.id.inscription_conf_password_text_input);
        confPasswordEditText = view.findViewById(R.id.inscription_conf_password_edit_text);

        rueTextInput = view.findViewById(R.id.inscription_rue_text_input);
        rueEditText = view.findViewById(R.id.inscription_rue_edit_text);

        numeroTextInput = view.findViewById(R.id.inscription_numero_text_input);
        numeroEditText = view.findViewById(R.id.inscription_numero_edit_text);

        localiteTextInput = view.findViewById(R.id.inscription_localite_text_input);
        localiteEditText = view.findViewById(R.id.inscription_localite_edit_text);

        codePostalTextInput = view.findViewById(R.id.inscription_code_postal_text_input);
        codePostalEditText = view.findViewById(R.id.inscription_code_postal_edit_text);

        MaterialButton inscriptionButton = view.findViewById(R.id.inscription_button_inscription);

        // Set cut corner background for API 23+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.findViewById(R.id.inscription_grid)
                    .setBackgroundResource(R.drawable.product_grid_background_shape);
        }


        // Set an error if the password is less than 8 characters.
        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(allValid())
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
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(dateNaissanceEditText.getYear(), dateNaissanceEditText.getMonth(), dateNaissanceEditText.getDayOfMonth());
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
                                passwordEditText.getText().toString(),
                                confPasswordEditText.getText().toString()
                        );
                        ajoutAdresseTask = new AjoutAdresse();
                        ajoutAdresseTask.execute(newAdresse);
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.retry, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        setErrorOnChangeListener();

        return view;
    }

    //Check all Input and set error if request
    private Boolean allValid(){
        Boolean valid = true;
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

        return valid;
    }

    // Clear the errors listener.
    private void setErrorOnChangeListener(){

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { usernameTextInput.setError(!isUsernameLengthValid(usernameEditText.getText())?getString(R.string.invalid_username_length):null); }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { passwordTextInput.setError(!isPasswordLengthValid(passwordEditText.getText())?getString(R.string.invalid_password_length):null); }
        });

        confPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { confPasswordTextInput.setError(!isConfPasswordValid(confPasswordEditText.getText().toString(), passwordEditText.getText().toString())?getString(R.string.invalid_conf_password):null); }
        });

        // Remove date error for API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dateNaissanceEditText.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(dateNaissanceEditText.getYear(), dateNaissanceEditText.getMonth(), dateNaissanceEditText.getDayOfMonth());
                    dateNaissanceTextInput.setError(!isDateNaissanceValid(calendar.getTime())?getString(R.string.invalid_date_naissance):null);
                }
            });

        nomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { nomTextInput.setError(!isNomValid(nomEditText.getText())?getString(R.string.invalid_nom):null); }
        });

        prenomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { prenomTextInput.setError(!isPrenomValid(prenomEditText.getText())?getString(R.string.invalid_prenom):null); }
        });

        mailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { mailTextInput.setError(!isMailValid(mailEditText.getText())?getString(R.string.invalid_mail):null); }
        });

        gsmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { gsmTextInput.setError(!isGsmValid(gsmEditText.getText())?getString(R.string.invalid_gsm):null); }
        });

        telephoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { telephoneTextInput.setError(!isTelephoneValid(telephoneEditText.getText())?getString(R.string.invalid_telephone):null); }
        });

        rueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { rueTextInput.setError(!isRueValid(rueEditText.getText())?getString(R.string.invalid_rue):null); }
        });

        numeroEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { numeroTextInput.setError(!isNumeroValid(numeroEditText.getText())?getString(R.string.invalid_numero):null); }
        });

        localiteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { localiteTextInput.setError(!isLocaliteValid(localiteEditText.getText())?getString(R.string.invalid_localite):null); }
        });

        codePostalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { codePostalTextInput.setError(!isCodePostalValid(codePostalEditText.getText())?getString(R.string.invalid_codePostal):null); }
        });

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

    public static boolean isDateNaissanceValid(Date date){
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
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(mail);
        return matcher.find() && mail!=null && mail.length() >= 2 && mail.length() < 50;
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.inscription_fragment_erreur_inscription) + "\n" + getString(R.string.retry), Toast.LENGTH_LONG).show();
                        inscriptionTask.cancel(true);
                    }
                });
            }

            return statusCodeUtilisateur;
        }

        @Override
        protected void onPostExecute(Integer statusCode)
        {
            if(statusCode == HttpURLConnection.HTTP_CREATED){
                Toast.makeText(getContext(), R.string.inscription_fragment_success_inscription, Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
                ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), false);
            }else{
                Toast.makeText(getContext(), R.string.inscription_fragment_error_unique_field, Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getString(R.string.inscription_fragment_erreur_ajout_adresse) + "\n" + getString(R.string.retry), Toast.LENGTH_LONG).show();
                        inscriptionTask.cancel(true);
                    }
                });
            }

            newUser.setAdresse(adresse.getId());
            return adresse;
        }

        @Override
        protected void onPostExecute(Adresse adresse)
        {
            if(adresse.getId() != null){
                inscriptionTask = new Inscription();
                inscriptionTask.execute(newUser);
            }else
                Toast.makeText(getContext(), R.string.inscription_fragment_error_adresse, Toast.LENGTH_SHORT).show();// TODO: faire ça avec @string
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(inscriptionTask!=null)
            inscriptionTask.cancel(true);
        if(ajoutAdresseTask!=null)
            ajoutAdresseTask.cancel(true);
    }

}