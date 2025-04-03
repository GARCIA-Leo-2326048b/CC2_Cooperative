package com.produitutilisateur.produitutilisateur;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
public class ProduitUtilisateurApplication extends Application {

    // Configuration de la base de données
    private static final String DB_URL = "jdbc:mariadb://mysql-garcialeo0909.alwaysdata.net/garcialeo0909_produitutilisateur_bd";
    private static final String DB_USER = "407191_produser";
    private static final String DB_PASSWORD = "produitutilisateur_mdp";

    /**
     * Méthode pour injecter le repository des produits
     * @return un objet implémentant ProduitRepositoryInterface
     */
    @Produces
    private ProduitRepositoryInterface openProduitDbConnection() {
        try {
            return new ProduitRepositoryMariadb(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données (Produit): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode pour injecter le repository des utilisateurs
     * @return un objet implémentant UtilisateurRepositoryInterface
     */
    @Produces
    private UtilisateurRepositoryInterface openUtilisateurDbConnection() {
        try {
            return new UtilisateurRepositoryMariadb(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données (Utilisateur): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Méthode pour injecter le service d'authentification
     * @param userRepo le repository des utilisateurs injecté automatiquement
     * @return un service d'authentification configuré
     */
    @Produces
    private UtilisateurAuthentificationService createAuthService(UtilisateurRepositoryInterface userRepo) {
        return new UtilisateurAuthentificationService(userRepo);
    }

    /**
     * Méthode pour fermer la connexion du repository des produits
     * @param produitRepo la connexion à fermer
     */
    private void closeProduitDbConnection(@Disposes ProduitRepositoryInterface produitRepo) {
        if (produitRepo != null) {
            produitRepo.close();
        }
    }

    /**
     * Méthode pour fermer la connexion du repository des utilisateurs
     * @param utilisateurRepo la connexion à fermer
     */
    private void closeUtilisateurDbConnection(@Disposes UtilisateurRepositoryInterface utilisateurRepo) {
        if (utilisateurRepo instanceof UtilisateurRepositoryMariadb) {
            try {
                ((UtilisateurRepositoryMariadb) utilisateurRepo).close();
            } catch (Exception e) {
                System.err.println("Erreur lors de la fermeture de la connexion Utilisateur: " + e.getMessage());
            }
        }
    }
}