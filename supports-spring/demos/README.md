# Démonstrations des huit cours Spring

Huit applications Maven autonomes accompagnent les cours. Chaque démo montre un état exécutable du concept traité ; les ateliers associés servent aux manipulations des étudiants. Prérequis : Java 25 et Maven 3.9. Les projets utilisent Spring Boot 3.5.16.

| Cours | Démonstration | Ressources associées |
| --- | --- | --- |
| 01 | [Découvrir Spring et lancer sa première application](course-01-premiers-pas/README.md) | [Cours](../cours/01-premiers-pas.md) · [Atelier](../ateliers/01-demarrage/README.md) |
| 02 | [Comprendre HTTP et envoyer des données JSON](course-02-http-json/README.md) | [Cours](../cours/02-http-json.md) · [Atelier](../ateliers/02-http-json/README.md) |
| 03 | [Comprendre les objets gérés par Spring et séparer les rôles](course-03-injection-couches/README.md) | [Cours](../cours/03-injection-couches.md) · [Atelier](../ateliers/03-injection/README.md) |
| 04 | [Créer une petite API de gestion d’utilisateurs](course-04-api-crud/README.md) | [Cours](../cours/04-api-crud.md) · [Atelier](../ateliers/04-crud/README.md) |
| 05 | [Conserver les données avec Spring Data JPA](course-05-persistance/README.md) | [Cours](../cours/05-persistance.md) · [Atelier](../ateliers/05-persistance/README.md) |
| 06 | [Vérifier son application et découvrir les transactions](course-06-tests-transactions/README.md) | [Cours](../cours/06-tests-transactions.md) · [Atelier](../ateliers/06-tests/README.md) |
| 07 | [Appeler une API depuis un programme Spring](course-07-client-http/README.md) | [Cours](../cours/07-client-http.md) · [Atelier](../ateliers/07-client/README.md) |
| 08 | [Découvrir les messages asynchrones avec Spring JMS](course-08-messages/README.md) | [Cours](../cours/08-messages.md) · [Atelier](../ateliers/08-messages/README.md) |

Les projets complémentaires [API utilisateurs complète](rest-users/README.md) et [événements de commande](jms-orders/README.md) montrent un assemblage plus avancé.

Depuis ce dossier, `mvn test` lance les tests de tous les modules. Pour exécuter une démo isolément, ouvrir son dossier `course-XX-*` puis suivre son README.
