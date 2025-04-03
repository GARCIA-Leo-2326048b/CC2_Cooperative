package com.produitutilisateur.produitutilisateur;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Service gérant les opérations métier sur les utilisateurs
 */
public class UtilisateurService {

    protected UtilisateurRepositoryInterface utilisateurRepo;

    public UtilisateurService(UtilisateurRepositoryInterface utilisateurRepo) {
        this.utilisateurRepo = utilisateurRepo;
    }

    /**
     * Méthode retournant les informations (sans mail et mot de passe) sur les utilisateurs au format JSON
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getAllUtilisateursJSON() {
        ArrayList<Utilisateur> allUtilisateurs = utilisateurRepo.getAllUtilisateurs();

        // On supprime les informations sensibles
        for (Utilisateur currentUtilisateur : allUtilisateurs) {
            currentUtilisateur.setMail("");
            currentUtilisateur.setMdp("");
        }

        // Création du JSON
        String result = null;
        try (Jsonb jsonb = JsonbBuilder.create()) {
            result = jsonb.toJson(allUtilisateurs);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    /**
     * Méthode retournant au format JSON les informations sur un utilisateur recherché
     * @param id l'identifiant de l'utilisateur recherché
     * @return une chaîne de caractère contenant les informations au format JSON
     */
    public String getUtilisateurJSON(String id) {
        String result = null;
        Utilisateur utilisateur = utilisateurRepo.getUtilisateur(id);

        if (utilisateur != null) {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                result = jsonb.toJson(utilisateur);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    /**
     * Crée un nouvel utilisateur
     * @param utilisateur l'utilisateur à créer (doit contenir un ID)
     * @return l'utilisateur créé avec son ID, ou null si échec
     */
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        // Validation des données
        if (utilisateur == null ||
                utilisateur.getId() == null || utilisateur.getId().isBlank() ||
                utilisateur.getNom() == null || utilisateur.getNom().isBlank() ||
                utilisateur.getMdp() == null || utilisateur.getMdp().isBlank() ||
                utilisateur.getMail() == null || utilisateur.getMail().isBlank() || !utilisateur.getMail().contains("@")) {
            return null;
        }

        // Vérification que l'email n'existe pas déjà
        if (utilisateurRepo.getUtilisateurByMail(utilisateur.getMail()) != null) {
            return null;
        }

        // Vérification que l'ID n'existe pas déjà
        if (utilisateurRepo.getUtilisateur(utilisateur.getId()) != null) {
            return null;
        }

        // Création dans le repository
        boolean success = utilisateurRepo.createUtilisateur(utilisateur);
        return success ? utilisateur : null;
    }

    public boolean updateUtilisateur(String id, Utilisateur utilisateur) {
        // Validation que l'ID correspond
        if (utilisateur.getId() != null && !utilisateur.getId().equals(id)) {
            return false;
        }

        return utilisateurRepo.updateUtilisateur(
                id,
                utilisateur.getNom(),
                utilisateur.getMdp(),
                utilisateur.getMail()
        );
    }

    /**
     * Supprime un utilisateur
     * @param id l'identifiant de l'utilisateur à supprimer
     * @return true si suppression réussie, false sinon
     */
    public boolean deleteUtilisateur(String id) {
        return utilisateurRepo.deleteUtilisateur(id);
    }

    /**
     * Génère un nouvel ID UUID pour un utilisateur
     * @return un nouvel ID unique sous forme de String
     */
    public String generateNewId() {
        return UUID.randomUUID().toString();
    }
}