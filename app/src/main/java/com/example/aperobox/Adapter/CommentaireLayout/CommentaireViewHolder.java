package com.example.aperobox.Adapter.CommentaireLayout;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Activity.CommentaireBoxFragment;
import com.example.aperobox.Model.Commentaire;
import com.example.aperobox.R;
import com.google.android.material.button.MaterialButton;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

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
        Locale locale = new Locale("fr", "FR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        date.setText(dateFormat.format(new Date(commentaire.getDateCreation().getTime())));
    }
}
