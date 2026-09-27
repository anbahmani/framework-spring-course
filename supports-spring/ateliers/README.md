# Les ateliers actifs du parcours débutant

Chaque sous-dossier est un projet Maven autonome avec tous ses fichiers. Ouvrir uniquement l’atelier du cours en cours : il constitue un état de départ fonctionnel, pas une application vide. La modification à réaliser et sa correction sont dans le cours associé.

Java 25 et Maven 3.9 sont utilisés pour les deux premiers ateliers. Pas de clé API, de Docker ni de base externe. Les tests fournis servent surtout à vérifier le comportement attendu ; leur écriture sera travaillée plus tard.

| Atelier | Travail de la séance | Lien |
| --- | --- | --- |
| 1 | Première application | [Ouvrir](01-demarrage/README.md) |
| 2 | HTTP et JSON | [Ouvrir](02-http-json/README.md) |

Les ateliers suivants seront repris quand les séances correspondantes seront finalisées.

## Lancement

Ouvrir un terminal dans le sous-dossier choisi, puis `mvn spring-boot:run`. Pour les tests : `mvn test`. Un seul serveur HTTP à la fois.

Chaque sous-projet peut être ouvert séparément dans l’IDE.
