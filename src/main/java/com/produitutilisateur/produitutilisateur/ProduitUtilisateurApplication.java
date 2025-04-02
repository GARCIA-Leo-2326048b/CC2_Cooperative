package com.produitutilisateur.produitutilisateur;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
public class ProduitUtilisateurApplication extends Application {

    /**
     * Méthode appelée par l'API CDI pour injecter la connexion à la base de données
     * @return un objet implémentant l'interface ProduitRepositoryInterface
     */
    @Produces
    private ProduitRepositoryInterface openDbConnection() {
        ProduitRepositoryMariadb db = null;

        try {
            String dbUrl = "jdbc:mariadb://mysql-garcialeo0909.alwaysdata.net/garcialeo0909_produitutilisateur_bd";
            String dbUser = "407191_produser";
            String dbPassword = "produitutilisateur_mdp";

            db = new ProduitRepositoryMariadb(dbUrl, dbUser, dbPassword);
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
        }
        return db;
    }

    /**
     * Méthode pour fermer la connexion à la base de données
     * @param produitRepo la connexion à fermer
     */
    private void closeDbConnection(@Disposes ProduitRepositoryInterface produitRepo) {
        if (produitRepo != null) {
            produitRepo.close();
        }
    }
}