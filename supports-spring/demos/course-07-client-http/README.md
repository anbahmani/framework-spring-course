# Démo — Cours 07 : Appeler une API depuis un programme Spring

Lance un client RestClient qui appelle le service de l’atelier 05.

**Cours associé :** [ouvrir le support](../../cours/07-client-http.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/07-client/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn spring-boot:run
```

**Résultat attendu :** Démarrer d’abord le projet ../05-persistance sur le port 8080.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
