package com.example.androidprojet.models;

public enum Etat {

        absent, fini, passage;

        public static Etat getEtat(String etat) {

            if (etat.equals("absent")) {
                return Etat.absent;
            } else if (etat.equals("fini")) {
                return Etat.fini;
            } else return Etat.passage;
        }

        public String toString(){
            return this.toString();
        }
}
