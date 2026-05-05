Étape 4: Intégration de la base dans l’application Java 
ArtConnect 
Objectif: Remplacer progressivement les services “en mémoire” de l’application ArtConnect 
par une couche de persistance réelle connectée à votre base ArtConnect. 
Tâches: 
DAO (Data Access Objects)
o Complétez/implémentez les interfaces DAO fournies dans le projet (par 
ex.
ArtistDao ,
ArtworkDao ,  CommunityMemberDao, etc.) dans un 
package  org.project.artconnect.dao.impl 
o Implémentez des classes DAO JDBC (par ex.  JdbcArtistDao) dans le 
package  org.project.artconnect.persistence. 
o Utilisez un PreparedStatement et des transactions lorsque nécessaire. Assurez
vous de gérer correctement la fermeture des ressources. 
Services
o Adaptez la couche service pour qu’elle utilise les DAO JDBC au lieu des services 
en mémoire. Veillez à toujours respecter l’architecture du code! 
o Vous pouvez conserver une implémentation  InMemoryXxxService  pour les 
tests, mais l’application finale doit utiliser les services connectés à la base. 
Connexion à la base
o Complétez la classe de configuration DatabaseConfig (ou équivalent) pour 
fournir une connexion JDBC valide vers votre base ArtConnect. 
o Paramétrez l’URL, l’utilisateur et le mot de passe dans un endroit clairement 
indiqué (fichier de configuration, constantes, etc.). 
Adaptation de l’UI
o Sans modifier profondément l’interface graphique, assurez-vous que les actions 
principales (liste, ajout, modification, suppression) utilisent maintenant les 
services connectés à la base. 
o Vérifiez que les différents écrans affichent des données issues de votre base et 
que les opérations de CRUD sont persistantes. 
Livrable: 
• Code des entities, DAO et services. 
• Description (courte) de l’architecture retenue (diagramme de classes simplifié ou 
schéma de couches). 
• Captures d’écran ou vidéo de l’application ArtConnect en fonctionnement avec votre 
base.