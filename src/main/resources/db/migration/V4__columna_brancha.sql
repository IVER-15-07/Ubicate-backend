-- ============================================================
-- V4__agregar_is_active_a_branch.sql
-- Agregar: estado de activación a las sucursales (branch)
-- ============================================================

-- 1. Agregar la columna is_active con valor por defecto TRUE
ALTER TABLE branch 
    ADD COLUMN is_active BOOLEAN DEFAULT FALSE;