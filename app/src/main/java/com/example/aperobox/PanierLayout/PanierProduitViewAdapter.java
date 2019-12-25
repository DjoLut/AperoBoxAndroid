package com.example.aperobox.PanierLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;
import com.example.aperobox.Application.SingletonPanier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PanierProduitViewAdapter extends RecyclerView.Adapter<PanierProduitViewHolder> {

    private Panier panier = SingletonPanier.getUniquePanier();
    private ArrayList<Produit> produit = new ArrayList<>();
    private ArrayList<Integer> quantite = new ArrayList<>();
    private TextView prixTotal, promotionTotal;

    public PanierProduitViewAdapter(Panier panier, Fragment fragment, TextView prixTotal, TextView promotionTotal) {
        this.prixTotal = prixTotal;
        this.promotionTotal = promotionTotal;
        for (Iterator<Map.Entry<Produit, Integer>> it = panier.getProduit().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<Produit, Integer> entry = it.next();
            this.produit.add(entry.getKey());
            this.quantite.add(entry.getValue());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public PanierProduitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produit_panier, parent, false);
        return new PanierProduitViewHolder(layoutView, prixTotal, promotionTotal);
    }

    @Override
    public void onBindViewHolder(@NonNull PanierProduitViewHolder holder, final int position) {
        PanierProduitViewHolder produitHolder = (PanierProduitViewHolder) holder;
        produitHolder.bind((Produit)produit.get(position), (Integer)quantite.get(position));
    }

    @Override
    public int getItemCount() {
        return panier.getProduit().size();
    }



}
