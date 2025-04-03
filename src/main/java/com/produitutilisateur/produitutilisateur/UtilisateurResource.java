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

/**
 * Ressource JAX-RS pour la gestion des utilisateurs.
 */
@Path("/utilisateurs")
@ApplicationScoped
public class UtilisateurResource {

    private UtilisateurService service;

    /**
     * Constructeur par défaut requis par CDI.
     */
    public UtilisateurResource() {
        // Constructeur requis pour CDI
    }

    /**
     * Constructeur avec injection de dépendances.
     * @param utilisateurRepo le repository injecté pour gérer les utilisateurs.
     */
    @Inject
    public UtilisateurResource(UtilisateurRepositoryInterface utilisateurRepo) {
        this.service = new UtilisateurService(utilisateurRepo);
    }

    /**
     * Récupère la liste de tous les utilisateurs.
     * @return une réponse JSON contenant la liste des utilisateurs sans mot de passe.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUtilisateurs() {
        try {
            if (service == null || service.utilisateurRepo == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"Repository des utilisateurs non initialisé\"}")
                        .build();
            }

            ArrayList<Utilisateur> allUtilisateurs = service.utilisateurRepo.getAllUtilisateurs();

            if (allUtilisateurs == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\":\"La liste des utilisateurs est null\"}")
                        .build();
            }

            List<Map<String, Object>> utilisateursSecurises = new ArrayList<>();
            for (Utilisateur utilisateur : allUtilisateurs) {
                Map<String, Object> utilisateurSecurise = new HashMap<>();
                utilisateurSecurise.put("id", utilisateur.getId());
                utilisateurSecurise.put("nom", utilisateur.getNom());
                utilisateursSecurises.add(utilisateurSecurise);
            }

            return Response.ok(utilisateursSecurises).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Erreur lors de la récupération des utilisateurs: "
                            + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Récupère un utilisateur par son identifiant.
     * @param id L'identifiant de l'utilisateur.
     * @return une réponse JSON contenant l'utilisateur (sans son mot de passe) ou une erreur si non trouvé.
     */
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
