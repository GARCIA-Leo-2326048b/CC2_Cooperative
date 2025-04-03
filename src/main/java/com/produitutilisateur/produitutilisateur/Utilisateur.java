package com.produitutilisateur.produitutilisateur;

import java.util.Objects;

public class Utilisateur {
    protected String id;
    protected String nom;
    protected String mdp;
    protected String mail;

    public Utilisateur() {
    }

    public Utilisateur(String id, String nom, String mdp, String mail) {
        setId(id);
        setNom(nom);
        setMdp(mdp);
        setMail(mail);
    }

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getMdp() { return mdp; }
    public String getMail() { return mail; }

    // Setters avec validation
    public void setId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("L'ID ne peut pas être vide");
        }
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom; // Pas de validation spécifique pour le nom
    }

    public void setMdp(String mdp) {
        if (mdp == null || mdp.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        this.mdp = mdp;
    }

    public void setMail(String mail) {
        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!mail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("L'email doit être valide");
        }
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(mail, that.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, mail);
    }

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