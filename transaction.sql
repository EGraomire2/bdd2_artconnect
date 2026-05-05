START TRANSACTION;

-- 1. Vérifier que le membre existe
SELECT member_id 
FROM community_members 
WHERE member_id = 7
FOR UPDATE;

-- 2. Vérifier disponibilité des workshops
SELECT w.workshop_id, w.max_participants, COUNT(b.booking_id) AS current_participants
FROM workshops w
LEFT JOIN bookings b ON w.workshop_id = b.workshop_id
WHERE w.workshop_id IN (1, 2, 3)
GROUP BY w.workshop_id
FOR UPDATE;

-- 3. Inscrire le membre aux workshops
INSERT INTO bookings (member_id, workshop_id, booking_date, payment_status)
VALUES 
(7, 1, NOW(), 'PENDING'),
(7, 2, NOW(), 'PENDING'),
(7, 3, NOW(), 'PENDING');

-- 4. Mettre à jour les réservations comme payées
UPDATE bookings
SET payment_status = 'PAID'
WHERE member_id = 7
AND workshop_id IN (1, 2, 3);

-- 5. Validation finale
COMMIT;

select * from bookings;
select * from community_members;