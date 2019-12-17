package com.example.aperobox.Model;

public class LigneProduit {
    private Integer id;
    private Integer quantite;

    private Integer box;
    private Integer produit;

    private Box boxComplete;
    private Produit produitComplete;

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
    public Integer getBox() { return box; }
    public void setBox(Integer box) { this.box = box; }
    public Integer getProduit() { return produit; }
    public void setProduit(Integer produit) { this.produit = produit; }
    public Box getBoxComplete() { return boxComplete; }
    public void setBoxComplete(Box boxComplete) { this.boxComplete = boxComplete; }
    public Produit getProduitComplete() { return produitComplete; }
    public void setProduitComplete(Produit produitComplete) { this.produitComplete = produitComplete; }
}
