# Cours 9 — Exposer un service Spring avec MCP

---

## 1. De l’API appelée par un programme à l’outil découvert par un client IA

Dans les cours précédents, nous avons appelé une API HTTP avec une adresse et un contrat connus à l’avance. Avec **Model Context Protocol (MCP)**, une application hôte peut découvrir les capacités d’un serveur puis demander l’exécution d’un outil avec des arguments structurés.

MCP ne remplace pas HTTP ou les API REST. C’est un protocole standardisé pour présenter des capacités à des applications compatibles. Le serveur reste une application Spring ; ses outils appellent ses composants métier.

```diagram
Application hote -> Client MCP -> Protocole MCP -> Serveur Spring -> Service metier
```

---

## 2. Les trois rôles

| Rôle | Responsabilité | Exemple de ce cours |
| --- | --- | --- |
| Hôte | Application qui fournit l’interface à l’utilisateur et gère les connexions MCP | Inspecteur MCP pendant le TP |
| Client MCP | Partie de l’hôte qui parle le protocole à un serveur | Client activé dans l’inspecteur |
| Serveur MCP | Application qui annonce et réalise ses capacités | Serveur Spring Boot de l’annuaire |

Un hôte peut utiliser plusieurs clients pour se connecter à plusieurs serveurs. Le client MCP et le serveur négocient leurs versions et capacités ; ils ne partagent pas leurs objets Java.

```uml-sequence
participant host as Application hote
participant client as Client MCP
participant server as Serveur Spring
participant service as Service metier
host -> client: demarre une connexion MCP
client -> server: initialize et capacites
server --> client: version et capacites acceptees
client -> server: tools/list
server --> client: noms, descriptions et schemas
```

---

## 3. Outils, ressources et prompts

MCP décrit plusieurs types de capacités. Spring AI fournit des annotations pour les déclarer dans une application Spring.

| Capacité | À quoi elle sert | Exemple |
| --- | --- | --- |
| Outil (tool) | Exécuter une opération demandée par l’utilisateur ou le modèle | Rechercher une fiche dans l’annuaire |
| Ressource (resource) | Lire une donnée présentée comme contexte | Consulter un document ou une fiche accessible par URI |
| Prompt | Proposer un modèle de consigne réutilisable | Guider un diagnostic selon un scénario |

Nous allons implémenter un outil en lecture seule. Les ressources et prompts sont des capacités différentes ; une méthode Java ne devient pas l’une ou l’autre simplement parce qu’elle renvoie du texte.

---

## 4. Ce que MCP échange

Les messages MCP utilisent JSON-RPC 2.0 et un cycle de découverte. Le client initialise la connexion, demande les outils disponibles, puis en invoque un par son nom et ses arguments.

```uml-sequence
participant client as Client MCP
participant server as Serveur Spring
participant tool as Outil annuaire
participant service as DirectoryService
client -> server: initialize
server --> client: protocolVersion et capabilities
client -> server: tools/list
server --> client: findUserById(id: number)
client -> server: tools/call(name, arguments)
server -> tool: appelle la methode annotee
tool -> service: findUserById(id)
service --> tool: resultat metier
tool --> server: resultat de l outil
server --> client: reponse JSON-RPC
```

Le schéma de l’outil décrit ses paramètres. Une bonne description aide le client à choisir et appeler la bonne capacité, mais ne remplace ni la validation Java ni les règles métier.

---

## 5. Le transport : comment les messages arrivent au serveur

Le protocole MCP et son transport répondent à deux questions différentes : **quels messages échanger ?** et **par quel canal les transporter ?**

| Transport | Canal | Exemple d’utilisation |
| --- | --- | --- |
| STDIO | Entrée et sortie standard d’un processus local | Hôte qui démarre un serveur sur le même poste |
| Streamable HTTP | Requêtes HTTP vers un serveur indépendant, avec flux facultatif | Serveur Spring sur `http://localhost:8080/mcp` |

Ce cours utilise Streamable HTTP pour rendre visibles deux applications séparées. Spring AI propose aussi un transport SSE historique ; le cours ne l’utilise pas. Le transport ne définit pas à lui seul l’authentification ni les permissions métier.

```diagram
Inspecteur MCP -> HTTP POST /mcp -> Spring AI -> Outil annuaire
```

---

## 6. Déclarer un outil avec Spring AI

Le projet de démonstration utilise une annotation sur une méthode d’un bean Spring :

```java
@Component
public class DirectoryTools {
    private final DirectoryService directory;

    public DirectoryTools(DirectoryService directory) {
        this.directory = directory;
    }

    @McpTool(description = "Recherche une fiche utilisateur par son identifiant")
    public String findUserById(
            @McpToolParam(description = "Identifiant numérique de la fiche") long id) {
        return directory.findUserById(id);
    }
}
```

Spring crée `DirectoryTools` et lui injecte le service par constructeur. Spring AI détecte la méthode annotée, publie son nom, sa description et le type de son paramètre. Lors d’un appel, la méthode délègue au service métier.

```uml-sequence
participant client as Client MCP
participant tools as DirectoryTools
participant service as DirectoryService
client -> tools: findUserById(id=1)
tools -> service: findUserById(1)
service --> tools: fiche ou resultat absent
tools --> client: contenu du resultat
```

---

## 7. Le serveur MCP reste un adaptateur

Le serveur MCP expose des opérations, mais ne doit pas contenir les règles métier. Le composant annoté traduit les arguments du protocole vers un appel de service ; le service conserve la responsabilité de la recherche et de ses règles.

```diagram
Client MCP -> Annotation @McpTool -> DirectoryTools -> DirectoryService -> Fiches de demonstration
```

Cette séparation permet de faire évoluer l’interface MCP sans déplacer la logique de l’annuaire. Le cours se concentre sur ce chemin simple ; il ne demande pas de construire une architecture hexagonale complète.

---

## 8. On peut essayer MCP sans fournir de modèle IA

Le **MCP Inspector** permet de se connecter au serveur, consulter la liste des outils et les appeler manuellement. Il n’a pas besoin d’un fournisseur de modèle pour cet essai : l’étudiant choisit lui-même l’outil et les valeurs des arguments.

```diagram
Etudiant choisit outil et arguments -> Inspecteur MCP -> Serveur Spring -> Resultat affiche
```

Un modèle de langage peut ensuite utiliser les descriptions et schémas pour proposer des appels, mais cette couche d’inférence n’est pas nécessaire pour comprendre le protocole ou vérifier un outil.

---

## 9. Sécurité : un outil expose une véritable capacité

Un outil MCP n’est pas une simple annotation documentaire. Un client qui peut atteindre le serveur peut découvrir les capacités exposées et tenter de les invoquer. Il faut donc choisir soigneusement les opérations publiées, valider leurs arguments et appliquer les contrôles d’accès adaptés au déploiement.

Le projet reste local et expose une recherche en lecture seule. Avant d’exposer un transport HTTP sur un réseau, il faut configurer une frontière d’authentification et d’autorisation ; Spring AI ne fournit pas à lui seul ces règles métier. Une confirmation côté utilisateur et une validation côté serveur sont nécessaires pour les actions sensibles.

---

## Démonstration — Découvrir et appeler un outil MCP

**Place dans le cours :** la démonstration montre le protocole en action avant le TP. Le [projet de démonstration du cours 9](https://github.com/anbahmani/framework-spring-demos/tree/main/course-09-mcp) expose un outil de recherche d’utilisateur via un serveur Spring AI. Il permet d’observer la découverte puis l’appel depuis MCP Inspector, sans clé d’API de modèle.

On voit `DirectoryTools` déclarer l’outil, `DirectoryService` fournir le résultat et Spring AI relier l’annotation au serveur MCP. Le TP demandera ensuite d’ajouter une seconde capacité et de vérifier qu’elle apparaît dans la découverte.

---

## TP guidé — ajouter une capacité MCP

**Projet :** [atelier 09 — outils MCP](../ateliers/09-mcp/README.md). Le projet démarre un serveur Streamable HTTP local, sans fournisseur d’IA.

### Étape 1 — démarrer le serveur

Dans `09-mcp`, exécuter `mvn spring-boot:run`. **Attendu :** Spring Boot démarre et le serveur écoute sur le port 8080.

### Étape 2 — ouvrir l’inspecteur

Lancer MCP Inspector avec `npx -y @modelcontextprotocol/inspector`. Dans l’interface, choisir le transport Streamable HTTP et l’adresse `http://localhost:8080/mcp`, puis connecter le client.

### Étape 3 — explorer la capacité fournie

Ouvrir la liste des outils et repérer `findUserById`, sa description et son paramètre `id`. L’appeler avec `id = 1`, puis `id = 42`. Comparer les résultats retournés par le service.

### Étape 4 — ajouter une capacité métier

Ajouter dans `DirectoryService` une méthode qui retourne le nombre de fiches, puis l’exposer avec une méthode annotée `@McpTool` dans `DirectoryTools`. Donner à l’outil un nom et une description compréhensibles. Ne pas exposer directement la collection interne.

### Étape 5 — vérifier la découverte et l’appel

Relancer le serveur, reconnecter l’inspecteur, confirmer que le nouvel outil apparaît et l’appeler sans argument. **Attendu :** un résultat numérique correspondant aux fiches de démonstration.

### Étape 6 — tester le service

Lancer `mvn test`. Ajouter ou adapter un test unitaire qui vérifie le nombre retourné, puis dessiner le chemin client MCP → outil Spring → service.

**À rendre :** les deux classes modifiées, le test, une capture ou transcription de la découverte et de l’appel, et le diagramme. **Réussite :** l’outil est découvert, son appel atteint le service, et le test vérifie le résultat.

<details>
<summary>Aide et correction du TP</summary>

Le service peut exposer une méthode `public int countUsers()` qui retourne la taille de sa collection. L’adaptateur MCP peut proposer `@McpTool(description = "Compte les fiches utilisateur disponibles") public int countUsers()`. Le test vérifie la valeur initiale connue du jeu de démonstration.

Si l’inspecteur ne se connecte pas, vérifier que le serveur a démarré, que le protocole sélectionné est Streamable HTTP et que l’adresse finit par `/mcp`.

</details>

Références : [guide de démarrage MCP avec Spring AI](https://docs.spring.io/spring-ai/reference/guides/getting-started-mcp.html), [transport HTTP Streamable](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-streamable-http-server-boot-starter-docs.html).

---

## Quiz — 8 questions

1. Quel problème MCP résout-il entre un hôte et un serveur ?
2. Quel rôle joue le serveur Spring dans le TP ?
3. Que fait une capacité de type outil ?
4. Quel format d’échange est utilisé par MCP ?
5. Quelle est la différence entre le protocole MCP et Streamable HTTP ?
6. Faut-il une clé d’API de modèle pour découvrir et appeler un outil depuis MCP Inspector ?
7. Pourquoi l’outil délègue-t-il à `DirectoryService` ?
8. Que faut-il ajouter avant d’exposer le serveur HTTP hors de la machine locale ?

<details>
<summary>Corrigé du quiz</summary>

1. Il standardise la découverte et l’utilisation de capacités fournies par des serveurs.
2. Il expose des capacités MCP et reçoit les appels du client.
3. Elle exécute une opération, par exemple rechercher une fiche.
4. Des messages JSON-RPC 2.0 selon les règles MCP.
5. MCP définit les messages et leur sens ; Streamable HTTP transporte ces messages par HTTP.
6. Non. L’inspecteur permet de choisir et d’appeler les outils manuellement.
7. Pour garder la logique métier séparée de l’adaptateur protocolaire.
8. Des contrôles d’authentification, d’autorisation et la validation adaptée au service.

</details>
