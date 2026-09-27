# Démo Spring JMS — OrdersQueue

**Complément enseignant, hors TP débutants.** Pour les premiers exercices, ouvrir les [ateliers progressifs](../../ateliers/README.md).

Application pédagogique de communication asynchrone avec Spring Boot, Spring JMS et Artemis. Cette démo n’appartient pas à la livraison active Java 25 centrée sur les deux premiers cours ; elle sera revue avec la séance messagerie. Conserver le POM parent. Depuis ce dossier :

```bash
mvn test
mvn spring-boot:run
```

Artemis démarre dans le processus, cinq messages JSON sont envoyés sur `OrdersQueue`, et le listener affiche leurs identifiants. L’application reste en écoute ; arrêter par Ctrl+C. Le broker embarqué est **non persistant** et cette configuration ne sert pas à démontrer la durabilité après arrêt.

Le contrat contient `type`, `orderId`, `createdAt`, `eventId` et `schemaVersion`. Le listener refuse les messages incomplets ou dont la version n’est pas prise en charge. L’identifiant d’événement permet de suivre une livraison indépendamment de l’identifiant de commande.

## Broker externe

Démarrer un broker Artemis de TP sur `localhost:61616`, avec les identifiants locaux attendus, puis :

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=external
```

Paramètres possibles : `BROKER_URL`, `BROKER_USER`, `BROKER_PASS`. Pour démarrer un autre consommateur sans republier cinq événements :

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=external \
  -Dspring-boot.run.arguments=--demo.produce=false
```

Pour un broker conteneurisé, fixer la version de l’image et monter effectivement son répertoire de données sur un volume avant une expérience de durabilité. Le laboratoire embarqué permet une première exécution sans installer de broker externe.

## Ce qui est vérifié et ce qui reste à réaliser

- Le test de transport envoie un événement et relit le JSON via le broker embarqué ; le listener est arrêté dans ce test pour éviter une course avec la réception de test.
- Les tests du listener vérifient le rejet de JSON mal formé et de contrat invalide.
- Le lancement manuel exerce le listener annoté et montre la réception des cinq événements.
- La session de réception est transactionnelle. La politique de retries/DLQ du broker externe relève d’un approfondissement ultérieur.
- Aucun effet métier persistant, dédoublonnage, outbox ou transaction distribuée n’est implémenté. L’application journalise les réceptions ; elle ne promet pas une exécution métier exactement une fois.

Les valeurs `admin/admin` du profil externe sont réservées au laboratoire. Producteur et consommateur utilisent la même queue configurée. Spring gère les connexions et sessions utilisées par les composants applicatifs.
