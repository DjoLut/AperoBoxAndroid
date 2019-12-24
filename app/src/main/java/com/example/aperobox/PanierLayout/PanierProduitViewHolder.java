package com.example.aperobox.PanierLayout;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PanierProduitViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout panierProduitNomTextView;
    public TextInputEditText panierProduitQuantiteEditText;

    PanierProduitViewHolder(@NonNull View itemView){
        super(itemView);
        panierProduitNomTextView = itemView.findViewById(R.id.panier_fragment_produit_nom_text_view);
        panierProduitQuantiteEditText = itemView.findViewById(R.id.panier_fragment_produit_quantite_edit_text);
    }

    public void bind(Produit produit, Integer quantite)
    {
        panierProduitNomTextView.setHint(produit.getNom());
        panierProduitQuantiteEditText.setText(quantite.toString());
    }



}
