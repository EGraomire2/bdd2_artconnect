-- Nettoyer les anciens users (au cas où)
DROP USER IF EXISTS 'ArtconnectRoot'@'localhost';
DROP USER IF EXISTS 'ArtconnectRoot'@'127.0.0.1';
DROP USER IF EXISTS 'defaultuser'@'localhost';
DROP USER IF EXISTS 'defaultuser'@'127.0.0.1';

-- Créer l'utilisateur ArtconnectRoot avec tous les droits
CREATE USER 'ArtconnectRoot'@'localhost' IDENTIFIED BY 'ArtConnect_2026_Secure';
GRANT ALL PRIVILEGES ON artconnect.* TO 'ArtconnectRoot'@'localhost';

-- Créer l'utilisateur defaultuser avec accès lecture seule
CREATE USER 'defaultuser'@'localhost' IDENTIFIED BY 'DefaultUser_2026';
GRANT SELECT ON artconnect.* TO 'defaultuser'@'localhost';
REVOKE SELECT ON artconnect.community_members FROM 'defaultuser'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;

-- Vérifier les privileges des utilisateurs
SHOW GRANTS FOR 'ArtconnectRoot'@'localhost';
SHOW GRANTS FOR 'defaultuser'@'localhost';

-- IDENTIFIANTS:
--    - Username: ArtconnectRoot
--    - Password: ArtConnect_2026_Secure
--    - Username: defaultuser
--    - Password: DefaultUser_2026
--    - Host: localhost
--    - Database: artconnect
