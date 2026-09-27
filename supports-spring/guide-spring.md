# Glossaire progressif — Les mots rencontrés dans les cours

Lire les entrées de la séance en cours ; il n’est pas nécessaire de mémoriser toute la liste avant le premier TP.

| Cours | Mot | Sens dans notre parcours |
| --- | --- | --- |
| 1 | Client | Programme qui envoie une demande |
| 1 | Serveur | Programme qui attend et traite des demandes |
| 1 | API | Interface utilisable par un autre programme |
| 1 | Framework | Cadre qui organise l’exécution et appelle notre code |
| 1 | Spring Boot | Outils pour configurer et démarrer l’application Spring |
| 1 | Maven / POM | Outil de préparation du projet / fichier qui le décrit |
| 1 | Dépendance Maven | Bibliothèque nécessaire au projet |
| 1 | Starter | Ensemble de dépendances préparé pour un besoin |
| 1 | Annotation | Information attachée au code, écrite avec `@` |
| 1 | Contrôleur | Classe qui reçoit des demandes HTTP |
| 1 | Port | Numéro permettant de joindre un service sur une machine |
| 2 | HTTP | Protocole de requête et de réponse |
| 2 | Route | Association entre méthode HTTP, chemin et traitement |
| 2 | Statut | Code décrivant le résultat d’une requête |
| 2 | Corps / en-têtes | Contenu du message / informations qui l’accompagnent |
| 2 | JSON | Format texte représentant des objets et tableaux de données |
| 2 | Sérialisation | Transformation de données Java en représentation texte |
| 3 | Dépendance entre objets | Objet dont une classe a besoin pour travailler |
| 3 | Injection | Fourniture d’une dépendance depuis l’extérieur |
| 3 | Bean | Objet géré par Spring |
| 3 | Conteneur | Infrastructure qui crée et assemble les beans |
| 3 | Service | Classe qui porte des opérations et règles applicatives |
| 3 | Couche | Regroupement de responsabilités dans le logiciel |
| 3 | Singleton | Instance partagée pour un bean dans son conteneur |
| 4 | CRUD | Créer, lire, modifier, supprimer |
| 4 | DTO | Petit objet décrivant les données échangées |
| 4 | Désérialisation | Conversion d’une représentation reçue vers un objet Java |
| 4 | Contrat | Accord sur les requêtes et réponses de l’API |
| 5 | Table / ligne / colonne | Ensemble de données / enregistrement / information nommée |
| 5 | Clé primaire | Identifiant unique d’une ligne dans sa table |
| 5 | Entité | Classe décrivant des données persistantes |
| 5 | Repository | Composant offrant les opérations d’accès aux données |
| 5 | Hibernate / H2 | Outil de persistance objet / base utilisée dans le TP |
| 6 | Assertion | Vérification d’un résultat attendu |
| 6 | Test unitaire / intégration | Test isolé / test de composants assemblés |
| 6 | Transaction | Groupe d’opérations de base à valider ou annuler ensemble |
| 6 | Commit / rollback | Validation / annulation des changements de la transaction |
| 7 | RestClient | Outil Spring pour appeler une API HTTP |
| 7 | Synchrone | Le code attend le résultat de l’appel avant de poursuivre |
| 8 | Asynchrone | L’envoi et la fin du traitement sont découplés |
| 8 | Producteur / consommateur | Composant qui envoie / qui reçoit et traite |
| 8 | File / broker | Destination d’attente / logiciel qui gère les échanges |
| 8 | Listener | Méthode appelée lorsqu’un événement ou message arrive |

## Retrouver une annotation par son usage

| Annotation | Première séance | Ce qu’elle indique ici |
| --- | --- | --- |
| `@SpringBootApplication` | 1 | Classe de démarrage |
| `@RestController` | 1 | Contrôleur renvoyant du contenu au client |
| `@GetMapping` | 1 | Route de lecture |
| `@RequestParam`, `@PathVariable` | 2 | Lecture d’un paramètre ou d’un segment du chemin |
| `@Service` | 3 | Service découvert et géré par Spring |
| `@RequestBody` | 4 | Lecture et conversion du corps de requête |
| `@PostMapping`, `@PutMapping`, `@DeleteMapping` | 4 | Routes des autres opérations CRUD |
| `@RestControllerAdvice`, `@ExceptionHandler` | 4 | Traitement commun des erreurs de contrôleur |
| `@Entity`, `@Id` | 5 | Classe persistante et identifiant |
| `@Test` | 6 | Méthode de test |
| `@Transactional` | 6 | Frontière d’une transaction de service |
| `@JmsListener` | 8 | Méthode recevant les messages d’une destination |
