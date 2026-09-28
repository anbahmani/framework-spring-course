# Cours 2 — Comprendre HTTP et envoyer des données JSON

---

## 1. Une conversation en deux messages

**HTTP** est le protocole de communication utilisé ici entre client et serveur. Le client envoie une **requête** ; le serveur renvoie une **réponse**. Une requête indique notamment une méthode et une adresse.

```text
Requête : GET /hello?name=Ana
Réponse : statut 200, corps « Bonjour Ana »
```

```diagram
Client -> Requete HTTP -> Controleur Spring -> Reponse HTTP
```

La **méthode HTTP** décrit l’action demandée. GET sert à lire. POST, PUT et DELETE seront utilisés au cours 4. Ce mot GET ne désigne pas le nom de la méthode Java : une méthode Java nommée `hello` peut traiter une requête GET.

Le **corps** est le contenu transporté. Les **en-têtes** sont des informations complémentaires ; par exemple `Content-Type` décrit le format du corps. Le **statut** indique le résultat : 200 signifie ici une lecture réussie, 404 une route ou ressource non trouvée, 400 une demande invalide.

---

## 2. Lire les différentes parties d’une URL

```text
http://localhost:8080/users/7?details=true
       ordinateur  port chemin  paramètres de recherche
```

```diagram
URL -> Chemin /users/7 -> Parametre details -> Methode Java
```

`/users/7` désigne une ressource. `?` commence les paramètres de recherche ; `&` sépare plusieurs paramètres. Nous utiliserons des noms simples sans espace dans les adresses de ce premier exercice.

Une **ressource** est un élément accessible par l’API, par exemple un utilisateur. Une **route** relie une méthode HTTP et un chemin à un traitement. Deux routes peuvent utiliser le même chemin avec des méthodes HTTP différentes.

---

## 3. Lire un paramètre avec `@RequestParam`

```java
@GetMapping("/hello")
public String hello(
        @RequestParam(name="name", defaultValue="tout le monde") String name) {
    return "Bonjour " + name;
}
```

`@RequestParam` demande à Spring de lire le paramètre nommé `name` dans l’URL. Sa valeur devient l’argument Java `name`. `defaultValue` donne une valeur de remplacement si le paramètre est absent ou vide.

| Adresse | Réponse |
| --- | --- |
| `/hello?name=Ana` | `Bonjour Ana` |
| `/hello?name=Lea` | `Bonjour Lea` |
| `/hello` | `Bonjour tout le monde` |

Nous n’écrivons pas nous-mêmes le découpage du texte de l’URL : c’est une tâche du framework.

---

## 4. Lire un identifiant avec `@PathVariable`

```java
@GetMapping("/users/{id}")
public UserView user(@PathVariable("id") long id) {
    return new UserView(id, "Ana");
}
```

Les accolades marquent un segment variable. Pour `/users/7`, Spring lit `7`, le convertit en `long` puis appelle `user(7)`. `@PathVariable("id")` fait le lien avec le segment `{id}`.

Ici le nom `Ana` est fixé dans le code : **cette démonstration ne consulte aucun stockage**. Demander `/users/42` renvoie donc l’identifiant 42 et le même nom. La lecture de véritables utilisateurs arrivera au cours 4.

Demander `/users/abc` ne permet pas de construire un `long` : la réponse est 400, avant l’exécution normale de la méthode.

---

## 5. Du Java au JSON

Le fichier `UserView.java` contient :

```java
public record UserView(long id, String name) {}
```

Si les records sont nouveaux : cette écriture Java décrit un petit objet de données avec un constructeur et des accesseurs `id()` et `name()`. Elle évite ici les champs, constructeur et getters écrits à la main. Aucun mécanisme Spring n’est nécessaire pour créer ce record.

**JSON** est un format texte d’échange : un objet est entouré d’accolades, ses propriétés sont séparées par des virgules et les noms de propriétés sont entre guillemets doubles.

```json
{"id":7,"name":"Ana"}
```

```diagram
Objet Java -> Serialisation -> Texte JSON -> Navigateur
```

Transformer l’objet Java en JSON s’appelle la **sérialisation**. Le starter web apporte les outils de conversion ; le contrôleur retourne l’objet et Spring construit la réponse. Nous n’avons pas à concaténer nous-mêmes les accolades et guillemets. L’ordre des propriétés JSON n’a pas d’importance pour notre contrat.

```uml-sequence
participant client as Client HTTP
participant mvc as Spring MVC
participant controller as UserController
participant converter as Convertisseur JSON
client -> mvc: GET /users/7
mvc -> controller: appelle user(7)
controller --> mvc: UserView(7, Ana)
mvc -> converter: convertit l objet en JSON
converter --> mvc: texte JSON
mvc --> client: HTTP 200 avec JSON
```

---

## 6. Observer la réponse autrement que dans le navigateur

`curl` est un programme en ligne de commande qui envoie une requête. `-i` affiche les en-têtes et le statut en plus du corps. Par défaut, cette commande envoie GET :

```bash
curl -i 'http://localhost:8080/users/7'
```

On doit repérer un statut 200, un format `application/json` et le corps attendu. Dans PowerShell, utiliser `curl.exe` pour appeler le programme curl. Les commandes sont à lancer dans un **second terminal**, le premier étant occupé par le serveur.

---

## Démonstration — Requête HTTP et réponse JSON

**Place dans le cours :** cette démonstration illustre le concept présenté dans la partie précédente. Le [projet de démonstration du cours 2](https://github.com/anbahmani/framework-spring-demos/tree/main/course-02-http-json) permet de voir le comportement complet et les principaux composants. Elle sert d’exemple commenté ; les modifications sont réservées au TP.

La démonstration compare une route qui lit un paramètre dans l’URL et une route qui reçoit une valeur dans le chemin. Le contrôleur retourne ensuite un objet Java ; Spring MVC le convertit en JSON dans la réponse HTTP.

`UserController` déclare les routes et récupère les valeurs avec les annotations Spring. `UserView` représente les données renvoyées. On observe ainsi le passage d’une requête HTTP à un objet Java, puis à une réponse JSON.

---

## TP guidé — faire varier les entrées

**Projet :** [atelier 02](../ateliers/02-http-json/README.md). Arrêter l’atelier 01 avant de lancer celui-ci.

### Étape 1 — prédire

Avant de démarrer, écrire les réponses attendues pour `/hello`, `/hello?name=Ana` et `/users/7`, en lisant `UserController`. Lancer ensuite `mvn spring-boot:run` dans le dossier de l’atelier 02 et comparer.

### Étape 2 — observer HTTP

Utiliser `curl -i` pour `/users/7`, `/users/abc` et `/inconnu`. **Attendu :** 200, 400, 404. Pour chaque cas, distinguer « le serveur ne démarre pas » et « le serveur répond à une demande incorrecte ».

### Étape 3 — lire du JSON

Noter les deux propriétés renvoyées pour `/users/7`. Identifier leur type : nombre ou texte. Expliquer pourquoi le navigateur reçoit du texte JSON plutôt qu’un objet Java vivant.

### Étape 4 — ajouter une petite API

Créer `SquareView.java` dans `fr.miage.debut` avec deux champs `int number` et `int result`. Ajouter au contrôleur une route GET `/square` recevant un paramètre `number`, de valeur par défaut 2, puis retournant son carré dans un `SquareView`.

**Attendu :** `/square?number=3` donne `{"number":3,"result":9}`. Limiter les essais à de petits entiers ; la gestion du dépassement de capacité n’est pas le sujet du TP.

### Étape 5 — expliquer

Dessiner la chaîne « URL → argument Java → valeur retournée → JSON ». Indiquer où intervient Spring.

**À rendre :** les deux fichiers modifiés/créés et les trois statuts observés. **Réussite :** le résultat varie avec l’entrée ; l’étudiant distingue chemin et paramètre.

<details>
<summary>Aide et correction du TP</summary>

```java
public record SquareView(int number, int result) {}
```

Dans le contrôleur :

```java
@GetMapping("/square")
public SquareView square(@RequestParam(name="number", defaultValue="2") int number) {
    return new SquareView(number, number * number);
}
```

Ajouter le package dans le fichier du record, comme pour `UserView`. Redémarrer après les changements. Sans paramètre, le résultat vaut 4 ; avec `number=abc`, la conversion échoue et le serveur répond 400.

</details>

Référence : [déclaration des routes Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-requestmapping.html).

---

## Quiz — 8 questions

1. Qui envoie la requête HTTP : client ou serveur ?
2. Dans notre usage, GET sert-il à lire ou à supprimer ?
3. Dans `/hello?name=Ana`, quel est le nom du paramètre et quelle est sa valeur ?
4. Quelle annotation lit le `7` de `/users/7` ?
5. Quel statut indique une lecture réussie : 200, 400 ou 404 ?
6. Dans `{"id":7,"name":"Ana"}`, quelle propriété contient du texte ?
7. Que signifie « sérialiser un objet en JSON » ?
8. Pourquoi `/users/abc` échoue-t-il quand le paramètre Java est un `long` ?

<details>
<summary>Corrigé du quiz</summary>

1. Le client ; le serveur envoie la réponse.
2. À lire une représentation de ressource.
3. Nom `name`, valeur `Ana`.
4. `@PathVariable`.
5. 200.
6. `name` ; `id` est un nombre.
7. Produire une représentation texte de ses données au format JSON.
8. Spring ne peut pas convertir `abc` en entier long ; il renvoie 400.

</details>