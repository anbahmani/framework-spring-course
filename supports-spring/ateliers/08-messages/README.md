# Atelier 08 — Messages texte

**Cours et TP détaillé :** [ouvrir la séance](../../cours/08-messages.md). **Préparation :** [fiche de démarrage](../../demarrage.md).

## Avant de commencer

Ouvrir ce dossier dans l’IDE comme projet Maven. Java 25 et Maven 3.9 doivent être disponibles. Le code est un état de départ fonctionnel ; les changements demandés sont expliqués dans le TP. Les autres ateliers sont indépendants.

## Exécuter

Depuis ce dossier, celui qui contient ce `pom.xml` :

```bash
mvn spring-boot:run
```

**Résultat attendu :** RECU : Bienvenue Ana.

Aucun serveur externe à installer : Artemis démarre avec le programme. Le broker est non persistant et cette démo ne vérifie pas la reprise après un arrêt. Le processus reste actif : Ctrl+C pour l’arrêter.

## Vérifier

```bash
mvn test
```

Le projet de départ contient **1 test(s)**. Leur lecture est facultative avant la séance 6. Les contrôles échoués indiquent ce qui diffère de l’attente ; ne pas les supprimer pour obtenir un résultat vert.

## Se repérer

`src/main/java/fr/miage/debut` contient les classes ; `src/main/resources/application.properties` la configuration ; `src/test/java/fr/miage/debut` les tests. Lire les classes dans l’ordre donné par la séance. Après une modification Java, arrêter et relancer le serveur s’il était en cours d’exécution.
