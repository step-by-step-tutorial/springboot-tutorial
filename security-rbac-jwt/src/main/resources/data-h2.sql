-- ========== User ==========
-- username: admin
-- password: admin
INSERT INTO users (id, username, password, email, enabled, created_by, created_at, version)
VALUES (1000, 'admin', '$2a$10$r5p.DEUMgBCpDasy86n/Ue4aNYvDYAt8EkYt4EziQHWrTPKoqFWZO', 'admin@email.com', true, 'admin',
        CURRENT_TIMESTAMP, 0);
-- username: user
-- password: user
INSERT INTO users (id, username, password, email, enabled, created_by, created_at, version)
VALUES (2000, 'user', '$2a$10$kdbtEguaRRGv3.y/wVIToOLIz4TzqIp2fk5E8slB.YOWvuyX.Z4u.', 'user@email.com', true, 'admin',
        CURRENT_TIMESTAMP, 0);
-- username: test
-- password: test
INSERT INTO users (id, username, password, email, enabled, created_by, created_at, version)
VALUES (3000, 'test', '$2a$10$0af5EU3EIOnMlPBtxEN49O3fGMbExAX4kwhAAHisXE86WNOz6WoQi', 'test@email.com', true, 'admin',
        CURRENT_TIMESTAMP, 0);

-- ========== Role ==========
INSERT INTO role (id, name, created_by, created_at, version)
VALUES (1000, 'ADMIN', 'admin', CURRENT_TIMESTAMP, 0);
INSERT INTO role (id, name, created_by, created_at, version)
VALUES (2000, 'USER', 'admin', CURRENT_TIMESTAMP, 0);
INSERT INTO role (id, name, created_by, created_at, version)
VALUES (3000, 'TEST', 'admin', CURRENT_TIMESTAMP, 0);

-- ========== Permission ==========
INSERT INTO permission (id, name, created_by, created_at, version)
VALUES (1000, 'CREAT', 'admin', CURRENT_TIMESTAMP, 0);
INSERT INTO permission (id, name, created_by, created_at, version)
VALUES (2000, 'READ', 'admin', CURRENT_TIMESTAMP, 0);
INSERT INTO permission (id, name, created_by, created_at, version)
VALUES (3000, 'UPDATE', 'admin', CURRENT_TIMESTAMP, 0);
INSERT INTO permission (id, name, created_by, created_at, version)
VALUES (4000, 'DELETE', 'admin', CURRENT_TIMESTAMP, 0);

-- ========== Role and Permission ==========
-- ADMIN role with CRUD permission
INSERT INTO role_permission (role_id, permission_id)
VALUES (1000, 1000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (1000, 2000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (1000, 3000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (1000, 4000);
-- USER role with READ permission
INSERT INTO role_permission (role_id, permission_id)
VALUES (2000, 2000);
-- TEST role with CRUD permission
INSERT INTO role_permission (role_id, permission_id)
VALUES (3000, 1000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (3000, 2000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (3000, 3000);
INSERT INTO role_permission (role_id, permission_id)
VALUES (3000, 4000);

-- ========== User and Role ==========
-- admin has ADMIN and USER roles
INSERT INTO user_role (user_id, role_id)
VALUES (1000, 1000);
INSERT INTO user_role (user_id, role_id)
VALUES (1000, 2000);
-- user has USER role
INSERT INTO user_role (user_id, role_id)
VALUES (2000, 2000);
-- test has TEST and USER roles
INSERT INTO user_role (user_id, role_id)
VALUES (3000, 2000);
INSERT INTO user_role (user_id, role_id)
VALUES (3000, 3000);
