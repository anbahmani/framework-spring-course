package fr.miage.debut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:transactions", "spring.jpa.hibernate.ddl-auto=create-drop"})
class TransactionTest {
    @Autowired UserService service;
    @Autowired UserRepository repository;
    @Test void secondInvalidNameCancelsBothCreations() {
        repository.deleteAll();
        assertThrows(InvalidName.class, () -> service.createPair("Ana", " "));
        assertEquals(0, repository.count());
    }
    @Test void validPairIsSaved() {
        repository.deleteAll();
        service.createPair("Ana", "Lea");
        assertEquals(2, repository.count());
    }
}
