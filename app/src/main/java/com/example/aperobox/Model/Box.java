package com.example.aperobox.Model;

import java.util.List;

public class Box {
    private Integer id;
    private String nom;
    private Double prix;
    private Double tva;
    private String description;
    private Double promotion;
    private String image;
    private List<Commentaire> commentaires;
    private List<LigneProduit> ligneProduits;

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

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
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

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

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

    public List<LigneProduit> getLigneProduits() {
        return ligneProduits;
    }

    public void setLigneProduits(List<LigneProduit> ligneProduits) {
        this.ligneProduits = ligneProduits;
    }
}
