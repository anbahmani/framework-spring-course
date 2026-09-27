package fr.miage.debut;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
class DirectoryClientTest {
    @Test void decodesUsers() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        server.expect(requestTo("http://localhost:8080/users"))
            .andRespond(withSuccess("[{\"id\":1,\"name\":\"Ana\"}]", MediaType.APPLICATION_JSON));
        UserView[] result = new DirectoryClient(builder).list();
        assertEquals("Ana", result[0].name());
        server.verify();
    }
}
