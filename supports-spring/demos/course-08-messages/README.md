# Démo — Cours 08 : Découvrir les messages asynchrones avec Spring JMS

Envoie un message à une file Artemis et le consomme sans serveur externe.

**Cours associé :** [ouvrir le support](../../cours/08-messages.md). **Atelier pratique :** [ouvrir l’atelier](../../ateliers/08-messages/README.md).

## Lancer la démonstration

Prérequis : Java 25 et Maven 3.9. Dans ce dossier, exécuter :

```bash
mvn spring-boot:run
```

**Résultat attendu :** Observer les lignes ENVOI et RECU dans la console.

## Explorer le code

Le projet est autonome : son `pom.xml` déclare ses dépendances et `src/main/java` contient l’application. La configuration se trouve dans `src/main/resources/application.properties` ; les tests éventuels sont dans `src/test/java`.

La démo reprend un état fonctionnel de l’atelier associé. Consulte le support de cours pour le diagramme UML et les explications, puis l’atelier pour les étapes de modification.
