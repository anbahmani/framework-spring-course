# Mini-projet de synthèse — Un répertoire de livres

## But et périmètre

Réutiliser les étapes déjà pratiquées pour gérer des livres avec Spring Boot. Un livre possède un identifiant généré et un titre obligatoire. Il n’y a ni stock, ni prix, ni comptes utilisateurs, ni relation entre tables à concevoir.

Le socle se réalise après le cours 6, en binôme, avec environ 2 à 3 heures de travail supplémentaire ou lors d’une séance dédiée. Il n’est pas à terminer en plus du TP de 65 minutes de la dernière séance. Les deux prolongements après les cours 7 et 8 sont facultatifs et accompagnés.

## Point de départ

Copier l’atelier `05-persistance` dans un nouveau dossier `bibliotheque`. Lancer d’abord ce projet sans modification. Puis remplacer progressivement les classes d’utilisateur par des classes de livre. Ne pas changer toutes les classes à la fois sans compiler.

## Étape 1 — décrire les données

Dessiner une table `books` avec colonnes `id` et `title`. Écrire deux exemples JSON : une entrée `{"title":"Apprendre Java"}` et une sortie avec identifiant. Définir `BookRequest`, `BookView` puis `BookEntity`, en prenant les classes utilisateur comme modèle.

**Vérification :** expliquer pourquoi l’entrée n’a pas d’identifiant alors que la sortie en a un.

## Étape 2 — relier les classes

Définir `BookRepository`, l’injecter dans `BookService`, puis injecter ce service dans `BookController`. La règle de titre obligatoire se place dans le service. Utiliser la même structure de gestion d’erreurs que dans l’annuaire pour le titre absent et le livre introuvable.

**Vérification :** dessiner contrôleur → service → repository et nommer le rôle de chaque flèche.

## Étape 3 — exposer le contrat

| Action | Requête | Résultat |
| --- | --- | --- |
| Lister | GET `/books` | 200 et tableau |
| Lire | GET `/books/{id}` | 200 ou 404 |
| Créer | POST `/books` | 201, Location et livre créé |
| Modifier | PUT `/books/{id}` | 200 ou 404 |
| Supprimer | DELETE `/books/{id}` | 204 ou 404 |
| Refuser une entrée | POST avec titre vide | 400 |

**Vérification :** dérouler le cycle complet avec un identifiant effectivement retourné, puis redémarrer et retrouver un livre conservé.

## Étape 4 — automatiser deux vérifications

Adapter les exemples de `UserApiTest` : création valide et refus du titre vide. Utiliser une base en mémoire pour les tests, comme au cours 6. Ne pas faire dépendre leur succès d’une ligne saisie manuellement la veille.

**Vérification :** `mvn test` réussit ; changer temporairement un statut attendu fait réellement échouer le test, puis la bonne assertion est restaurée.

## Aide attendue, sans mécanisme nouveau

Le repository est `JpaRepository<BookEntity, Long>`. Le préfixe du contrôleur est `/books`. Une classe `InvalidTitle` peut jouer le rôle d’`InvalidName`. La configuration de base en fichier reste identique dans le nouveau dossier ; changer le nom du fichier en `./data/books` rend le rôle plus lisible. Toute annotation employée doit avoir été expliquée dans les six premiers cours.

## Prolongements facultatifs

Après le cours 7, adapter le client pour afficher les titres de `/books`. Après le cours 8, publier et recevoir le texte `Un livre a été ajouté` dans un laboratoire de messagerie indépendant. L’assemblage fiable entre transaction de base et envoi de message n’est pas demandé.

## À rendre et barème sur 20

| Élément | Points | Attendu |
| --- | --- | --- |
| Lancement et explication du projet | 3 | README avec dossier, commandes et exemple |
| Contrôleur et contrat HTTP | 5 | Routes et statuts vérifiés |
| Service et injection | 4 | Règle de titre, constructeur expliqué, responsabilités séparées |
| Entité et repository | 4 | Données retrouvées après redémarrage |
| Tests et explication des résultats | 4 | Deux comportements vérifiés automatiquement |

Les prolongements ne sont pas nécessaires pour obtenir 20/20. Une explication orale de cinq minutes suit une création de livre du JSON à la base puis à la réponse. La mémorisation des imports ou du POM n’est pas évaluée.
