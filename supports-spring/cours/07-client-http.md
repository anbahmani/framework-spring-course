# Cours 7 — Appeler une API depuis un programme Spring

**Durée : 3 h. Prérequis : GET, JSON, service, injection et annuaire du cours 5.**

**Déroulé indicatif :** 15 min de rappel, 50 min d’explications, 25 min de démonstration guidée, 10 min de pause, 65 min de TP et 15 min de quiz/correction.

Objectifs : distinguer serveur et client dans deux applications Java, lire une API avec `RestClient`, convertir la réponse et reconnaître une panne de connexion. Pas de fournisseur externe, compte ou service payant à configurer.

---

## 1. Une application Java peut aussi être cliente

Jusqu’ici, le navigateur ou curl appelait notre serveur. Un autre programme Java peut envoyer la même requête. Les rôles client et serveur dépendent de l’échange, pas du langage utilisé.

```diagram
Client Java -> GET /users -> Serveur Spring -> Reponse JSON -> Client affiche les utilisateurs
```

Les applications sont deux processus distincts. Le client ne peut pas accéder directement aux objets en mémoire du serveur. Il utilise son adresse et son contrat HTTP. Il serait possible que les deux processus fonctionnent sur des machines différentes ; ici tout reste local.

---

## 2. Réutiliser le contrat que nous connaissons

Nous savons que GET `/users` retourne un tableau, par exemple :

```json
[{"id":1,"name":"Ana"},{"id":2,"name":"Lea"}]
```

Le client prépare une représentation Java de ces données : `UserView(long id, String name)`. Le serveur et le client possèdent chacun leur propre classe. Le réseau transporte le JSON, pas une référence vers le même objet Java.

Si le serveur change ses champs ou leur sens, le client peut devoir évoluer. Le **contrat** est donc un accord sur l’adresse, la méthode, le format et la signification des données.

---

## 3. Construire un client HTTP avec Spring

```java
@Service
public class DirectoryClient {
    private final RestClient client;

    public DirectoryClient(RestClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:8080").build();
    }

    public UserView[] list() {
        return client.get().uri("/users").retrieve().body(UserView[].class);
    }
}
```

`RestClient` est l’outil qui envoie les requêtes HTTP. Spring Boot fournit le `RestClient.Builder` demandé au constructeur. Un **builder** est un objet qui aide à préparer un autre objet : ici on lui donne l’adresse de base, puis `build()` crée le client configuré.

```uml-sequence
participant runner as ClientRunner
participant client as DirectoryClient
participant server as Serveur Spring
runner -> client: list()
client -> server: GET http://localhost:8080/users
server --> client: reponse HTTP avec JSON
client --> runner: tableau UserView[]
```

Lire la dernière ligne de gauche à droite : `get()` choisit GET ; `uri("/users")` complète l’adresse ; `retrieve()` prépare la lecture de la réponse ; `body(UserView[].class)` la convertit en tableau d’objets Java. Le symbole `[]` désigne bien un tableau Java.

Cet appel est **synchrone** : la suite du code attend que l’appel fournisse un résultat ou échoue. Un serveur lent peut donc faire attendre le client.

```diagram
RestClient construit la requete -> Envoie GET -> Attend la reponse -> Convertit JSON en UserView[]
```

---

## 4. Exécuter une action au démarrage

L’atelier 07 n’expose pas de routes. Il appelle l’annuaire puis se termine. La propriété `spring.main.web-application-type=none` indique que nous ne voulons pas démarrer un serveur web pour ce client.

`ClientRunner` implémente `CommandLineRunner`, une interface Spring Boot dont la méthode `run` est appelée une fois après préparation de l’application. Elle reçoit `DirectoryClient` par constructeur et parcourt le tableau :

```java
UserView[] users = directory.list();
if (users != null) {
    System.out.println("Utilisateurs reçus : " + users.length);
    for (UserView user : users) {
        System.out.println(user.id() + " : " + user.name());
    }
}
```

Nous connaissons déjà la boucle `for` et l’écriture console. Les nouvelles notions sont le point d’exécution fourni par Spring et l’origine distante des données.

---

## 5. Quand le serveur n’est pas disponible

Si le serveur est arrêté, il ne peut envoyer aucun statut HTTP. L’échec est une erreur de connexion. C’est différent d’une réponse 404, qui suppose qu’un serveur a répondu.

Le code fourni utilise `try/catch` pour présenter un message compréhensible lorsqu’un appel HTTP échoue :

```java
try {
    UserView[] users = directory.list();
    // Utiliser les données reçues.
} catch (RestClientException error) {
    System.out.println("Impossible de lire l'annuaire : vérifier le serveur et son adresse.");
}
```

Un annuaire vide doit produire `Utilisateurs reçus : 0`. Une connexion échouée doit produire un message d’échec. Transformer toute erreur en tableau vide masquerait la différence entre « aucun utilisateur » et « impossible de savoir ».

```diagram
Serveur disponible -> Reponse HTTP -> Client lit les donnees
Serveur arrete -> Aucune reponse HTTP -> Erreur de connexion
```

Des délais d’attente explicites sont importants dans une application déployée. Leur configuration, les tentatives multiples et les circuits de protection seront un approfondissement ; ils ne sont pas à réaliser pour ce premier client local.

---

**Démo associée :** [ouvrir le code du cours 7 dans le dépôt GitHub](https://github.com/anbahmani/framework-spring-course/tree/master/supports-spring/demos/course-07-client-http). Le dépôt regroupe les démos des huit séances ; les instructions de lancement sont dans le README de ce dossier.

## TP guidé — faire communiquer deux programmes

**Projets :** [atelier 05](../ateliers/05-persistance/README.md) côté serveur et [atelier 07](../ateliers/07-client/README.md) côté client. Durée : 65 min. Deux terminaux sont nécessaires.

### Étape 1 — préparer le serveur (10 min)

Dans le terminal A, ouvrir `05-persistance` et lancer `mvn spring-boot:run`. Si des utilisateurs existent déjà, les conserver. Sinon, dans un autre terminal, envoyer un POST avec `{"name":"Ana"}` puis vérifier GET `/users`.

### Étape 2 — lancer le client (10 min)

Dans le terminal B, ouvrir `07-client` et lancer `mvn spring-boot:run`. **Attendu :** le nombre d’utilisateurs, leurs identifiants et leurs noms s’affichent dans B. Le serveur A reste démarré. Le client B rend la main après son action.

### Étape 3 — tracer l’échange (10 min)

Repérer `baseUrl`, `uri` et `UserView`. Écrire l’adresse finale appelée. Comparer les données affichées au JSON obtenu avec curl. Les identifiants peuvent différer des exemples selon les créations précédentes.

### Étape 4 — modifier l’affichage (15 min)

Dans `ClientRunner`, afficher chaque ligne sous la forme `Utilisateur #1 : Ana`, en conservant les vraies valeurs reçues. Relancer seulement le client. Ne pas modifier le serveur pour ce changement de présentation.

### Étape 5 — observer un échec réel (10 min)

Arrêter le serveur A, puis relancer B. **Attendu :** le message « Impossible de lire l’annuaire… ». Ce n’est pas une réponse vide du serveur, puisqu’il est arrêté.

### Étape 6 — reprendre (10 min)

Relancer A puis B. **Attendu :** les données redeviennent lisibles. Dessiner les deux processus et indiquer lequel doit rester en écoute.

**À rendre :** modification de l’affichage et tableau à deux lignes « serveur démarré / arrêté » avec observations. **Réussite :** deux applications distinctes, données réellement lues par HTTP et différence entre panne et annuaire vide comprise.

<details>
<summary>Aide et correction du TP</summary>

```java
System.out.println("Utilisateur #" + user.id() + " : " + user.name());
```

Adresse finale : `http://localhost:8080/users`. Si les données ne sont pas celles attendues, vérifier quel serveur occupe le port 8080 et le dossier depuis lequel l’atelier 05 a été lancé. L’atelier 07 n’a pas besoin de ce port pour écouter, car il ne joue que le rôle client.

</details>

Référence : [clients REST Spring](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html).

---

## Quiz — 8 questions

1. Un programme Java peut-il jouer le rôle de client HTTP ?
2. Quelle adresse résulte de `baseUrl("http://localhost:8080")` et `uri("/users")` ?
3. Quel format traverse le réseau dans notre exemple ?
4. Quel type Java représente la liste reçue par le client ?
5. Que signifie « appel synchrone » dans ce cours ?
6. Un serveur arrêté peut-il renvoyer un statut HTTP 404 ?
7. Pourquoi ne pas remplacer une panne par un tableau vide ?
8. Faut-il modifier le serveur pour préfixer les lignes affichées dans le terminal client ?

<details>
<summary>Corrigé du quiz</summary>

1. Oui, comme curl ou un navigateur ; le rôle dépend de l’échange.
2. `http://localhost:8080/users`.
3. Du texte JSON dans une réponse HTTP.
4. `UserView[]`, un tableau d’objets `UserView`.
5. Le code attend le résultat ou l’échec de l’appel avant de poursuivre ce traitement.
6. Non : il n’y a pas de réponse de ce serveur ; la connexion échoue.
7. Cela confondrait absence de données et impossibilité de les lire.
8. Non : l’affichage console relève de l’application cliente.

</details>
