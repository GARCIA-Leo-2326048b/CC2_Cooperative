package com.produitutilisateur.produitutilisateur;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.ArrayList;

/**
 * Service gérant les opérations métier sur les produits
 */
public class ProduitService {

    /**
     * Répository pour l'accès aux données des produits
     */
    protected ProduitRepositoryInterface produitRepo;

    /**
     * Constructeur avec injection de dépendance
     * @param produitRepo le repository à utiliser
     */
    public ProduitService(ProduitRepositoryInterface produitRepo) {
        this.produitRepo = produitRepo;
    }

    /**
     * Récupère tous les produits au format JSON
     * @return String contenant le JSON des produits
     */
    public String getAllProduitsJSON() {
        ArrayList<Produit> allProduits = produitRepo.getAllProduits();
        return convertToJson(allProduits);
    }

    /**
     * Récupère les produits d'une catégorie spécifique au format JSON
     * @param categorie la catégorie recherchée
     * @return String contenant le JSON des produits
     */
    public String getProduitsByCategorieJSON(String categorie) {
        ArrayList<Produit> produits = produitRepo.getProduitsByCategorie(categorie);
        return convertToJson(produits);
    }

    /**
     * Récupère un produit spécifique au format JSON
     * @param id l'identifiant du produit
     * @return String contenant le JSON du produit ou null si non trouvé
     */
    public String getProduitJSON(int id) {
        Produit produit = produitRepo.getProduit(id);
        return produit != null ? convertToJson(produit) : null;
    }

    /**
     * Met à jour un produit existant
     * @param id l'identifiant du produit
     * @param produit le produit avec les nouvelles données
     * @return boolean indiquant si la mise à jour a réussi
     */
    public boolean updateProduit(int id, Produit produit) {
        return produitRepo.updateProduit(
                id,
                produit.getNom(),
                produit.getCategorie(),
                produit.getQuantite(),
                produit.getUnite(),
                produit.getPrix()
        );
    }

    /**
     * Met à jour la quantité disponible d'un produit
     * @param id l'identifiant du produit
     * @param nouvelleQuantite la nouvelle quantité
     * @return boolean indiquant si la mise à jour a réussi
     */
    public boolean updateQuantite(int id, double nouvelleQuantite) {
        return produitRepo.updateQuantite(id, nouvelleQuantite);
    }

    /**
     * Méthode utilitaire pour la conversion en JSON
     * @param object l'objet à convertir
     * @return String le JSON résultant
     */
    private String convertToJson(Object object) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(object);
        } catch (Exception e) {
            System.err.println("Erreur de conversion JSON : " + e.getMessage());
            return null;
        }
    }
}