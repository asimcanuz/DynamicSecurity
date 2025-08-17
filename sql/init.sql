BEGIN;

-- Roles
CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Permissions
CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    description TEXT NOT NULL
);

-- Users
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(30),
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150),
    password VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    role_id INTEGER NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
);

-- Join table: role_permissions
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_users_role_id ON users (role_id);

CREATE INDEX IF NOT EXISTS idx_role_permissions_permission_id ON role_permissions (permission_id);

-- Seed roles
INSERT INTO
    roles (name)
VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;

INSERT INTO
    roles (name)
VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;

-- Seed permissions (add as many as needed)
INSERT INTO
    permissions (code, description)
VALUES (
        'god',
        'Superuser / all permissions'
    ) ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('user.create', 'Create users') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('user.read', 'Read user data') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('user.update', 'Update users') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('user.delete', 'Delete users') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('role.create', 'Create roles') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('role.read', 'Read roles') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('role.update', 'Update roles') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES ('role.delete', 'Delete roles') ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES (
        'permission.create',
        'Create permissions'
    ) ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES (
        'permission.read',
        'Read permissions'
    ) ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES (
        'permission.update',
        'Update permissions'
    ) ON CONFLICT (code) DO NOTHING;

INSERT INTO
    permissions (code, description)
VALUES (
        'permission.delete',
        'Delete permissions'
    ) ON CONFLICT (code) DO NOTHING;

-- Grant all permissions to ROLE_ADMIN
WITH
    r AS (
        SELECT id
        FROM roles
        WHERE
            name = 'ROLE_ADMIN'
    )
INSERT INTO
    role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM
    r,
    permissions p ON CONFLICT DO NOTHING;

-- Give ROLE_USER minimal permissions (example: read user)
WITH
    r AS (
        SELECT id
        FROM roles
        WHERE
            name = 'ROLE_USER'
    ),
    p AS (
        SELECT id
        FROM permissions
        WHERE
            code = 'user.read'
    )
INSERT INTO
    role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM r, p ON CONFLICT DO NOTHING;

COMMIT;