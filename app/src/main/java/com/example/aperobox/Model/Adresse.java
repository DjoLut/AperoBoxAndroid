package com.example.aperobox.Model;

public class Adresse {
    private Integer id;
    private String rue;
    private Integer numero;
    private String localite;
    private Integer codePostal;
    private String pays;

    public Adresse() {
    }

    public Adresse(Integer id, String rue, Integer numero, String localite, Integer codePostal, String pays) {
        this.id = id;
        this.rue = rue;
        this.numero = numero;
        this.localite = localite;
        this.codePostal = codePostal;
        this.pays = pays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(Integer codePostal) {
        this.codePostal = codePostal;
    }

}
