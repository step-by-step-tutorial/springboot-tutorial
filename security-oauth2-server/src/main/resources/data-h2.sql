-- ========== Add User ==========
-- username: admin, password: admin
INSERT INTO users (id, username, password, email, enabled)
VALUES (1000, 'admin', '$2a$10$r5p.DEUMgBCpDasy86n/Ue4aNYvDYAt8EkYt4EziQHWrTPKoqFWZO', 'admin@email.com', true);
-- username: user, password: user
INSERT INTO users (id, username, password, email, enabled)
VALUES (2000, 'user', '$2a$10$kdbtEguaRRGv3.y/wVIToOLIz4TzqIp2fk5E8slB.YOWvuyX.Z4u.', 'user@email.com', true);
-- username: test, password: test
INSERT INTO users (id, username, password, email, enabled)
VALUES (3000, 'test', '$2a$10$0af5EU3EIOnMlPBtxEN49O3fGMbExAX4kwhAAHisXE86WNOz6WoQi', 'test@email.com', true);

-- ========== Add Role ==========
INSERT INTO role (id, name)
VALUES (1000, 'ADMIN');
INSERT INTO role (id, name)
VALUES (2000, 'USER');
INSERT INTO role (id, name)
VALUES (3000, 'TEST');

-- ========== Add Permission ==========
INSERT INTO permission (id, name)
VALUES (1000, 'READ_PRIVILEGE');
INSERT INTO permission (id, name)
VALUES (2000, 'WRITE_PRIVILEGE');

-- ========== Add Role and Permission ==========
-- admin has ADMIN role with read and write permission
INSERT INTO role_permission (role_id, permission_id)
VALUES (1000, 1000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (1000, 2000);
-- user has USER role with read permission
INSERT INTO role_permission (role_id, permission_id)
VALUES (2000, 1000);
-- test has TEST role with read and write permission
INSERT INTO role_permission (role_id, permission_id)
VALUES (3000, 1000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (3000, 2000);

-- ========== Add User and Role ==========
-- admin has ADMIN role
INSERT INTO user_role (user_id, role_id)
VALUES (1000, 1000);
-- user has USER
INSERT INTO user_role (user_id, role_id)
VALUES (2000, 2000);
-- test has TEST role
INSERT INTO user_role (user_id, role_id)
VALUES (3000, 3000);