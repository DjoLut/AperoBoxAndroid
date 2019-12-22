package com.example.aperobox.PanierLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;
import java.util.Map;

public class PanierViewAdapter extends RecyclerView.Adapter<PanierViewHolder> {
    private Map<Box, Integer> box;

    public PanierViewAdapter(Map<Box, Integer> box, Fragment fragment) {
        this.box = box;
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
        holder.panierBoxNomTextView.setText(box.get(position));

    }

    @Override
    public int getItemCount() {
        return box.size();
    }
}
