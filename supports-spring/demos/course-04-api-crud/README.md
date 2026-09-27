# Démo — Cours 04 : Créer une petite API de gestion d’utilisateurs

Montre une API CRUD en mémoire, ses réponses JSON et ses statuts HTTP.

**Cours associé :** [ouvrir le support](../../cours/04-api-crud.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/04-crud/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn spring-boot:run
```

**Résultat attendu :** Tester GET, POST, PUT et DELETE sur /api/users avec curl.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
