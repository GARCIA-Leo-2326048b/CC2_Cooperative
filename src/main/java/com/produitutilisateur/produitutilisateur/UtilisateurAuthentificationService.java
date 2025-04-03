package com.produitutilisateur.produitutilisateur;

/**
 * Classe représentant le cas d'utilisation "authentifier un utilisateur"
 */
public class UtilisateurAuthentificationService {

    /**
     * Objet permettant d'accéder au dépôt où sont stockées les informations sur les utilisateurs
     */
    protected UtilisateurRepositoryInterface userRepo ;

    /**
     * Constructeur permettant d'injecter l'accès aux données
     * @param userRepo objet implémentant l'interface d'accès aux données
     */
    public UtilisateurAuthentificationService(UtilisateurRepositoryInterface userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Méthode d'authentifier un utilisateur
     * @param id identifiant de l'utilisateur
     * @param pwd mot de passe de l'utilisateur
     * @return true si l'utilisateur a été authentifié, false sinon
     */
    public boolean isValidUser( String id, String pwd){

        Utilisateur currentUser = userRepo.getUtilisateur( id );

        // si l'utilisateur n'a pas été trouvé
        if( currentUser == null )
            return false;

        // si le mot de passe n'est pas correcte
        if( ! currentUser.mdp.equals(pwd) )
            return false;

        return true;
    }
}
