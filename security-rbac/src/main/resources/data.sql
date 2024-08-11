-- username: admin, password: admin
INSERT INTO users (id, username, password, email, enabled)
VALUES (1000, 'admin', '$2a$10$r5p.DEUMgBCpDasy86n/Ue4aNYvDYAt8EkYt4EziQHWrTPKoqFWZO', 'admin@email.com', true);
-- username: user, password: user
INSERT INTO users (id, username, password, email, enabled)
VALUES (2000, 'user', '$2a$10$kdbtEguaRRGv3.y/wVIToOLIz4TzqIp2fk5E8slB.YOWvuyX.Z4u.', 'user@email.com', true);

INSERT INTO role (id, authority)
VALUES (1000, 'ADMIN');
INSERT INTO role (id, authority)
VALUES (2000, 'USER');

INSERT INTO permission (id, name)
VALUES (1000, 'READ_PRIVILEGE');
INSERT INTO permission (id, name)
VALUES (2000, 'WRITE_PRIVILEGE');

-- admin has ADMIN role with read and write permission
INSERT INTO role_permission (id, role_id, permission_id)
VALUES (1000, 1000, 1000);
INSERT INTO role_permission (id, role_id, permission_id)
VALUES (2000, 1000, 2000);
-- user has USER role with read permission
INSERT INTO role_permission (id, role_id, permission_id)
VALUES (3000, 2000, 1000);

-- admin has ADMIN role
INSERT INTO user_role (id, user_id, role_id)
VALUES (1000, 1000, 1000);
-- user has USER
INSERT INTO user_role (id, user_id, role_id)
VALUES (2000, 2000, 2000);
