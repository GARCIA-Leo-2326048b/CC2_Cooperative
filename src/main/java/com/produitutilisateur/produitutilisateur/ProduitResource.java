package com.produitutilisateur.produitutilisateur;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Ressource RESTful pour la gestion des produits.
 * Fournit des opérations CRUD pour manipuler les produits.
 */
@Path("/produits")
@ApplicationScoped
public class ProduitResource {

    private ProduitService service;

    /**
     * Constructeur par défaut requis par JAX-RS.
     */
    public ProduitResource() {}

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param produitRepo le repository injecté pour gérer les produits.
     */
    @Inject
    public ProduitResource(ProduitRepositoryInterface produitRepo) {
        this.service = new ProduitService(produitRepo);
    }

    /**
     * Récupère tous les produits.
     *
     * @return une réponse contenant la liste des produits au format JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProduits() {
        String produitsJson = service.getAllProduitsJSON();
        if (produitsJson != null) {
            return Response.ok(produitsJson).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Erreur lors de la récupération des produits\"}")
                    .build();
        }
    }

    /**
     * Récupère un produit par son ID.
     *
     * @param id l'identifiant du produit.
     * @return une réponse contenant le produit au format JSON.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduit(@PathParam("id") int id) {
        String produitJson = service.getProduitJSON(id);
        if (produitJson != null) {
            return Response.ok(produitJson).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Produit non trouvé\"}")
                    .build();
        }
    }

    /**
     * Récupère les produits d'une catégorie spécifique.
     *
     * @param categorie la catégorie des produits à rechercher.
     * @return une réponse contenant les produits filtrés par catégorie au format JSON.
     */
    @GET
    @Path("/categorie/{categorie}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduitsByCategorie(@PathParam("categorie") String categorie) {
        String produitsJson = service.getProduitsByCategorieJSON(categorie);
        return Response.ok(produitsJson).build();
    }

    /**
     * Crée un nouveau produit.
     *
     * @param produit l'objet Produit à créer.
     * @return une réponse indiquant le succès ou l'échec de la création.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduit(Produit produit) {
        try {
            Produit createdProduit = service.createProduit(produit);
            if (createdProduit != null) {
                return Response.status(Response.Status.CREATED)
                        .entity(createdProduit)
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Impossible de créer le produit\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Erreur serveur lors de la création: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * Met à jour un produit existant.
     *
     * @param id l'identifiant du produit à mettre à jour.
     * @param produit l'objet Produit avec les nouvelles valeurs.
     * @return une réponse indiquant le succès ou l'échec de la mise à jour.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduit(@PathParam("id") int id, Produit produit) {
        if (service.updateProduit(id, produit)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Échec de la mise à jour du produit\"}")
                    .build();
        }
    }

    /**
     * Met à jour la quantité d'un produit spécifique.
     *
     * @param id l'identifiant du produit.
     * @param quantite la nouvelle quantité sous forme de chaîne.
     * @return une réponse indiquant le succès ou l'échec de la mise à jour.
     */
    @PUT
    @Path("/{id}/quantite")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updateQuantite(@PathParam("id") int id, String quantite) {
        try {
            double nouvelleQuantite = Double.parseDouble(quantite);
            if (service.updateQuantite(id, nouvelleQuantite)) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"La quantité doit être un nombre\"}")
                    .build();
        }
    }

    /**
     * Supprime un produit par son ID.
     *
     * @param id l'identifiant du produit à supprimer.
     * @return une réponse indiquant le succès ou l'échec de la suppression.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduit(@PathParam("id") int id) {
        try {
            if (service.deleteProduit(id)) {
                return Response.ok()
                        .entity("{\"message\":\"Produit supprimé avec succès\"}")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Produit non trouvé ou déjà supprimé\"}")
                        .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Erreur lors de la suppression: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
