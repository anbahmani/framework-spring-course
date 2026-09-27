# Découvrir Spring et construire ses premières API

**Public : étudiants connaissant Java, débutants en Spring et en développement d’API.** Aucune connaissance préalable de Maven, HTTP, JSON, SQL ou d’un framework n’est exigée : ces notions sont introduites au moment où elles deviennent utiles.

Ouvrir [index.html](index.html) pour la lecture hors ligne. Chaque cours existe en Markdown modifiable et en HTML projetable/imprimable. Le bouton **Diaporama** affiche une seule section à la fois et se pilote avec les flèches du clavier. Les volets d’aide et de corrigé peuvent être masqués ; les ouvrir avant impression pour les inclure. Après une modification, `python3 render_html.py` régénère les pages.

Télécharger le [pack complet des deux premiers cours et ateliers](telechargements/supports-spring-cours-01-02-java25.zip).

## Commencer ici

1. Préparer le poste avec la [fiche de démarrage](demarrage.md).
2. Lire le [cours 1](cours/01-premiers-pas.md) et ouvrir l’atelier 01 seulement.
3. Passer ensuite au [cours 2](cours/02-http-json.md) et à l’atelier 02.

Les TP actifs utilisent **Java 25, Maven 3.9 et Spring Boot 3.5.16**. Ils n’exigent ni Docker, ni compte externe, ni base à installer. Le socle est fixé pour reproduire les exemples ; ce n’est pas une invitation à choisir une version au hasard dans chaque atelier.

## Publication en ligne

Le workflow GitHub Pages du dépôt publie le contenu de ce dossier à chaque mise à jour de `main` ou `master`. Dans les réglages du dépôt GitHub, choisir **Settings → Pages → Build and deployment → GitHub Actions**. Le dépôt doit contenir le dossier `supports-spring` à sa racine.

## Livraison actuelle — 2 séances de 3 heures

| Séance | Cours | Nouveauté principale | TP |
| --- | --- | --- | --- |
| 1 | [Premiers pas](cours/01-premiers-pas.md) | Framework, Spring Boot, annotation, serveur | Lancer puis ajouter une réponse texte |
| 2 | [HTTP et JSON](cours/02-http-json.md) | Requête, réponse, paramètres et sérialisation | Paramétrer une réponse et calculer un carré |

Les séances 3 à 8 seront reprises plus tard. Les fichiers qui existent déjà pour ces thèmes doivent être considérés comme du matériau de travail, pas comme des supports prêts à diffuser.

Chaque séance prévoit environ 15 min de rappel, 50 min d’explications, 25 min de démonstration accompagnée, 10 min de pause, 65 min de TP et 15 min de quiz/correction. L’installation est préparée avant la séance avec l’enseignant.

## Ce que contient chaque support

- Objectifs limités et prérequis issus des séances précédentes.
- Vocabulaire défini avant les exercices, exemples courts et lecture des annotations.
- Diagrammes intégrés pour projeter les concepts importants.
- TP découpé en étapes avec fichiers, commandes, observations attendues et aide/correction.
- Huit questions portant sur la séance, suivies de réponses expliquées : **16 questions corrigées** dans cette livraison.

Les extraits des cours se concentrent sur les lignes étudiées ; les [deux ateliers actifs](ateliers/README.md) fournissent tous les fichiers et imports pour compiler. Les packages techniques nécessaires aux bibliothèques restent naturellement présents dans le code exécutable.

## Pour apprendre et enseigner

Le [glossaire progressif](guide-spring.md) aide à retrouver les mots nouveaux. Le [guide pédagogique](guide-pedagogique.md) précise les acquis attendus et le rythme.

Les exemples du dossier `demos/` sont des compléments pour l’enseignant ; **les TP de ce parcours sont exclusivement dans `ateliers/`**. Les thèmes avancés comme sécurité complète, relations complexes, concurrence distribuée ou reprise durable ne font pas partie de l’évaluation débutante.
