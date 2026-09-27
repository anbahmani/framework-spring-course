# Démo — Cours 01 : Découvrir Spring et lancer sa première application

Démarre un serveur Spring Boot et expose une première route HTTP.

**Cours associé :** [ouvrir le support](../../cours/01-premiers-pas.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/01-demarrage/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn spring-boot:run
```

**Résultat attendu :** Ouvrir http://localhost:8080/hello.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
