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
import java.util.Map;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private Object[] produits;
    private Object[] quantites;

    public ProductViewAdapter(Map<Produit, Integer> listeProduits, Fragment fragment) {
        this.produits = listeProduits.keySet().toArray();
        this.quantites = listeProduits.values().toArray();
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
