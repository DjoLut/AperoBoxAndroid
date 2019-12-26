package com.example.aperobox.CommentaireLayout;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Activity.CommentaireBoxFragment;
import com.example.aperobox.Application.AperoBoxApplication;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.Model.Utilisateur;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;

public class CommentaireViewHolder extends RecyclerView.ViewHolder {
    public TextView texte;
    private TextView date;
    public MaterialButton button;
    private CommentaireBoxFragment fragment;

    CommentaireViewHolder(@NonNull View itemView, CommentaireBoxFragment fragment){
        super(itemView);
        texte = itemView.findViewById(R.id.commentaire_texte);
        date = itemView.findViewById(R.id.commentaire_dateCreation);
        this.fragment = fragment;
    }

    public void bind(final Commentaire commentaire){
        texte.setText(commentaire.getTexte());
        date.setText(commentaire.getDateCreation().toLocaleString());
    }
}
