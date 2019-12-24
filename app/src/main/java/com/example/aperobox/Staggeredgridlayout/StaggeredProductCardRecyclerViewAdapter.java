package com.example.aperobox.Staggeredgridlayout;

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

import java.util.List;

/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1 item in the second column, and so on.
 */
public class StaggeredProductCardRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredProductCardViewHolder> {

    private Fragment fragment;
    private List<Box> boxList;

    public StaggeredProductCardRecyclerViewAdapter(List<Box> boxList, Fragment fragment) {
        this.fragment = fragment;
        this.boxList = boxList;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @NonNull
    @Override
    public StaggeredProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.staggered_boxs_card_first;
        if (viewType == 1) {
            layoutId = R.layout.staggered_boxs_card_second;
        } else if (viewType == 2) {
            layoutId = R.layout.staggered_boxs_card_third;
        }

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StaggeredProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredProductCardViewHolder holder, final int position) {
        if (boxList != null && position < boxList.size()) {
            Box product = boxList.get(position);
            holder.productTitle.setText(product.getNom());

            Double sommeHTVA = 0.0;
            Double promotion = 0.0;
            sommeHTVA = (product.getPrixUnitaireHtva()*(1+product.getTva())) * 1;
            if(product.getPromotion()!=null)
                promotion = sommeHTVA*(1-product.getPromotion());

            String prix;
            if(sommeHTVA!=0) {
                prix = UtilDAO.format.format(Math.round(sommeHTVA*100.0)/100.0);
                if (promotion != 0) {
                    prix += " - " +UtilDAO.format.format(Math.round(promotion*100.0)/100.0);
                    prix += " = " + UtilDAO.format.format(Math.round((sommeHTVA-promotion)*100.0)/100.0);
                }
            } else
                prix = fragment.getString(R.string.box_fragment_box_prix_gratuit);

            holder.productPrice.setText(prix);
            try{
                Glide.with(fragment).load(Constantes.URL_IMAGE_API + product.getPhoto()).into(holder.productImage);
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
                    ((NavigationHost) fragment.getActivity()).navigateTo(new BoxFragment(boxList.get(position).getId()), true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return boxList.size();
    }
}
