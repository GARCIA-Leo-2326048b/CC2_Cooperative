package com.produitutilisateur.produitutilisateur;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/utilisateurs")
@ApplicationScoped
public class UtilisateurResource {

    private UtilisateurService service;

    /**
     * Constructeur par défaut requis par CDI
     */
    public UtilisateurResource() {
        // Constructeur requis pour CDI
    }

    /**
     * Constructeur avec injection de dépendances
     * @param utilisateurRepo le repository injecté
     */
    @Inject
    public UtilisateurResource(UtilisateurRepositoryInterface utilisateurRepo) {
        this.service = new UtilisateurService(utilisateurRepo);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUtilisateurs() {
        try {
            // 1. Vérification que le repository est bien initialisé
            if (service == null || service.utilisateurRepo == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Repository des utilisateurs non initialisé\"}")
                        .build();
            }

            // 2. Récupération des utilisateurs
            ArrayList<Utilisateur> allUtilisateurs = service.utilisateurRepo.getAllUtilisateurs();

            // 3. Vérification que la liste n'est pas null
            if (allUtilisateurs == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"La liste des utilisateurs est null\"}")
                        .build();
            }

            // 4. Construction de la réponse
            List<Map<String, Object>> utilisateursSecurises = new ArrayList<>();
            for (Utilisateur utilisateur : allUtilisateurs) {
                Map<String, Object> utilisateurSecurise = new HashMap<>();
                utilisateurSecurise.put("id", utilisateur.getId());
                utilisateurSecurise.put("nom", utilisateur.getNom());
                // Ajoutez d'autres champs si nécessaire (sauf mot de passe)
                utilisateursSecurises.add(utilisateurSecurise);
            }

            return Response.ok(utilisateursSecurises).build();

        } catch (Exception e) {
            e.printStackTrace(); // Log l'erreur complète
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Erreur lors de la récupération des utilisateurs: "
                            + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUtilisateur(@PathParam("id") String id) {
        try {
            Utilisateur utilisateur = service.utilisateurRepo.getUtilisateur(id);
            if (utilisateur != null) {
                Map<String, Object> utilisateurSecurise = new HashMap<>();
                utilisateurSecurise.put("id", utilisateur.getId());
                utilisateurSecurise.put("nom", utilisateur.getNom());
                return Response.ok(utilisateurSecurise).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Utilisateur non trouvé\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Erreur serveur\"}")
                    .build();
        }
    }
}