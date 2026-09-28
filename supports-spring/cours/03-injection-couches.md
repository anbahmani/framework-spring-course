# Cours 3 — Comprendre les objets gérés par Spring et séparer les rôles

---

## 1. Un besoin très simple de séparation

Notre contrôleur salue un utilisateur. Demain, un autre écran doit utiliser la même règle de salutation. Copier cette règle dans plusieurs contrôleurs rendrait son évolution plus difficile.

Nous créons une classe qui sait saluer et laissons au contrôleur la lecture de la requête. C’est un premier choix d’**architecture** : organiser les responsabilités pour comprendre et faire évoluer le programme.

```diagram
Demande HTTP -> HelloController -> GreetingService -> Reponse texte
                     lit le nom      fabrique le message
```

Une **couche** regroupe des responsabilités de même nature. Cela n’impose pas une machine différente : ces deux classes fonctionnent dans la même application Java.

---

## 2. Faire d’abord l’assemblage en Java

```java
public class GreetingService {
    public String greet(String name) { return "Bonjour " + name; }
}
```

Le contrôleur utilise ce service. En Java, une solution consiste à recevoir l’objet au constructeur :

```java
public class HelloController {
    private final GreetingService service;
    public HelloController(GreetingService service) {
        this.service = service;
    }
}
```

On pourrait assembler les deux manuellement : `new HelloController(new GreetingService())`. Le mot **dépendance** signifie ici « objet dont une classe a besoin ». Une **injection** consiste à fournir cette dépendance depuis l’extérieur, au lieu de la créer à l’intérieur.

Ne pas confondre cette dépendance entre objets avec une dépendance Maven, qui est une bibliothèque ajoutée au projet.

---

## 3. Confier cet assemblage à Spring

Dans l’atelier, ajouter `@Service` sur `GreetingService` et `@RestController` sur `HelloController` indique à Spring quelles classes il doit gérer. Un objet géré par Spring s’appelle un **bean**. L’ensemble qui crée et assemble les beans est le **conteneur**.

Au démarrage, Spring découvre ces classes dans les packages de l’application, crée le service puis le fournit au constructeur du contrôleur. C’est l’**injection de dépendances**. Avec le constructeur unique de notre exemple, aucune annotation supplémentaire n’est nécessaire sur ce constructeur.

```uml-sequence
participant spring as Conteneur Spring
participant service as GreetingService
participant controller as HelloController
spring -> service: cree le service
spring -> controller: appelle le constructeur avec GreetingService
controller --> spring: instance du controleur creee
```

```java
@Service
public class GreetingService {
    public String greet(String name) { return "Bonjour " + name; }
}
```

Les annotations viennent de bibliothèques différentes selon leur rôle : l’IDE peut ajouter les imports. Les fichiers complets de l’atelier contiennent les imports corrects.

```diagram
Application Spring -> Cherche les composants -> Cree GreetingService -> Injecte le service -> Cree HelloController
```

---

## 4. Suivre une requête après l’assemblage

```java
@RestController
public class HelloController {
    private final GreetingService service;

    public HelloController(GreetingService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(name="name", defaultValue="tout le monde") String name) {
        return service.greet(name);
    }
}
```

Le constructeur intervient lors de la création de l’objet. La méthode `hello` intervient quand une requête arrive. Ce sont deux moments différents.

```diagram
Demarrage -> Spring assemble les objets -> Requete /hello -> hello(name) -> service.greet(name)
```

Pour `/hello?name=Ana`, Spring lit le paramètre et appelle `hello("Ana")`. Le contrôleur appelle ensuite le service comme n’importe quel objet Java. Le résultat revient au contrôleur puis au client.

```uml-sequence
participant browser as Navigateur
participant controller as HelloController
participant service as GreetingService
browser -> controller: GET /hello?name=Ana
controller -> service: greet("Ana")
service --> controller: Bonjour Ana
controller --> browser: HTTP 200 avec texte
```

---

## 5. Ce que la séparation apporte

| Modification | Classe concernée principalement |
| --- | --- |
| Changer `/hello` en `/greeting` | Contrôleur |
| Ajouter `!` au message | Service |
| Changer le paramètre HTTP | Contrôleur |
| Appliquer la même règle depuis un autre contrôleur | Réutiliser le service |

Le service n’a pas besoin de connaître une URL ou un navigateur. On peut appeler `new GreetingService().greet("Ana")` dans un programme Java ordinaire. Nous exploiterons cette propriété pour écrire des tests.

Par défaut, Spring réutilise une même instance de chaque bean de ce type dans son conteneur : c’est la portée **singleton**. Éviter de stocker le nom de la personne courante dans un attribut du service ; utiliser un paramètre et une variable locale évite de mélanger les demandes.

---

## 6. Comprendre une erreur d’assemblage

Si `GreetingService` n’est plus déclaré comme bean, le contrôleur demande un objet que Spring ne sait plus fournir. L’application échoue au démarrage avec un message indiquant un bean manquant.

Il faut examiner l’erreur, la présence de `@Service`, les imports et le package. Ajouter un `new` au hasard dans le contrôleur ferait perdre l’exercice d’assemblage. Plusieurs beans candidats au même type constituent un autre cas, réservé à plus tard.

Référence : [injection de dépendances Spring](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html).

---

## Démonstration — Contrôleur, service et injection

**Place dans le cours :** cette démonstration illustre le concept présenté dans la partie précédente. Le [projet de démonstration du cours 3](https://github.com/anbahmani/framework-spring-demos/tree/main/course-03-injection-couches) permet de voir le comportement complet et les principaux composants. Elle sert d’exemple commenté ; les modifications sont réservées au TP.

La démonstration suit une requête de salutation. Spring repère le contrôleur et le service, construit les objets au démarrage, puis fournit le service au constructeur du contrôleur. Lorsqu’une requête arrive, le contrôleur délègue la règle de salutation au service.

`HelloController` porte la frontière HTTP ; `GreetingService` réalise le traitement. L’injection assemble les composants avant les requêtes et rend leurs responsabilités visibles.

---

## TP guidé — observer puis utiliser l’injection

**Projet :** [atelier 03](../ateliers/03-injection/README.md). Arrêter le serveur précédent.

### Étape 1 — identifier les rôles

Ouvrir `Application`, `HelloController` et `GreetingService`. Colorier ou noter : classe de démarrage, contrôleur, service. Entourer l’argument du constructeur et son affectation à l’attribut.

### Étape 2 — suivre une demande

Lancer le projet avec `mvn spring-boot:run`. Appeler `/hello?name=Ana`. **Attendu :** `Bonjour Ana`. Écrire les noms des méthodes appelées dans l’ordre, sans supposer que Spring rappelle le constructeur à chaque requête.

### Étape 3 — déplacer une règle

Modifier uniquement `GreetingService` pour que le nom apparaisse en majuscules. Après redémarrage, `/hello?name=Ana` doit répondre `Bonjour ANA`. Vérifier que le contrôleur n’a pas changé.

### Étape 4 — observer un bean manquant

Retirer temporairement `@Service`, arrêter et relancer. Chercher dans le message d’échec le nom `GreetingService`. Expliquer le lien avec le constructeur du contrôleur. Remettre l’annotation et relancer avec succès.

### Étape 5 — réutiliser le service

Ajouter `/welcome?name=Lea` dans le même contrôleur. La nouvelle méthode doit appeler le même service. **Attendu :** `Bonjour LEA`, sans recopier la règle de mise en majuscules.

**À rendre :** les deux classes et un schéma des appels. **Réussite :** règle écrite une seule fois, constructeur expliqué et application à nouveau fonctionnelle après la manipulation.

<details>
<summary>Aide et correction du TP</summary>

Dans le service :

```java
public String greet(String name) {
    return "Bonjour " + name.toUpperCase(java.util.Locale.ROOT);
}
```

Dans le contrôleur :

```java
@GetMapping("/welcome")
public String welcome(@RequestParam(name="name", defaultValue="tout le monde") String name) {
    return service.greet(name);
}
```

`Locale.ROOT` rend l’exemple indépendant de la langue du poste. `@Service` permet au conteneur de découvrir le service et de fournir l’objet demandé par le constructeur.

</details>

---

## Quiz — 8 questions

1. Qu’appelle-t-on un bean Spring ?
2. Quelle dépendance reçoit `HelloController` dans son constructeur ?
3. Dans l’atelier, qui crée et fournit le service au contrôleur ?
4. Pourquoi `@Service` n’est-il pas le nom d’une méthode à appeler ?
5. Où modifier la règle de salutation pour la réutiliser partout ?
6. Deux couches logiques exigent-elles deux ordinateurs ?
7. Pourquoi éviter un attribut `currentName` dans le service partagé ?
8. Si un message de démarrage indique un service introuvable, citer deux vérifications utiles.

<details>
<summary>Corrigé du quiz</summary>

1. Un objet dont Spring gère notamment la création et l’assemblage.
2. Un `GreetingService`.
3. Le conteneur Spring, au démarrage dans cet exemple.
4. C’est une annotation décrivant le rôle d’une classe et permettant sa découverte.
5. Dans `GreetingService` ; les contrôleurs utilisent sa méthode.
6. Non : il s’agit d’une séparation des responsabilités dans le code.
7. Plusieurs demandes peuvent partager le même objet et écraser cet état ; utiliser un paramètre.
8. Vérifier `@Service` et le package situé sous celui d’`Application` ; vérifier aussi les imports et le type attendu.

</details>