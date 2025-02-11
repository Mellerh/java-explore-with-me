CREATE TABLE IF NOT EXISTS stats (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);