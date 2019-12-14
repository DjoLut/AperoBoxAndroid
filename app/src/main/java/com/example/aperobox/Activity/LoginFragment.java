package com.example.aperobox.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

                if(valid)
                    ((NavigationHost) getActivity()).navigateTo(new BoxsGridFragment(), false); // Navigate to the next Fragment
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

    /*
        In reality, this will have more complex logic including, but not limited to, actual
        authentication of the username and password.
     */
    private boolean isPasswordLengthValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    private boolean isUsernameLengthValid(@Nullable Editable text) {
        return text!=null && text.length() >=8;
    }
}
