package com.example.aperobox.Adapter.ProductLayout;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.aperobox.Activity.BoxPersonnaliseFragment;
import com.example.aperobox.Dao.UtilDAO;
import com.example.aperobox.Model.Produit;
import com.example.aperobox.R;

public class ProductPersonnaliseViewHolder extends RecyclerView.ViewHolder {

    public TextView quantiteLayout;
    public EditText quantiteTextInput;
    public Button plus;
    public Button moins;
    private Integer quantite;
    private Context personnaliseContext;
    private TextView box_price;


    ProductPersonnaliseViewHolder(@NonNull View itemView, Context context, TextView box_price){
        super(itemView);
        personnaliseContext = context;
        this.box_price = box_price;
        quantiteLayout = itemView.findViewById(R.id.box_fragment_produit_number_input);
        quantiteTextInput = itemView.findViewById(R.id.box_fragment_produit_edit_text);
        plus = itemView.findViewById(R.id.box_fragment_produit_button_plus);
        moins = itemView.findViewById(R.id.box_fragment_produit_button_moins);
    }

    public void bind(Produit produit, final Integer position){
        quantite = 0;
        quantiteLayout.setText(produit.getNom());
        Produit produit1 = ProductPersonnaliseViewAdapter.produits[position];
        quantite = BoxPersonnaliseFragment.listeProduits.get(produit1);
        quantiteTextInput.setText(quantite.toString());

        quantiteTextInput.setEnabled(false);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(quantiteTextInput.getText().toString());
                    quantite++;
                    BoxPersonnaliseFragment.listeProduits.put(ProductPersonnaliseViewAdapter.produits[position], quantite);
                    quantiteTextInput.setText(quantite.toString());
                } catch(NumberFormatException e) {
                    quantiteTextInput.setText("bug");
                } catch(NullPointerException e) {
                    quantiteTextInput.setText("bug2");
                }
                UtilDAO.affichePrix(UtilDAO.calculTotal(BoxPersonnaliseFragment.listeProduits), personnaliseContext, box_price);
            }
        });

        moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quantite = Integer.parseInt(quantiteTextInput.getText().toString());
                    if(quantite>0) {
                        quantite--;
                        BoxPersonnaliseFragment.listeProduits.put(((Produit[]) ProductPersonnaliseViewAdapter.produits)[position], quantite);
                        quantiteTextInput.setText(quantite.toString());
                    }
                } catch(NumberFormatException e) {
                    quantiteTextInput.setText("bug");
                } catch(NullPointerException e) {
                    quantiteTextInput.setText("bug2");
                }
                UtilDAO.affichePrix(UtilDAO.calculTotal(BoxPersonnaliseFragment.listeProduits), personnaliseContext, box_price);
            }
        });

    }
}