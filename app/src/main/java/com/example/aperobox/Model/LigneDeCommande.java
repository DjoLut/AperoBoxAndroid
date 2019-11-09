package com.example.aperobox.Model;

public class LigneDeCommande {
    private Integer id;
    private Double prixReel;
    private Integer quantite;
    private Commande commande;
    private Box box;

    public LigneDeCommande() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrixReel() {
        return prixReel;
    }

    public void setPrixReel(Double prixReel) {
        this.prixReel = prixReel;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
