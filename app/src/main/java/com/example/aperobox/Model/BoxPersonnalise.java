package com.example.aperobox.Model;

public class BoxPersonnalise {
    private Integer id;
    private Integer quantite;
    private LigneDeCommande ligneDeCommande;
    private Produit produit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LigneDeCommande getLigneDeCommande() {
        return ligneDeCommande;
    }

    public void setLigneDeCommande(LigneDeCommande ligneDeCommande) {
        this.ligneDeCommande = ligneDeCommande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}
