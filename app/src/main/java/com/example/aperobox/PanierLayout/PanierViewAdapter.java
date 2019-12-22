package com.example.aperobox.PanierLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.R;
import com.example.aperobox.application.SingletonPanier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PanierViewAdapter extends RecyclerView.Adapter<PanierViewHolder> {
    private Panier panier = SingletonPanier.getUniquePanier();
    private ArrayList<Box> box = new ArrayList<>();
    private ArrayList<Integer> quantite = new ArrayList<>();

    public PanierViewAdapter(Panier panier, Fragment fragment) {
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
    public PanierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_panier, parent, false);
        return new PanierViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull PanierViewHolder holder, final int position) {
        holder.panierBoxNomTextView.setText(box.get(position).getNom());
        holder.panierBoxQuantiteEditText.setText(quantite.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return panier.getBox().size();
    }
}
