package com.example.aperobox.PanierLayout;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PanierBoxViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout panierBoxNomTextView;
    public TextInputEditText panierBoxQuantiteEditText;

    PanierBoxViewHolder(@NonNull View itemView){
        super(itemView);
        panierBoxNomTextView = itemView.findViewById(R.id.panier_fragment_box_nom_text_view);
        panierBoxQuantiteEditText = itemView.findViewById(R.id.panier_fragment_box_quantite_edit_text);
    }

    public void bind(Box box, Integer quantite)
    {
        panierBoxNomTextView.setHint(box.getNom());
        panierBoxQuantiteEditText.setText(quantite.toString());
    }
}
