# Préparer son premier TP Spring

## Ce que vous devez déjà savoir

Créer une classe Java, appeler une méthode, utiliser un constructeur, une liste et une exception simple. Les mots propres à Spring et aux API seront expliqués dans les cours. Un petit rappel des records est inclus au cours 2.

## Préparer le poste avec l’enseignant

Installer ou faire préparer **un JDK 25**, **Maven 3.9**, un éditeur Java et un navigateur. Le JDK contient les outils de compilation et d’exécution Java. Maven prépare le projet à partir du fichier `pom.xml`. Il faut une connexion réseau au premier lancement pour télécharger les bibliothèques ; une fois téléchargées, elles sont gardées localement.

Les neuf ateliers sont fournis avec le parcours. Les neuf projets de démonstration, qui servent pendant une séquence guidée du cours, sont regroupés dans un [dépôt GitHub séparé](https://github.com/anbahmani/framework-spring-demos). Pour le télécharger :

```bash
git clone https://github.com/anbahmani/framework-spring-demos.git
```

Aucun générateur de projet ni serveur HTTP externe n’est nécessaire. Les ateliers 5 et 6 utilisent une base H2 locale ; l’atelier 8 démarre un broker avec l’application.

## Vérifier avant de coder

Ouvrir un terminal et exécuter séparément :

```bash
java -version
mvn -v
```

Les deux sorties doivent annoncer Java 25. Si Maven affiche une autre version Java, demander à l’enseignant de régler le JDK du terminal et du projet. Si `mvn` est introuvable, cela concerne l’installation de l’outil, pas le code de votre contrôleur.

Ouvrir le dossier de l’atelier dans l’IDE comme projet Maven. Attendre la fin de l’import des dépendances. Ne pas copier seulement un fichier `.java` dans un projet console vide : les bibliothèques Spring seraient absentes.

## Où exécuter les commandes ?

Pour le cours 1, ouvrir un terminal **dans `supports-spring/ateliers/01-demarrage`**, le dossier où se trouve son `pom.xml`.

```bash
mvn spring-boot:run
```

Le serveur doit rester actif pendant que vous utilisez le navigateur ou un second terminal. Attendre `Started Application`. Pour arrêter : Ctrl+C dans le terminal du serveur. Après chaque modification Java dans ces ateliers, arrêter et relancer ; le rechargement automatique n’est pas configuré.

Pour changer d’atelier, arrêter le précédent, ouvrir le nouveau dossier et lancer la commande à cet endroit. Les ateliers qui exposent l’API de l’annuaire utilisent le port 8080 : **un seul de ces serveurs à la fois**.

## Envoyer des requêtes

Pour les premières lectures, saisir une URL dans le navigateur suffit. Pour voir le statut HTTP :

```bash
curl -i 'http://localhost:8080/hello'
```

Les commandes curl des cours sont écrites pour Bash/zsh ou un terminal compatible, comme Git Bash. Dans PowerShell, `curl.exe -i http://localhost:8080/hello` appelle explicitement curl pour une lecture.

Pour un POST JSON dans PowerShell, utiliser cette alternative lorsque les guillemets de curl posent problème :

```powershell
$body = @{ name = "Ana" } | ConvertTo-Json
Invoke-WebRequest -Uri 'http://localhost:8080/users' -Method Post -ContentType 'application/json' -Body $body
```

`Invoke-WebRequest` affiche le statut et le contenu d’une réponse réussie ; il signale les réponses 4xx comme erreurs. Les exercices d’observation des statuts se font plus simplement avec les commandes curl dans un terminal Bash/zsh fourni en salle. Ne pas confondre une erreur HTTP attendue et une panne de compilation.

## Dépanner dans l’ordre

| Symptôme | Vérification utile |
| --- | --- |
| Pas de `pom.xml` trouvé | Ouvrir le bon dossier avant de lancer Maven |
| Imports Spring en rouge | Attendre/recharger l’import Maven, vérifier le réseau |
| Erreur de compilation | Lire le premier fichier et numéro de ligne signalés |
| Port 8080 déjà utilisé | Arrêter l’autre atelier plutôt que lancer plusieurs copies |
| Connexion refusée | Vérifier que le serveur a démarré et reste actif |
| Réponse 404 | Vérifier le chemin demandé et le mapping du contrôleur |
| Ancien texte toujours affiché | Arrêter, relancer puis actualiser la page |
| Test qui échoue | Comparer valeur attendue et valeur obtenue dans le rapport |
| Base H2 verrouillée | Arrêter l’autre processus utilisant le même fichier |

## Utiliser les solutions sans sauter l’apprentissage

La démo est un temps guidé du cours : commencer par observer ce qu’elle fait, puis relier le résultat au concept et au diagramme UML. L’atelier associé est un projet distinct où appliquer les modifications demandées dans le TP. Le volet « Aide et correction du TP » montre la solution de cette modification. Au début, les tests fournis servent à vérifier le projet ; leur écriture devient un objectif au cours 6.
