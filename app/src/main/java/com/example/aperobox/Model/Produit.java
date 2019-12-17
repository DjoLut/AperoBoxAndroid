package com.example.aperobox.Model;

import java.util.Date;

public class Produit {
    private Integer id;
    private String nom;
    private Date datePeremption;
    private Double prixUnitaireHtva;
    private Double tva;
    private Integer alcool;

    public Produit() {
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

    public Date getDatePeremption() {
        return datePeremption;
    }

    public void setDatePeremption(Date datePeremption) {
        this.datePeremption = datePeremption;
    }

    public Double getPrixUnitaireHtva() {
        return prixUnitaireHtva;
    }

    public void setPrixUnitaireHtva(Double prixUnitaireHtva) {
        this.prixUnitaireHtva = prixUnitaireHtva;
    }

    public Double getTva() {
        return tva;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }

    public Integer getAlcool() {
        return alcool;
    }

    public void setAlcool(Integer alcool) {
        this.alcool = alcool;
    }
}
