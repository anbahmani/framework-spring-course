package fr.miage.users;

import fr.miage.users.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsersApiTest {
    @Autowired MockMvc mvc;
    @Autowired UserRepository users;
    @BeforeEach void reset() { users.deleteAll(); }

    @Test void rejectsMissingKey() throws Exception {
        mvc.perform(get("/api/users"))
            .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.status").value(401));
    }
    @Test void rejectsInvalidInputWithoutSaving() throws Exception {
        mvc.perform(post("/api/users").header("X-API-KEY", "demo-key")
                .contentType(MediaType.APPLICATION_JSON).content("""
                {"name":"Ana","email":"invalide"}
                """))
            .andExpect(status().isBadRequest());
        assertThat(users.count()).isZero();
    }
    @Test void crudAndMissingResource() throws Exception {
        String location = mvc.perform(post("/api/users").header("X-API-KEY", "demo-key")
                .contentType(MediaType.APPLICATION_JSON).content("""
                {"name":"Ana","email":"ana@example.com"}
                """))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andReturn().getResponse().getHeader("Location");
        mvc.perform(get(location).header("X-API-KEY", "demo-key"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Ana"));
        mvc.perform(put(location).header("X-API-KEY", "demo-key")
                .contentType(MediaType.APPLICATION_JSON).content("""
                {"name":"Anabelle","email":"ana@example.com"}
                """))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Anabelle"));
        mvc.perform(get(location).header("X-API-KEY", "demo-key"))
            .andExpect(jsonPath("$.name").value("Anabelle"));
        mvc.perform(delete(location).header("X-API-KEY", "demo-key"))
            .andExpect(status().isNoContent());
        mvc.perform(get(location).header("X-API-KEY", "demo-key"))
            .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        assertThat(users.count()).isZero();
    }
}
