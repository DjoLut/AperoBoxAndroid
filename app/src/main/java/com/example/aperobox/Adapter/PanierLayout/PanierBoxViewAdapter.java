package com.example.aperobox.Adapter.PanierLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.R;
import com.example.aperobox.Application.SingletonPanier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PanierBoxViewAdapter extends RecyclerView.Adapter<PanierBoxViewHolder> {
    private Panier panier = SingletonPanier.getUniquePanier();
    private ArrayList<Box> box = new ArrayList<>();
    private ArrayList<Integer> quantite = new ArrayList<>();
    private TextView prixTotal, promotionTotal, prixBox;
    private Fragment fragment;

    public PanierBoxViewAdapter(Panier panier, Fragment fragment, TextView prixTotal, TextView promotionTotal, TextView prixBox) {
        this.prixTotal = prixTotal;
        this.promotionTotal = promotionTotal;
        this.prixBox = prixBox;
        this.fragment = fragment;
        for (Iterator<Map.Entry<Box, Integer>> it = panier.getBox().entrySet().iterator(); it.hasNext();)
        {
            Map.Entry<Box, Integer> entry = it.next();
            this.box.add(entry.getKey());
            this.quantite.add(entry.getValue());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public PanierBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_box_panier, parent, false);
        return new PanierBoxViewHolder(layoutView, fragment, prixTotal, promotionTotal, prixBox);
    }

    @Override
    public void onBindViewHolder(@NonNull PanierBoxViewHolder holder, final int position) {
        PanierBoxViewHolder produitHolder = (PanierBoxViewHolder) holder;
        produitHolder.bind((Box)box.get(position), (Integer)quantite.get(position));
    }

    @Override
    public int getItemCount() {
        return panier.getBox().size();
    }
}
