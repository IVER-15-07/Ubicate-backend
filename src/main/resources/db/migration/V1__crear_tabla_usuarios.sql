-- ROLES
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- USUARIOS
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT REFERENCES roles(id),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PREFERENCIAS DE USUARIO
CREATE TABLE preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    push_enabled BOOLEAN DEFAULT TRUE,
    language VARCHAR(10) DEFAULT 'es'
);

-- CATEGORIAS
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    icono VARCHAR(100)
);

-- PREFERENCIAS DE CATEGORIA POR USUARIO
CREATE TABLE user_category_preferences (
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    category_id INT REFERENCES category(id) ON DELETE CASCADE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, category_id)
);

-- NEGOCIOS
CREATE TABLE business (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    category_id INT REFERENCES category(id),
    logo_url VARCHAR(255),
    banner_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



-- SUCURSALES
CREATE TABLE branch (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    lat DECIMAL(10, 8),
    lng DECIMAL(11, 8),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- MENUS
CREATE TABLE menus (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    nombre VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- RELACION SUCURSAL - MENU
CREATE TABLE branch_menu (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id UUID REFERENCES branch(id) ON DELETE CASCADE,
    menu_id UUID REFERENCES menus(id) ON DELETE CASCADE,
    UNIQUE (branch_id, menu_id)
);

-- ITEMS DEL MENU
CREATE TABLE menu_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    menu_id UUID REFERENCES menus(id) ON DELETE CASCADE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    photo_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- IMAGENES (galeria del negocio y sucursales)
CREATE TABLE images (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    branch_id UUID REFERENCES branch(id) ON DELETE CASCADE,
    url VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- UBICACIONES
CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    lat DECIMAL(10, 8) NOT NULL,
    lng DECIMAL(11, 8) NOT NULL,
    direccion VARCHAR(255),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- RESEÑAS
CREATE TABLE reviews (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comentario TEXT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FAVORITOS
CREATE TABLE favorites (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, business_id)
);

-- HISTORIAL DE BUSQUEDA
CREATE TABLE search_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    query VARCHAR(255),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- NOTIFICACIONES
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(150) NOT NULL,
    cuerpo TEXT,
    category_id INT REFERENCES category(id) ON DELETE SET NULL,
    business_id UUID REFERENCES business(id) ON DELETE CASCADE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- NOTIFICACIONES POR USUARIO
CREATE TABLE user_notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    notification_id UUID REFERENCES notifications(id) ON DELETE CASCADE,
    is_read BOOLEAN DEFAULT FALSE,
    enviado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);