import com.produitutilisateur.produitutilisateur.Produit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProduitTest {

    @Test
    void testConstructorAndGetters() {
        Produit p = new Produit(1, "Tomates", "Légumes", 10.5, "kilo", 3.99);

        assertEquals(1, p.getId());
        assertEquals("Tomates", p.getNom());
        assertEquals("Légumes", p.getCategorie());
        assertEquals(10.5, p.getQuantite(), 0.001);
        assertEquals("kilo", p.getUnite());
        assertEquals(3.99, p.getPrix(), 0.001);
    }

    @Test
    void testSetters() {
        Produit p = new Produit();

        p.setId(2);
        p.setNom("Carottes");
        p.setCategorie("Légumes");
        p.setQuantite(5.0);
        p.setUnite("kilo");
        p.setPrix(2.49);

        assertEquals(2, p.getId());
        assertEquals("Carottes", p.getNom());
        assertEquals(5.0, p.getQuantite(), 0.001);
    }

    @Test
    void testQuantiteNegative() {
        Produit p = new Produit();
        assertThrows(IllegalArgumentException.class, () -> {
            p.setQuantite(-1.0);
        });
    }

    @Test
    void testPrixZero() {
        Produit p = new Produit();
        assertThrows(IllegalArgumentException.class, () -> {
            p.setPrix(0.0);
        });
    }

    @Test
    void testToString() {
        Produit p = new Produit(3, "Œufs", "Volaille", 12, "douzaine", 4.5);
        String expected = "Produit{id=3, nom='Œufs', categorie='Volaille', quantite=12.0, unite='douzaine', prix=4.5}";
        assertEquals(expected, p.toString());
    }
}