package com.example.aperobox.Model;

import java.util.List;

public class Box {
    private Integer id;
    private String nom;
    private Double prixUnitaireHtva;
    private Double tva;
    private Double promotion;
    private String description;
    private Integer affichable;
    private String photo;
    private List<Commentaire> commentaires;
    private List<Produit> produits;

    public Box() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPromotion() {
        return promotion;
    }

    public void setPromotion(Double promotion) {
        this.promotion = promotion;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Double getTva() {
        return tva;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    public Double getPrixUnitaireHtva() {
        return prixUnitaireHtva;
    }

    public void setPrixUnitaireHtva(Double prixUnitaireHtva) {
        this.prixUnitaireHtva = prixUnitaireHtva;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getAffichable() {
        return affichable;
    }

    public void setAffichable(Integer affichable) {
        this.affichable = affichable;
    }
}
