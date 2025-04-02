import com.produitutilisateur.produitutilisateur.Produit;
import com.produitutilisateur.produitutilisateur.ProduitRepositoryMariadb;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ProduitRepositoryMariadbTest {

    private static ProduitRepositoryMariadb repository;
    private static Connection testConnection;
    private static final String TEST_DB_URL = "jdbc:mariadb://mysql-garcialeo0909.alwaysdata.net/garcialeo0909_produitutilisateur_bd";
    private static final String TEST_DB_USER = "407191_produser";
    private static final String TEST_DB_PASS = "produitutilisateur_mdp";

    @BeforeAll
    static void setupAll() throws Exception {
        // Créer la base de test et table
        testConnection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASS);
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Produit (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "nom VARCHAR(255) NOT NULL," +
                    "categorie VARCHAR(100) NOT NULL," +
                    "quantite DECIMAL(10,2) NOT NULL," +
                    "unite VARCHAR(20) NOT NULL," +
                    "prix DECIMAL(10,2) NOT NULL)");
        }
    }

    @BeforeEach
    void setup() throws Exception {
        // Nettoyer et initialiser la table avant chaque test
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("DELETE FROM Produit");
            stmt.execute("INSERT INTO Produit (nom, categorie, quantite, unite, prix) VALUES " +
                    "('Tomates', 'Légumes', 10.0, 'kilo', 2.99)," +
                    "('Œufs', 'Volaille', 12.0, 'douzaine', 3.50)");
        }
        repository = new ProduitRepositoryMariadb(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASS);
    }

    @AfterEach
    void tearDown() throws Exception {
        repository.close();
    }

    @Test
    @DisplayName("Test createProduit - Création réussie")
    void testCreateProduit_Success() {
        Produit newProduit = new Produit(0, "Pommes", "Fruits", 5.0, "kilo", 1.99);
        boolean result = repository.createProduit(newProduit);

        assertTrue(result);
        assertTrue(newProduit.getId() > 0); // Vérifie que l'ID a été attribué

        Produit created = repository.getProduit(newProduit.getId());
        assertEquals("Pommes", created.getNom());
    }

    @Test
    @DisplayName("Test createProduit - Données invalides")
    void testCreateProduit_InvalidData() {
        Produit invalidProduit = new Produit(0, null, "Fruits", 5.0, "kilo", 1.99);
        boolean result = repository.createProduit(invalidProduit);

        assertFalse(result);
    }

    @Test
    @DisplayName("Test deleteProduit - Suppression réussie")
    void testDeleteProduit_Success() throws SQLException {
        // Arrange
        Produit testProduit = new Produit(0, "ProduitTest", "Test", 1.0, "unité", 1.0);
        repository.createProduit(testProduit);
        int idToDelete = testProduit.getId();

        // Act
        boolean result = repository.deleteProduit(idToDelete);

        // Assert
        assertTrue(result);
        assertEquals(0, repository.countProduitsWithId(idToDelete));
    }

    @Test
    @DisplayName("Test deleteProduit - ID inexistant")
    void testDeleteProduit_NonExistingId() {
        boolean result = repository.deleteProduit(999);

        assertFalse(result);
    }

    @Test
    @DisplayName("Test getAllProduits après création")
    void testGetAllAfterCreate() {
        int initialCount = repository.getAllProduits().size();

        Produit newProduit = new Produit(0, "Carottes", "Légumes", 8.0, "kilo", 1.49);
        repository.createProduit(newProduit);

        ArrayList<Produit> produits = repository.getAllProduits();
        assertEquals(initialCount + 1, produits.size());
    }

    @Test
    @DisplayName("Test intégré CRUD complet")
    void testFullCrudCycle() {
        // Create
        Produit produit = new Produit(0, "Bananes", "Fruits", 15.0, "kilo", 2.99);
        assertTrue(repository.createProduit(produit));
        int newId = produit.getId();

        // Read
        Produit created = repository.getProduit(newId);
        assertEquals("Bananes", created.getNom());

        // Update
        assertTrue(repository.updateProduit(newId, "Bananes bio", "Fruits", 10.0, "kilo", 3.49));
        Produit updated = repository.getProduit(newId);
        assertEquals("Bananes bio", updated.getNom());
        assertEquals(3.49, updated.getPrix(), 0.001);

        // Delete
        assertTrue(repository.deleteProduit(newId));
        assertNull(repository.getProduit(newId));
    }
}