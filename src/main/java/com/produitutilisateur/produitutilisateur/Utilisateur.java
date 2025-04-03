package com.produitutilisateur.produitutilisateur;

import java.util.Objects;

/**
 * Classe représentant un utilisateur du système.
 * Contient les informations de base d'un utilisateur (identifiant, nom, mot de passe et email)
 * avec des mécanismes de validation pour certaines propriétés.
 */
public class Utilisateur {
    /**
     * Identifiant unique de l'utilisateur
     */
    protected String id;

    /**
     * Nom complet de l'utilisateur
     */
    protected String nom;

    /**
     * Mot de passe de l'utilisateur (stocké en clair - à hasher en production)
     */
    protected String mdp;

    /**
     * Adresse email de l'utilisateur
     */
    protected String mail;

    /**
     * Constructeur par défaut créant un utilisateur vide
     */
    public Utilisateur() {
    }

    /**
     * Constructeur complet initialisant toutes les propriétés de l'utilisateur
     * @param id Identifiant de l'utilisateur (ne peut pas être vide)
     * @param nom Nom de l'utilisateur
     * @param mdp Mot de passe de l'utilisateur (ne peut pas être vide)
     * @param mail Email de l'utilisateur (doit être valide)
     * @throws IllegalArgumentException Si une des validations échoue
     */
    public Utilisateur(String id, String nom, String mdp, String mail) {
        setId(id);
        setNom(nom);
        setMdp(mdp);
        setMail(mail);
    }

    // Getters

    /**
     * Retourne l'identifiant de l'utilisateur
     * @return L'identifiant de l'utilisateur
     */
    public String getId() { return id; }

    /**
     * Retourne le nom de l'utilisateur
     * @return Le nom de l'utilisateur
     */
    public String getNom() { return nom; }

    /**
     * Retourne le mot de passe de l'utilisateur
     * @return Le mot de passe (non sécurisé - à hasher en production)
     */
    public String getMdp() { return mdp; }

    /**
     * Retourne l'email de l'utilisateur
     * @return L'email de l'utilisateur
     */
    public String getMail() { return mail; }

    // Setters avec validation

    /**
     * Définit l'identifiant de l'utilisateur
     * @param id Le nouvel identifiant (ne peut pas être vide)
     * @throws IllegalArgumentException Si l'identifiant est vide
     */
    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'ID ne peut pas être vide");
        }
        this.id = id;
    }

    /**
     * Définit le nom de l'utilisateur
     * @param nom Le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom; // Pas de validation spécifique pour le nom
    }

    /**
     * Définit le mot de passe de l'utilisateur
     * @param mdp Le nouveau mot de passe (ne peut pas être vide)
     * @throws IllegalArgumentException Si le mot de passe est vide
     */
    public void setMdp(String mdp) {
        if (mdp == null || mdp.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        this.mdp = mdp;
    }

    /**
     * Définit l'email de l'utilisateur
     * @param mail Le nouvel email (doit être valide)
     * @throws IllegalArgumentException Si l'email est vide ou invalide
     */
    public void setMail(String mail) {
        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!mail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("L'email doit être valide");
        }
        this.mail = mail;
    }

    /**
     * Compare deux utilisateurs pour l'égalité
     * @param o L'objet à comparer
     * @return true si les utilisateurs ont le même id, nom et email, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(mail, that.mail);
    }

    /**
     * Génère un code de hachage pour l'utilisateur
     * @return Le code de hachage basé sur l'id, le nom et l'email
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nom, mail);
    }

    /**
     * Retourne une représentation textuelle de l'utilisateur
     * @return Une chaîne représentant l'utilisateur (le mot de passe est masqué)
     */
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", mdp='[PROTECTED]'" +
                ", mail='" + mail + '\'' +
                '}';
    }
}