# Cours 4 — Créer une petite API de gestion d’utilisateurs

**Durée : 3 h. Prérequis : requête/réponse HTTP, JSON, contrôleur et service.**

**Déroulé indicatif :** 15 min de rappel, 50 min d’explications, 25 min de démonstration guidée, 10 min de pause, 65 min de TP et 15 min de quiz/correction.

Objectifs : envoyer du JSON au serveur, créer et retrouver un utilisateur, reconnaître les quatre opérations de base et retourner une erreur simple. Le stockage reste une collection Java pour se concentrer sur l’API.

---

## 1. Notre annuaire et son contrat

Jusqu’ici, le serveur retournait des données écrites dans le code. Nous voulons maintenant ajouter un utilisateur depuis un client. Le serveur conservera ces données pendant son exécution.

**CRUD** désigne quatre opérations : Create, Read, Update, Delete — créer, lire, modifier, supprimer. Le **contrat de l’API** décrit comment un client les demande et ce qu’il reçoit.

| Action | Requête | Résultat attendu |
| --- | --- | --- |
| Lister | `GET /users` | 200 et tableau JSON |
| Lire un utilisateur | `GET /users/1` | 200 et objet JSON, ou 404 |
| Créer | `POST /users` avec un corps JSON | 201 et utilisateur créé |
| Modifier le nom | `PUT /users/1` avec un corps JSON | 200 et utilisateur modifié, ou 404 |
| Supprimer | `DELETE /users/1` | 204 sans corps, ou 404 |

Nous utilisons des noms de ressources dans les chemins. REST est un style d’organisation des échanges autour de ressources ; ici nous apprenons une API HTTP simple qui suit cette logique.

```diagram
Client -> Requete HTTP -> UserController -> UserService -> Collection en memoire
Client <- Reponse JSON <- UserController <- UserService <- Collection en memoire
```

---

## 2. Envoyer un corps JSON

Une barre d’adresse de navigateur convient à nos essais GET, mais pas à la saisie d’un corps POST. Utilisons curl dans un second terminal :

```bash
curl -i -X POST 'http://localhost:8080/users' -H 'Content-Type: application/json' -d '{"name":"Ana"}'
```

`-X POST` choisit la méthode ; `-H` ajoute un en-tête ; `-d` fournit le corps. `Content-Type: application/json` indique au serveur comment lire ce corps. Nous fournissons seulement le nom : le serveur choisira l’identifiant.

La fiche [démarrage](../demarrage.md) donne une commande PowerShell équivalente si les guillemets du terminal posent problème. Aucun compte ni en-tête de clé API n’est nécessaire dans les ateliers débutants.

---

## 3. Transformer le JSON en argument Java

```java
public record UserRequest(String name) {}
public record UserView(long id, String name) {}
```

Ces deux déclarations sont dans deux fichiers. `UserRequest` décrit l’entrée attendue et `UserView` la sortie. Ce sont des **DTO**, c’est-à-dire de petits objets destinés au transfert de données.

```java
@PostMapping
public ResponseEntity<UserView> create(@RequestBody UserRequest request) {
    UserView created = service.create(request.name());
    return ResponseEntity.created(URI.create("/users/" + created.id())).body(created);
}
```

`@RequestBody` demande la conversion du corps JSON en `UserRequest` : c’est la **désérialisation**. `request.name()` lit le nom. Le service crée l’utilisateur. `ResponseEntity` permet de choisir le statut, les en-têtes et le corps de réponse.

`created(...)` construit une réponse 201 avec un en-tête `Location`, l’adresse du nouvel utilisateur. `.body(created)` ajoute ses données. Les imports exacts sont fournis dans `UserController.java`.

```uml-sequence
participant client as Client HTTP
participant controller as UserController
participant service as UserService
participant map as Map en memoire
client -> controller: POST /users avec nom Ana
controller -> service: create("Ana")
service -> map: ajoute UserView avec id
map --> service: utilisateur cree
service --> controller: UserView
controller --> client: HTTP 201 et JSON
```

```diagram
JSON du client -> UserRequest -> Validation du service -> Nouvel identifiant -> Reponse 201 et Location
```

---

## 4. Regrouper les routes

La classe porte `@RequestMapping("/users")`. Ce préfixe s’applique aux méthodes qu’elle contient. `@GetMapping` sans chemin supplémentaire traite donc `/users`, et `@GetMapping("/{id}")` traite `/users/7`, par exemple.

Le corps de `get` appelle `service.get(id)`. Le corps de `delete` appelle `service.delete(id)`, puis renvoie `ResponseEntity.noContent().build()`, une réponse 204 sans contenu. Ne pas essayer de lire du JSON dans cette réponse vide.

Pour le PUT, le client envoie un nouveau nom au chemin de l’utilisateur existant. L’identifiant ne change pas. Dans ce petit contrat, modifier un identifiant absent renvoie 404.

---

## 5. Stocker dans une collection Java

`UserService` contient une `Map<Long, UserView>` : chaque clé est un identifiant et chaque valeur un utilisateur. `nextId` fournit des identifiants successifs. `put` ajoute/remplace ; `get` lit ; `remove` supprime.

```java
public synchronized UserView create(String name) {
    checkName(name);
    UserView user = new UserView(nextId++, name.strip());
    users.put(user.id(), user);
    return user;
}
```

`strip()` retire les espaces au début et à la fin. Le `synchronized` fourni protège les accès à cette collection partagée dans cette petite application ; sa programmation détaillée n’est pas demandée dans ce TP.

Cette collection disparaît lorsque le processus s’arrête. Ce n’est pas un bug : nous n’avons encore écrit aucun stockage durable. Au cours 5, une base de données prendra ce rôle.

```diagram
Serveur demarre -> Collection vide -> Requetes modifient la collection -> Serveur arrete -> Donnees perdues
```

---

## 6. Refuser une entrée invalide

Le service vérifie une règle très simple : le nom ne doit pas être absent ni ne contenir que des espaces.

```java
private void checkName(String name) {
    if (name == null || name.isBlank()) throw new InvalidName();
}
```

L’exception `InvalidName` est une classe fournie qui étend `RuntimeException`. Lancer une exception interrompt le déroulement normal. Le service décrit ainsi le problème sans construire lui-même une réponse HTTP.

Dans `ApiErrors`, `@RestControllerAdvice` déclare un gestionnaire commun aux contrôleurs. `@ExceptionHandler(InvalidName.class)` choisit le problème traité et `@ResponseStatus(HttpStatus.BAD_REQUEST)` impose le statut 400. Le même principe traduit `UserNotFound` en 404. Les détails du format d’erreur restent volontairement simples : un texte explicatif.

---

## Démonstration guidée — Observer le cycle CRUD et les réponses HTTP (25 min)

**Objectif de la démonstration :** comprendre un parcours Spring complet en observant le projet exécutable avant de le modifier dans le TP. Le code, les étapes de lancement et les vérifications sont regroupés dans le [dépôt dédié des démos — cours 4](https://github.com/anbahmani/framework-spring-demos/tree/main/course-04-api-crud). Java 25 et Maven 3.9 sont requis.

### 1. Démarrer l’API (4 min)

Dans `course-04-api-crud`, exécuter `mvn spring-boot:run`, puis `curl -i http://localhost:8080/users`. Au démarrage, la collection en mémoire est vide.

### 2. Créer et lire une ressource (7 min)

Créer un utilisateur : `curl -i -H 'Content-Type: application/json' -d '{"name":"Ana"}' http://localhost:8080/users`. Repérer HTTP 201, l’en-tête `Location` et l’identifiant dans le JSON. Réutiliser l’URL de `Location` pour la lecture avec GET.

### 3. Modifier puis supprimer (7 min)

Avec l’URL obtenue, envoyer `curl -i -X PUT -H 'Content-Type: application/json' -d '{"name":"Ada"}' http://localhost:8080/users/1`, puis `curl -i -X DELETE http://localhost:8080/users/1`. Observer HTTP 200 puis 204. Dans `UserController`, retrouver les annotations GET, POST, PUT et DELETE et la délégation à `UserService`.

### 4. Observer les erreurs et les rôles (7 min)

Tester `curl -i -H 'Content-Type: application/json' -d '{"name":" "}' http://localhost:8080/users` (400), puis GET `/users/999` (404). Relier `UserService` aux règles métier et `ApiErrors` à la conversion d’exceptions en statuts HTTP. La collection est volatile : le redémarrage efface les données.

**Transition vers le TP :** la démo montre un parcours fonctionnel ; le TP reprend le même sujet pour faire modifier et expliquer le code.

---

## TP guidé — un cycle complet puis une nouvelle règle

**Projet :** [atelier 04](../ateliers/04-crud/README.md). Prévoir 65 min. Utiliser le projet fourni et lire seulement les fichiers indiqués à chaque étape.

### Étape 1 — partir d’un annuaire vide (5 min)

Démarrer l’application puis envoyer `GET /users`. **Attendu :** `[]`, un tableau JSON vide. Ce résultat n’est pas une erreur.

### Étape 2 — créer puis retrouver (15 min)

Exécuter le POST montré plus haut. Repérer le statut 201, `Location` et l’identifiant retourné. Envoyer un GET à cette adresse. **Attendu :** le même identifiant et le nom `Ana`. Ne pas supposer que l’identifiant sera toujours 1 après plusieurs essais.

### Étape 3 — modifier puis supprimer (15 min)

Dans les commandes suivantes, remplacer `1` par l’identifiant obtenu :

```bash
curl -i -X PUT 'http://localhost:8080/users/1' -H 'Content-Type: application/json' -d '{"name":"Lea"}'
curl -i -X DELETE 'http://localhost:8080/users/1'
curl -i 'http://localhost:8080/users/1'
```

**Attendu :** 200 avec `Lea`, puis 204 sans corps, puis 404. Relever ces trois résultats.

### Étape 4 — suivre une erreur (10 min)

Envoyer `{"name":" "}` par POST. **Attendu :** 400 et aucune création. Retrouver `checkName`, `InvalidName` et sa méthode de traitement dans `ApiErrors`.

### Étape 5 — ajouter une règle (15 min)

Dans `checkName`, refuser aussi un nom de plus de 30 caractères après suppression des espaces extérieurs. Conserver la même exception pour cet exercice. Tester un nom de 30 caractères puis de 31 : le premier est accepté, le second refusé. Utiliser le nom compté, pas une estimation visuelle.

### Étape 6 — constater la limite (5 min)

Créer un utilisateur valide, arrêter puis redémarrer le serveur. **Attendu :** le listing est vide. Expliquer où étaient les données.

**À rendre :** méthode `checkName`, tableau des statuts observés et explication de la perte au redémarrage.

<details>
<summary>Aide et correction du TP</summary>

```java
private void checkName(String name) {
    if (name == null || name.isBlank() || name.strip().length() > 30) {
        throw new InvalidName();
    }
}
```

Le `||` évite d’appeler une méthode sur `null` si le premier test est vrai. Pour compter exactement, utiliser une chaîne préparée, par exemple trois fois `abcdefghij` pour 30 caractères, puis ajouter `k` pour 31. Le tableau attendu est : création 201, lecture 200, modification 200, suppression 204, lecture supprimée 404, nom invalide 400.

</details>

Référence : [réponses des contrôleurs Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/return-types.html).

---

## Quiz — 8 questions

1. Que signifient les quatre lettres CRUD ?
2. Quelle méthode HTTP utilisons-nous pour créer un utilisateur ?
3. Quel rôle joue `@RequestBody` ?
4. Pourquoi le client n’envoie-t-il pas l’identifiant dans `UserRequest` ?
5. Quelle information fournit l’en-tête `Location` après création ?
6. Une réponse 204 de notre API contient-elle un utilisateur JSON ?
7. Où place-t-on la règle de nom obligatoire : dans le service ou dans le navigateur ?
8. Pourquoi les utilisateurs disparaissent-ils au redémarrage de cet atelier ?

<details>
<summary>Corrigé du quiz</summary>

1. Create, Read, Update, Delete : créer, lire, modifier, supprimer.
2. POST sur `/users` avec le nom dans le corps.
3. Il demande la conversion du corps de requête en objet Java.
4. L’identifiant est choisi par le serveur lors de la création.
5. L’adresse de l’utilisateur créé, que le client peut ensuite lire.
6. Non : 204 signifie ici succès sans corps de réponse.
7. Dans le service : tous les appels à cette opération doivent respecter la règle.
8. Ils étaient dans une collection en mémoire du processus, sans stockage durable.

</details>
