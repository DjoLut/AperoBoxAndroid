package com.example.aperobox.Productlayout;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;

import java.io.Console;

public class ProductPersonnaliseViewHolder extends RecyclerView.ViewHolder {

    public TextView quantiteLayout;
    public EditText quantiteTextInput;
    public Button plus;
    public Button moins;


    ProductPersonnaliseViewHolder(@NonNull View itemView){
        super(itemView);
        quantiteLayout = itemView.findViewById(R.id.box_fragment_produit_number_input);
        quantiteTextInput = itemView.findViewById(R.id.box_fragment_produit_edit_text);
        plus = itemView.findViewById(R.id.box_fragment_produit_button_plus);
        moins = itemView.findViewById(R.id.box_fragment_produit_button_moins);
    }

    public void bind(Produit produit, final Integer position){
        quantiteLayout.setText(produit.getNom());
        Produit produit1 = ProductPersonnaliseViewAdapter.produits[position];
        Integer integer = ProductPersonnaliseViewAdapter.listeProduits.get(produit1);
        quantiteTextInput.setText(integer.toString());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer integer = Integer.parseInt(quantiteTextInput.getText().toString());
                    integer++;
                    ProductPersonnaliseViewAdapter.listeProduits.put(ProductPersonnaliseViewAdapter.produits[position], integer);
                    quantiteTextInput.setText(integer.toString());
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
                    Integer integer = Integer.parseInt(quantiteTextInput.getText().toString());
                    if(integer>0) {
                        integer--;
                        ProductPersonnaliseViewAdapter.listeProduits.put(((Produit[]) ProductPersonnaliseViewAdapter.produits)[position], integer);
                        quantiteTextInput.setText(integer.toString());
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