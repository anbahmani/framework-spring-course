# Démo — Cours 03 : Comprendre les objets gérés par Spring et séparer les rôles

Montre un contrôleur qui délègue à un service injecté par constructeur.

**Cours associé :** [ouvrir le support](../../cours/03-injection-couches.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/03-injection/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn spring-boot:run
```

**Résultat attendu :** Ouvrir http://localhost:8080/hello.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
