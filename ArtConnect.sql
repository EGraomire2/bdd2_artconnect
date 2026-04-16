-- =========================================================
-- ArtConnect Pro - Schéma relationnel
-- Version MySQL 8+
-- =========================================================

-- =========================================================
-- 0. Suppression des tables si elles existent
-- =========================================================

USE ArtConnect;

DROP TABLE IF EXISTS artwork_tag_map;
DROP TABLE IF EXISTS exhibition_artworks;
DROP TABLE IF EXISTS member_favorite_disciplines;
DROP TABLE IF EXISTS artist_disciplines;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS workshops;
DROP TABLE IF EXISTS exhibitions;
DROP TABLE IF EXISTS galleries;
DROP TABLE IF EXISTS artwork_tags;
DROP TABLE IF EXISTS artworks;
DROP TABLE IF EXISTS community_members;
DROP TABLE IF EXISTS disciplines;
DROP TABLE IF EXISTS artists;

-- =========================================================
-- 1. Tables principales
-- =========================================================

CREATE TABLE artists (
    artist_id       INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    bio             TEXT,
    birth_year      INT,
    contact_email   VARCHAR(255),
    phone           VARCHAR(50),
    city            VARCHAR(100),
    website         VARCHAR(255),
    social_media    VARCHAR(255),
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_artist_name UNIQUE (name),
    CONSTRAINT uq_artist_email UNIQUE (contact_email)
) ;

CREATE TABLE disciplines (
    discipline_id   INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,

    CONSTRAINT uq_discipline_name UNIQUE (name)
) ;

CREATE TABLE community_members (
    member_id         INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(150) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    birth_year        INT,
    phone             VARCHAR(50),
    city              VARCHAR(100),
    membership_type   ENUM('free', 'premium') NOT NULL DEFAULT 'free',
    created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_member_email UNIQUE (email)
) ;

CREATE TABLE galleries (
    gallery_id      INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    address         VARCHAR(255),
    owner_name      VARCHAR(150),
    opening_hours   VARCHAR(150),
    contact_phone   VARCHAR(50),
    rating          DECIMAL(3,2) NOT NULL DEFAULT 0.00,
    website         VARCHAR(255),
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_gallery_name UNIQUE (name),
    CONSTRAINT chk_gallery_rating CHECK (rating BETWEEN 0 AND 5)
) ;

CREATE TABLE artwork_tags (
    tag_id          INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,

    CONSTRAINT uq_artwork_tag_name UNIQUE (name)
) ;

CREATE TABLE artworks (
    artwork_id       INT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(200) NOT NULL,
    creation_year    INT,
    type             VARCHAR(100),
    medium           VARCHAR(150),
    dimensions       VARCHAR(100),
    description      TEXT,
    price            DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status           ENUM('FOR_SALE', 'SOLD', 'EXHIBITED') NOT NULL DEFAULT 'FOR_SALE',
    artist_id        INT NOT NULL,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_artwork_artist
        FOREIGN KEY (artist_id)
        REFERENCES artists(artist_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_artwork_price CHECK (price >= 0)
) ;

CREATE TABLE exhibitions (
    exhibition_id    INT AUTO_INCREMENT PRIMARY KEY,
    title            VARCHAR(200) NOT NULL,
    start_date       DATE,
    end_date         DATE,
    description      TEXT,
    gallery_id       INT NOT NULL,
    curator_name     VARCHAR(150),
    theme            VARCHAR(150),
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_exhibition_gallery
        FOREIGN KEY (gallery_id)
        REFERENCES galleries(gallery_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ;

CREATE TABLE workshops (
    workshop_id        INT AUTO_INCREMENT PRIMARY KEY,
    title              VARCHAR(200) NOT NULL,
    workshop_date      DATETIME NOT NULL,
    duration_minutes   INT NOT NULL DEFAULT 0,
    max_participants   INT NOT NULL DEFAULT 0,
    price              DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    instructor_id      INT NOT NULL,
    location           VARCHAR(255),
    description        TEXT,
    level              ENUM('beginner', 'intermediate', 'advanced'),
    created_at         DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_workshop_instructor
        FOREIGN KEY (instructor_id)
        REFERENCES artists(artist_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ;

CREATE TABLE bookings (
    booking_id        INT AUTO_INCREMENT PRIMARY KEY,
    workshop_id       INT NOT NULL,
    member_id         INT NOT NULL,
    booking_date      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_status    ENUM('PENDING', 'PAID', 'CANCELLED') NOT NULL DEFAULT 'PENDING',

    CONSTRAINT fk_booking_workshop
        FOREIGN KEY (workshop_id)
        REFERENCES workshops(workshop_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_booking_member
        FOREIGN KEY (member_id)
        REFERENCES community_members(member_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uq_booking_workshop_member UNIQUE (workshop_id, member_id)
) ;

CREATE TABLE reviews (
    review_id         INT AUTO_INCREMENT PRIMARY KEY,
    reviewer_id       INT NOT NULL,
    artwork_id        INT NOT NULL,
    rating            INT NOT NULL,
    comment           TEXT,
    review_date       DATE NOT NULL DEFAULT (CURRENT_DATE),

    CONSTRAINT fk_review_member
        FOREIGN KEY (reviewer_id)
        REFERENCES community_members(member_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_review_artwork
        FOREIGN KEY (artwork_id)
        REFERENCES artworks(artwork_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT uq_review_member_artwork UNIQUE (reviewer_id, artwork_id)
) ;

-- =========================================================
-- 2. Tables d’association
-- =========================================================

CREATE TABLE artist_disciplines (
    artist_id        INT NOT NULL,
    discipline_id    INT NOT NULL,

    PRIMARY KEY (artist_id, discipline_id),

    CONSTRAINT fk_artist_disc_artist
        FOREIGN KEY (artist_id)
        REFERENCES artists(artist_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_artist_disc_discipline
        FOREIGN KEY (discipline_id)
        REFERENCES disciplines(discipline_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ;

CREATE TABLE member_favorite_disciplines (
    member_id        INT NOT NULL,
    discipline_id    INT NOT NULL,

    PRIMARY KEY (member_id, discipline_id),

    CONSTRAINT fk_member_fav_member
        FOREIGN KEY (member_id)
        REFERENCES community_members(member_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_member_fav_discipline
        FOREIGN KEY (discipline_id)
        REFERENCES disciplines(discipline_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ;

CREATE TABLE exhibition_artworks (
    exhibition_id    INT NOT NULL,
    artwork_id       INT NOT NULL,

    PRIMARY KEY (exhibition_id, artwork_id),

    CONSTRAINT fk_exh_art_exhibition
        FOREIGN KEY (exhibition_id)
        REFERENCES exhibitions(exhibition_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_exh_art_artwork
        FOREIGN KEY (artwork_id)
        REFERENCES artworks(artwork_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ;

CREATE TABLE artwork_tag_map (
    artwork_id       INT NOT NULL,
    tag_id           INT NOT NULL,

    PRIMARY KEY (artwork_id, tag_id),

    CONSTRAINT fk_artwork_tagmap_artwork
        FOREIGN KEY (artwork_id)
        REFERENCES artworks(artwork_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_artwork_tagmap_tag
        FOREIGN KEY (tag_id)
        REFERENCES artwork_tags(tag_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ;

-- =========================================================
-- 3. Trigger : Contraintes check
-- =========================================================

-- ---------------------------------------------------------
-- ARTISTS
-- ---------------------------------------------------------
DROP TRIGGER IF EXISTS chk_artist_birth_year_insert;
DELIMITER //
CREATE TRIGGER chk_artist_birth_year_insert
BEFORE INSERT ON artists
FOR EACH ROW
BEGIN 
    IF NOT (NEW.birth_year IS NULL OR NEW.birth_year BETWEEN 1000 AND YEAR(CURDATE())) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Date de naissance invalide - l'année de naissance doit être comprise entre l'an 1000 et cette année"; 
    END IF;
END //
DELIMITER ;

DROP TRIGGER IF EXISTS chk_artist_birth_year_update;
DELIMITER //
CREATE TRIGGER chk_artist_birth_year_update
BEFORE UPDATE ON artists
FOR EACH ROW
BEGIN 
    IF NOT (NEW.birth_year IS NULL OR NEW.birth_year BETWEEN 1000 AND YEAR(CURDATE())) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Date de naissance invalide - l'année de naissance doit être comprise entre l'an 1000 et cette année"; 
    END IF;
END //
DELIMITER ;

-- ---------------------------------------------------------
-- COMMUNITY MEMBERS
-- ---------------------------------------------------------
DROP TRIGGER IF EXISTS chk_member_birth_year_insert;
DELIMITER //
CREATE TRIGGER chk_member_birth_year_insert
BEFORE INSERT ON community_members
FOR EACH ROW
BEGIN 
    IF NOT (NEW.birth_year IS NULL OR NEW.birth_year BETWEEN 1000 AND YEAR(CURDATE())) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Date de naissance invalide - l'année de naissance doit être comprise entre l'an 1000 et cette année"; 
    END IF;
END //
DELIMITER ;

DROP TRIGGER IF EXISTS chk_member_birth_year_update;
DELIMITER //
CREATE TRIGGER chk_member_birth_year_update
BEFORE UPDATE ON community_members
FOR EACH ROW
BEGIN 
    IF NOT (NEW.birth_year IS NULL OR NEW.birth_year BETWEEN 1000 AND YEAR(CURDATE())) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Date de naissance invalide - l'année de naissance doit être comprise entre l'an 1000 et cette année"; 
    END IF;
END //
DELIMITER ;

-- ---------------------------------------------------------
-- EXHIBITIONS
-- ---------------------------------------------------------
DROP TRIGGER IF EXISTS chk_exhibition_dates_insert;
DELIMITER //
CREATE TRIGGER chk_exhibition_dates_insert
BEFORE INSERT ON exhibitions
FOR EACH ROW
BEGIN 
    IF NOT (NEW.start_date IS NULL OR NEW.end_date IS NULL OR NEW.start_date <= NEW.end_date) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Dates invalides - la date de début doit être antérieure à la date de fin"; 
    END IF;
END //
DELIMITER ;

DROP TRIGGER IF EXISTS chk_exhibition_dates_update;
DELIMITER //
CREATE TRIGGER chk_exhibition_dates_update
BEFORE UPDATE ON exhibitions
FOR EACH ROW
BEGIN 
    IF NOT (NEW.start_date IS NULL OR NEW.end_date IS NULL OR NEW.start_date <= NEW.end_date) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Dates invalides - la date de début doit être antérieure à la date de fin"; 
    END IF;
END //
DELIMITER ;

-- ---------------------------------------------------------
-- REVIEWS
-- ---------------------------------------------------------
DROP TRIGGER IF EXISTS chk_review_rating_insert;
DELIMITER //
CREATE TRIGGER chk_review_rating_insert
BEFORE INSERT ON reviews
FOR EACH ROW
BEGIN 
    IF NOT (NEW.rating BETWEEN 1 AND 5) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Note invalide - la note doit être comprise entre 1 et 5"; 
    END IF;
END //
DELIMITER ;

DROP TRIGGER IF EXISTS chk_review_rating_update;
DELIMITER //
CREATE TRIGGER chk_review_rating_update
BEFORE UPDATE ON reviews
FOR EACH ROW
BEGIN 
    IF NOT (NEW.rating BETWEEN 1 AND 5) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Note invalide - la note doit être comprise entre 1 et 5"; 
    END IF;
END //
DELIMITER ;

-- ---------------------------------------------------------
-- WORKSHOPS
-- ---------------------------------------------------------
DROP TRIGGER IF EXISTS chk_workshop_insert;
DELIMITER //
CREATE TRIGGER chk_workshop_insert
BEFORE INSERT ON workshops
FOR EACH ROW
BEGIN 
    IF NOT (NEW.duration_minutes >= 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Durée invalide - la durée du workshop doit être supérieure ou égale à 0"; 
    END IF;
    IF NOT (NEW.max_participants >= 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Nombre de participants invalide - le nombre de participants maximum doit être supérieur ou égal à 0";
    END IF;
    IF NOT (NEW.price >= 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Prix invalide - le prix doit être supérieur ou égal à 0";
    END IF;
END //
DELIMITER ;

DROP TRIGGER IF EXISTS chk_workshop_update;
DELIMITER //
CREATE TRIGGER chk_workshop_update
BEFORE UPDATE ON workshops
FOR EACH ROW
BEGIN 
    IF NOT (NEW.duration_minutes >= 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Durée invalide - la durée du workshop doit être supérieure ou égale à 0"; 
    END IF;
    IF NOT (NEW.max_participants >= 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Nombre de participants invalide - le nombre de participants maximum doit être supérieur ou égal à 0";
    END IF;
    IF NOT (NEW.price >= 0) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Prix invalide - le prix doit être supérieur ou égal à 0";
    END IF;
END //
DELIMITER ;

-- ---------------------------------------------------------
-- ARTWORKS
-- ---------------------------------------------------------
DROP TRIGGER IF EXISTS chk_artwork_creation_year_insert;
DELIMITER //
CREATE TRIGGER chk_artwork_creation_year_insert
BEFORE INSERT ON artworks
FOR EACH ROW
BEGIN 
    IF NOT (NEW.creation_year IS NULL OR NEW.creation_year BETWEEN 1000 AND YEAR(CURDATE())) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Année de création invalide - l'année doit être comprise entre l'an 1000 et cette année"; 
    END IF;
END //
DELIMITER ;

DROP TRIGGER IF EXISTS chk_artwork_creation_year_update;
DELIMITER //
CREATE TRIGGER chk_artwork_creation_year_update
BEFORE UPDATE ON artworks
FOR EACH ROW
BEGIN 
    IF NOT (NEW.creation_year IS NULL OR NEW.creation_year BETWEEN 1000 AND YEAR(CURDATE())) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = "Année de création invalide - l'année doit être comprise entre l'an 1000 et cette année"; 
    END IF;
END //
DELIMITER ;

-- =========================================================
-- 4. Index utiles
-- =========================================================

CREATE INDEX idx_artists_city ON artists(city);
CREATE INDEX idx_members_city ON community_members(city);
CREATE INDEX idx_artworks_status ON artworks(status);
CREATE INDEX idx_artworks_artist ON artworks(artist_id);
CREATE INDEX idx_exhibitions_gallery ON exhibitions(gallery_id);
CREATE INDEX idx_exhibitions_dates ON exhibitions(start_date, end_date);
CREATE INDEX idx_workshops_date ON workshops(workshop_date);
CREATE INDEX idx_workshops_instructor ON workshops(instructor_id);
CREATE INDEX idx_bookings_payment_status ON bookings(payment_status);
CREATE INDEX idx_reviews_artwork ON reviews(artwork_id);
CREATE INDEX idx_reviews_reviewer ON reviews(reviewer_id);

-- =========================================================
-- 5. Vues utiles
-- =========================================================

-- =========================================================
-- 5. Trigger utiles
-- =========================================================

