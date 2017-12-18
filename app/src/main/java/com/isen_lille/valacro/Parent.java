package com.isen_lille.valacro;

/**
 * Classe Parent. Utilisée pour récupérer toutes les informations relatives au parent via Firebase
 */
public class Parent {

    /**
     * Nom du parent
     */
    String nom;

    /**
     * Prenom du parent
     */
    String prenom;

    /**
     * Adresse du parent
     */
    String adresse;

    /**
     * Adresse mail du parent
     */
    String adresseMail;

    /**
     * Code postal du parent
     */
    String codepostal;

    /**
     * Ville du parent
     */
    String ville;

    /**
     * Numero de téléphone du parent
     */
    String numtel;

    /**
     * Flag du parent
     */
    String flag;

    /**
     * Mode de paiement du parent
     */
    String paiement;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setAdresse(String ADRESSE) {
        this.adresse = ADRESSE;
    }

    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public void setNom(String NOM) {
        this.nom = NOM;
    }

    public void setPrenom(String PRENOM) {
        this.prenom = PRENOM;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setPaiement(String paiement) {
        this.paiement = paiement;
    }

    public String getPaiement() {
        return paiement;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCodepostal() {
        return codepostal;
    }

    public String getNumtel() {
        return numtel;
    }

    public String getVille() {
        return ville;
    }

    /**
     * Constructeur sans paramètres
     */
    public Parent(){

    }

    /**
     * Constructeur de la classe parent
     * @param NOM nom du parent
     * @param PRENOM prenom du parent
     * @param adresseMail adresse mail du parent
     * @param ADRESSE adresse postal du parent
     * @param codePostal code postal du parent
     * @param Ville ville du parent
     * @param NumTel numero de téléphone du parent
     * @param flag flag du parent
     */
    public Parent(String NOM, String PRENOM, String adresseMail, String ADRESSE, String codePostal, String Ville, String NumTel, String flag){
        this.nom= NOM;
        this.prenom = PRENOM;
        this.adresse = ADRESSE;
        this.codepostal = codePostal;
        this.ville = Ville;
        this.numtel = NumTel;
        this.flag = flag;
        this.adresseMail = adresseMail;
    }
}
