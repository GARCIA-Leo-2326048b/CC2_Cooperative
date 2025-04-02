package com.produitutilisateur.produitutilisateur;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/produits")
@ApplicationScoped
public class ProduitResource {

    private ProduitService service;

    /**
     * Constructeur par défaut requis par JAX-RS
     */
    public ProduitResource() {}

    /**
     * Constructeur avec injection de dépendances
     * @param produitRepo le repository injecté
     */
    @Inject
    public ProduitResource(ProduitRepositoryInterface produitRepo) {
        this.service = new ProduitService(produitRepo);
    }

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

    @GET
    @Path("/categorie/{categorie}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProduitsByCategorie(@PathParam("categorie") String categorie) {
        String produitsJson = service.getProduitsByCategorieJSON(categorie);
        return Response.ok(produitsJson).build();
    }

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
}