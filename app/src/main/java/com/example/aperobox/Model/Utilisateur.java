package com.example.aperobox.Model;

import java.util.Date;
import java.util.List;

public class Utilisateur {
    private Integer id;

    private String nom;

    private String prenom;

    private Date dateNaissance;

    private String mail;

    private Long telephone;

    private Long gsm;

    private String username;

    private String motDePasse;

    private String confMotDePasse;

    private Integer adresse;

    private List<Commentaire> commentaires;

    private List<Commande> commandes;

    private List<UtilisateurRole> utilisateurRoles;

    public Utilisateur(String nom, String prenom, Date dateNaissance, String mail, Long telephone, Long gsm, String username, String motDePasse)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.mail = mail;
        this.telephone = telephone;
        this.gsm = gsm;
        this.username = username;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String username, String motDePasse)
    {
        this.username = username;
        this.motDePasse = motDePasse;
    }

    public Integer getId() {
        return id;
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

    public String getConfMotDePasse() {
        return confMotDePasse;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setConfMotDePasse(String confMotDePasse) {
        this.confMotDePasse = confMotDePasse;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Integer getAdresse() {
        return adresse;
    }

    public void setAdresse(Integer adresse) {
        this.adresse = adresse;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }

    public List<UtilisateurRole> getUtilisateurRoles() {
        return utilisateurRoles;
    }

    public void setUtilisateurRoles(List<UtilisateurRole> utilisateurRoles) {
        this.utilisateurRoles = utilisateurRoles;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public Long getGsm() {
        return gsm;
    }

    public void setGsm(Long gsm) {
        this.gsm = gsm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
