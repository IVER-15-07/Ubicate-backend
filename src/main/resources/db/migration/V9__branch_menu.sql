-- V_xx__redesign_menus_relation.sql

ALTER TABLE public.menus DROP CONSTRAINT IF EXISTS menus_branch_id_fkey;
ALTER TABLE public.menus DROP COLUMN IF EXISTS branch_id;

ALTER TABLE public.menus ALTER COLUMN is_active DROP DEFAULT;
ALTER TABLE public.menus ALTER COLUMN is_active TYPE boolean USING (is_active::boolean);
ALTER TABLE public.menus ALTER COLUMN is_active SET DEFAULT false;

ALTER TABLE public.menus ADD COLUMN owner_user_id uuid;
ALTER TABLE public.menus ADD CONSTRAINT menus_owner_user_id_fkey
    FOREIGN KEY (owner_user_id) REFERENCES public.users(id);

CREATE TABLE public.branch_menus (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    branch_id uuid NOT NULL,
    menu_id uuid NOT NULL,
    is_active boolean DEFAULT true,
    creado_en timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT branch_menus_pkey PRIMARY KEY (id),
    CONSTRAINT branch_menus_branch_id_fkey FOREIGN KEY (branch_id) REFERENCES public.branch(id),
    CONSTRAINT branch_menus_menu_id_fkey FOREIGN KEY (menu_id) REFERENCES public.menus(id),
    CONSTRAINT branch_menus_unique UNIQUE (branch_id, menu_id)
);