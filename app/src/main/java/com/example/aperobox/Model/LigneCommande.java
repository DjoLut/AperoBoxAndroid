package com.example.aperobox.Model;

public class LigneCommande {
    private Integer id;
    private Integer quantite;
    private Integer commande;
    private Integer box;
    private Integer produit;

    private Commande commandeComplet;
    private Box boxComplet;
    private Produit produitComplet;

    public LigneCommande() {
    }

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

    public Integer getCommande() {
        return commande;
    }

    public void setCommande(Integer commande) {
        this.commande = commande;
    }

    public Integer getBox() {
        return box;
    }

    public void setBox(Integer box) {
        this.box = box;
    }

    public Integer getProduit() {
        return produit;
    }

    public void setProduit(Integer produit) {
        this.produit = produit;
    }

    public Commande getCommandeComplet() {
        return commandeComplet;
    }

    public void setCommandeComplet(Commande commandeComplet) {
        this.commandeComplet = commandeComplet;
    }

    public Box getBoxComplet() {
        return boxComplet;
    }

    public void setBoxComplet(Box boxComplet) {
        this.boxComplet = boxComplet;
    }

    public Produit getProduitComplet() {
        return produitComplet;
    }

    public void setProduitComplet(Produit produitComplet) {
        this.produitComplet = produitComplet;
    }
}
