package fr.miage.debut;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
@Service
public class DirectoryClient {
    private final RestClient client;
    public DirectoryClient(RestClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:8080").build();
    }
    public UserView[] list() {
        return client.get().uri("/users").retrieve().body(UserView[].class);
    }
}
