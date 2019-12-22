package com.example.aperobox.Productlayout;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Activity.BoxFragment;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;

import java.io.Console;

public class ProductPersonnaliseViewHolder extends RecyclerView.ViewHolder {

    public TextView quantiteLayout;
    public EditText quantiteTextInput;
    public Button plus;
    public Button moins;
    private Integer quantite;


    ProductPersonnaliseViewHolder(@NonNull View itemView){
        super(itemView);
        quantiteLayout = itemView.findViewById(R.id.box_fragment_produit_number_input);
        quantiteTextInput = itemView.findViewById(R.id.box_fragment_produit_edit_text);
        plus = itemView.findViewById(R.id.box_fragment_produit_button_plus);
        moins = itemView.findViewById(R.id.box_fragment_produit_button_moins);
    }

    public void bind(Produit produit, final Integer position){
        quantite = 0;
        quantiteLayout.setText(produit.getNom());
        Produit produit1 = ProductPersonnaliseViewAdapter.produits[position];
        quantite = BoxFragment.listeProduits.get(produit1);
        quantiteTextInput.setText(quantite.toString());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(quantiteTextInput.getText().toString());
                    quantite++;
                    BoxFragment.listeProduits.put(ProductPersonnaliseViewAdapter.produits[position], quantite);
                    quantiteTextInput.setText(quantite.toString());
                } catch(NumberFormatException e) {
                    quantiteTextInput.setText("bug");
                } catch(NullPointerException e) {
                    quantiteTextInput.setText("bug2");
                }
            }
        });

        moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(quantiteTextInput.getText().toString());
                    if(quantite>0) {
                        quantite--;
                        BoxFragment.listeProduits.put(((Produit[]) ProductPersonnaliseViewAdapter.produits)[position], quantite);
                        quantiteTextInput.setText(quantite.toString());
                    }
                } catch(NumberFormatException e) {
                    quantiteTextInput.setText("bug");
                } catch(NullPointerException e) {
                    quantiteTextInput.setText("bug2");
                }
            }
        });

    }
}