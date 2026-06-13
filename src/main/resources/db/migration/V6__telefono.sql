ALTER TABLE branch
    ADD COLUMN telefono VARCHAR(30),
    DROP CONSTRAINT IF EXISTS branch_business_id_fkey,
    DROP COLUMN IF EXISTS business_id;