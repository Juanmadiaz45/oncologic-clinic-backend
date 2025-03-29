-- Insertar permisos básicos
INSERT INTO permissions (id, name)
VALUES (1, 'READ'),
       (2, 'WRITE'),
       (3, 'ADMIN');

-- Insertar roles básicos (asegurando que tengan al menos un permiso)
INSERT INTO roles (id, name)
VALUES (1, 'USER'),
       (2, 'EDITOR'),
       (3, 'ADMIN');

-- Asignar permisos a roles
INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1), -- USER tiene READ
       (2, 1), -- EDITOR tiene READ
       (2, 2), -- EDITOR tiene WRITE
       (3, 3); -- ADMIN tiene ADMIN


ALTER SEQUENCE roles_id_seq RESTART WITH 4;
ALTER SEQUENCE permissions_id_seq RESTART WITH 4;