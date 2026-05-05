1. DAO (Data Access Objects) 
o Complétez/implémentez les interfaces DAO fournies dans le projet (par 
ex.  
ArtistDao ,  
ArtworkDao ,  CommunityMemberDao, etc.) dans un 
package  org.project.artconnect.dao.impl 
o Implémentez des classes DAO JDBC (par ex.  JdbcArtistDao) dans le 
package  org.project.artconnect.persistence. 
o Utilisez un PreparedStatement et des transactions lorsque nécessaire. Assurez
vous de gérer correctement la fermeture des ressources. 
2. Services 
o Adaptez la couche service pour qu’elle utilise les DAO JDBC au lieu des services 
en mémoire. Veillez à toujours respecter l’architecture du code! 
o Vous pouvez conserver une implémentation  InMemoryXxxService  pour les 
tests, mais l’application finale doit utiliser les services connectés à la base. 
3. Connexion à la base 
o Complétez la classe de configuration DatabaseConfig (ou équivalent) pour 
fournir une connexion JDBC valide vers votre base ArtConnect. 
o Paramétrez l’URL, l’utilisateur et le mot de passe dans un endroit clairement 
indiqué (fichier de configuration, constantes, etc.). 
4. Adaptation de l’UI 
o Sans modifier profondément l’interface graphique, assurez-vous que les actions 
principales (liste, ajout, modification, suppression) utilisent maintenant les 
services connectés à la base. 
o Vérifiez que les différents écrans affichent des données issues de votre base et 
que les opérations de CRUD sont persistantes. 