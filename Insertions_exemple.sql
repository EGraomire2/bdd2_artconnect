-- =========================================================
-- 1. ARTISTS
-- =========================================================

INSERT INTO artists
(name, bio, birth_year, contact_email, phone, city, website, social_media, is_active)
VALUES
('Alice Moreau', 'Artiste contemporaine spécialisée en peinture abstraite et installations visuelles.', 1985, 'alice.moreau@artconnect.com', '0600000001', 'Paris', 'https://alicemoreau.art', '@alice.moreau.art', TRUE),
('Karim Benali', 'Sculpteur et artiste multidisciplinaire travaillant sur les matériaux recyclés.', 1978, 'karim.benali@artconnect.com', '0600000002', 'Lyon', 'https://karimbenali.art', '@karim.sculpt', TRUE),
('Sofia Laurent', 'Artiste numérique explorant les mondes immersifs et l’art génératif.', 1992, 'sofia.laurent@artconnect.com', '0600000003', 'Marseille', 'https://sofialaurent.art', '@sofia.digital', TRUE),
('Thomas Rivière', 'Photographe plasticien influencé par l’architecture et la lumière urbaine.', 1988, 'thomas.riviere@artconnect.com', '0600000004', 'Bordeaux', 'https://thomasriviere.art', '@thomas.riviere.photo', TRUE);

-- =========================================================
-- 2. DISCIPLINES
-- =========================================================

INSERT INTO disciplines (name)
VALUES
('Painting'),
('Sculpture'),
('Digital Art'),
('Photography'),
('Installation Art');

-- =========================================================
-- 3. ARTIST_DISCIPLINES
-- =========================================================

INSERT INTO artist_disciplines (artist_id, discipline_id)
VALUES
(1, 1), -- Alice -> Painting
(1, 5), -- Alice -> Installation Art
(2, 2), -- Karim -> Sculpture
(2, 5), -- Karim -> Installation Art
(3, 3), -- Sofia -> Digital Art
(3, 5), -- Sofia -> Installation Art
(4, 4), -- Thomas -> Photography
(4, 3); -- Thomas -> Digital Art

-- =========================================================
-- 4. COMMUNITY_MEMBERS
-- =========================================================

INSERT INTO community_members
(name, email, birth_year, phone, city, membership_type)
VALUES
('Emma Dupont', 'emma.dupont@email.com', 1995, '0610000001', 'Paris', 'premium'),
('Lucas Martin', 'lucas.martin@email.com', 1990, '0610000002', 'Lyon', 'free'),
('Chloé Bernard', 'chloe.bernard@email.com', 1998, '0610000003', 'Marseille', 'premium'),
('Nicolas Petit', 'nicolas.petit@email.com', 1987, '0610000004', 'Bordeaux', 'free'),
('Sarah Leclerc', 'sarah.leclerc@email.com', 1993, '0610000005', 'Paris', 'premium');

-- =========================================================
-- 5. MEMBER_FAVORITE_DISCIPLINES
-- =========================================================

INSERT INTO member_favorite_disciplines (member_id, discipline_id)
VALUES
(1, 1), -- Emma -> Painting
(1, 3), -- Emma -> Digital Art
(2, 2), -- Lucas -> Sculpture
(2, 5), -- Lucas -> Installation Art
(3, 3), -- Chloé -> Digital Art
(3, 4), -- Chloé -> Photography
(4, 4), -- Nicolas -> Photography
(5, 1), -- Sarah -> Painting
(5, 5); -- Sarah -> Installation Art

-- =========================================================
-- 6. ARTWORK_TAGS
-- =========================================================

INSERT INTO artwork_tags (name)
VALUES
('Abstract'),
('Modern'),
('Eco'),
('Immersive'),
('Urban'),
('Experimental'),
('Colorful'),
('Minimalist');

-- =========================================================
-- 7. ARTWORKS
-- =========================================================

INSERT INTO artworks
(title, creation_year, type, medium, dimensions, description, price, status, artist_id)
VALUES
('Fragments Chromatiques', 2022, 'Painting', 'Acrylic on canvas', '120x90 cm', 'Une composition abstraite aux couches colorées dynamiques.', 1800.00, 'FOR_SALE', 1),
('Mémoire Liquide', 2023, 'Installation', 'Mixed media', '200x150x180 cm', 'Installation immersive autour du thème du souvenir.', 3500.00, 'EXHIBITED', 1),
('Résilience Métallique', 2021, 'Sculpture', 'Recycled metal', '180x70x60 cm', 'Sculpture conçue à partir de matériaux industriels recyclés.', 4200.00, 'FOR_SALE', 2),
('Équilibre Brut', 2020, 'Sculpture', 'Stone and steel', '160x80x75 cm', 'Dialogue entre la pierre brute et l’acier poli.', 3900.00, 'SOLD', 2),
('Dream Code v2', 2024, 'Digital Artwork', 'Generative art projection', 'Projection variable', 'Œuvre générative inspirée des flux de données.', 2500.00, 'FOR_SALE', 3),
('Nébuleuse Synthétique', 2023, 'Digital Installation', 'Interactive screen installation', '300x200 cm', 'Expérience visuelle interactive et évolutive.', 4700.00, 'EXHIBITED', 3),
('Nocturne Urbain', 2022, 'Photography', 'Fine art print', '80x120 cm', 'Photographie de nuit explorant les lumières architecturales.', 1200.00, 'FOR_SALE', 4),
('Perspectives Silencieuses', 2021, 'Photography', 'Digital print', '100x70 cm', 'Série photographique sur l’isolement dans les espaces urbains.', 1400.00, 'FOR_SALE', 4);

-- =========================================================
-- 8. ARTWORK_TAG_MAP
-- =========================================================

INSERT INTO artwork_tag_map (artwork_id, tag_id)
VALUES
(1, 1), -- Fragments Chromatiques -> Abstract
(1, 7), -- Colorful
(1, 2), -- Modern

(2, 4), -- Mémoire Liquide -> Immersive
(2, 6), -- Experimental

(3, 3), -- Résilience Métallique -> Eco
(3, 2), -- Modern

(4, 8), -- Équilibre Brut -> Minimalist
(4, 2), -- Modern

(5, 4), -- Dream Code v2 -> Immersive
(5, 6), -- Experimental

(6, 4), -- Nébuleuse Synthétique -> Immersive
(6, 7), -- Colorful
(6, 6), -- Experimental

(7, 5), -- Nocturne Urbain -> Urban
(7, 8), -- Minimalist

(8, 5), -- Perspectives Silencieuses -> Urban
(8, 2); -- Modern

-- =========================================================
-- 9. GALLERIES
-- =========================================================

INSERT INTO galleries
(name, address, owner_name, opening_hours, contact_phone, rating, website)
VALUES
('Galerie Horizon', '12 Rue des Arts, Paris', 'Claire Fontaine', 'Tue-Sat 10:00-18:00', '0155000001', 4.70, 'https://galeriehorizon.fr'),
('Atelier Lumière', '45 Quai Créatif, Lyon', 'Marc Delcourt', 'Wed-Sun 11:00-19:00', '0472000002', 4.50, 'https://atelierlumiere.fr'),
('Espace Nova', '8 Avenue Culturelle, Marseille', 'Julie Renaud', 'Mon-Sat 09:30-18:30', '0491000003', 4.80, 'https://espacenova.fr');

-- =========================================================
-- 10. EXHIBITIONS
-- =========================================================

INSERT INTO exhibitions
(title, start_date, end_date, description, gallery_id, curator_name, theme)
VALUES
('Couleurs en Mouvement', '2026-05-10', '2026-06-15', 'Exposition collective sur l’énergie visuelle et le geste artistique.', 1, 'Claire Fontaine', 'Abstraction contemporaine'),
('Matières Futures', '2026-06-01', '2026-07-20', 'Dialogue entre sculpture, recyclage et installation.', 2, 'Marc Delcourt', 'Éco-création'),
('Digital Dreams', '2026-05-20', '2026-07-05', 'Exploration des formes immersives et génératives.', 3, 'Julie Renaud', 'Art numérique immersif');

-- =========================================================
-- 11. EXHIBITION_ARTWORKS
-- Montre des participations croisées
-- =========================================================

INSERT INTO exhibition_artworks (exhibition_id, artwork_id)
VALUES
(1, 1), -- Couleurs en Mouvement -> Fragments Chromatiques
(1, 2), -- Couleurs en Mouvement -> Mémoire Liquide
(1, 7), -- Couleurs en Mouvement -> Nocturne Urbain

(2, 2), -- Matières Futures -> Mémoire Liquide
(2, 3), -- Matières Futures -> Résilience Métallique
(2, 4), -- Matières Futures -> Équilibre Brut

(3, 5), -- Digital Dreams -> Dream Code v2
(3, 6), -- Digital Dreams -> Nébuleuse Synthétique
(3, 7), -- Digital Dreams -> Nocturne Urbain
(3, 8); -- Digital Dreams -> Perspectives Silencieuses

-- =========================================================
-- 12. WORKSHOPS
-- =========================================================

INSERT INTO workshops
(title, workshop_date, duration_minutes, max_participants, price, instructor_id, location, description, level)
VALUES
('Initiation à la peinture abstraite', '2026-05-18 14:00:00', 180, 12, 45.00, 1, 'Galerie Horizon - Salle 1', 'Atelier pratique autour de la composition abstraite et des textures.', 'beginner'),
('Sculpture et matériaux recyclés', '2026-05-25 10:00:00', 240, 10, 60.00, 2, 'Atelier Lumière - Studio B', 'Création d’une petite sculpture à partir de matériaux récupérés.', 'intermediate'),
('Créer une œuvre générative', '2026-06-02 15:00:00', 210, 15, 75.00, 3, 'Espace Nova - Lab Digital', 'Introduction à l’art génératif et aux logiques visuelles algorithmiques.', 'advanced'),
('Photographie urbaine créative', '2026-06-10 09:30:00', 180, 14, 50.00, 4, 'Bordeaux City Walk', 'Sortie photo autour de la lumière, de l’architecture et des contrastes.', 'beginner');

-- =========================================================
-- 13. BOOKINGS
-- Réservations croisées sur plusieurs ateliers
-- =========================================================

INSERT INTO bookings
(workshop_id, member_id, booking_date, payment_status)
VALUES
(1, 1, '2026-05-01 10:00:00', 'PAID'),
(1, 2, '2026-05-02 11:30:00', 'PENDING'),
(1, 5, '2026-05-03 09:15:00', 'PAID'),

(2, 2, '2026-05-05 14:20:00', 'PAID'),
(2, 4, '2026-05-06 16:45:00', 'CANCELLED'),
(2, 5, '2026-05-07 12:00:00', 'PAID'),

(3, 1, '2026-05-10 08:50:00', 'PAID'),
(3, 3, '2026-05-11 13:10:00', 'PAID'),
(3, 5, '2026-05-12 17:40:00', 'PENDING'),

(4, 3, '2026-05-15 10:05:00', 'PAID'),
(4, 4, '2026-05-16 11:25:00', 'PAID');

-- =========================================================
-- 14. REVIEWS
-- Plusieurs membres évaluent plusieurs œuvres
-- =========================================================

INSERT INTO reviews
(reviewer_id, artwork_id, rating, comment, review_date)
VALUES
(1, 1, 5, 'Une œuvre vibrante et très expressive.', '2026-05-20'),
(1, 5, 4, 'Très belle approche du génératif, immersive.', '2026-05-21'),
(2, 3, 5, 'La matière recyclée est superbement mise en valeur.', '2026-05-22'),
(3, 6, 5, 'Installation numérique impressionnante.', '2026-05-22'),
(3, 7, 4, 'Une atmosphère urbaine très réussie.', '2026-05-23'),
(4, 8, 4, 'Composition sobre et forte émotion visuelle.', '2026-05-24'),
(5, 2, 5, 'Une expérience artistique immersive remarquable.', '2026-05-25'),
(5, 1, 4, 'Très belle palette de couleurs et mouvement intéressant.', '2026-05-26');
