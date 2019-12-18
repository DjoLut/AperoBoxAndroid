package com.example.aperobox.Productlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private Fragment fragment;
    private Boolean quantiteModifiable;
    private LinkedHashMap<Produit, Integer> listeProduits;
    private Object[] produits;
    private Object[] quantites;
    private Iterator<Produit> iterable;

    public ProductViewAdapter(LinkedHashMap<Produit, Integer> listeProduits, Boolean quantiteModifiable, Fragment fragment) {
        this.fragment = fragment;
        this.listeProduits = listeProduits;
        this.produits = listeProduits.keySet().toArray();
        this.quantites = listeProduits.values().toArray();
        iterable = listeProduits.keySet().iterator();
        this.quantiteModifiable = quantiteModifiable;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        if(produits!=null && position < produits.length){
            /*holder.quantiteLayout.setHint(((Produit)produits[position]).getNom());
            holder.quantiteTextInput.setText(((Integer)quantites[position]).toString());
*/
            if(!quantiteModifiable)
                holder.quantiteTextInput.setEnabled(false);

            ProductViewHolder produitHolder = (ProductViewHolder) holder;
            produitHolder.bind((Produit)produits[position], (Integer)quantites[position]);
        }
    }

    @Override
    public int getItemCount() {
        return produits.length;
    }
}
