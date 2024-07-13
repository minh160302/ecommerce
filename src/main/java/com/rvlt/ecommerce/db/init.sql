CREATE TABLE if not exists users
(
    id         BIGSERIAL PRIMARY KEY,
    firstName  VARCHAR(255) NOT NULL,
    lastName   VARCHAR(255) NOT NULL,
    dob        VARCHAR(255),
    created_at TIMESTAMP
);

-- total = instock + processing + delivered
-- balance = 0 (expected, for debugging purpose)
-- in_session_holding: might be using later
CREATE TABLE if not exists inventories
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    total_count        INT,
    in_stock_count     INT,
    processing_count   INT,
    delivered_count    INT,
    in_session_holding INT,
    balance            INT
);


CREATE TABLE if not exists products
(
    id       BIGSERIAL PRIMARY KEY references inventories (id),
    name     VARCHAR(255) NOT NULL,
    in_stock INT,
    price    NUMERIC(10, 2)
);

-- always only one active session per user (at any given time)
-- create new session each time the previous session is submitted
CREATE TABLE if not exists sessions
(
    id         BIGSERIAL PRIMARY KEY,
    status     VARCHAR(255) NOT NULL, -- IN_PROGRESS/SUBMITTED
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    user_id    BIGINT       NOT NULL references users (id)
);


CREATE TABLE if not exists orders
(
    id         BIGSERIAL PRIMARY KEY references sessions (id),
    status     VARCHAR(255) NOT NULL, -- PROCESSING/IN_PROGRESS/DELIVERED
    created_at TIMESTAMP NOT NULL ,
    history    VARCHAR(255) NOT NULL, -- update history
    user_id    BIGINT       NOT NULL references users (id)
);

/**
Relationships:
  - user - session:             1 to many
  - session - product:          many to many
  - user - order:               1 to many
  - order - session:            1 to 1
  - product - inventory:        1 to 1

  not necessary
  - order - product:            1 to many

  might come up with different design to store order tracking history
 */

CREATE TABLE if not exists sessions_products
(
    session_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_session FOREIGN KEY (session_id) REFERENCES sessions (id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id),
    count      INT
);


-- INDEXING
create unique index idx_users_id ON users using hash(id);
