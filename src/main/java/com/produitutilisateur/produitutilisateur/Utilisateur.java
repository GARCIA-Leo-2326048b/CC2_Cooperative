package com.produitutilisateur.produitutilisateur;

/**
 * Classe représentant un utilisateur
 */
public class Utilisateur {

    /**
     * Identifiant unique de l'utilisateur
     */
    protected int id;

    /**
     * Nom de l'utilisateur
     */
    protected String nom;

    /**
     * Mot de passe de l'utilisateur (devrait être stocké de manière sécurisée)
     */
    protected String mdp;

    /**
     * Email de l'utilisateur
     */
    protected String mail;

    /**
     * Constructeur par défaut
     */
    public Utilisateur() {
    }

    /**
     * Constructeur d'utilisateur
     * @param id identifiant unique de l'utilisateur
     * @param nom nom de l'utilisateur
     * @param mdp mot de passe de l'utilisateur
     * @param mail email de l'utilisateur
     */
    public Utilisateur(int id, String nom, String mdp, String mail) {
        this.id = id;
        this.nom = nom;
        this.mdp = mdp;
        this.mail = mail;
    }

    /**
     * Méthode permettant d'accéder à l'identifiant de l'utilisateur
     * @return un entier avec l'identifiant de l'utilisateur
     */
    public int getId() {
        return id;
    }

    /**
     * Méthode permettant d'accéder au nom de l'utilisateur
     * @return une chaîne de caractères avec le nom de l'utilisateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Méthode permettant d'accéder au mot de passe de l'utilisateur
     * @return une chaîne de caractères avec le mot de passe
     */
    public String getMdp() {
        return mdp;
    }

    /**
     * Méthode permettant d'accéder à l'email de l'utilisateur
     * @return une chaîne de caractères avec l'email
     */
    public String getMail() {
        return mail;
    }

    /**
     * Méthode permettant de modifier l'identifiant de l'utilisateur
     * @param id un entier avec le nouvel identifiant
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Méthode permettant de modifier le nom de l'utilisateur
     * @param nom une chaîne de caractères avec le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Méthode permettant de modifier le mot de passe de l'utilisateur
     * @param mdp une chaîne de caractères avec le nouveau mot de passe
     */
    public void setMdp(String mdp) {
        if (mdp == null || mdp.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        if (mdp.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères");
        }
        this.mdp = mdp;
    }

    /**
     * Méthode permettant de modifier l'email de l'utilisateur
     * @param mail une chaîne de caractères avec le nouvel email
     */
    public void setMail(String mail) {
        if (mail == null || !mail.contains("@")) {
            throw new IllegalArgumentException("L'email doit être valide");
        }
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", mdp='[PROTECTED]'" + // Ne pas afficher le mot de passe réel
                ", mail='" + mail + '\'' +
                '}';
    }
}