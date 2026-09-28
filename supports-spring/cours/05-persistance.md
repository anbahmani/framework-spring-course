# Cours 5 — Conserver les données avec Spring Data JPA

---

## 1. Pourquoi une base de données ?

Une collection Java appartient au processus en cours. Pour retrouver les informations après son arrêt, nous avons besoin de les stocker durablement. Une **base de données** organise des données et permet de les lire ou de les modifier.

Dans une base relationnelle, une **table** ressemble à un ensemble de lignes de même structure. Une **colonne** décrit une information. Une **clé primaire** identifie chaque ligne sans ambiguïté dans sa table.

| id | name |
| --- | --- |
| 1 | Ana |
| 2 | Lea |

Ici `id` est la clé primaire. Deux utilisateurs pourraient porter le même nom tout en ayant deux identifiants distincts.

---

## 2. Situer les outils sans tout apprendre à la fois

**H2** est la base légère utilisée dans l’atelier. **SQL** est un langage pour demander des opérations à une base relationnelle. Exemple à lire : `select * from app_users` signifie « lire les colonnes des lignes de la table app_users ».

**JPA** décrit comment représenter des données persistantes par des objets Java. **Hibernate** est l’outil qui réalise cette correspondance dans le projet. **Spring Data JPA** fournit des repositories afin d’utiliser des opérations usuelles sans les réécrire.

```diagram
UserController -> UserService -> UserRepository -> Hibernate -> Base H2
```

Une **entité** décrit un objet persistant. Un **repository** offre des opérations pour le stocker et le retrouver. Nous ne demandons pas encore de maîtriser les relations entre plusieurs tables ni les requêtes complexes.

---

## 3. Lire une entité ligne par ligne

Le fichier complet `UserEntity.java` contient les imports. Voici la partie à comprendre :

```java
@Entity
@Table(name="app_users")
public class UserEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected UserEntity() {}
    public UserEntity(String name) { this.name = name; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

`@Entity` déclare la classe persistante. `@Table` choisit le nom de table. `@Id` désigne l’identifiant ; `@GeneratedValue` indique que sa valeur sera générée. `Long`, avec une majuscule, peut valoir `null` avant l’attribution de l’identifiant.

Le constructeur sans argument est utilisé par l’outil de persistance ; le constructeur avec nom sert à notre code. Cette classe n’est pas un contrôleur : aucune méthode HTTP n’y est déclarée.

```diagram
UserEntity en Java -> Hibernate -> Ligne app_users
id devient cle primaire; name devient colonne
```

---

## 4. Une interface dont Spring fournit l’implémentation

```java
public interface UserRepository extends JpaRepository<UserEntity, Long> {}
```

Les paramètres entre chevrons indiquent le type d’entité et le type de son identifiant. `extends` permet d’hériter des méthodes du repository de base. Spring Data fournit l’objet qui implémente l’interface au démarrage ; nous n’écrivons pas `new UserRepository()`.

| Méthode | Utilité dans l’atelier |
| --- | --- |
| `save(entity)` | Enregistrer un utilisateur |
| `findAll()` | Lire tous les utilisateurs |
| `findById(id)` | Chercher une ligne par identifiant |
| `delete(entity)` | Supprimer cette ligne |
| `count()` | Compter les utilisateurs |

`findById` retourne un `Optional` : il peut contenir une entité ou être vide. Dans le code fourni, `.orElseThrow(UserNotFound::new)` transforme l’absence en exception. Cette écriture signifie « si rien n’est trouvé, créer et lancer UserNotFound ».

---

## 5. Relier le repository au service

Le service reçoit désormais `UserRepository` au constructeur. Pour créer :

```java
public UserView create(String name) {
    checkName(name);
    UserEntity entity = new UserEntity(name.strip());
    UserEntity saved = repository.save(entity);
    return new UserView(saved.getId(), saved.getName());
}
```

L’entité sert au stockage ; `UserView` sert à la réponse. Cette séparation permet de choisir ce que reçoit le client. Le contrôleur conserve les mêmes routes et reçoit toujours les mêmes DTO.

```uml-sequence
participant controller as UserController
participant service as UserService
participant repository as UserRepository
participant database as Base H2
controller -> service: create("Ana")
service -> repository: save(UserEntity)
repository -> database: INSERT dans app_users
database --> repository: ligne avec id genere
repository --> service: UserEntity enregistree
service --> controller: UserView pour la reponse
```

**Idée d’architecture à retenir :** le client n’a pas à connaître le détail du stockage. Une nouvelle organisation interne n’impose pas de changer les adresses publiques si le contrat reste identique.

```diagram
Client et routes HTTP -> Controleur -> Service -> Repository -> Base persistante
```

---

## 6. Lire la configuration de l’atelier

```properties
spring.datasource.url=jdbc:h2:file:./data/users
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
```

Une propriété est une ligne `clé=valeur`. L’URL indique une base H2 stockée dans des fichiers sous `data`, à partir du dossier de lancement. `ddl-auto=update` permet à cet atelier de créer/adapter ses tables. `show-sql=true` affiche les requêtes générées pour les observer. La propriété `open-in-view` est fournie : son étude détaillée n’est pas requise ici.

Ce réglage de création automatique est un confort pédagogique, pas une méthode de mise à jour de schéma à généraliser sans étude. Notre objectif est d’observer une table simple. Ne pas lancer deux instances sur le même fichier H2.

```diagram
Lancement -> Lecture de la base fichier -> Requetes -> Arret -> Fichier conserve les donnees
```

---

## Démonstration — Repository et stockage H2

**Place dans le cours :** cette démonstration illustre le concept présenté dans la partie précédente. Le [projet de démonstration du cours 5](https://github.com/anbahmani/framework-spring-demos/tree/main/course-05-persistance) permet de voir le comportement complet et les principaux composants. Elle sert d’exemple commenté ; les modifications sont réservées au TP.

La démonstration reprend l’API d’utilisateurs et montre ce qui change quand les données sont stockées en base. Le contrôleur délègue au service, qui utilise un repository Spring Data JPA ; celui-ci effectue les opérations sur une base H2 configurée en fichier. Les requêtes SQL produites par la persistance peuvent être observées dans la console.

`UserEntity` représente une ligne stockée et `UserRepository` fournit l’accès aux données. Après un redémarrage, les utilisateurs sont toujours présents. Le parcours HTTP reste semblable à celui du cours précédent ; c’est le mécanisme de stockage qui a changé.

---

## TP guidé — vérifier la persistance

**Projet :** [atelier 05](../ateliers/05-persistance/README.md). Arrêter l’atelier 04 ; les projets sont indépendants et ne partagent pas ses données.

### Étape 1 — retrouver les trois rôles

Ouvrir `UserEntity`, `UserRepository` et `UserService`. Dans chacun, retrouver respectivement le mapping, les opérations de stockage et les règles d’utilisation. Repérer l’injection du repository.

### Étape 2 — enregistrer

Démarrer depuis le dossier `05-persistance`. Envoyer le même POST `{"name":"Ana"}` que précédemment. Noter l’identifiant, puis vérifier avec GET. **Attendu :** 201 puis 200, avec le nom enregistré.

### Étape 3 — redémarrer au même endroit

Arrêter par Ctrl+C puis relancer depuis le même dossier. Relire l’URL notée. **Attendu :** l’utilisateur est encore présent. Identifier le dossier `data` sans modifier ses fichiers à la main.

### Étape 4 — observer le SQL

Regarder le terminal lors d’une création puis d’une lecture. Relever les mots `insert` et `select`. Associer chaque mot à l’action réalisée. La syntaxe SQL complète ne sera pas évaluée.

### Étape 5 — ajouter une lecture simple

Ajouter `count()` au service en déléguant au repository. Dans le contrôleur, ajouter GET `/users/count` qui retourne ce nombre. La route fixe `/count` est plus précise que `/{id}` et peut coexister avec elle.

**Attendu :** créer un nouvel utilisateur fait augmenter le nombre de 1 ; supprimer cet utilisateur le fait diminuer de 1. Utiliser la valeur initiale observée, pas une valeur supposée.

### Étape 6 — expliquer

Comparer l’atelier 04 et l’atelier 05 : ce qui est identique pour le client, et ce qui change après redémarrage.

**À rendre :** les deux nouvelles méthodes, deux nombres observés et l’explication du rôle de l’entité.

<details>
<summary>Aide et correction du TP</summary>

Dans `UserService` :

```java
public long count() { return repository.count(); }
```

Dans `UserController`, dont le préfixe est déjà `/users` :

```java
@GetMapping("/count")
public long count() { return service.count(); }
```

Ne pas réécrire `/users/count` sur la méthode, sinon le préfixe serait répété. Si les données semblent absentes, vérifier le dossier de lancement et l’URL de base : chaque chemin de fichier désigne son propre stockage.

</details>

Référence : [repositories Spring Data](https://docs.spring.io/spring-data/jpa/reference/repositories/core-concepts.html).

---

## Quiz — 8 questions

1. Dans la table montrée au début, `name` est-il une ligne ou une colonne ?
2. À quoi sert une clé primaire ?
3. Quelle annotation indique qu’une classe est une entité persistante ?
4. Qui implémente le `UserRepository` vide de notre exemple ?
5. Quelle méthode permet d’enregistrer une entité ?
6. Pourquoi ne demande-t-on pas au client d’inventer l’identifiant généré ?
7. Quel élément de configuration permet de distinguer ici une base en fichier d’une simple collection mémoire ?
8. Pourquoi les routes HTTP peuvent-elles rester identiques après le changement de stockage ?

<details>
<summary>Corrigé du quiz</summary>

1. Une colonne ; chaque utilisateur occupe une ligne.
2. À identifier une ligne sans ambiguïté dans la table.
3. `@Entity`.
4. Spring Data fournit l’implémentation au démarrage.
5. `save`.
6. Son attribution est gérée côté serveur/base selon la configuration de l’entité.
7. L’URL `jdbc:h2:file:./data/users` indique un stockage dans des fichiers.
8. Le contrôleur expose le contrat, tandis que le service et le repository organisent l’accès aux données.

</details>