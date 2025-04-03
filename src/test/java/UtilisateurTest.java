import com.produitutilisateur.produitutilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @Test
    void testConstructeurParDefaut() {
        Utilisateur utilisateur = new Utilisateur();
        assertNotNull(utilisateur);
        assertNull(utilisateur.getId());
        assertNull(utilisateur.getNom());
        assertNull(utilisateur.getMdp());
        assertNull(utilisateur.getMail());
    }

    @Test
    void testConstructeurAvecParametres() {
        Utilisateur utilisateur = new Utilisateur("user-123", "John Doe", "hashedPassword123", "john@example.com");

        assertEquals("user-123", utilisateur.getId());
        assertEquals("John Doe", utilisateur.getNom());
        assertEquals("hashedPassword123", utilisateur.getMdp());
        assertEquals("john@example.com", utilisateur.getMail());
    }

    @Test
    void testSettersEtGetters() {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setId("user-456");
        assertEquals("user-456", utilisateur.getId());

        utilisateur.setNom("Jane Doe");
        assertEquals("Jane Doe", utilisateur.getNom());

        utilisateur.setMdp("newHashedPassword");
        assertEquals("newHashedPassword", utilisateur.getMdp());

        utilisateur.setMail("jane@example.com");
        assertEquals("jane@example.com", utilisateur.getMail());
    }

    @Test
    void testSetIdInvalide() {
        Utilisateur utilisateur = new Utilisateur();

        assertThrows(IllegalArgumentException.class, () -> utilisateur.setId(null));
        assertThrows(IllegalArgumentException.class, () -> utilisateur.setId(""));
        assertThrows(IllegalArgumentException.class, () -> utilisateur.setId("   "));
    }

    @Test
    void testSetMdpInvalide() {
        Utilisateur utilisateur = new Utilisateur();

        assertThrows(IllegalArgumentException.class, () -> utilisateur.setMdp(null));
        assertThrows(IllegalArgumentException.class, () -> utilisateur.setMdp(""));
        assertThrows(IllegalArgumentException.class, () -> utilisateur.setMdp("   "));
    }

    @Test
    void testSetMailInvalide() {
        Utilisateur utilisateur = new Utilisateur();

        assertThrows(IllegalArgumentException.class, () -> utilisateur.setMail(null));
    }

    @Test
    void testToString() {
        Utilisateur utilisateur = new Utilisateur("user-789", "Alice", "secret123", "alice@example.com");
        String expectedString = "Utilisateur{id='user-789', nom='Alice', mdp='[PROTECTED]', mail='alice@example.com'}";

        assertEquals(expectedString, utilisateur.toString());
        assertFalse(utilisateur.toString().contains("secret123"));
    }

    @Test
    void testEqualsEtHashCode() {
        Utilisateur utilisateur1 = new Utilisateur("user-001", "Test", "mdp", "test@test.com");
        Utilisateur utilisateur2 = new Utilisateur("user-001", "Test", "mdp", "test@test.com");
        Utilisateur utilisateur3 = new Utilisateur("user-001", "Different", "mdp", "test@test.com");
        Utilisateur utilisateur4 = new Utilisateur("user-002", "Test", "mdp", "test@test.com");

        // Réflexivité
        assertEquals(utilisateur1, utilisateur1);

        // Symétrie
        assertEquals(utilisateur1, utilisateur2);
        assertEquals(utilisateur2, utilisateur1);

        // Non-égalité
        assertNotEquals(utilisateur1, utilisateur3); // Nom différent
        assertNotEquals(utilisateur1, utilisateur4); // ID différent

        // Test avec null et autre type
        assertNotEquals(null, utilisateur1);
        assertNotEquals("une chaîne", utilisateur1);

        // Test hashCode
        assertEquals(utilisateur1.hashCode(), utilisateur2.hashCode());
        assertNotEquals(utilisateur1.hashCode(), utilisateur3.hashCode());
    }

    @Test
    void testValidationMailValide() {
        Utilisateur utilisateur = new Utilisateur();

        // Test avec différents formats d'email valides
        assertDoesNotThrow(() -> utilisateur.setMail("simple@example.com"));
        assertDoesNotThrow(() -> utilisateur.setMail("with.dots@example.com"));
        assertDoesNotThrow(() -> utilisateur.setMail("with-hyphen@example.com"));
        assertDoesNotThrow(() -> utilisateur.setMail("user@sub.domain.com"));
        assertDoesNotThrow(() -> utilisateur.setMail("user@example.co.uk")); // Domaine à plusieurs parties
    }
}