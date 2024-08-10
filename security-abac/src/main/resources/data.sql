-- username: admin, password: admin
INSERT INTO users (id, username, password, enabled) VALUES (1, 'admin', '$2a$10$r5p.DEUMgBCpDasy86n/Ue4aNYvDYAt8EkYt4EziQHWrTPKoqFWZO', true);
-- username: user, password: user
INSERT INTO users (id, username, password, enabled) VALUES (2, 'user', '$2a$10$kdbtEguaRRGv3.y/wVIToOLIz4TzqIp2fk5E8slB.YOWvuyX.Z4u.', true);

INSERT INTO role (id, authority) VALUES (1, 'ADMIN');
INSERT INTO role (id, authority) VALUES (2, 'USER');

INSERT INTO permission (id, name) VALUES (1, 'READ_PRIVILEGE');
INSERT INTO permission (id, name) VALUES (2, 'WRITE_PRIVILEGE');

-- admin has ADMIN role with read and write permission
INSERT INTO roles_permissions (role_id, permission_id) VALUES (1, 1);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (1, 2);
-- user has USER role with read permission
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 1);

-- admin has ADMIN role
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
-- user has USER
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
