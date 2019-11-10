package com.example.androidprojet.models;


public class Personne {
    String nom;
    String prenom;
    String groupe;
    public String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    Etat etat;

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Personne(String nom) {
        this.nom = nom;
        this.prenom = "";
        this.groupe = "";
    }

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.groupe = "";
    }

    public Personne(String nom, String prenom, String groupe) {
        this.nom = nom;
        this.prenom = prenom;
        this.groupe = groupe;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
         return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }



    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }


}
