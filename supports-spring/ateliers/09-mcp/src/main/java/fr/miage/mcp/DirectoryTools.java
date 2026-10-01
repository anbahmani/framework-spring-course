package fr.miage.mcp;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class DirectoryTools {
    private final DirectoryService directory;

    public DirectoryTools(DirectoryService directory) {
        this.directory = directory;
    }

    @McpTool(description = "Recherche une fiche utilisateur de démonstration par son identifiant")
    public String findUserById(
            @McpToolParam(description = "Identifiant numérique de la fiche") long id) {
        return directory.findUserById(id);
    }
}
