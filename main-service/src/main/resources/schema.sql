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
    category_name VARCHAR(20)                             NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (category_name)
);