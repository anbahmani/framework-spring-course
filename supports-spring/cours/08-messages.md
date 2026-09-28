# Cours 8 — Découvrir les messages asynchrones avec Spring JMS

---

## 1. Un autre mode de communication

Dans le cours précédent, le client attendait une réponse à son appel. Pour certaines actions, nous souhaitons plutôt déposer une demande et la traiter ensuite, par exemple préparer une notification de bienvenue.

Une **communication asynchrone** sépare l’envoi de la demande de son traitement final. L’expéditeur n’attend pas que tout le travail métier soit terminé. Cela ne signifie pas que l’envoi réseau lui-même prend zéro temps ou qu’il ne peut jamais échouer.

L’analogie d’une boîte de dépôt aide : déposer un document ne prouve pas que son destinataire l’a déjà lu. De même, « message envoyé » ne signifie pas « notification terminée ».

---

## 2. Quatre mots à comprendre

| Terme | Rôle dans l’exemple |
| --- | --- |
| Message | Texte `Bienvenue Ana` à transporter |
| Producteur | Composant qui envoie le texte |
| File, ou queue | Destination nommée `notifications` où attendent les messages |
| Consommateur | Composant qui reçoit et traite un message |
| Broker | Logiciel intermédiaire qui gère les files et leur distribution |

```diagram
NotificationSender -> File notifications -> NotificationReceiver
Producteur -> Broker Artemis -> Consommateur
```

**Artemis** est le broker choisi. **Spring JMS** fournit les outils pour l’utiliser depuis nos composants Spring. Dans cet atelier, le broker, le producteur et le consommateur sont réunis dans le même processus pour simplifier le lancement. Dans d’autres architectures, ils peuvent être séparés.

---

## 3. Envoyer avec `JmsTemplate`

```java
@Service
public class NotificationSender {
    private final JmsTemplate jms;
    public NotificationSender(JmsTemplate jms) { this.jms = jms; }
    public void send(String message) {
        jms.convertAndSend("notifications", message);
    }
}
```

Le constructeur reçoit un objet configuré par Spring. `JmsTemplate` est l’outil d’envoi. Le premier argument est le nom de la destination ; le second est le contenu à envoyer. Ici `convertAndSend` convertit la chaîne Java en message texte approprié.

```uml-sequence
participant sender as NotificationSender
participant template as JmsTemplate
participant broker as Broker Artemis
participant queue as File notifications
sender -> template: convertAndSend avec message texte
template -> broker: envoie le message a la destination
broker -> queue: place le message en attente
queue --> broker: message disponible
```

Le service ne contient pas de boucle réseau ni de code pour ouvrir et fermer manuellement chaque connexion. Ces détails sont pris en charge par l’infrastructure fournie.

```diagram
Code Java -> JmsTemplate -> Broker Artemis -> File notifications
```

---

## 4. Recevoir avec `@JmsListener`

```java
@Component
public class NotificationReceiver {
    @JmsListener(destination="notifications")
    public void receive(String message) {
        System.out.println("RECU : " + message);
    }
}
```

`@Component` déclare une classe gérée par Spring ; `@Service` est une annotation plus spécialisée que nous utilisons pour les services métier. `@JmsListener` demande d’appeler cette méthode lorsqu’un message arrive sur la destination indiquée.

On ne doit pas appeler `receive` directement depuis le producteur pour faire la démonstration : cela contournerait le broker. Ici, Spring déclenche la réception et fournit le contenu du message au paramètre Java.

```uml-sequence
participant queue as File notifications
participant broker as Broker Artemis
participant listener as NotificationReceiver
queue -> broker: message pret a etre distribue
broker -> listener: appelle receive(message)
listener --> broker: traitement du texte termine
```

```diagram
Message dans la file -> Spring detecte le message -> @JmsListener -> Methode receive(String)
```

---

## 5. Déclencher l’essai au démarrage

Comme au cours 7, un `CommandLineRunner` exécute une action après le démarrage :

```java
sender.send("Bienvenue Ana");
System.out.println("Demande envoyée");
```

Le listener affiche de son côté `RECU : Bienvenue Ana`. Les lignes peuvent s’entrelacer avec les messages techniques du serveur, et leur ordre d’affichage n’est pas une preuve de la durée du traitement. L’application reste en écoute après cet envoi ; l’arrêter avec Ctrl+C.

La configuration embarquée est fournie dans `application.properties`. Elle crée la file `notifications` et utilise `persistent=false` : les messages ne sont pas conservés durablement après l’arrêt du broker. Le TP ne prétend pas démontrer une résistance aux pannes.

---

## 6. Les limites à connaître sans les implémenter maintenant

Si un traitement échoue, un système de messagerie peut présenter à nouveau un message, selon sa configuration. Une réception ne garantit donc pas à elle seule que l’effet métier ne se produira jamais deux fois. Il faut aussi connaître la configuration du stockage et des confirmations pour parler de fiabilité.

Pour cette première découverte, nous attendons seulement un échange de texte observé, avec les acteurs correctement identifiés. Les reprises automatiques, files d’erreur et garanties entre base et broker sont des sujets ultérieurs.

Une file est utile pour distribuer des travaux entre consommateurs. Nous n’ajoutons pas de second modèle de diffusion dans le TP : maîtriser un premier chemin complet est l’objectif.

---

## Démonstration — Producteur, broker et consommateur (15 min)

**Place dans le cours :** cette démonstration illustre le concept présenté dans la partie précédente. Le [projet de démonstration du cours 8](https://github.com/anbahmani/framework-spring-demos/tree/main/course-08-messages) permet de voir le comportement complet et les principaux composants. Elle sert d’exemple commenté ; les modifications sont réservées au TP.

La démonstration suit l’envoi d’un message texte par un producteur vers une file Artemis. Le broker reçoit le message puis le remet au consommateur qui écoute cette file. L’envoi et le traitement sont deux moments distincts ; le producteur ne reçoit pas directement le résultat du consommateur.

`NotificationSender` utilise `JmsTemplate` pour envoyer le message, et `NotificationReceiver` le traite avec un écouteur Spring JMS. Le broker est embarqué et non persistant dans cet exemple ; la démonstration présente le trajet d’un message asynchrone.

---

## TP guidé — envoyer et reconnaître deux messages

**Projet :** [atelier 08](../ateliers/08-messages/README.md). Durée : 65 min. Utiliser Java 25. Aucun serveur HTTP précédent n’est nécessaire ; vous pouvez l’arrêter.

### Étape 1 — repérer les acteurs (10 min)

Ouvrir `NotificationSender`, `NotificationReceiver` et `DemoRunner`. Noter la ligne d’envoi, la méthode de réception et le nom de la file. Repérer la dépendance `spring-boot-starter-artemis` dans le POM sans modifier les bibliothèques.

### Étape 2 — démarrer et observer (15 min)

Dans `08-messages`, lancer `mvn spring-boot:run`. **Attendu :** une ligne contenant `Demande envoyée` et une ligne `RECU : Bienvenue Ana`. Les autres logs ne sont pas des messages applicatifs à compter.

### Étape 3 — changer le contenu (10 min)

Remplacer dans `DemoRunner` le texte par `Bienvenue MIAGE`. Arrêter puis relancer. **Attendu :** `RECU : Bienvenue MIAGE`.

### Étape 4 — envoyer deux messages (15 min)

Ajouter un second appel `sender.send("Bienvenue Lea")`. Après redémarrage, identifier les deux textes reçus. Le but n’est pas de déduire une garantie générale d’ordre à partir de cette petite observation.

### Étape 5 — changer ensemble la destination (10 min)

Remplacer le nom `notifications` par `accueil` dans le producteur, l’annotation du consommateur et la propriété `spring.artemis.embedded.queues`. Relancer et vérifier que les deux textes arrivent. Comprendre pourquoi ces trois éléments doivent désigner la même destination.

### Étape 6 — expliquer (5 min)

Dessiner la chaîne d’un message et expliquer pourquoi une ligne « envoyé » ne prouve pas à elle seule la fin d’un traitement.

**À rendre :** les trois fichiers de code/configuration concernés, deux lignes reçues et le schéma. **Réussite :** passage par le broker et destination cohérente, sans appel direct au récepteur.

<details>
<summary>Aide et correction du TP</summary>

Dans `DemoRunner.run` :

```java
sender.send("Bienvenue MIAGE");
sender.send("Bienvenue Lea");
System.out.println("Demandes envoyées");
```

Pour le changement de file : `convertAndSend("accueil", message)`, `@JmsListener(destination="accueil")` et `spring.artemis.embedded.queues=accueil`. Redémarrer pour prendre en compte toutes les modifications. Garder l’application active assez longtemps pour observer les réceptions.

</details>

Référence : [messagerie avec Spring Boot](https://docs.spring.io/spring-boot/3.5/reference/messaging/jms.html).

---

## Quiz — 8 questions

1. Quel composant joue le rôle de producteur dans l’atelier ?
2. Qu’est-ce qu’un broker ?
3. À quoi sert le nom `notifications` dans l’envoi ?
4. Quel outil Spring utilisons-nous pour envoyer ?
5. Quelle annotation déclenche une méthode à la réception ?
6. Le producteur doit-il appeler directement `receive` pour passer par la file ?
7. « Envoyé » signifie-t-il nécessairement « traitement final terminé » ?
8. Notre broker embarqué non persistant garantit-il la conservation des messages après arrêt ?

<details>
<summary>Corrigé du quiz</summary>

1. `NotificationSender`.
2. Le logiciel intermédiaire qui gère les files et la distribution des messages, ici Artemis.
3. Il identifie la destination de messagerie utilisée par producteur et consommateur.
4. `JmsTemplate`, avec `convertAndSend`.
5. `@JmsListener`.
6. Non : c’est Spring qui appelle le listener lorsque le broker fournit un message.
7. Non : dépôt/envoi et fin du traitement sont deux étapes distinctes.
8. Non ; cet atelier sert à observer un échange, pas à prouver la durabilité.

</details>