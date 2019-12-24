package com.example.aperobox.Model;

import java.util.Date;
import java.util.List;

public class Commande {
    private Integer id;
    private Date dateCreation;
    private Double promotion;
    private Integer utilisateur;
    private Integer adresse;

    private Double totalPrix;
    private Double totalTva;
    private Utilisateur utilisateurComplet;
    private Adresse adresseComplet;
    private List<LigneCommande> ligneCommandes;

    public Commande() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getPromotion() {
        return promotion;
    }

    public void setPromotion(Double promotion) {
        this.promotion = promotion;
    }

    public Integer getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Integer utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Integer getAdresse() {
        return adresse;
    }

    public void setAdresse(Integer adresse) {
        this.adresse = adresse;
    }

    public Double getTotalPrix() {
        return totalPrix;
    }

    public void setTotalPrix(Double totalPrix) {
        this.totalPrix = totalPrix;
    }

    public Double getTotalTva() {
        return totalTva;
    }

    public void setTotalTva(Double totalTva) {
        this.totalTva = totalTva;
    }

    public Utilisateur getUtilisateurComplet() {
        return utilisateurComplet;
    }

    public void setUtilisateurComplet(Utilisateur utilisateurComplet) {
        this.utilisateurComplet = utilisateurComplet;
    }

    public Adresse getAdresseComplet() {
        return adresseComplet;
    }

    public void setAdresseComplet(Adresse adresseComplet) {
        this.adresseComplet = adresseComplet;
    }

    public List<LigneCommande> getLigneCommandes() {
        return ligneCommandes;
    }

    public void setLigneCommandes(List<LigneCommande> ligneCommandes) {
        this.ligneCommandes = ligneCommandes;
    }
}
