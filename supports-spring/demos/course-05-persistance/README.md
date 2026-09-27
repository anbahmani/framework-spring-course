# Démo — Cours 05 : Conserver les données avec Spring Data JPA

Montre la séparation contrôleur/service/repository et la persistance H2.

**Cours associé :** [ouvrir le support](../../cours/05-persistance.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/05-persistance/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn spring-boot:run
```

**Résultat attendu :** Ouvrir http://localhost:8080/api/users.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
