# Guide pédagogique — Accompagner une première découverte de Spring

## Prérequis réels

Le public connaît Java, mais pas Spring, les frameworks d’API, HTTP, JSON, Maven ou la persistance objet. Ne pas utiliser ces notions comme des rappels implicites. L’enseignant prépare l’environnement avant la séance ; la lecture du projet et le rôle des outils font partie du premier cours.

## Progression par petits résultats observables

| Séance | Avant d’introduire la notion suivante, l’étudiant doit pouvoir… |
| --- | --- |
| 1 | Lancer/arrêter, appeler `/hello`, différencier console et réponse |
| 2 | Lire une URL, prédire un paramètre et identifier le corps JSON |
| 3 | Expliquer qui construit le service et qui l’appelle à la requête |
| 4 | Dérouler un cycle CRUD et relier 400/404 à une cause simple |
| 5 | Distinguer entité/repository et observer les données après redémarrage |
| 6 | Lire une assertion et expliquer l’annulation des deux écritures |
| 7 | Identifier les deux processus et distinguer annuaire vide et panne |
| 8 | Nommer producteur/file/broker/consommateur et suivre un message |

L’annuaire constitue le fil rouge des séances 2 à 7. Le premier endpoint et la salutation servent d’entrée progressive ; la dernière séance utilise une notification texte pour isoler la nouveauté de la messagerie.

## Une séance en trois passages

1. **Expliquer et prédire.** Lire peu de lignes, définir chaque annotation nouvelle et demander le résultat attendu avant l’exécution.
2. **Manipuler avec guidage.** Faire fonctionner l’état fourni, observer un résultat, changer une seule chose puis vérifier.
3. **Reformuler.** Faire dessiner le chemin de l’information et répondre au quiz avant de consulter les corrigés.

Chaque TP comporte des temps indicatifs, un projet explicite, des résultats attendus, une petite modification et sa correction. Il n’exige pas de recréer seul une application complète. Les étapes indépendantes évitent qu’une erreur de la séance précédente bloque toute la suite.

## Réduire la charge de lecture

Les sources complètes sont disponibles, mais ne faire ouvrir que les fichiers cités à l’étape en cours. Les tests présents dans les premiers projets ne sont pas à analyser avant le cours 6. Le `synchronized` de la collection mémoire est fourni ; aucune démonstration de programmation concurrente n’est demandée.

L’entité et ses annotations arrivent après la manipulation d’une collection ; les transactions après les premières écritures et après l’explication des assertions. Les noms de composants ne doivent pas remplacer l’explication de leurs responsabilités.

## Évaluer les acquis enseignés

Huit questions par cours, une réponse attendue chacune. Une question vaut un point ; créditer une formulation équivalente si l’idée est correcte. Donner le quiz avant la correction collective. Aucun quiz ne porte sur un mécanisme réservé à un cours ultérieur.

Les preuves de TP sont modestes : fichier modifié, statut observé, dessin d’appel ou explication courte. La synthèse sur 20 porte sur une petite API semblable à celle travaillée en classe. Les séances 7–8 peuvent être traitées en découverte accompagnée si le groupe a besoin de davantage de pratique sur le CRUD.

## Hors évaluation débutante

Les architectures distribuées, les schémas de données complexes, les proxies détaillés, les configurations de sécurité, les différentes propagations transactionnelles, les reprises de messages et la fiabilité base/broker sont réservés à un enseignement ultérieur. Leur présence dans un exemple complémentaire n’en fait pas un prérequis.

## Préparation et vérification

Précharger les dépendances Maven sur les postes ; tester Java 25, le port 8080 et les commandes du premier cours. Les projets utilisent un socle fixé. Pour cette livraison, les étudiants lancent uniquement les ateliers 01 et 02 depuis leur sous-dossier. Voir le bilan de vérification pour les contrôles effectués et leurs limites.
