import com.produitutilisateur.produitutilisateur.Utilisateur;
import com.produitutilisateur.produitutilisateur.UtilisateurRepositoryMariadb;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UtilisateurRepositoryMariadbTest {

    private static UtilisateurRepositoryMariadb repository;
    private static Connection testConnection;
    private static final String TEST_DB_URL = "jdbc:mariadb://mysql-garcialeo0909.alwaysdata.net/garcialeo0909_produitutilisateur_bd";
    private static final String TEST_DB_USER = "407191_produser";
    private static final String TEST_DB_PASS = "produitutilisateur_mdp";

    @BeforeAll
    static void setupAll() throws Exception {
        // Créer la base de test et table avec ID en VARCHAR
        testConnection = DriverManager.getConnection(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASS);
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Utilisateur (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "nom VARCHAR(255) NOT NULL," +
                    "mdp VARCHAR(255) NOT NULL," +
                    "mail VARCHAR(255) NOT NULL UNIQUE)");
        }
    }

    @BeforeEach
    void setup() throws Exception {
        // Nettoyer et initialiser la table avant chaque test
        try (Statement stmt = testConnection.createStatement()) {
            stmt.execute("DELETE FROM Utilisateur");
            stmt.execute("INSERT INTO Utilisateur (id, nom, mdp, mail) VALUES " +
                    "('user-1', 'Jean Dupont', 'mdp123', 'jean.dupont@example.com')," +
                    "('user-2', 'Marie Martin', 'mdp456', 'marie.martin@example.com')");
        }
        repository = new UtilisateurRepositoryMariadb(TEST_DB_URL, TEST_DB_USER, TEST_DB_PASS);
    }

    @AfterEach
    void tearDown() throws Exception {
        repository.close();
    }

    @Test
    @DisplayName("Test createUtilisateur - Création réussie")
    void testCreateUtilisateur_Success() {
        String newId = UUID.randomUUID().toString();
        Utilisateur newUtilisateur = new Utilisateur(newId, "Paul Durand", "mdp789", "paul.durand@example.com");
        boolean result = repository.createUtilisateur(newUtilisateur);

        assertTrue(result);

        Utilisateur created = repository.getUtilisateur(newId);
        assertEquals("Paul Durand", created.getNom());
        assertEquals(newId, created.getId());
    }

    @Test
    @DisplayName("Test createUtilisateur - Données invalides")
    void testCreateUtilisateur_InvalidData() {
        // Tous avec emails valides mais d'autres champs invalides
        Utilisateur invalidUtilisateur1 = new Utilisateur("user-3", null, "mdp", "valide@test.com");
        Utilisateur invalidUtilisateur2 = new Utilisateur("user-4", "Test", "mdp", "valide@test.com");

        assertFalse(repository.createUtilisateur(invalidUtilisateur1));
        // Testez d'autres cas d'invalidité...
    }

    @Test
    @DisplayName("Test deleteUtilisateur - Suppression réussie")
    void testDeleteUtilisateur_Success() throws SQLException {
        // Arrange
        String testId = "user-del";
        Utilisateur testUtilisateur = new Utilisateur(testId, "Test User", "testmdp", "test@example.com");
        repository.createUtilisateur(testUtilisateur);

        // Act
        boolean result = repository.deleteUtilisateur(testId);

        // Assert
        assertTrue(result);
        assertEquals(0, repository.countUtilisateursWithId(testId));
    }

    @Test
    @DisplayName("Test deleteUtilisateur - ID inexistant")
    void testDeleteUtilisateur_NonExistingId() {
        boolean result = repository.deleteUtilisateur("non-existent-id");
        assertFalse(result);
    }

    @Test
    @DisplayName("Test getAllUtilisateurs après création")
    void testGetAllAfterCreate() {
        int initialCount = repository.getAllUtilisateurs().size();

        String newId = UUID.randomUUID().toString();
        Utilisateur newUtilisateur = new Utilisateur(newId, "Nouvel Utilisateur", "newmdp", "new@example.com");
        repository.createUtilisateur(newUtilisateur);

        ArrayList<Utilisateur> utilisateurs = repository.getAllUtilisateurs();
        assertEquals(initialCount + 1, utilisateurs.size());
    }

    @Test
    @DisplayName("Test getUtilisateurByMail - Utilisateur existant")
    void testGetUtilisateurByMail_Existing() {
        Utilisateur utilisateur = repository.getUtilisateurByMail("jean.dupont@example.com");
        assertNotNull(utilisateur);
        assertEquals("Jean Dupont", utilisateur.getNom());
        assertEquals("user-1", utilisateur.getId());
    }

    @Test
    @DisplayName("Test getUtilisateurByMail - Utilisateur inexistant")
    void testGetUtilisateurByMail_NonExisting() {
        Utilisateur utilisateur = repository.getUtilisateurByMail("inexistant@example.com");
        assertNull(utilisateur);
    }

    @Test
    @DisplayName("Test updateUtilisateur - Mise à jour réussie")
    void testUpdateUtilisateur_Success() {
        // Trouver un utilisateur existant
        Utilisateur existing = repository.getUtilisateurByMail("jean.dupont@example.com");
        assertNotNull(existing);

        // Mettre à jour
        boolean result = repository.updateUtilisateur(
                existing.getId(),
                "Jean Dupont Modifié",
                "nouveaumdp",
                "jean.dupont.modifie@example.com"
        );

        assertTrue(result);

        // Vérifier les modifications
        Utilisateur updated = repository.getUtilisateur(existing.getId());
        assertEquals("Jean Dupont Modifié", updated.getNom());
        assertEquals("jean.dupont.modifie@example.com", updated.getMail());
    }

    @Test
    @DisplayName("Test updateMotDePasse - Mise à jour réussie")
    void testUpdateMotDePasse_Success() {
        // Trouver un utilisateur existant
        Utilisateur existing = repository.getUtilisateurByMail("marie.martin@example.com");
        assertNotNull(existing);

        // Mettre à jour le mot de passe
        boolean result = repository.updateMotDePasse(existing.getId(), "nouveaumdp");
        assertTrue(result);

        // Vérifier l'authentification avec le nouveau mot de passe
        assertTrue(repository.authenticate("marie.martin@example.com", "nouveaumdp"));
    }

    @Test
    @DisplayName("Test authenticate - Authentification réussie")
    void testAuthenticate_Success() {
        assertTrue(repository.authenticate("jean.dupont@example.com", "mdp123"));
    }

    @Test
    @DisplayName("Test authenticate - Échec d'authentification")
    void testAuthenticate_Failure() {
        assertFalse(repository.authenticate("jean.dupont@example.com", "mauvais_mdp"));
        assertFalse(repository.authenticate("inexistant@example.com", "mdp123"));
    }

    @Test
    @DisplayName("Test intégré CRUD complet")
    void testFullCrudCycle() {
        // Create
        String newId = UUID.randomUUID().toString();
        Utilisateur utilisateur = new Utilisateur(newId, "Test CRUD", "mdptest", "test.crud@example.com");
        assertTrue(repository.createUtilisateur(utilisateur));

        // Read
        Utilisateur created = repository.getUtilisateur(newId);
        assertEquals("Test CRUD", created.getNom());

        // Update
        assertTrue(repository.updateUtilisateur(newId, "Test CRUD Modifié", "nouveaumdp", "test.crud.modifie@example.com"));
        Utilisateur updated = repository.getUtilisateur(newId);
        assertEquals("Test CRUD Modifié", updated.getNom());
        assertEquals("test.crud.modifie@example.com", updated.getMail());

        // Delete
        assertTrue(repository.deleteUtilisateur(newId));
        assertNull(repository.getUtilisateur(newId));
    }

    @Test
    @DisplayName("Test countUtilisateursWithId")
    void testCountUtilisateursWithId() throws SQLException {
        assertEquals(1, repository.countUtilisateursWithId("user-1"));
        assertEquals(0, repository.countUtilisateursWithId("non-existent-id"));
    }
}