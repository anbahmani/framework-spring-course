# Atelier 09 — Exposer un outil avec MCP

Ce projet démarre un serveur Spring AI MCP local. Il fournit une première capacité de lecture ; l’atelier demande d’en ajouter une seconde.

Prérequis : Java 25, Maven 3.9, Node.js et npx pour lancer MCP Inspector. Dans ce dossier :

```bash
mvn test
mvn spring-boot:run
```

Dans un autre terminal, lancer `npx -y @modelcontextprotocol/inspector`, choisir le transport Streamable HTTP et se connecter à `http://localhost:8080/mcp`.

La collection d’utilisateurs ne contient que deux fiches en mémoire. Aucun fournisseur d’IA ni aucune clé d’API n’est nécessaire : Inspector découvre et appelle manuellement les capacités du serveur.

Après les modifications, `mvn test` vérifie les règles du service. Le transport est local et cette application ne définit pas d’authentification pour un déploiement réseau.
