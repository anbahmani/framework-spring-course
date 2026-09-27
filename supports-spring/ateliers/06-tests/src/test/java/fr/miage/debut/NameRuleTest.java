package fr.miage.debut;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
class NameRuleTest {
    @Test void invalidNameDoesNotReachDatabase() {
        UserRepository repository = mock(UserRepository.class);
        UserService service = new UserService(repository);
        assertThrows(InvalidName.class, () -> service.create(" "));
        verifyNoInteractions(repository);
    }
}
