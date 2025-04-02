package com.produitutilisateur.produitutilisateur;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe permettant d'accéder aux produits stockés dans une base de données Mariadb
 */
public class ProduitRepositoryMariadb implements ProduitRepositoryInterface, Closeable {

    /**
     * Accès à la base de données (session)
     */
    protected Connection dbConnection;

    /**
     * Constructeur de la classe
     * @param infoConnection chaîne de caractères avec les informations de connexion
     * @param user chaîne de caractères contenant l'identifiant de connexion
     * @param pwd chaîne de caractères contenant le mot de passe
     */
    public ProduitRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close() {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

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
}