package com.example.androidprojet.models;

import java.util.ArrayList;

public class Categorie {
    String nom ;
    ArrayList<Personne> Liste ;
    ArrayList<Personne> Liste_restant;



    public Categorie(String nom, ArrayList<Personne> liste) {
        this.nom = nom;
        Liste = liste;
        this.Liste_restant=new ArrayList<>();
    }

    public ArrayList<Personne> getListe_restant() {
        return Liste_restant;
    }

    public void setListe_restant(ArrayList<Personne> liste_restant) {
        Liste_restant = liste_restant;
    }

    public Categorie(String nom) {
        this.nom = nom;
        this.Liste = new ArrayList<>();
        this.Liste_restant=new ArrayList<>();
    }


    public void Ajouter(Personne p){
        this.Liste.add(p);
        this.AjouterRestant(p);
    }

    public void Remove(int position){
        Personne personne = this.getPersonne(position);
        this.Liste_restant.remove(personne);
        this.Liste.remove(position);
    }
    public void AjouterRestant(Personne p){ this.Liste_restant.add(p); }

    public Personne getPersonne(int i){
        return this.Liste.get(i);
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Personne> getListe() {
        return Liste;
    }

    public void setListe(ArrayList<Personne> liste) {
        Liste = liste;
    }

    public void initCategorieRapide(int n){
        this.Liste=new ArrayList<>();
        Personne personne;
        for (int i=1 ;i<=n;i++){
            personne =new Personne(String.valueOf(i));
            this.Liste.add(personne);
        }

    }

}
