package com.produitutilisateur.produitutilisateur;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.ArrayList;

/**
 * Service gérant les opérations métier sur les produits
 */
public class ProduitService {

    protected ProduitRepositoryInterface produitRepo;

    public ProduitService(ProduitRepositoryInterface produitRepo) {
        this.produitRepo = produitRepo;
    }

    public String getAllProduitsJSON() {
        ArrayList<Produit> allProduits = produitRepo.getAllProduits();
        return convertToJson(allProduits);
    }

    public String getProduitsByCategorieJSON(String categorie) {
        ArrayList<Produit> produits = produitRepo.getProduitsByCategorie(categorie);
        return convertToJson(produits);
    }

    public String getProduitJSON(int id) {
        Produit produit = produitRepo.getProduit(id);
        return produit != null ? convertToJson(produit) : null;
    }

    /**
     * Crée un nouveau produit
     * @param produit le produit à créer (sans ID)
     * @return le produit créé avec son nouvel ID, ou null si échec
     */
    public Produit createProduit(Produit produit) {
        // Validation des données
        if (produit == null ||
                produit.getNom() == null || produit.getNom().isBlank() ||
                produit.getCategorie() == null || produit.getCategorie().isBlank() ||
                produit.getQuantite() < 0 ||
                produit.getUnite() == null || produit.getUnite().isBlank() ||
                produit.getPrix() <= 0) {
            return null;
        }

        // Création dans le repository
        boolean success = produitRepo.createProduit(produit);
        return success ? produit : null;
    }

    public boolean updateProduit(int id, Produit produit) {
        // Validation que l'ID correspond
        if (produit.getId() != 0 && produit.getId() != id) {
            return false;
        }

        return produitRepo.updateProduit(
                id,
                produit.getNom(),
                produit.getCategorie(),
                produit.getQuantite(),
                produit.getUnite(),
                produit.getPrix()
        );
    }

    public boolean updateQuantite(int id, double nouvelleQuantite) {
        if (nouvelleQuantite < 0) {
            return false;
        }
        return produitRepo.updateQuantite(id, nouvelleQuantite);
    }

    /**
     * Supprime un produit
     * @param id l'identifiant du produit à supprimer
     * @return true si suppression réussie, false sinon
     */
    public boolean deleteProduit(int id) {
        return produitRepo.deleteProduit(id);
    }

    private String convertToJson(Object object) {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(object);
        } catch (Exception e) {
            System.err.println("Erreur de conversion JSON : " + e.getMessage());
            return null;
        }
    }

    /**
     * Génère un nouvel ID pour un produit
     * @return le prochain ID disponible
     */
    public int generateNewId() {
        ArrayList<Produit> produits = produitRepo.getAllProduits();
        return produits.stream()
                .mapToInt(Produit::getId)
                .max()
                .orElse(0) + 1;
    }
}