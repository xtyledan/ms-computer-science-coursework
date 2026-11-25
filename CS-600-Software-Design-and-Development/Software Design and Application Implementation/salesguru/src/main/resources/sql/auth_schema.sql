CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS auth_user (
    username VARCHAR(80) PRIMARY KEY,
    password_hash VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Replace these placeholders before executing.
-- INSERT INTO auth_user (
--     username,
--     password_hash,
--     enabled
-- )
-- VALUES (
--     'your_username',
--     crypt('your_unique_password', gen_salt('bf', 12)),
--     TRUE
-- );