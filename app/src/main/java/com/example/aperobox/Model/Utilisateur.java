package com.example.aperobox.Model;

import java.util.Date;
import java.util.List;

public class Utilisateur {
    private Integer id;

    private String nomUtilisateur;

    private String motDePasse;

    private String confMotDePasse;

    private Date dateNaissance;

    private String prenom;

    private String nom;

    private String email;

    private Adresse adresse;

    private List<Commentaire> commentaires;

    private List<Commande> commandes;

    public Utilisateur(Integer id, String nomUtilisateur, String motDePasse, Date dateNaissance, String prenom, String nom, String email)
    {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.dateNaissance = dateNaissance;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
    }

    public Utilisateur(String nomUtilisateur, String motDePasse)
    {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    public Integer getId() {
        return id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getConfMotDePasse() {
        return confMotDePasse;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setConfMotDePasse(String confMotDePasse) {
        this.confMotDePasse = confMotDePasse;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }
}
