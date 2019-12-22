package com.example.aperobox.Productlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Activity.BoxFragment;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProductPersonnaliseViewAdapter extends RecyclerView.Adapter<ProductPersonnaliseViewHolder> {
    public static Produit[] produits;

    public ProductPersonnaliseViewAdapter() {
        Set<Produit> set = BoxFragment.listeProduits.keySet();
        produits = new Produit[BoxFragment.listeProduits.size()];
        produits = set.toArray(produits);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public ProductPersonnaliseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_personnalise, parent, false);
        return new ProductPersonnaliseViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPersonnaliseViewHolder holder, final int position) {
        if(produits!=null && position < produits.length){
            ProductPersonnaliseViewHolder produitHolder = (ProductPersonnaliseViewHolder) holder;
            produitHolder.bind((Produit)produits[position], position);
        }
    }

    @Override
    public int getItemCount() {
        return produits.length;
    }
}
