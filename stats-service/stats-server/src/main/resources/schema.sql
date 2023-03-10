CREATE TABLE IF NOT EXISTS apps
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app_name VARCHAR(20)                             NOT NULL,
    CONSTRAINT PK_APPS PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS hits
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app_id   BIGINT                                  NOT NULL,
    uri      VARCHAR(254)                            NOT NULL,
    ip       VARCHAR(15)                             NOT NULL,
    hit_date TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT PK_HITS PRIMARY KEY (id),
    CONSTRAINT HITS_APPS_FK
    FOREIGN KEY (app_id) REFERENCES apps (id)
);