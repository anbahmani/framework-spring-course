# Sommaire des ateliers Spring

Chaque sous-dossier est un projet Maven autonome. Ouvrir l’atelier lié au cours en cours : il constitue un état de départ fonctionnel. La modification à réaliser et sa correction sont dans le cours associé.

Tous les ateliers utilisent **Java 25, Maven 3.9 et Spring Boot 3.5.16**. Aucun ne demande de clé API ou de service payant ; les bases et le broker utilisés démarrent localement.

| Atelier | Cours associé | Projet |
| --- | --- | --- |
| 1 | [Premiers pas](../cours/01-premiers-pas.html) | [Première application](01-demarrage/index.html) |
| 2 | [HTTP et JSON](../cours/02-http-json.html) | [Paramètres et réponse JSON](02-http-json/index.html) |
| 3 | [Injection et couches](../cours/03-injection-couches.html) | [Service de salutation](03-injection/index.html) |
| 4 | [API CRUD](../cours/04-api-crud.html) | [Annuaire en mémoire](04-crud/index.html) |
| 5 | [Persistance](../cours/05-persistance.html) | [Annuaire avec base H2](05-persistance/index.html) |
| 6 | [Tests et transactions](../cours/06-tests-transactions.html) | [Tests de l’annuaire](06-tests/index.html) |
| 7 | [Client HTTP](../cours/07-client-http.html) | [Client Java de l’annuaire](07-client/index.html) |
| 8 | [Messages](../cours/08-messages.html) | [Envoi et réception de messages](08-messages/index.html) |

## Lancement

Ouvrir un terminal dans le sous-dossier choisi, puis `mvn spring-boot:run`. Pour les tests : `mvn test`. Un seul serveur HTTP à la fois ; l’atelier 07 est un client console et l’atelier 06 exécute seulement les tests.

Chaque sous-projet peut être ouvert séparément dans l’IDE.
