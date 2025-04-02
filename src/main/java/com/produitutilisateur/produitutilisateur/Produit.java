package com.produitutilisateur.produitutilisateur;

/**
 * Classe représentant un produit agricole
 */
public class Produit {

    /**
     * Identifiant unique du produit
     */
    protected int id;

    /**
     * Nom du produit
     */
    protected String nom;

    /**
     * Catégorie du produit (légumes, oeufs, volailles, fromages, etc.)
     */
    protected String categorie;

    /**
     * Quantité disponible du produit
     */
    protected double quantite;

    /**
     * Unité de mesure du produit (kilo, unité, douzaine, etc.)
     */
    protected String unite;

    /**
     * Prix unitaire du produit
     */
    protected double prix;

    /**
     * Constructeur par défaut
     */
    public Produit() {
    }

    /**
     * Constructeur de produit
     * @param id identifiant unique du produit
     * @param nom nom du produit
     * @param categorie catégorie du produit
     * @param quantite quantité disponible
     * @param unite unité de mesure du produit
     * @param prix prix unitaire
     */
    public Produit(int id, String nom, String categorie, double quantite, String unite, double prix) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.quantite = quantite;
        this.unite = unite;
        this.prix = prix;
    }

    /**
     * Méthode permettant d'accéder à l'identifiant du produit
     * @return un entier avec l'identifiant du produit
     */
    public int getId() {
        return id;
    }

    /**
     * Méthode permettant d'accéder au nom du produit
     * @return une chaîne de caractères avec le nom du produit
     */
    public String getNom() {
        return nom;
    }

    /**
     * Méthode permettant d'accéder à la catégorie du produit
     * @return une chaîne de caractères avec la catégorie du produit
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Méthode permettant d'accéder à la quantité disponible
     * @return un double avec la quantité disponible
     */
    public double getQuantite() {
        return quantite;
    }

    /**
     * Méthode permettant d'accéder à l'unité de mesure du produit
     * @return une chaîne de caractères avec l'unité de mesure
     */
    public String getUnite() {
        return unite;
    }

    /**
     * Méthode permettant d'accéder au prix unitaire
     * @return un double avec le prix unitaire
     */
    public double getPrix() {
        return prix;
    }

    /**
     * Méthode permettant de modifier l'identifiant du produit
     * @param id un entier avec le nouvel identifiant
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Méthode permettant de modifier le nom du produit
     * @param nom une chaîne de caractères avec le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Méthode permettant de modifier la catégorie du produit
     * @param categorie une chaîne de caractères avec la nouvelle catégorie
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Méthode permettant de modifier la quantité disponible
     * @param quantite un double avec la nouvelle quantité
     */
    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    /**
     * Méthode permettant de modifier l'unité de mesure du produit
     * @param unite une chaîne de caractères avec la nouvelle unité
     */
    public void setUnite(String unite) {
        this.unite = unite;
    }

    /**
     * Méthode permettant de modifier le prix unitaire
     * @param prix un double avec le nouveau prix
     */
    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", quantite=" + quantite +
                ", unite='" + unite + '\'' +
                ", prix=" + prix +
                '}';
    }
}