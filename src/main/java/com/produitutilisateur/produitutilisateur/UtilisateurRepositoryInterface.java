package com.produitutilisateur.produitutilisateur;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface d'accès aux données des utilisateurs
 */
public interface UtilisateurRepositoryInterface {

    /**
     * Méthode fermant le dépôt où sont stockées les informations sur les utilisateurs
     */
    public void close();

    /**
     * Méthode retournant l'utilisateur dont l'identifiant est passé en paramètre
     * @param id identifiant de l'utilisateur recherché (String)
     * @return un objet Utilisateur représentant l'utilisateur recherché
     */
    public Utilisateur getUtilisateur(String id);

    /**
     * Méthode retournant l'utilisateur dont l'email est passé en paramètre
     * @param mail email de l'utilisateur recherché
     * @return un objet Utilisateur représentant l'utilisateur recherché
     */
    public Utilisateur getUtilisateurByMail(String mail);

    /**
     * Méthode retournant la liste de tous les utilisateurs
     * @return une liste d'objets Utilisateur
     */
    public ArrayList<Utilisateur> getAllUtilisateurs();

    /**
     * Méthode permettant de créer un nouvel utilisateur
     * @param utilisateur l'utilisateur à créer
     * @return true si la création a réussi, false sinon
     */
    public boolean createUtilisateur(Utilisateur utilisateur);

    /**
     * Méthode permettant de mettre à jour un utilisateur enregistré
     * @param id identifiant de l'utilisateur à mettre à jour (String)
     * @param nom nouveau nom de l'utilisateur
     * @param mdp nouveau mot de passe haché
     * @param mail nouvel email
     * @return true si l'utilisateur existe et la mise à jour a été faite, false sinon
     */
    public boolean updateUtilisateur(String id, String nom, String mdp, String mail);

    /**
     * Méthode permettant de mettre à jour le mot de passe d'un utilisateur
     * @param id identifiant de l'utilisateur (String)
     * @param nouveauMdp nouveau mot de passe haché
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateMotDePasse(String id, String nouveauMdp);

    /**
     * Méthode permettant de supprimer un utilisateur
     * @param id identifiant de l'utilisateur à supprimer (String)
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteUtilisateur(String id);

    /**
     * Méthode permettant de vérifier les credentials d'un utilisateur
     * @param mail email de l'utilisateur
     * @param mdp mot de passe haché
     * @return true si les credentials sont valides, false sinon
     */
    public boolean authenticate(String mail, String mdp);

    /**
     * Méthode permettant de compter le nombre d'utilisateurs avec un identifiant donné
     * @param id identifiant à rechercher (String)
     * @return nombre d'utilisateurs avec cet identifiant
     * @throws SQLException en cas d'erreur SQL
     */
    public int countUtilisateursWithId(String id) throws SQLException;
}