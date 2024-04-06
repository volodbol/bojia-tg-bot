CREATE TABLE IF NOT EXISTS bojia_bot_details
(
    id                  SERIAL PRIMARY KEY,
    my_commands_present BOOLEAN
);

CREATE TABLE IF NOT EXISTS bojia_bot_user
(
    id         INTEGER NOT NULL PRIMARY KEY,
    chat_id    INTEGER,
    first_name VARCHAR(512),
    prompt     VARCHAR(2048)
);