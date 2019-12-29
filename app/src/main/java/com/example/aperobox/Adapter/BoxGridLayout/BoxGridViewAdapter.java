package com.example.aperobox.Adapter.BoxGridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.aperobox.Activity.BoxFragment;
import com.example.aperobox.Activity.NavigationHost;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Model.Box;
import com.example.aperobox.R;
import com.example.aperobox.Utility.Constantes;
import java.util.ArrayList;

public class BoxGridViewAdapter extends RecyclerView.Adapter<BoxGridViewHolder> {

    private ArrayList<Box> boxes;
    private Fragment fragment;

    public BoxGridViewAdapter(ArrayList<Box> box, Fragment fragment) {
        this.boxes = box;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public BoxGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_box_box_grid, parent, false);
        return new BoxGridViewHolder(layoutView, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxGridViewHolder holder, final int position) {
        if (boxes != null && position < boxes.size())
        {
            Box box = boxes.get(position);
            holder.boxNomTextView.setText(box.getNom());

            Double promotion = 0.0;
            Double sommeHTVA = (box.getPrixUnitaireHtva()*(1.0+box.getTva())) * 1.0;
            if(box.getPromotion()!=null)
                promotion = sommeHTVA*(1.0-box.getPromotion());

            String prix;
            if(sommeHTVA!=0) {
                prix = UtilDAO.format.format(Math.round(sommeHTVA*100.0)/100.0);
                if (promotion != 0) {
                    prix += " - " + UtilDAO.format.format(Math.round(promotion*100.0)/100.0);
                    prix += " = " + UtilDAO.format.format(Math.round((sommeHTVA-promotion)*100.0)/100.0);
                }
            } else
                prix = fragment.getString(R.string.box_fragment_box_prix_gratuit);

            holder.boxPrixTextView.setText(prix);

            try{
                Glide.with(fragment).load(Constantes.URL_IMAGE_API + box.getPhoto()).into(holder.boxImageView);
            } catch (Exception e) {
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(fragment.getContext(), fragment.getString(R.string.chargement_lost_connection), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost) fragment.getActivity()).navigateTo(new BoxFragment(boxes.get(position).getId()), true);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return boxes.size();
    }
}
