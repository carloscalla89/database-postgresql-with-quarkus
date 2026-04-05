-- Tabla 1: Clientes
CREATE TABLE client (
    id UUID NOT NULL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,

    -- Campos de BaseAuditEntity
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Tabla 2: Pedidos
CREATE TABLE order_client (
    id UUID NOT NULL PRIMARY KEY,
    client_id UUID NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    notes TEXT,

    -- Campos de BaseAuditEntity
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),

    CONSTRAINT fk_order_client FOREIGN KEY (client_id) REFERENCES client(id)
);