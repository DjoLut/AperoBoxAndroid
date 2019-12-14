package com.example.aperobox.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aperobox.Dao.network.BoxEntry;
import com.example.aperobox.Dao.network.ImageRequester;

import com.example.aperobox.R;

import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class BoxsCardRecyclerViewAdapter extends RecyclerView.Adapter<BoxsCardViewHolder> {

    private List<BoxEntry> productList;
    private ImageRequester imageRequester;

    BoxsCardRecyclerViewAdapter(List<BoxEntry> productList) {
        this.productList = productList;
        imageRequester = ImageRequester.getInstance();
    }

    @NonNull
    @Override
    public BoxsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boxs_card, parent, false);
        return new BoxsCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxsCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
            BoxEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);

            imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
