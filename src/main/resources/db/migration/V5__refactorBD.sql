-- ============================================================
-- V5__refactor_branch_principal.sql
-- Refactor: branch como entidad principal
-- - Migrar datos de business a branch
-- - Agregar device_tokens
-- - Agregar push_sent en notifications
-- - Agregar deleted en user_notifications
-- - Actualizar FKs de reviews, favorites, notifications, zone_favorites
-- - Eliminar tablas: business, locations, branch_menu
-- ============================================================


-- ============================================================
-- 1. AGREGAR COLUMNAS DE NEGOCIO A BRANCH
--    (absorbe lo que estaba en business)
-- ============================================================
ALTER TABLE branch
    ADD COLUMN owner_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    ADD COLUMN category_id INT REFERENCES category(id) ON DELETE SET NULL,
    ADD COLUMN descripcion TEXT,
    ADD COLUMN logo_url VARCHAR(255),
    ADD COLUMN banner_url VARCHAR(255),
    ADD COLUMN lat DECIMAL(10, 8),
    ADD COLUMN lng DECIMAL(11, 8),
    ADD COLUMN direccion VARCHAR(255);


-- ============================================================
-- 2. MIGRAR DATOS DE business → branch
--    (copiar owner, category, descripcion, logo, banner)
-- ============================================================
UPDATE branch b
SET
    owner_user_id = bs.owner_user_id,
    category_id   = bs.category_id,
    descripcion   = bs.descripcion,
    logo_url      = bs.logo_url,
    banner_url    = bs.banner_url
FROM business bs
WHERE b.business_id = bs.id;


-- ============================================================
-- 3. MIGRAR COORDENADAS DE locations → branch
-- ============================================================
UPDATE branch b
SET
    lat       = l.lat,
    lng       = l.lng,
    direccion = l.direccion
FROM locations l
WHERE l.branch_id = b.id;


-- ============================================================
-- 4. NUEVA TABLA: device_tokens
-- ============================================================
CREATE TABLE device_tokens (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID REFERENCES users(id) ON DELETE CASCADE,
    token      VARCHAR(500) NOT NULL,
    platform   VARCHAR(20) NOT NULL,
    active     BOOLEAN DEFAULT TRUE,
    creado_en  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, token)
);


-- ============================================================
-- 5. AGREGAR push_sent EN notifications
-- ============================================================
ALTER TABLE notifications
    ADD COLUMN push_sent BOOLEAN DEFAULT FALSE;


-- ============================================================
-- 6. AGREGAR deleted EN user_notifications
-- ============================================================
ALTER TABLE user_notifications
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE;


-- ============================================================
-- 7. ACTUALIZAR menus: business_id → branch_id
-- ============================================================
-- Primero agregamos la nueva columna
ALTER TABLE menus
    ADD COLUMN branch_id UUID REFERENCES branch(id) ON DELETE CASCADE;

-- Migramos: buscamos el branch que pertenece al mismo business
UPDATE menus m
SET branch_id = b.id
FROM branch b
WHERE b.business_id = m.business_id;

-- Quitamos la columna vieja
ALTER TABLE menus
    DROP COLUMN business_id;


-- ============================================================
-- 8. ACTUALIZAR reviews: business_id → branch_id
-- ============================================================
ALTER TABLE reviews
    ADD COLUMN branch_id UUID REFERENCES branch(id) ON DELETE CASCADE;

UPDATE reviews r
SET branch_id = b.id
FROM branch b
WHERE b.business_id = r.business_id;

ALTER TABLE reviews
    DROP COLUMN business_id;

-- Constraint: un usuario solo puede reseñar una vez por sucursal
ALTER TABLE reviews
    ADD CONSTRAINT reviews_user_branch_unique UNIQUE (user_id, branch_id);


-- ============================================================
-- 9. ACTUALIZAR favorites: business_id → branch_id
-- ============================================================
ALTER TABLE favorites
    ADD COLUMN branch_id UUID REFERENCES branch(id) ON DELETE CASCADE;

UPDATE favorites f
SET branch_id = b.id
FROM branch b
WHERE b.business_id = f.business_id;

ALTER TABLE favorites
    DROP COLUMN business_id;

-- Nuevo constraint único
ALTER TABLE favorites
    ADD CONSTRAINT favorites_user_branch_unique UNIQUE (user_id, branch_id);


-- ============================================================
-- 10. ACTUALIZAR notifications: quitar business_id, mantener branch_id
-- ============================================================
-- Rellenar branch_id donde solo había business_id
UPDATE notifications n
SET branch_id = b.id
FROM branch b
WHERE b.business_id = n.business_id
  AND n.branch_id IS NULL;

ALTER TABLE notifications
    DROP CONSTRAINT IF EXISTS notifications_business_id_fkey,
    DROP COLUMN business_id;


-- ============================================================
-- 11. ACTUALIZAR zone_favorites: business_id → branch_id
-- ============================================================
ALTER TABLE zone_favorites
    ADD COLUMN branch_id UUID REFERENCES branch(id) ON DELETE CASCADE;

UPDATE zone_favorites zf
SET branch_id = b.id
FROM branch b
WHERE b.business_id = zf.business_id;

ALTER TABLE zone_favorites
    DROP COLUMN business_id;

ALTER TABLE zone_favorites
    ADD CONSTRAINT zone_favorites_unique UNIQUE (zone_id, branch_id);


-- ============================================================
-- 12. LIMPIAR: quitar business_id de branch ahora que ya migramos
-- ============================================================
ALTER TABLE branch
    DROP CONSTRAINT IF EXISTS branch_business_id_fkey,
    DROP COLUMN business_id;


-- ============================================================
-- 13. ELIMINAR TABLAS YA INNECESARIAS
-- ============================================================
DROP TABLE IF EXISTS branch_menu;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS business;


-- ============================================================
-- 14. TRIGGER: notificar al agregar una nueva sucursal
-- ============================================================
CREATE OR REPLACE FUNCTION notify_nueva_sucursal()
RETURNS TRIGGER AS $$
DECLARE
    v_notif_id UUID;
BEGIN
    INSERT INTO notifications (branch_id, category_id, titulo, cuerpo, tipo, payload)
    VALUES (
        NEW.id,
        NEW.category_id,
        'Nueva sucursal disponible',
        NEW.nombre || ' ya esta disponible en ' || COALESCE(NEW.direccion, 'una nueva ubicacion'),
        'nueva_sucursal',
        jsonb_build_object(
            'branch_id', NEW.id,
            'branch_nombre', NEW.nombre,
            'lat', NEW.lat,
            'lng', NEW.lng
        )
    )
    RETURNING id INTO v_notif_id;

    -- Notificar a usuarios que tienen favoritos con notify_new_branch = true
    INSERT INTO user_notifications (user_id, notification_id)
    SELECT f.user_id, v_notif_id
    FROM favorites f
    WHERE f.branch_id = NEW.id
      AND f.notify_new_branch = TRUE;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_nueva_sucursal
AFTER INSERT ON branch
FOR EACH ROW
EXECUTE FUNCTION notify_nueva_sucursal();