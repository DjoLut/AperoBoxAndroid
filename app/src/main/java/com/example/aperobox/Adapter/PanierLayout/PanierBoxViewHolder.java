package com.example.aperobox.Adapter.PanierLayout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Activity.PanierFragment;
import com.example.aperobox.Application.SingletonPanier;
import com.example.aperobox.Model.Box;
import com.example.aperobox.Model.Panier;
import com.example.aperobox.R;

public class PanierBoxViewHolder extends RecyclerView.ViewHolder {

    public TextView panierBoxNomTextView;
    public TextView panierBoxQuantiteEditText;
    private Button moins;
    private Button plus;
    private TextView prixTotal, promotionTotal, prixBox;
    private Integer quantite;
    private Panier panier;
    private Fragment fragment;

    PanierBoxViewHolder(@NonNull View itemView, Fragment fragment, TextView prixTotal, TextView promotionTotal, TextView prixBox){
        super(itemView);
        this.prixTotal = prixTotal;
        this.promotionTotal = promotionTotal;
        this.prixBox = prixBox;
        this.fragment = fragment;
        panierBoxNomTextView = itemView.findViewById(R.id.panier_fragment_box_nom_text_view);
        panierBoxQuantiteEditText = itemView.findViewById(R.id.panier_fragment_box_quantite_edit_text);
        moins = itemView.findViewById(R.id.panier_fragment_box_button_moins);
        plus = itemView.findViewById(R.id.panier_fragment_box_button_plus);
        panier = SingletonPanier.getUniquePanier();
        PanierFragment.affichePrixTotalPromotion(this.prixTotal, this.promotionTotal, prixBox, null);
    }

    public void bind(final Box box, final Integer quantit)
    {
        this.quantite = quantit;
        panierBoxNomTextView.setText(box.getNom());
        panierBoxQuantiteEditText.setText(quantite.toString());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(quantite < 25) {
                        quantite = Integer.parseInt(panierBoxQuantiteEditText.getText().toString());
                        quantite++;
                        panier.modifQuantiteBox(box, quantite);
                        panierBoxQuantiteEditText.setText(quantite.toString());
                    }
                } catch(NumberFormatException e) {
                    panierBoxQuantiteEditText.setText("bug");
                } catch(NullPointerException e) {
                    panierBoxQuantiteEditText.setText("bug2");
                }
                PanierFragment.affichePrixTotalPromotion(prixTotal,promotionTotal, prixBox, null);
            }
        });

        moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(panierBoxQuantiteEditText.getText().toString());
                    if(quantite>0) {
                        quantite--;
                        panier.modifQuantiteBox(box,quantite);
                        panierBoxQuantiteEditText.setText(quantite.toString());
                    }
                    if(quantite == 0) {
                        panier.deleteBox(box);
                        fragment.getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }
                } catch(NumberFormatException e) {
                    panierBoxQuantiteEditText.setText("bug");
                } catch(NullPointerException e) {
                    panierBoxQuantiteEditText.setText("bug2");
                }
                PanierFragment.affichePrixTotalPromotion(prixTotal,promotionTotal, prixBox, null);
            }
        });
    }
}
