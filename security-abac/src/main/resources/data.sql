-- Insert permissions
INSERT INTO permission (id, name)
VALUES (1, 'READ_PRIVILEGE');
INSERT INTO permission (id, name)
VALUES (2, 'WRITE_PRIVILEGE');

-- Insert roles
INSERT INTO role (id, authority)
VALUES (1, 'ADMIN');
INSERT INTO role (id, authority)
VALUES (2, 'USER');

-- Link roles and permissions
-- admin has read and write permission
-- user has read permission
INSERT INTO roles_permissions (role_id, permission_id)
VALUES (1, 1);
INSERT INTO roles_permissions (role_id, permission_id)
VALUES (1, 2);
INSERT INTO roles_permissions (role_id, permission_id)
VALUES (2, 1);

-- Insert users
-- password: admin
INSERT INTO users (id, username, password, enabled)
VALUES (1, 'admin', '$2a$10$r5p.DEUMgBCpDasy86n/Ue4aNYvDYAt8EkYt4EziQHWrTPKoqFWZO', true);
-- password: user
INSERT INTO users (id, username, password, enabled)
VALUES (2, 'user', '$2a$10$kdbtEguaRRGv3.y/wVIToOLIz4TzqIp2fk5E8slB.YOWvuyX.Z4u.', true);

-- Link users and roles
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id)
VALUES (2, 2);
