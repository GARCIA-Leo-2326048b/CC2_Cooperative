import com.produitutilisateur.produitutilisateur.Produit;
import com.produitutilisateur.produitutilisateur.ProduitRepositoryMariadb;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ProduitRepositoryMariadbTest {
    private static ProduitRepositoryMariadb repo;

    @BeforeAll
    static void setup() throws Exception {
        repo = new ProduitRepositoryMariadb("jdbc:mariadb://mysql-garcialeo0909.alwaysdata.net/garcialeo0909_produitutilisateur_bd",
                "407191_produser",
                "produitutilisateur_mdp");
    }

    @AfterAll
    static void cleanup() {
        repo.close();
    }

    @Test
    @DisplayName("Test récupération produit existant")
    void testGetExistingProduit() {
        Produit p = repo.getProduit(1); // Supposant que l'ID 1 existe
        assertNotNull(p);
        assertEquals(1, p.getId());
    }

    @Test
    @DisplayName("Test mise à jour quantité")
    void testUpdateQuantite() {
        double newQty = 30.0;
        assertTrue(repo.updateQuantite(1, newQty));
        assertEquals(newQty, repo.getProduit(1).getQuantite());
    }

    @Test
    @DisplayName("Test filtrage par catégorie")
    void testGetByCategory() {
        var legumes = repo.getProduitsByCategorie("Légumes");
        assertFalse(legumes.isEmpty());
        assertTrue(legumes.stream().allMatch(p -> p.getCategorie().equals("Légumes")));
    }
}