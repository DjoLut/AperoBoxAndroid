package com.example.aperobox.staggeredgridlayout;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.aperobox.R;

public class StaggeredProductCardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView productImage;
    public TextView productTitle;
    public TextView productPrice;

    StaggeredProductCardViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productTitle = itemView.findViewById(R.id.product_title);
        productPrice = itemView.findViewById(R.id.product_price);
    }
}
