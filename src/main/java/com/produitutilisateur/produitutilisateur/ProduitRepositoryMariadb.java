package com.produitutilisateur.produitutilisateur;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;

/**
 * Implémentation d'un repository pour la gestion des produits dans une base de données MariaDB.
 * Cette classe permet d'effectuer les opérations CRUD (Create, Read, Update, Delete) sur les produits.
 * Elle implémente à la fois l'interface ProduitRepositoryInterface et l'interface Closeable pour la gestion des ressources.
 */
public class ProduitRepositoryMariadb implements ProduitRepositoryInterface, Closeable {

    /**
     * Connexion à la base de données MariaDB
     */
    protected Connection dbConnection;

    /**
     * Constructeur établissant la connexion à la base de données MariaDB
     * @param infoConnection URL de connexion à la base de données (ex: "jdbc:mariadb://localhost:3306/nom_bdd")
     * @param user Identifiant de connexion à la base de données
     * @param pwd Mot de passe de connexion à la base de données
     * @throws SQLException En cas d'erreur lors de la connexion à la base de données
     * @throws ClassNotFoundException Si le driver JDBC MariaDB n'est pas trouvé
     */
    public ProduitRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    /**
     * Ferme la connexion à la base de données.
     * Cette méthode est appelée automatiquement lorsque l'objet est utilisé dans un try-with-resources.
     */
    @Override
    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Récupère un produit par son identifiant
     * @param id Identifiant du produit à rechercher
     * @return Le produit correspondant à l'identifiant, ou null si non trouvé
     * @throws RuntimeException En cas d'erreur SQL lors de l'accès à la base de données
     */
    @Override
    public Produit getProduit(int id) {
        Produit selectedProduit = null;
        String query = "SELECT * FROM Produit WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                String nom = result.getString("nom");
                String categorie = result.getString("categorie");
                double quantite = result.getDouble("quantite");
                String unite = result.getString("unite");
                double prix = result.getDouble("prix");

                selectedProduit = new Produit(id, nom, categorie, quantite, unite, prix);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedProduit;
    }

    /**
     * Récupère la liste de tous les produits de la base de données
     * @return ArrayList contenant tous les produits
     * @throws RuntimeException En cas d'erreur SQL lors de l'accès à la base de données
     */
    @Override
    public ArrayList<Produit> getAllProduits() {
        ArrayList<Produit> listProduits = new ArrayList<>();
        String query = "SELECT * FROM Produit";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                String nom = result.getString("nom");
                String categorie = result.getString("categorie");
                double quantite = result.getDouble("quantite");
                String unite = result.getString("unite");
                double prix = result.getDouble("prix");

                Produit currentProduit = new Produit(id, nom, categorie, quantite, unite, prix);
                listProduits.add(currentProduit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listProduits;
    }

    /**
     * Récupère les produits appartenant à une catégorie spécifique
     * @param categorie La catégorie de produits à rechercher
     * @return ArrayList des produits de la catégorie spécifiée
     * @throws RuntimeException En cas d'erreur SQL lors de l'accès à la base de données
     */
    @Override
    public ArrayList<Produit> getProduitsByCategorie(String categorie) {
        ArrayList<Produit> listProduits = new ArrayList<>();
        String query = "SELECT * FROM Produit WHERE categorie=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, categorie);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                int id = result.getInt("id");
                String nom = result.getString("nom");
                double quantite = result.getDouble("quantite");
                String unite = result.getString("unite");
                double prix = result.getDouble("prix");

                Produit currentProduit = new Produit(id, nom, categorie, quantite, unite, prix);
                listProduits.add(currentProduit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listProduits;
    }

    /**
     * Met à jour les informations d'un produit existant
     * @param id Identifiant du produit à mettre à jour
     * @param nom Nouveau nom du produit
     * @param categorie Nouvelle catégorie du produit
     * @param quantite Nouvelle quantité du produit
     * @param unite Nouvelle unité de mesure du produit
     * @param prix Nouveau prix du produit
     * @return true si la mise à jour a réussi, false sinon
     * @throws RuntimeException En cas d'erreur SQL lors de l'accès à la base de données
     */
    @Override
    public boolean updateProduit(int id, String nom, String categorie, double quantite, String unite, double prix) {
        String query = "UPDATE Produit SET nom=?, categorie=?, quantite=?, unite=?, prix=? WHERE id=?";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, categorie);
            ps.setDouble(3, quantite);
            ps.setString(4, unite);
            ps.setDouble(5, prix);
            ps.setInt(6, id);

            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Met à jour uniquement la quantité d'un produit existant
     * @param id Identifiant du produit à mettre à jour
     * @param nouvelleQuantite Nouvelle quantité du produit
     * @return true si la mise à jour a réussi, false sinon
     * @throws RuntimeException En cas d'erreur SQL lors de l'accès à la base de données
     */
    @Override
    public boolean updateQuantite(int id, double nouvelleQuantite) {
        String query = "UPDATE Produit SET quantite=? WHERE id=?";
        int nbRowModified = 0;

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setDouble(1, nouvelleQuantite);
            ps.setInt(2, id);

            nbRowModified = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return (nbRowModified != 0);
    }

    /**
     * Crée un nouveau produit dans la base de données
     * @param produit Le produit à créer (doit avoir un nom non vide)
     * @return true si la création a réussi, false sinon
     */
    @Override
    public boolean createProduit(Produit produit) {
        if (produit == null || produit.getNom() == null || produit.getNom().isEmpty()) {
            return false;
        }

        String query = "INSERT INTO Produit(nom, categorie, quantite, unite, prix) VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement ps = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getCategorie());
            ps.setDouble(3, produit.getQuantite());
            ps.setString(4, produit.getUnite());
            ps.setDouble(5, produit.getPrix());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produit.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du produit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un produit de la base de données
     * @param id Identifiant du produit à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    @Override
    public boolean deleteProduit(int id) {
        String query = "DELETE FROM Produit WHERE id = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du produit: " + e.getMessage());
            return false;
        }
    }

    /**
     * Compte le nombre de produits ayant un identifiant spécifique
     * @param id Identifiant du produit à rechercher
     * @return Le nombre de produits correspondant à l'identifiant
     * @throws SQLException En cas d'erreur SQL lors de l'accès à la base de données
     */
    public int countProduitsWithId(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM Produit WHERE id = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}