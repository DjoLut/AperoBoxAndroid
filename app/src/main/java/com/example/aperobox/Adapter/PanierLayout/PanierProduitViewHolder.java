package com.example.aperobox.Adapter.PanierLayout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Activity.PanierFragment;
import com.example.aperobox.Application.SingletonPanier;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;

public class PanierProduitViewHolder extends RecyclerView.ViewHolder {

    public TextView panierProduitNomTextView;
    public TextView panierProduitQuantiteEditText;
    private Button moins;
    private Button plus;
    private TextView prixTotal, promotionTotal, prixProduit;
    private Integer quantite;
    private Panier panier;
    private Fragment fragment;

    PanierProduitViewHolder(@NonNull View itemView, Fragment fragment, TextView prixTotal, TextView promotionTotal, TextView prixProduit){
        super(itemView);
        this.prixTotal = prixTotal;
        this.promotionTotal = promotionTotal;
        this.prixProduit = prixProduit;
        this.fragment = fragment;
        panierProduitNomTextView = itemView.findViewById(R.id.panier_fragment_produit_nom_text_view);
        panierProduitQuantiteEditText = itemView.findViewById(R.id.panier_fragment_produit_quantite_edit_text);
        moins = itemView.findViewById(R.id.panier_fragment_produit_button_moins);
        plus = itemView.findViewById(R.id.panier_fragment_produit_button_plus);
        panier = SingletonPanier.getUniquePanier();
        PanierFragment.affichePrixTotalPromotion(this.prixTotal, this.promotionTotal, null, prixProduit);
    }

    public void bind( final Produit produit, Integer quantit)
    {
        this.quantite = quantit;
        panierProduitNomTextView.setText(produit.getNom());
        panierProduitQuantiteEditText.setText(quantite.toString());
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(panierProduitQuantiteEditText.getText().toString());
                    quantite++;
                    panier.modifQuantiteProduit(produit, quantite);
                    panierProduitQuantiteEditText.setText(quantite.toString());
                } catch(NumberFormatException e) {
                    panierProduitQuantiteEditText.setText("bug");
                } catch(NullPointerException e) {
                    panierProduitQuantiteEditText.setText("bug2");
                }
                PanierFragment.affichePrixTotalPromotion(prixTotal,promotionTotal, null, prixProduit);
            }
        });

        moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(panierProduitQuantiteEditText.getText().toString());
                    if(quantite>0) {
                        quantite--;
                        panier.modifQuantiteProduit(produit,quantite);
                        panierProduitQuantiteEditText.setText(quantite.toString());
                    }
                    if(quantite == 0)
                    {
                        panier.deleteProduit(produit);
                        fragment.getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }
                } catch(NumberFormatException e) {
                    panierProduitQuantiteEditText.setText("bug");
                } catch(NullPointerException e) {
                    panierProduitQuantiteEditText.setText("bug2");
                }
                PanierFragment.affichePrixTotalPromotion(prixTotal,promotionTotal, null, prixProduit);
            }
        });
    }



}
