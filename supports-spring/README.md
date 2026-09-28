# Sommaire des cours — Architecture et Spring

Supports destinés à des étudiants qui connaissent Java et découvrent Spring ainsi que les frameworks d’API. Chaque séance dure **3 heures** et comprend un cours projetable, un TP guidé de 65 minutes et un quiz corrigé de huit questions.

Le parcours utilise **Java 25, Maven 3.9 et Spring Boot 3.5.16**. Les diagrammes de séquence UML sont intégrés aux cours et fonctionnent hors ligne.

| Séance | Cours | Démo | Atelier associé | Notions abordées |
| --- | --- | --- | --- |
| 1 | [Découvrir Spring et lancer sa première application](cours/01-premiers-pas.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-01-premiers-pas) | [Atelier 01 — Première application](ateliers/01-demarrage/index.html) | Framework, Spring Boot, contrôleur et première route |
| 2 | [Comprendre HTTP et envoyer des données JSON](cours/02-http-json.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-02-http-json) | [Atelier 02 — HTTP et JSON](ateliers/02-http-json/index.html) | Requêtes, réponses, paramètres, objets Java et JSON |
| 3 | [Comprendre les objets gérés par Spring et séparer les rôles](cours/03-injection-couches.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-03-injection-couches) | [Atelier 03 — Injection et service](ateliers/03-injection/index.html) | Beans, injection par constructeur, contrôleur et service |
| 4 | [Créer une petite API de gestion d’utilisateurs](cours/04-api-crud.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-04-api-crud) | [Atelier 04 — Annuaire en mémoire](ateliers/04-crud/index.html) | Requêtes CRUD, corps JSON, validation et statuts HTTP |
| 5 | [Conserver les données avec Spring Data JPA](cours/05-persistance.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-05-persistance) | [Atelier 05 — Annuaire persistant](ateliers/05-persistance/index.html) | Entité, repository, base H2 et persistance |
| 6 | [Vérifier son application et découvrir les transactions](cours/06-tests-transactions.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-06-tests-transactions) | [Atelier 06 — Tests et transactions](ateliers/06-tests/index.html) | JUnit, assertions, MockMvc et rollback |
| 7 | [Appeler une API depuis un programme Spring](cours/07-client-http.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-07-client-http) | [Atelier 07 — Client HTTP](ateliers/07-client/index.html) | RestClient, contrat HTTP, JSON et erreurs de connexion |
| 8 | [Découvrir les messages asynchrones avec Spring JMS](cours/08-messages.html) | [Code GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-08-messages) | [Atelier 08 — Messages texte](ateliers/08-messages/index.html) | Producteur, broker, file et consommateur |

## Ressources

- [Démonstrations exécutables des huit cours](demos/index.html)
- [Préparer le poste et démarrer les ateliers](demarrage.html)
- [Glossaire Spring](guide-spring.html)
- [Guide pédagogique](guide-pedagogique.html)
- [Bilan de vérification](verification.html)
- [Télécharger les supports et projets des huit séances](telechargements/supports-spring-cours-complet-java25.zip)

Les ateliers sont autonomes et utilisent tous Maven. Les diagrammes UML, exemples, étapes de TP et quiz corrigés sont accessibles dans chaque cours ; le bouton **Diaporama** affiche une section à la fois.
