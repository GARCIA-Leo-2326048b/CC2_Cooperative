import com.produitutilisateur.produitutilisateur.Produit;
import com.produitutilisateur.produitutilisateur.ProduitRepositoryInterface;
import com.produitutilisateur.produitutilisateur.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ProduitServiceManualTest {

    private ProduitService produitService;
    private ProduitRepositoryTestImpl testRepository;

    // Implémentation de test manuelle
    static class ProduitRepositoryTestImpl implements ProduitRepositoryInterface {
        private final ArrayList<Produit> produits = new ArrayList<>();

        public ProduitRepositoryTestImpl() {
            produits.add(new Produit(1, "Tomates", "Légumes", 10.0, "kilo", 2.99));
            produits.add(new Produit(2, "Œufs", "Volaille", 12.0, "douzaine", 3.50));
        }

        @Override public void close() {}
        @Override public Produit getProduit(int id) {
            return produits.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        }
        @Override public ArrayList<Produit> getAllProduits() {
            return new ArrayList<>(produits);
        }
        @Override public ArrayList<Produit> getProduitsByCategorie(String categorie) {
            ArrayList<Produit> result = new ArrayList<>();
            for(Produit p : produits) {
                if(p.getCategorie().equalsIgnoreCase(categorie)) {
                    result.add(p);
                }
            }
            return result;
        }
        @Override public boolean updateProduit(int id, String nom, String categorie,
                                               double quantite, String unite, double prix) {
            Produit p = getProduit(id);
            if(p != null) {
                p.setNom(nom);
                p.setCategorie(categorie);
                p.setQuantite(quantite);
                p.setUnite(unite);
                p.setPrix(prix);
                return true;
            }
            return false;
        }
        @Override public boolean updateQuantite(int id, double nouvelleQuantite) {
            Produit p = getProduit(id);
            if(p != null) {
                p.setQuantite(nouvelleQuantite);
                return true;
            }
            return false;
        }
    }

    @BeforeEach
    void setUp() {
        testRepository = new ProduitRepositoryTestImpl();
        produitService = new ProduitService(testRepository);
    }

    @Test
    void testUpdateProduit() {
        Produit updated = new Produit(1, "Tomates bio", "Légumes", 15.0, "kilo", 3.99);
        boolean success = produitService.updateProduit(1, updated);
        assertTrue(success);
        assertEquals("Tomates bio", testRepository.getProduit(1).getNom());
    }
}