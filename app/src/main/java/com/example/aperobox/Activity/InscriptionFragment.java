package com.example.aperobox.Activity;

import android.content.Context;
import android.net.Uri;
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
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inscription_fragment, container, false);

        final TextInputLayout usernameTextInput = view.findViewById(R.id.inscription_username_text_input);
        final TextInputEditText usernameEditText = view.findViewById(R.id.inscription_username_edit_text);

        final TextInputLayout passwordTextInput = view.findViewById(R.id.inscription_password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.inscription_password_edit_text);

        final TextInputLayout confPasswordTextInput = view.findViewById(R.id.inscription_conf_password_text_input);
        final TextInputEditText confPasswordEditText = view.findViewById(R.id.inscription_conf_password_edit_text);

        final TextInputLayout nomTextInput = view.findViewById(R.id.inscription_nom_text_input);
        final TextInputEditText nomEditText = view.findViewById(R.id.inscription_nom_edit_text);

        final TextInputLayout prenomTextInput = view.findViewById(R.id.inscription_prenom_text_input);
        final TextInputEditText prenomEditText = view.findViewById(R.id.inscription_prenom_edit_text);

        final TextInputLayout dateNaissanceTextInput = view.findViewById(R.id.inscription_date_naissance_input);
        final DatePicker dateNaissanceEditText = view.findViewById(R.id.inscription_date_naissance_datePicker1);

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

                if(!isConfPasswordValid(confPasswordEditText.getText(), passwordEditText.getText())) {
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
                    ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), false); // Navigate to the next Fragment
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
                if(isConfPasswordValid(confPasswordEditText.getText(), passwordEditText.getText()))
                    confPasswordTextInput.setError(null);
                return false;
            }
        });
        return view;
    }

    private boolean isPasswordLengthValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    private boolean isUsernameLengthValid(@Nullable Editable text) {
        return text!=null && text.length() >=8;
    }

    private boolean isConfPasswordValid(@Nullable Editable text, @Nullable Editable text2) {
        return text.equals(text2);
    }

    private boolean isDateNaissanceValid(Date date){
        return date.after(new Date());
    }
}
