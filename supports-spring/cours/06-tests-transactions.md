# Cours 6 — Vérifier son application et découvrir les transactions

---

## 1. Pourquoi un test automatisé ?

Nous avons vérifié l’API avec curl. Mais après chaque modification, refaire tous les essais à la main devient long et facile à oublier. Un **test automatisé** exécute une action puis compare le résultat à ce qui est attendu.

Une **assertion** est cette comparaison. Si elle échoue, le test échoue. Un test réussi prouve seulement le comportement qu’il vérifie dans ses conditions : il ne prouve pas que tout le programme est correct.

JUnit est la bibliothèque qui organise les tests Java dans les ateliers. `@Test` marque une méthode à exécuter comme test. Maven lance les tests par `mvn test` ; lire ensuite le nombre d’échecs et d’erreurs dans le terminal.

---

## 2. Commencer avec une classe Java simple

Nous savons créer le service du cours 3 sans démarrer de serveur. Le même principe permet ce test :

```java
@Test
void greetingContainsTheName() {
    GreetingService service = new GreetingService();
    String result = service.greet("Ana");
    assertEquals("Bonjour Ana", result);
}
```

Les trois étapes sont : préparer l’objet, agir, vérifier. `assertEquals` reçoit ici la valeur attendue puis la valeur réelle. Ce code est illustratif : le `GreetingService` se trouve dans l’atelier 03. Le TP utilisera le `UserService` de l’atelier 06.

Un **test unitaire** vise une petite partie du code isolée. Un **test d’intégration** vérifie plusieurs composants assemblés, par exemple Spring et la base de données.

```diagram
Preparation -> Action testee -> Resultat obtenu -> Assertion compare -> Test reussi ou echoue
```

---

## 3. Vérifier le contrat HTTP avec MockMvc

**MockMvc** envoie une requête à la chaîne de traitement Spring MVC dans le test, sans ouvrir un serveur sur le port 8080.

```java
mvc.perform(get("/users/999999"))
   .andExpect(status().isNotFound());
```

`perform` exécute la requête simulée ; `andExpect` exprime le résultat attendu. Dans un test où cet identifiant n’existe pas, on attend 404. Un test de création peut aussi vérifier 201 et le nom dans le JSON.

Dans `UserApiTest`, `@SpringBootTest` démarre le contexte Spring pour assembler les composants ; `@AutoConfigureMockMvc` prépare l’outil MVC. Le champ annoté `@Autowired` reçoit l’objet de test fourni par Spring. Cette injection dans un champ du test est un raccourci ; nos classes applicatives gardent l’injection par constructeur.

```uml-sequence
participant test as Test Java
participant mockmvc as MockMvc
participant controller as UserController
participant service as UserService
test -> mockmvc: demande GET /users/999999
mockmvc -> controller: transmet la requete MVC
controller -> service: cherche l utilisateur
service --> controller: UserNotFound
controller --> mockmvc: reponse HTTP 404
mockmvc --> test: assertion de statut satisfaite
```

```diagram
Test Java -> Requete MockMvc -> Controleur Spring -> Resultat HTTP -> Assertion de statut
```

---

## 4. Ne pas confondre données de test et données du TP

Les tests fournis utilisent une base H2 **en mémoire**, créée pour leur exécution. Elle est distincte de la base en fichier utilisée lors de `spring-boot:run`. Lancer `mvn test` ne doit donc pas effacer l’annuaire que vous avez saisi manuellement.

Un test doit préparer ses données ou indiquer précisément celles qu’il attend. Tester une ligne « qui devrait déjà être là depuis mon essai d’hier » rend le résultat dépendant du poste et de l’ordre d’exécution.

---

## 5. Le problème des deux écritures

Imaginons une opération qui crée deux utilisateurs ensemble. La première création réussit, mais le second nom est vide. Si les opérations sont enregistrées indépendamment, le premier utilisateur peut rester en base malgré l’échec global.

Une **transaction** regroupe des opérations de base de données en une unité de travail. **Commit** signifie valider les changements ; **rollback** signifie annuler les changements de cette transaction.

```text
Début → créer Ana → deuxième nom invalide → rollback
Résultat voulu : aucune des deux créations n’est conservée.
```

C’est une question de comportement métier : l’opération promise est « créer la paire », pas « essayer séparément deux créations ».

```diagram
Debut transaction -> Sauver Ana -> Deuxieme nom invalide -> Rollback -> Aucune creation conservee
```

---

## 6. Déclarer l’unité de travail avec Spring

```java
@Transactional
public void createPair(String first, String second) {
    create(first);
    create(second);
}
```

Dans l’atelier 06, cette méthode appartient à `UserService`. L’annotation `@Transactional` est celle de Spring. Quand un autre composant appelle cette méthode sur le service géré par Spring, l’infrastructure encadre les opérations de base.

`InvalidName` étend `RuntimeException` : si elle sort de la méthode, Spring annule normalement la transaction. Une fin normale permet sa validation. D’autres types d’exceptions ou appels nécessitent des précisions ; nous nous limitons à ce cas expliqué et testé.

```uml-sequence
participant test as TransactionTest
participant service as UserService transactionnel
participant repository as UserRepository
participant database as H2 en memoire
test -> service: createPair("Ana", " ")
service -> repository: save(Ana)
repository -> database: INSERT Ana
database --> repository: ecriture en attente
service --> test: InvalidName sur le deuxieme nom
test -> repository: count()
repository -> database: SELECT COUNT(*)
database --> repository: zero ligne apres rollback
repository --> test: aucune creation conservee
```

```diagram
Appel du service -> Spring ouvre la transaction -> Methode termine -> Commit
Appel du service -> Spring ouvre la transaction -> Exception sort -> Rollback
```

Pour ce TP, utiliser **le service injecté dans le test**, pas `new UserService(...)`, afin d’exercer aussi le comportement fourni par Spring. L’infrastructure d’interception détaillée sera étudiée plus tard.

---

## Démonstration — Tests HTTP et annulation transactionnelle (15 min)

**Place dans le cours :** cette démonstration illustre le concept présenté dans la partie précédente. Le [projet de démonstration du cours 6](https://github.com/anbahmani/framework-spring-demos/tree/main/course-06-tests-transactions) permet de voir le comportement complet et les principaux composants. Elle sert d’exemple commenté ; les modifications sont réservées au TP.

La démonstration montre deux façons de vérifier l’application. Un test avec MockMvc envoie une requête au contrôleur sans démarrer de serveur HTTP accessible par le réseau. Un test de service vérifie qu’une opération transactionnelle qui échoue n’enregistre aucune des écritures commencées.

`UserApiTest` illustre le contrat HTTP ; `TransactionTest` observe le tout-ou-rien de la transaction. La base H2 est isolée pour les tests afin que leur exécution ne modifie pas les données de l’application.

---

## TP guidé — lire un test, en ajouter un, observer un rollback

**Projet :** [atelier 06](../ateliers/06-tests/README.md). Durée : 65 min. Il reprend l’annuaire du cours 5 avec une opération supplémentaire et des tests. Aucun serveur à lancer.

### Étape 1 — exécuter la vérification (10 min)

Ouvrir le terminal dans `06-tests`, lancer `mvn test`. **Attendu initial :** 5 tests réussis. Repérer les fichiers sous `src/test/java/fr/miage/debut` et distinguer tests et code de l’application.

### Étape 2 — comprendre une assertion HTTP (10 min)

Ouvrir `UserApiTest`, lire `rejectEmptyName`. Identifier le JSON envoyé et le statut attendu. Remplacer temporairement `isBadRequest()` par `isOk()` et relancer. **Attendu :** un échec signalant 200 attendu et 400 obtenu. Remettre la bonne assertion.

### Étape 3 — écrire une variante (15 min)

Dupliquer ce test sous le nom `rejectMissingName`, en envoyant `{}` comme corps. Garder le statut attendu 400. **Attendu :** 6 tests réussis après cet ajout. L’absence de propriété produit ici un nom `null`, rejeté par le service.

### Étape 4 — observer le tout-ou-rien (15 min)

Lire `TransactionTest.secondInvalidNameCancelsBothCreations`. Il vide sa base de test, appelle `createPair("Ana", " ")`, attend `InvalidName` et vérifie `repository.count() == 0` après l’échec. Retirer temporairement `@Transactional` de `createPair`, puis lancer seulement :

```bash
mvn -Dtest=TransactionTest test
```

**Attendu :** le test d’échec ne passe plus ; le premier utilisateur reste enregistré. Remettre l’annotation et relancer : les deux tests de transaction passent.

### Étape 5 — expliquer et stabiliser (15 min)

Relancer tous les tests, noter le résultat et dessiner ce qui se passe avec l’annotation puis sans elle. Dans `NameRuleTest`, `mock(UserRepository.class)` fournit un faux repository et `verifyNoInteractions` vérifie qu’il n’est pas appelé pour un nom invalide ; la création de mocks est une lecture accompagnée, pas un exercice à reproduire de mémoire.

**À rendre :** nouveau test, dessin du rollback et résultat final. **Réussite :** test ajouté utile, bonne assertion restaurée et transaction rétablie. Ne pas annoter le test lui-même avec `@Transactional` : cela changerait l’expérience observée.

<details>
<summary>Aide et correction du TP</summary>

Dans `UserApiTest` :

```java
@Test
void rejectMissingName() throws Exception {
    mvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());
}
```

Sans transaction de service, la première opération `save` peut se valider avant la seconde. Avec la transaction englobante et l’exception non absorbée, la première écriture est annulée. Les bases de tests sont isolées de `data/users`.

</details>

Références : [MockMvc](https://docs.spring.io/spring-framework/reference/testing/mockmvc.html) et [transactions Spring](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html).

---

## Quiz — 8 questions

1. À quoi sert une assertion ?
2. Quelle commande lance les tests Maven ?
3. Quel résultat attend `assertEquals(0, repository.count())` ?
4. MockMvc ouvre-t-il obligatoirement le port 8080 ?
5. Quelle différence étudie-t-on entre test unitaire et test d’intégration ?
6. Que signifie rollback ?
7. Dans `createPair`, pourquoi grouper les deux créations dans une transaction ?
8. Un test réussi prouve-t-il tous les comportements de l’application ?

<details>
<summary>Corrigé du quiz</summary>

1. À vérifier un résultat réel par rapport à une attente explicite.
2. `mvn test` depuis le dossier du projet.
3. Aucun utilisateur dans la base concernée à ce moment du test.
4. Non : il exerce MVC sans serveur HTTP en écoute.
5. Le premier isole une petite partie ; le second vérifie l’assemblage de plusieurs composants.
6. L’annulation des changements de la transaction.
7. Pour qu’une erreur sur le deuxième nom ne laisse pas une création partielle.
8. Non : seulement les cas qu’il couvre dans les conditions du test.

</details>