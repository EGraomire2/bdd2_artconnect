-- Nettoyer les anciens users (au cas où)
DROP USER IF EXISTS 'ArtconnectRoot'@'localhost';
DROP USER IF EXISTS 'ArtconnectRoot'@'127.0.0.1';

-- Créer l'utilisateur avec le mot de passe ArtConnect_2026_Secure
CREATE USER 'ArtconnectRoot'@'localhost' IDENTIFIED BY 'ArtConnect_2026_Secure';

-- Accorder tous les droits sur la base de données artconnect
GRANT ALL PRIVILEGES ON artconnect.* TO 'ArtconnectRoot'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;


-- Vérifier les privileges de l'utilisateur
SHOW GRANTS FOR 'ArtconnectRoot'@'localhost';

-- IDENTIFIANTS:
--    - Username: ArtconnectRoot
--    - Password: ArtConnect_2026_Secure
--    - Host: localhost
--    - Database: artconnect
