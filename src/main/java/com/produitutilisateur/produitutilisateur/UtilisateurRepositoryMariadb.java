package com.produitutilisateur.produitutilisateur;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;

/**
 * Classe permettant d'accéder aux utilisateurs stockés dans une base de données Mariadb
 */
public class UtilisateurRepositoryMariadb implements UtilisateurRepositoryInterface, Closeable {

    protected Connection dbConnection;

    public UtilisateurRepositoryMariadb(String infoConnection, String user, String pwd)
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
    public Utilisateur getUtilisateur(String id) {
        Utilisateur selectedUtilisateur = null;
        String query = "SELECT * FROM Utilisateur WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, id);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                String nom = result.getString("nom");
                String mdp = result.getString("mdp");
                String mail = result.getString("mail");

                selectedUtilisateur = new Utilisateur(id, nom, mdp, mail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedUtilisateur;
    }

    @Override
    public Utilisateur getUtilisateurByMail(String mail) {
        Utilisateur selectedUtilisateur = null;
        String query = "SELECT * FROM Utilisateur WHERE mail=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, mail);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                String id = result.getString("id");
                String nom = result.getString("nom");
                String mdp = result.getString("mdp");

                selectedUtilisateur = new Utilisateur(id, nom, mdp, mail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return selectedUtilisateur;
    }

    @Override
    public ArrayList<Utilisateur> getAllUtilisateurs() {
        ArrayList<Utilisateur> listUtilisateurs = new ArrayList<>();
        String query = "SELECT * FROM Utilisateur";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                String id = result.getString("id");
                String nom = result.getString("nom");
                String mdp = result.getString("mdp");
                String mail = result.getString("mail");

                Utilisateur currentUtilisateur = new Utilisateur(id, nom, mdp, mail);
                listUtilisateurs.add(currentUtilisateur);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listUtilisateurs;
    }

    @Override
    public boolean createUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null || utilisateur.getNom() == null || utilisateur.getNom().isEmpty() ||
                utilisateur.getMail() == null || utilisateur.getMail().isEmpty()) {
            return false;
        }

        String query = "INSERT INTO Utilisateur(id, nom, mdp, mail) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, utilisateur.getId());
            ps.setString(2, utilisateur.getNom());
            ps.setString(3, utilisateur.getMdp());
            ps.setString(4, utilisateur.getMail());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            return false;
        }
    }

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

    @Override
    public boolean updateMotDePasse(String id, String nouveauMdp) {
        String query = "UPDATE Utilisateur SET mdp=? WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, nouveauMdp);
            ps.setString(2, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Override
    public boolean authenticate(String mail, String mdp) {
        String query = "SELECT COUNT(*) FROM Utilisateur WHERE mail=? AND mdp=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, mail);
            ps.setString(2, mdp);

            try (ResultSet result = ps.executeQuery()) {
                return result.next() && result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countUtilisateursWithId(String id) throws SQLException {
        String query = "SELECT COUNT(*) FROM Utilisateur WHERE id = ?";
        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}