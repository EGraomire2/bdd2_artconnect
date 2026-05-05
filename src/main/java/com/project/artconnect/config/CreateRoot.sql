-- Créer l'utilisateur
CREATE USER 'ArtconnectRoot'@'localhost' IDENTIFIED BY 'R00TSAF3p$ssw0rd';

-- Accorder tous les droits sur la base de données artconnect
GRANT ALL PRIVILEGES ON artconnect.* TO 'ArtconnectRoot'@'localhost';

-- Appliquer les changements
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'ArtconnectRoot'@'localhost';