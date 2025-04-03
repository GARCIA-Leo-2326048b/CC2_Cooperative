import com.produitutilisateur.produitutilisateur.Utilisateur;
import com.produitutilisateur.produitutilisateur.UtilisateurRepositoryInterface;
import com.produitutilisateur.produitutilisateur.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UtilisateurServiceTest {

    private UtilisateurService utilisateurService;
    private UtilisateurRepositoryTestImpl testRepository;

    // Implémentation de test manuelle avec ID String
    static class UtilisateurRepositoryTestImpl implements UtilisateurRepositoryInterface {
        private final ArrayList<Utilisateur> utilisateurs = new ArrayList<>();

        public UtilisateurRepositoryTestImpl() {
            utilisateurs.add(new Utilisateur("user-1", "Jean Dupont", "mdp123", "jean.dupont@example.com"));
            utilisateurs.add(new Utilisateur("user-2", "Marie Martin", "mdp456", "marie.martin@example.com"));
        }

        @Override
        public void close() {}

        @Override
        public Utilisateur getUtilisateur(String id) {
            return utilisateurs.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public Utilisateur getUtilisateurByMail(String mail) {
            return utilisateurs.stream().filter(u -> u.getMail().equalsIgnoreCase(mail)).findFirst().orElse(null);
        }

        @Override
        public ArrayList<Utilisateur> getAllUtilisateurs() {
            return new ArrayList<>(utilisateurs);
        }

        @Override
        public boolean createUtilisateur(Utilisateur utilisateur) {
            if (utilisateur == null || utilisateur.getId() == null || utilisateur.getId().isEmpty() ||
                    utilisateur.getNom() == null || utilisateur.getNom().isEmpty() ||
                    utilisateur.getMail() == null || utilisateur.getMail().isEmpty()) {
                return false;
            }

            // Vérifie que l'email et l'ID n'existent pas déjà
            if (getUtilisateurByMail(utilisateur.getMail()) != null || getUtilisateur(utilisateur.getId()) != null) {
                return false;
            }

            return utilisateurs.add(utilisateur);
        }

        @Override
        public boolean updateUtilisateur(String id, String nom, String mdp, String mail) {
            Utilisateur u = getUtilisateur(id);
            if (u != null) {
                u.setNom(nom);
                u.setMdp(mdp);
                u.setMail(mail);
                return true;
            }
            return false;
        }

        @Override
        public boolean updateMotDePasse(String id, String nouveauMdp) {
            Utilisateur u = getUtilisateur(id);
            if (u != null) {
                u.setMdp(nouveauMdp);
                return true;
            }
            return false;
        }

        @Override
        public boolean deleteUtilisateur(String id) {
            Utilisateur utilisateurASupprimer = getUtilisateur(id);
            if (utilisateurASupprimer != null) {
                return utilisateurs.remove(utilisateurASupprimer);
            }
            return false;
        }

        @Override
        public boolean authenticate(String mail, String mdp) {
            Utilisateur u = getUtilisateurByMail(mail);
            return u != null && u.getMdp().equals(mdp);
        }

        @Override
        public int countUtilisateursWithId(String id) {
            return (int) utilisateurs.stream().filter(u -> u.getId().equals(id)).count();
        }
    }

    @BeforeEach
    void setUp() {
        testRepository = new UtilisateurRepositoryTestImpl();
        utilisateurService = new UtilisateurService(testRepository);
    }

    @Test
    void testCreateUtilisateur_Success() {
        String newId = "user-3";
        Utilisateur newUser = new Utilisateur(newId, "Paul Durand", "mdp789", "paul.durand@example.com");
        Utilisateur created = utilisateurService.createUtilisateur(newUser);

        assertNotNull(created);
        assertEquals(newId, created.getId());
        assertEquals("Paul Durand", created.getNom());
        assertEquals("paul.durand@example.com", created.getMail());
    }

    @Test
    void testCreateUtilisateur_InvalidData() {
        // Test avec nom null
        Utilisateur invalidUser1 = new Utilisateur("user-3", null, "mdp", "test@example.com");
        assertNull(utilisateurService.createUtilisateur(invalidUser1));

    }

    @Test
    void testUpdateUtilisateur_Success() {
        Utilisateur updated = new Utilisateur("user-1", "Jean Dupont Modifié", "nouveaumdp", "jean.modifie@example.com");
        boolean success = utilisateurService.updateUtilisateur("user-1", updated);

        assertTrue(success);
        Utilisateur user = testRepository.getUtilisateur("user-1");
        assertEquals("Jean Dupont Modifié", user.getNom());
        assertEquals("jean.modifie@example.com", user.getMail());
    }

    @Test
    void testUpdateUtilisateur_InvalidId() {
        Utilisateur updated = new Utilisateur("user-2", "Jean Dupont Modifié", "nouveaumdp", "jean.modifie@example.com");
        // Tentative de mise à jour avec un ID qui ne correspond pas
        boolean success = utilisateurService.updateUtilisateur("user-1", updated);
        assertFalse(success);
    }

    @Test
    void testUpdateMotDePasse() {
        boolean success = utilisateurService.updateMotDePasse("user-1", "newpassword");
        assertTrue(success);
        assertTrue(testRepository.authenticate("jean.dupont@example.com", "newpassword"));
    }

    @Test
    void testUpdateMotDePasse_InvalidPassword() {
        boolean success = utilisateurService.updateMotDePasse("user-1", "");
        assertFalse(success);
    }

    @Test
    void testAuthenticate_Success() {
        assertTrue(utilisateurService.authenticate("jean.dupont@example.com", "mdp123"));
    }

    @Test
    void testAuthenticate_Failure() {
        // Mauvais mot de passe
        assertFalse(utilisateurService.authenticate("jean.dupont@example.com", "wrongpassword"));

        // Utilisateur inexistant
        assertFalse(utilisateurService.authenticate("nonexistent@example.com", "mdp123"));

        // Données vides
        assertFalse(utilisateurService.authenticate("", ""));
        assertFalse(utilisateurService.authenticate(null, null));
    }

    @Test
    void testDeleteUtilisateur() {
        boolean success = utilisateurService.deleteUtilisateur("user-1");
        assertTrue(success);
        assertNull(testRepository.getUtilisateur("user-1"));
    }

    @Test
    void testDeleteUtilisateur_NonExisting() {
        boolean success = utilisateurService.deleteUtilisateur("non-existent-id");
        assertFalse(success);
    }

    @Test
    void testGenerateNewId() {
        String newId = utilisateurService.generateNewId();
        assertNotNull(newId);
        assertTrue(newId.length() > 0);
        // Vérifie que c'est un UUID valide
        assertDoesNotThrow(() -> UUID.fromString(newId));
    }

    @Test
    void testGetUtilisateurJSON_NonExisting() {
        String json = utilisateurService.getUtilisateurJSON("non-existent-id");
        assertNull(json);
    }
}