package com.isen_lille.valacro;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Enfant. Utilisée pour récupérer toutes les informations relatives à l'adhérent via Firebase
 */

public class Enfant {
    /**
     * Nom de l'enfant
     */
    String nom;

    /**
     * Prénom de l'enfant
     */
    String prenom;

    /**
     * Date de naisse de l'enfant
     */
    String dateNaissance;

    /**
     * Accord parental
     */
    String accordParental;

    /**
     * Certificat médical
     */
    String certifMed;

    /**
     * Droit à l'image
     */
    String droitImage;

    /**
     * Formation de l'enfant
     */
    String formation;

    /**
     * Groupe de l'enfnat
     */
    String groupe;

    /**
     * Liste des partenaires
     */
    List<String> partenaires = new ArrayList<String>();

    public void setAccordParental(String accordParental) {
        this.accordParental = accordParental;
    }

    public void setCertifMed(String certifMed) {
        this.certifMed = certifMed;
    }

    public void setDroitImage(String droitImage) {
        this.droitImage = droitImage;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPartenaires(List<String> partenaires) {
        this.partenaires = partenaires;
    }

    public void setDateNaissance(String datenaissance) {
        this.dateNaissance = datenaissance;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getAccordParental() {
        return accordParental;
    }

    public String getCertifMed() {
        return certifMed;
    }

    public String getDroitImage() {
        return droitImage;
    }

    public String getFormation() {
        return formation;
    }

    public String getGroupe() {
        return groupe;
    }

    public String getNom() {
        return nom;
    }

    public List<String> getPartenaires() {
        return partenaires;
    }

    public String getPrenom() {
        return prenom;
    }


    /**
     * Constructeur sans paramètres
     */
    public Enfant(){
        // Le constructeur vide est nécessaire pour Firebase
    }

    /**
     * Constructeur enfant avec 3 paramètres
     * @param prenom prénom de l'enfant
     * @param datenaissance date de naissance de l'enfant
     * @param partenaires partenaires de l'enfant
     */
    public Enfant(String prenom, String datenaissance, List<String> partenaires){
        this.prenom = prenom;
        this.dateNaissance = datenaissance;
        this.partenaires = partenaires;
    }

    /**
     * Constructeur enfant
     * @param prenom Prénom de l'enfant
     * @param nom Nom de l'enfant
     * @param dateNaissance date de naissance de l'enfant
     * @param partenaires partenaires de l'enfant
     * @param accordParental accord parental
     * @param certifMed certificat médical
     * @param droitImage droit à l'image
     * @param groupe groupe de l'enfant
     * @param formation formation de l'enfant
     */
    public Enfant(String prenom, String nom, String dateNaissance, List<String> partenaires, String accordParental, String certifMed, String droitImage, String groupe, String formation){
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.accordParental = accordParental;
        this.droitImage = droitImage;
        this.groupe = groupe;
        this.formation = formation;
        this.partenaires = partenaires;
        this.certifMed = certifMed;
    }
}
