package com.produitutilisateur.produitutilisateur;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe permettant d'accéder aux utilisateurs stockés dans une base de données MariaDB.
 * Implémente l'interface {@link UtilisateurRepositoryInterface} et permet la gestion des utilisateurs.
 */
public class UtilisateurRepositoryMariadb implements UtilisateurRepositoryInterface, Closeable {

    protected Connection dbConnection;

    /**
     * Constructeur établissant la connexion à la base de données.
     *
     * @param infoConnection URL de connexion à la base de données.
     * @param user Nom d'utilisateur pour la connexion.
     * @param pwd Mot de passe pour la connexion.
     * @throws SQLException En cas d'erreur lors de la connexion à la base de données.
     * @throws ClassNotFoundException Si le driver JDBC MariaDB n'est pas trouvé.
     */
    public UtilisateurRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    /**
     * Ferme la connexion à la base de données.
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
     * Récupère un utilisateur par son identifiant.
     *
     * @param id Identifiant de l'utilisateur.
     * @return L'utilisateur correspondant, ou null s'il n'existe pas.
     */
    @Override
    public Utilisateur getUtilisateur(String id) {
        Utilisateur selectedUtilisateur = null;
        String query = "SELECT * FROM Utilisateur WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, id);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                selectedUtilisateur = new Utilisateur(
                        result.getString("id"),
                        result.getString("nom"),
                        result.getString("mdp"),
                        result.getString("mail")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedUtilisateur;
    }

    /**
     * Récupère un utilisateur par son adresse e-mail.
     *
     * @param mail Adresse e-mail de l'utilisateur.
     * @return L'utilisateur correspondant, ou null s'il n'existe pas.
     */
    @Override
    public Utilisateur getUtilisateurByMail(String mail) {
        Utilisateur selectedUtilisateur = null;
        String query = "SELECT * FROM Utilisateur WHERE mail=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, mail);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                selectedUtilisateur = new Utilisateur(
                        result.getString("id"),
                        result.getString("nom"),
                        result.getString("mdp"),
                        mail
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedUtilisateur;
    }

    /**
     * Récupère tous les utilisateurs enregistrés.
     *
     * @return Liste des utilisateurs.
     */
    @Override
    public ArrayList<Utilisateur> getAllUtilisateurs() {
        ArrayList<Utilisateur> listUtilisateurs = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                listUtilisateurs.add(new Utilisateur(
                        result.getString("id"),
                        result.getString("nom"),
                        result.getString("mdp"),
                        result.getString("mail")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listUtilisateurs;
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param utilisateur L'utilisateur à ajouter.
     * @return true si l'opération a réussi, false sinon.
     */
    @Override
    public boolean createUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null || utilisateur.getNom().isEmpty() || utilisateur.getMail().isEmpty()) {
            return false;
        }

        String query = "INSERT INTO Utilisateur(id, nom, mdp, mail) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, utilisateur.getId());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getMdp());
            ps.setString(4, utilisateur.getMail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param id Identifiant de l'utilisateur.
     * @param nom Nouveau nom.
     * @param mdp Nouveau mot de passe.
     * @param mail Nouvelle adresse e-mail.
     * @return true si la mise à jour a réussi, false sinon.
     */
    @Override
    public boolean updateUtilisateur(String id, String nom, String mdp, String mail) {
        String query = "UPDATE Utilisateur SET nom=?, mdp=?, mail=? WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, mdp);
            ps.setString(3, mail);
            ps.setString(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime un utilisateur par son ID.
     *
     * @param id Identifiant de l'utilisateur.
     * @return true si la suppression a réussi, false sinon.
     */
    @Override
    public boolean deleteUtilisateur(String id) {
        String query = "DELETE FROM Utilisateur WHERE id = ?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
            return false;
        }
    }
}
