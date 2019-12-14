package com.example.aperobox.staggeredgridlayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Activity.BoxFragment;
import com.example.aperobox.Activity.NavigationHost;
import com.example.aperobox.R;
import com.example.aperobox.Dao.network.ImageRequester;
import com.example.aperobox.Dao.network.BoxEntry;

import java.util.List;

/**
 * Adapter used to show an asymmetric grid of products, with 2 items in the first column, and 1
 * item in the second column, and so on.
 */
public class StaggeredProductCardRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredProductCardViewHolder> {

    private Fragment fragment;
    private List<BoxEntry> productList;
    private ImageRequester imageRequester;

    public StaggeredProductCardRecyclerViewAdapter(List<BoxEntry> productList, Fragment fragment) {
        this.fragment = fragment;
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
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
    public void onBindViewHolder(@NonNull StaggeredProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            BoxEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
            //A modifier par id de la box
            final int id = 1;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((NavigationHost) fragment.getActivity()).navigateTo(new BoxFragment(id), true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
