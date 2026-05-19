# ArtConnect Pro - Local Art Community Platform

## Overview
ArtConnect Pro is a JavaFX-based management system for local art communities. It allows managing artists, artworks, exhibitions, galleries, workshops, and community members.

This project is a skeleton designed for students to practice:
1. **Layered Architecture**: Presentation, Service, DAO, and Model layers.
2. **Database Persistence**: Implementing JDBC DAOs to connect to a MySQL database.
3. **JavaFX UI**: Working with FXML, TableViews, and Controllers.

## Project Structure
- `com.project.artconnect.MainApp`: Entry point.
- `com.project.artconnect.model`: Domain entities (POJOs/Stubs).
- `com.project.artconnect.dao`: Data Access Object interfaces.
- `com.project.artconnect.persistence`: JDBC implementations (TODO: Students implement these).
- `com.project.artconnect.service`: Business logic layer.
- `com.project.artconnect.ui`: JavaFX Controllers and FXML views.
- `com.project.artconnect.util`: Utility classes like `ConnectionManager` and `ServiceProvider`.

# ArtConnect — Documentation complète

## Résumé
ArtConnect est une application Java/JavaFX pour gérer une petite communauté artistique (artistes, œuvres, expositions, galeries, ateliers, membres). Ce README explique comment configurer la base de données MySQL, compiler et lancer l'application, et comment basculer des services en mémoire vers la persistance JDBC.

## Fonctionnalités principales
- Gestion d'artistes et d'œuvres
- Planning d'expositions et gestion de galeries
- Ateliers, réservations et avis
- UI JavaFX (FXML) fournie pour démonstration
- Architecture en couches : UI → Service → DAO → Persistence

## Prérequis
- Java 17 ou supérieur (JDK compatible JavaFX)
- Maven 3.6+
- MySQL 8.x (ou compatible with `mysql` CLI)

## Fichiers importants
- Code d'entrée : [src/main/java/com/project/artconnect/MainApp.java](src/main/java/com/project/artconnect/MainApp.java)
- Configuration BD : [src/main/java/com/project/artconnect/config/DatabaseConfig.java](src/main/java/com/project/artconnect/config/DatabaseConfig.java)
- Script de création utilisateurs / schéma : [src/main/java/com/project/artconnect/config/CreateRoot.sql](src/main/java/com/project/artconnect/config/CreateRoot.sql)
- Scripts SQL supplémentaires : [ArtConnect.sql](ArtConnect.sql), [InitData.sql](InitData.sql), [Insertions_exemple.sql](Insertions_exemple.sql), [transaction.sql](transaction.sql)
- DAO interfaces : [src/main/java/com/project/artconnect/dao](src/main/java/com/project/artconnect/dao)
- Impl. JDBC : [src/main/java/com/project/artconnect/persistence](src/main/java/com/project/artconnect/persistence)
- Vues FXML : [src/main/resources/com/project/artconnect/ui](src/main/resources/com/project/artconnect/ui)

## Préparation de la base de données (MySQL)
Les constantes par défaut de connexion se trouvent dans [src/main/java/com/project/artconnect/config/DatabaseConfig.java](src/main/java/com/project/artconnect/config/DatabaseConfig.java). Adaptez-les si vous utilisez d'autres identifiants.

Exemples de commandes (depuis la racine du projet) :

1) Créer l'utilisateur / schéma (si fourni) :

```bash
mysql -u root -p < src/main/java/com/project/artconnect/config/CreateRoot.sql
```

2) Créer le schéma et tables (si vous préférez utiliser `ArtConnect.sql`) :

```bash
mysql -u root -p < ArtConnect.sql
```

3) Charger les données d'exemple :

```bash
mysql -u ArtconnectRoot -pArtConnect_2026_Secure artconnect < InitData.sql
# ou
mysql -u ArtconnectRoot -pArtConnect_2026_Secure artconnect < Insertions_exemple.sql
```

Remarques :
- Les noms d'utilisateurs et mots de passe par défaut sont définis dans [src/main/java/com/project/artconnect/config/DatabaseConfig.java](src/main/java/com/project/artconnect/config/DatabaseConfig.java). Modifiez ces valeurs si vous changez les credentials.
- Si vous utilisez un autre port ou hôte, mettez à jour `URL` dans `DatabaseConfig`.

## Compilation et exécution

1) Compiler et lancer via Maven (mode développement) :

```bash
# maven doit être installé
mvn clean javafx:run
```

2) Générer un JAR exécutable (selon configuration `pom.xml`) :

```bash
mvn clean package
# puis exécuter le JAR produit dans target/ si le pom génère un jar autonome
java -jar target/artconnect-<version>.jar
```

3) Lancer depuis un IDE (IntelliJ/VSCode) :
- Importez le projet Maven, configurez la JVM (Java 17+), et lancez la classe `com.project.artconnect.MainApp`.

## Utilisation du programme
Voici les identifiants pour tester l'application:
```
Username: ArtconnectRoot
Password: ArtConnect_2026_Secure
```

## Basculer des services en mémoire vers JDBC
Par défaut, l'application contient des services en mémoire pour faciliter le prototypage. Pour utiliser la persistance réelle :

1. Implémentez les DAO JDBC dans `src/main/java/com/project/artconnect/persistence` (ex. `JdbcArtistDao`, `JdbcArtworkDao`, ...).
2. Vérifiez que les scripts SQL (schéma + données) ont été exécutés dans MySQL.
3. Mettez à jour le fournisseur de services pour utiliser vos implémentations JDBC (classe `ServiceProvider` — recherchez `ServiceProvider` dans le projet) : remplacez les instances `InMemory...Service` par les services qui consomment vos `Jdbc...Dao`.

## Structure recommandée et mapping BD
- Les modèles Java sont orientés objet (références d'objets, sans exposer d'ID dans les POJOs). Vos DAO JDBC doivent mapper les clés primaires de la base aux objets Java et reconstruire le graphe d'objets (ex. associer `Artwork` à son `Artist`).
- Implémentez des méthodes utilitaires de mappage (factories/constructors privés) dans les DAO pour centraliser cette logique.

## Bonnes pratiques
- Ne modifiez `DatabaseConfig` qu'après avoir sécurisé vos credentials ou utilisé des variables d'environnement pour la production.
- Testez chaque DAO avec un jeu de données minimal avant d'intégrer dans les services.
- Gardez la couche UI découplée : UI ↔ Service (interfaces), Service ↔ DAO (interfaces). Cela facilite les tests et le remplacement d'implémentations.

## Débogage et validation
- Utilisez les logs (ajoutez `System.out` ou un logger) dans vos DAO pour vérifier les requêtes SQL et les mappages.
- Vérifiez les transactions si vous avez des opérations multi-étapes (voir `transaction.sql` pour exemples).

## Contribution
- Ouvrez une issue ou créez une PR si vous améliorez les scripts SQL, ajoutez des tests ou corrigez des DAO.

## Ressources utiles
- Voir la configuration JDBC : [src/main/java/com/project/artconnect/config/DatabaseConfig.java](src/main/java/com/project/artconnect/config/DatabaseConfig.java)
- Scripts SQL : [ArtConnect.sql](ArtConnect.sql), [InitData.sql](InitData.sql), [Insertions_exemple.sql](Insertions_exemple.sql), [transaction.sql](transaction.sql)

---

Si vous voulez, je peux :
- Ajouter des exemples de commandes `mysql` plus sécurisées (utiliser `mysql_config_editor` ou variables d'environnement),
- Ajouter une section « Tests unitaires » avec exemples de tests JUnit pour les DAO,
- Générer un script d'installation automatisé (shell) pour la base.
