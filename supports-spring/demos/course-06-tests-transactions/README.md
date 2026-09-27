# Démo — Cours 06 : Vérifier son application et découvrir les transactions

Exécute des tests HTTP et de service, avec base H2 isolée et rollback.

**Cours associé :** [ouvrir le support](../../cours/06-tests-transactions.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/06-tests/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn test
```

**Résultat attendu :** Les résultats s’affichent dans la console Maven.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
