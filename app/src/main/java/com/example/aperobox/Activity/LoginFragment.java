package com.example.aperobox.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Dao.UtilisateurDAO;
import com.example.aperobox.Exception.HttpResultException;
import com.example.aperobox.Model.JwtToken;
import com.example.aperobox.Model.LoginModel;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment representing the login screen for AperoBox.
 */
public class LoginFragment extends Fragment {

    private SharedPreferences preferences;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.login_password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.login_password_edit_text);
        final TextInputLayout usernameTextInput = view.findViewById(R.id.login_username_text_input);
        final TextInputEditText usernameEditText = view.findViewById(R.id.login_username_edit_text);
        final MaterialButton nextButton = view.findViewById(R.id.connexion_button);
        final MaterialButton inscriptionButton = view.findViewById(R.id.inscription_button);

        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                if(!isUsernameLengthValid(usernameEditText.getText())) {
                    usernameTextInput.setError(getString(R.string.invalid_username_length));
                    valid = false;
                }
                else
                    usernameTextInput.setError(null);

                if (!isPasswordLengthValid(passwordEditText.getText())) {
                    passwordTextInput.setError(getString(R.string.invalid_password));
                    valid = false;
                } else
                    passwordTextInput.setError(null); // Clear the error

                if(valid) {
                    LoginModel utilisateurConnection = new LoginModel(
                            usernameEditText.getText().toString(),
                            passwordEditText.getText().toString()
                    );

                    if(UtilDAO.isInternetAvailable(getContext()))
                        new Connection().execute(utilisateurConnection);
                }
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new InscriptionFragment(), true);
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

        return view;
    }


    private boolean isPasswordLengthValid(@Nullable Editable text) {
        return text != null && text.length() >= 3;
    }

    private boolean isUsernameLengthValid(@Nullable Editable text) {
        return text!=null && text.length() >= 3;
    }



    private class Connection extends AsyncTask<LoginModel, Void, JwtToken> {

        private HttpResultException exception;

        @Override
        protected JwtToken doInBackground(LoginModel... loginModels) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            JwtToken token = null;
            try {
                token = utilisateurDAO.connection(loginModels[0]);
            } catch (HttpResultException e) {
                exception = e;
                cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return token;
        }

        @Override
        protected void onPostExecute(JwtToken token)
        {
            preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("access_token", token.getAccess_token());
            if (editor.commit()) {
                //new RefreshingToken().execute();
                Toast.makeText(getContext(), "Success Login", Toast.LENGTH_LONG).show();
                ((NavigationHost) getActivity()).navigateTo(new BoxsGridFragment(), false);
            } else {
                Toast.makeText(getContext(), "Erreur Login", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
