# Démo Spring MVC + JPA — utilisateurs

**Complément enseignant, hors TP débutants.** Pour les premiers exercices, ouvrir les [ateliers progressifs](../../ateliers/README.md).

Application pédagogique de gestion d’utilisateurs avec Spring Boot, Spring MVC et Spring Data JPA. Prérequis : JDK 25 ou version compatible, Maven 3.9 recommandé. Exécuter les commandes dans ce dossier ; conserver le `pom.xml` parent dans `../`.

```bash
mvn test
mvn spring-boot:run
```

Dans un autre terminal :

```bash
curl -i -H 'X-API-KEY: demo-key' http://localhost:8080/api/hello
curl -i -H 'X-API-KEY: demo-key' -H 'Content-Type: application/json' \
  -d '{"name":"Ana","email":"ana@example.com"}' http://localhost:8080/api/users
curl -i -H 'X-API-KEY: demo-key' http://localhost:8080/api/users
```

Le POST renvoie 201 et `Location`. Utiliser l’URL reçue pour GET, PUT et DELETE ; ne pas supposer un identifiant fixe. Le listing est vide au premier démarrage : créer un premier utilisateur avec le POST. H2 est en mémoire et les données sont perdues à l’arrêt.

```bash
curl -i -H 'X-API-KEY: demo-key' -H 'Content-Type: application/json' \
  -d '{"name":"Ana","email":"invalide"}' http://localhost:8080/api/users
curl -i http://localhost:8080/api/users
```

Résultats attendus : 400 puis 401. La clé est configurable par variable `DEMO_API_KEY`. C’est une authentification illustrative commune à tous les appels, pas un modèle d’utilisateurs ni une sécurité de production. Aucune contrainte d’unicité d’email n’est imposée.

| Composant | Responsabilité |
| --- | --- |
| UsersApplication | Démarrer Spring Boot et son serveur HTTP |
| UserController | Exposer les routes et valider les entrées |
| UserService | Orchestrer les opérations et les transactions |
| UserRepository | Accéder aux utilisateurs stockés dans H2 |
| ApiErrors | Construire les réponses d’erreur |
| ApiKeyFilter | Contrôler l’accès par clé de démonstration |
| UsersApiTest | Vérifier CRUD, validation et refus sans clé |

La démo utilise une base H2 pour observer les opérations de persistance. Le service dépend ici de Spring Data et le DTO de sortie mappe une entité : c’est une petite architecture en couches, pas une implémentation complète d’architecture hexagonale. Les transactions concurrentes, la pagination, la journalisation structurée, le client HTTP, OpenAPI et Spring Security relèvent d’un approfondissement ultérieur, pas des fonctionnalités annoncées comme déjà livrées.
