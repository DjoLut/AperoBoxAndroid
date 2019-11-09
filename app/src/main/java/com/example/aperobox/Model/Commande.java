package com.example.aperobox.Model;

import java.util.Date;
import java.util.List;

public class Commande {
    private Integer id;
    private Date dateCommande;
    private Double promotion;
    private Double totalPrix;
    private Double totalTva;
    private Utilisateur utilisateur;
    private Adresse adresse;
    private List<LigneDeCommande> ligneDeCommandes;

    public Commande() {
    }

    public Integer getId() {
        return id;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public Double getPromotion() {
        return promotion;
    }

    public Double getTotalPrix() {
        return totalPrix;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public void setPromotion(Double promotion) {
        this.promotion = promotion;
    }

    public void setTotalPrix(Double totalPrix) {
        this.totalPrix = totalPrix;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Double getTotalTva() {
        return totalTva;
    }

    public void setTotalTva(Double totalTva) {
        this.totalTva = totalTva;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public List<LigneDeCommande> getLigneDeCommandes() {
        return ligneDeCommandes;
    }

    public void setLigneDeCommandes(List<LigneDeCommande> ligneDeCommandes) {
        this.ligneDeCommandes = ligneDeCommandes;
    }
}
