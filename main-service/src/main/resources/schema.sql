DROP TABLE IF EXISTS users, categories, locations, events, compilations, requests, compilations_events;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR NOT NULL,
    category_id BIGINT,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    description VARCHAR NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT,
    location_id BIGINT,
    paid BOOLEAN,
    participation_limit INTEGER,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state VARCHAR,
    title VARCHAR,
    CONSTRAINT fk_category_events FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_events FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_location_events FOREIGN KEY (location_id) REFERENCES locations (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created TIMESTAMP WITHOUT TIME ZONE,
    event_id BIGINT,
    requester_id BIGINT,
    status VARCHAR,
    CONSTRAINT fk_event_requesters FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_requests_requesters FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations_events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    compilation_id BIGINT,
    CONSTRAINT fk_events_compilations_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_compilations_compilations_events FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
);
