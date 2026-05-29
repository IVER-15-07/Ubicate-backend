-- ============================================================
-- V3__nueva_migration.sql
-- Agregar: zonas, favoritos con notificaciones, notifications mejorado
-- ============================================================

-- 1. MODIFICAR favorites: agregar flags de notificacion
ALTER TABLE favorites
    ADD COLUMN notify_offers BOOLEAN DEFAULT TRUE,
    ADD COLUMN notify_new_branch BOOLEAN DEFAULT TRUE;

-- 2. MODIFICAR notifications: agregar tipo, payload y branch_id
--    para que funcione como trigger de notificacion tambien
ALTER TABLE notifications
    ADD COLUMN branch_id UUID REFERENCES branch(id) ON DELETE CASCADE,
    ADD COLUMN tipo VARCHAR(50) NOT NULL DEFAULT 'general',
    ADD COLUMN payload JSONB;

-- 3. NUEVA TABLA: zonas dibujadas por el usuario en el mapa
CREATE TABLE zone_polygons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL,
    coordinates JSONB NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. NUEVA TABLA: negocios que caen dentro de una zona (calculado por backend)
CREATE TABLE zone_favorites (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id UUID REFERENCES zone_polygons(id) ON DELETE CASCADE,
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (zone_id, business_id)
);