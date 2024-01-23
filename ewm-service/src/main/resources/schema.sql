  CREATE TABLE IF NOT EXISTS categories (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     name VARCHAR(50) NOT NULL,
     CONSTRAINT PK_CATEGORY PRIMARY KEY (id),
     CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
  );

  CREATE TABLE IF NOT EXISTS users (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     name VARCHAR(250) NOT NULL,
     email VARCHAR(254) NOT NULL,
     CONSTRAINT PK_USER PRIMARY KEY (id),
     CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
  );

  CREATE TABLE IF NOT EXISTS compilations (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     title VARCHAR(50) NOT NULL,
     pinned BOOLEAN NOT NULL,
     CONSTRAINT PK_COMPILATION PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS locations (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     lat FLOAT NOT NULL,
     lon FLOAT NOT NULL,
     CONSTRAINT PK_LOCATION PRIMARY KEY (id)
  );

  CREATE TABLE IF NOT EXISTS events (
     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     title VARCHAR(120) NOT NULL,
     annotation VARCHAR(2000) NOT NULL,
     description VARCHAR(7000) NOT NULL,
     initiator_id BIGINT NOT NULL,
     category_id BIGINT NOT NULL,
     location_id BIGINT NOT NULL,
     event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     paid BOOLEAN NOT NULL,
     participant_limit BIGINT NOT NULL,
     request_moderation BOOLEAN NOT NULL,
     state VARCHAR(50) NOT NULL,
     confirmed_requests BIGINT NOT NULL,
     created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
     published TIMESTAMP WITHOUT TIME ZONE,
     views BIGINT NOT NULL,
     CONSTRAINT PK_EVENT PRIMARY KEY (id),
     CONSTRAINT FK_EVENT_INITIATOR FOREIGN KEY (initiator_id) REFERENCES users (id),
     CONSTRAINT FK_EVENT_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id),
     CONSTRAINT FK_EVENT_LOCATION FOREIGN KEY (location_id) REFERENCES locations (id)
  );

  CREATE TABLE IF NOT EXISTS requests (
      id BIGINT GENERATED BY DEFAULT AS IDENTITY,
      requester_id  BIGINT NOT NULL,
      event_id BIGINT NOT NULL,
      status VARCHAR(60) NOT NULL,
      created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      CONSTRAINT PK_REQUEST PRIMARY KEY (id),
      CONSTRAINT FK_REQUEST_REQUESTER FOREIGN KEY (requester_id) REFERENCES users (id),
      CONSTRAINT FK_REQUEST_EVENT FOREIGN KEY (event_id) REFERENCES events (id),
      CONSTRAINT UQ_REQUESTER_EVENT UNIQUE (requester_id, event_id)
  );

  CREATE TABLE IF NOT EXISTS compilation_event (
     compilation_id BIGINT NOT NULL,
     event_id BIGINT NOT NULL,
     CONSTRAINT FK_COMPILATION FOREIGN KEY (compilation_id) REFERENCES compilations (id),
     CONSTRAINT FK_EVENT FOREIGN KEY (event_id) REFERENCES events (id),
     PRIMARY KEY (compilation_id, event_id)
  );