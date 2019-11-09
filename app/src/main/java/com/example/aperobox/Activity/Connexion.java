package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.aperobox.R;

public class Connexion extends AppCompatActivity {

    private EditText nomUtilisateur;
    private EditText motDePasse;
    private Button boutonConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);


        nomUtilisateur = (EditText) findViewById(R.id.nomUtilisateurConnexion);
        motDePasse = (EditText) findViewById(R.id.motDePasseConnexion);
        boutonConnexion = (Button) findViewById(R.id.boutonConnexion);

        boutonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(nomUtilisateur.getText().toString(), motDePasse.getText().toString());
            }
        });
    }

    private void validate(String nomUtilisateur, String motDePasse)
    {
        // LOGIN
    }
}
