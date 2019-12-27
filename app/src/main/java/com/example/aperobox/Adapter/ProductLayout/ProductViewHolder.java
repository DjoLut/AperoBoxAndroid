package com.example.aperobox.Adapter.ProductLayout;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout quantiteLayout;
    public TextInputEditText quantiteTextInput;

    ProductViewHolder(@NonNull View itemView){
        super(itemView);
        quantiteLayout = itemView.findViewById(R.id.box_fragment_produit_number_input);
        quantiteTextInput = itemView.findViewById(R.id.box_fragment_produit_edit_text);
    }

    public void bind(Produit produit, Integer quantite){
        quantiteLayout.setHint(produit.getNom());
        quantiteTextInput.setText(quantite.toString());
    }
}
