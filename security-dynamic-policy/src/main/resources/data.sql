INSERT INTO policy (id, type, criteria, identifier, condition, criteria_script, action_name, action_script,
                    action_params, created_by, created_at, version)
VALUES (1000, 'BLACKLIST', 'IP', '192.168.1.100', '#criteria == ''192.168.1.100''', 'return request.remoteAddr',
        'Deny Access', 'response.sendError(403, ''Denied'');', NULL, 'admin', CURRENT_TIMESTAMP, 0);
INSERT INTO policy (id, type, criteria, identifier, condition, criteria_script, action_name, action_script,
                    action_params, created_by, created_at, version)
VALUES (2000, 'BLACKLIST', 'COUNTRY', 'XX', '#criteria == ''XX''', 'return request.getHeader(''X-Country'')',
        'Deny Country', 'response.sendError(403, ''Access Denied from Country: '' + #criteria);', NULL, 'admin',
        CURRENT_TIMESTAMP, 0);
