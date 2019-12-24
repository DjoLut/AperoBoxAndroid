package com.example.aperobox.Model;

import java.util.Date;

public class Commentaire {
    private Integer id;
    private String texte;
    private Date dateCreation;
    private Integer utilisateur;
    private Integer box;

    private Utilisateur utilisateurComplet;
    private Box boxComplet;

    public Commentaire() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Integer getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Integer utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Integer getBox() {
        return box;
    }

    public void setBox(Integer box) {
        this.box = box;
    }

    public Utilisateur getUtilisateurComplet() {
        return utilisateurComplet;
    }

    public void setUtilisateurComplet(Utilisateur utilisateurComplet) {
        this.utilisateurComplet = utilisateurComplet;
    }

    public Box getBoxComplet() {
        return boxComplet;
    }

    public void setBoxComplet(Box boxComplet) {
        this.boxComplet = boxComplet;
    }
}
