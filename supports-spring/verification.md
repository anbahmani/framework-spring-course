# Vérification de la livraison complète

La livraison comprend les huit cours et ateliers du parcours Architecture et Spring. Les supports ciblent des étudiants connaissant Java et découvrant les frameworks Spring et API.

Le socle est **Java 25, Maven 3.9 et Spring Boot 3.5.16**. Les huit ateliers autonomes déclarent Java 25 et utilisent le même parent Spring Boot.

## Contrôles de contenu

- Chaque cours comprend un TP guidé et huit questions avec leurs réponses.
- Les cours 1 à 8 sont rendus en HTML, avec navigation en diaporama et export imprimable.
- Les cours 3 à 8 contiennent des diagrammes de séquence UML rendus en SVG autonome, ainsi que des schémas explicatifs.
- Le sommaire référence les huit cours et ateliers, et le pack téléchargeable contient les projets correspondants.
- Le workflow GitHub Pages prépare les huit cours, les huit ateliers et leurs sources sans inclure les répertoires `target`.

Les liens HTML internes sont contrôlés après génération. Les tests Maven des ateliers ne sont pas relancés dans cette vérification de contenu.
