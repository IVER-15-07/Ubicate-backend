-- Quitar images
DROP TABLE IF EXISTS images;

-- Quitar ubicacion de branch
ALTER TABLE branch DROP COLUMN IF EXISTS lat;
ALTER TABLE branch DROP COLUMN IF EXISTS lng;
ALTER TABLE branch DROP COLUMN IF EXISTS direccion;


-- Locations ahora apunta a branch
ALTER TABLE locations DROP CONSTRAINT IF EXISTS locations_business_id_fkey;
ALTER TABLE locations DROP COLUMN IF EXISTS business_id;
ALTER TABLE locations ADD COLUMN branch_id UUID REFERENCES branch(id) ON DELETE CASCADE;

-- Nueva tabla menu_images
CREATE TABLE IF NOT EXISTS menu_images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    menu_item_id UUID REFERENCES menu_items(id) ON DELETE CASCADE,
    url VARCHAR(255) NOT NULL,
    orden INT DEFAULT 0,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);