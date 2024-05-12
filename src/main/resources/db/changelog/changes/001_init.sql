CREATE TABLE IF NOT EXISTS bojia_bot_details
(
    id                  SERIAL PRIMARY KEY,
    my_commands_present BOOLEAN
);

CREATE TABLE IF NOT EXISTS bojia_bot_user
(
    id         INTEGER       NOT NULL PRIMARY KEY,
    chat_id    INTEGER       NOT NULL,
    first_name VARCHAR(512)  NOT NULL,
    prompt     VARCHAR(2048) NOT NULL
);

CREATE TABLE IF NOT EXISTS bojia_bot_user_search
(
    id       SERIAL PRIMARY KEY,
    user_id  INTEGER       NOT NULL,
    provider VARCHAR(512)  NOT NULL,
    keywords VARCHAR(1024) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES bojia_bot_user (id)
);