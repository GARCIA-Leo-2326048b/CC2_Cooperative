package com.produitutilisateur.produitutilisateur;

import java.util.ArrayList;

/**
 * Interface d'accès aux données des produits agricoles
 */
public interface ProduitRepositoryInterface {

    /**
     * Méthode fermant le dépôt où sont stockées les informations sur les produits
     */
    public void close();

    /**
     * Méthode retournant le produit dont l'identifiant est passé en paramètre
     * @param id identifiant du produit recherché
     * @return un objet Produit représentant le produit recherché
     */
    public Produit getProduit(int id);

    /**
     * Méthode retournant la liste de tous les produits
     * @return une liste d'objets Produit
     */
    public ArrayList<Produit> getAllProduits();

    /**
     * Méthode retournant la liste des produits d'une catégorie spécifique
     * @param categorie catégorie des produits recherchés
     * @return une liste d'objets Produit de la catégorie spécifiée
     */
    public ArrayList<Produit> getProduitsByCategorie(String categorie);

    /**
     * Méthode permettant de créer un nouveau produit
     * @param produit le produit à créer
     * @return true si la création a réussi, false sinon
     */
    public boolean createProduit(Produit produit);

    /**
     * Méthode permettant de mettre à jour un produit enregistré
     * @param id identifiant du produit à mettre à jour
     * @param nom nouveau nom du produit
     * @param categorie nouvelle catégorie du produit
     * @param quantite nouvelle quantité disponible
     * @param unite nouvelle unité de mesure
     * @param prix nouveau prix unitaire
     * @return true si le produit existe et la mise à jour a été faite, false sinon
     */
    public boolean updateProduit(int id, String nom, String categorie, double quantite, String unite, double prix);

    /**
     * Méthode permettant de mettre à jour la quantité disponible d'un produit
     * @param id identifiant du produit
     * @param nouvelleQuantite nouvelle quantité disponible
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateQuantite(int id, double nouvelleQuantite);

    /**
     * Méthode permettant de supprimer un produit
     * @param id identifiant du produit à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteProduit(int id);
}