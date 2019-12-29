package com.example.aperobox.Adapter.BoxGridLayout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;
import com.google.android.material.card.MaterialCardView;

public class BoxGridViewHolder extends RecyclerView.ViewHolder {

    ImageView boxImageView;
    TextView boxNomTextView;
    TextView boxPrixTextView;
    MaterialCardView layout;

    BoxGridViewHolder(@NonNull View itemView, Fragment fragment){
        super(itemView);
        boxImageView = itemView.findViewById(R.id.box_grid_fragment_box_image);
        boxNomTextView = itemView.findViewById(R.id.box_grid_fragment_box_nom_text_view);
        boxPrixTextView = itemView.findViewById(R.id.box_grid_fragment_box_price);
        layout = itemView.findViewById(R.id.box_grid_fragment_item_layout);
    }

    public void bind(Box box)
    {
        boxNomTextView.setText(box.getNom());
        boxPrixTextView.setText(box.getPrixUnitaireHtva().toString());
    }
}
