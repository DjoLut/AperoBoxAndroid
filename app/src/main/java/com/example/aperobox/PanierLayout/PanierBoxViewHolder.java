package com.example.aperobox.PanierLayout;

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
    private TextView prixTotal, promotionTotal;
    private Integer quantite;
    private Panier panier;

    PanierBoxViewHolder(@NonNull View itemView, TextView prixTotal, TextView promotionTotal){
        super(itemView);
        this.prixTotal = prixTotal;
        this.promotionTotal = promotionTotal;
        panierBoxNomTextView = itemView.findViewById(R.id.panier_fragment_box_nom_text_view);
        panierBoxQuantiteEditText = itemView.findViewById(R.id.panier_fragment_box_quantite_edit_text);
        moins = itemView.findViewById(R.id.panier_fragment_box_button_moins);
        plus = itemView.findViewById(R.id.panier_fragment_box_button_plus);
        panier = SingletonPanier.getUniquePanier();
        PanierFragment.affichePrixTotalPromotion(this.prixTotal, this.promotionTotal);
    }

    public void bind(final Box box, Integer quantit)
    {
        this.quantite = quantit;
        panierBoxNomTextView.setText(box.getNom());
        panierBoxQuantiteEditText.setText(quantite.toString());

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(panierBoxQuantiteEditText.getText().toString());
                    quantite++;
                    panier.modifQuantiteBox(box,quantite);
                    panierBoxQuantiteEditText.setText(quantite.toString());
                } catch(NumberFormatException e) {
                    panierBoxQuantiteEditText.setText("bug");
                } catch(NullPointerException e) {
                    panierBoxQuantiteEditText.setText("bug2");
                }
                PanierFragment.affichePrixTotalPromotion(prixTotal,promotionTotal);
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
                    if(quantite == 0)
                    {
                        panier.deleteBox(box);
                        //RELOAD FRAGMENT
                    }
                } catch(NumberFormatException e) {
                    panierBoxQuantiteEditText.setText("bug");
                } catch(NullPointerException e) {
                    panierBoxQuantiteEditText.setText("bug2");
                }
                PanierFragment.affichePrixTotalPromotion(prixTotal,promotionTotal);
            }
        });
    }
}
