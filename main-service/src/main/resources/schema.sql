CREATE TABLE IF NOT EXISTS users
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name VARCHAR(70)                             NOT NULL,
    email     VARCHAR(254)                            NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS categories
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    category_name VARCHAR(70)                             NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (category_name)
);
CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL                                    NOT NULL,
    lon REAL                                    NOT NULL,
    CONSTRAINT PK_LOCATION PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    participant_limit  BIGINT                                  NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    status             VARCHAR(9)                              NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    title              VARCHAR(120)                            NOT NULL,
    initiator_id       BIGINT                                  NOT NULL,
    category_id        BIGINT                                  NOT NULL,
    location_id        BIGINT                                  NOT NULL,
    CONSTRAINT PK_EVENT PRIMARY KEY (id),
    FOREIGN KEY (initiator_id) REFERENCES users (id),
    FOREIGN KEY (category_id)  REFERENCES categories (id),
    FOREIGN KEY (location_id)  REFERENCES locations (id)
);
CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id     BIGINT                                  NOT NULL,
    requester_id BIGINT                                  NOT NULL,
    status       VARCHAR(9)                              NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    FOREIGN KEY (event_id)      REFERENCES events (id),
    FOREIGN KEY (requester_id)  REFERENCES users (id)
);
CREATE TABLE IF NOT EXISTS compilations
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title    VARCHAR(120)                            NOT NULL,
    pinned   BOOLEAN                                 NOT NULL,
    CONSTRAINT PK_COMPILATION PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    events_id      BIGINT NOT NULL,
    CONSTRAINT PK_COMPILATION_EVENT PRIMARY KEY (compilation_id, events_id),
    FOREIGN KEY (compilation_id)  REFERENCES compilations (id) ON DELETE CASCADE,
    FOREIGN KEY (events_id)       REFERENCES events (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS comments
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id       BIGINT                                  NOT NULL,
    author_id      BIGINT                                  NOT NULL,
    comment_text   VARCHAR(7000)                           NOT NULL,
    created        TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    edited         TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (event_id)      REFERENCES events (id),
    FOREIGN KEY (author_id)  REFERENCES users (id)
);