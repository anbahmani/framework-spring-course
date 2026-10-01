package fr.miage.mcp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DirectoryServiceTest {
    private final DirectoryService directory = new DirectoryService();

    @Test
    void findsAnExistingUser() {
        assertEquals("Utilisateur #1 : Ana", directory.findUserById(1));
    }

    @Test
    void reportsWhenTheUserDoesNotExist() {
        assertEquals("Aucun utilisateur avec l’identifiant 42", directory.findUserById(42));
    }
}
