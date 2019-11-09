package com.example.aperobox.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.aperobox.R;

public class Inscription extends AppCompatActivity {

    private EditText nomUtilisateur;
    private EditText prenom;
    private EditText nom;
    private EditText email;
    private EditText dateNaissance;
    private EditText motDePasse;
    private Button boutonInscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        nomUtilisateur = (EditText) findViewById(R.id.nomUtilisateurInscription);
        prenom = (EditText) findViewById(R.id.prenomInscription);
        nom = (EditText) findViewById(R.id.nomInscription);
        email = (EditText) findViewById(R.id.emailInscription);
        dateNaissance = (EditText) findViewById(R.id.dateNaissanceInscription);
        motDePasse = (EditText) findViewById(R.id.motDePasseInscription);
        boutonInscription = (Button) findViewById(R.id.boutonInscription);

        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    public void validation()
    {
        //REGISTER
    }
}
