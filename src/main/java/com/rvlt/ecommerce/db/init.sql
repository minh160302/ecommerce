CREATE TABLE if not exists users
(
    id         BIGSERIAL PRIMARY KEY,
    firstName  VARCHAR(255) NOT NULL,
    lastName   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    dob        VARCHAR(255),
    created_at TIMESTAMP
);

-- total = instock + processing + delivered
-- balance = 0 (expected, for debugging purpose)
-- in_session_holding: might be using later
CREATE TABLE if not exists inventories
(
    id                         BIGSERIAL PRIMARY KEY,
    name                       VARCHAR(255) NOT NULL UNIQUE,
    total_count                INT,
    in_stock_count             INT,
    processing_submit_count    INT,
    delivery_in_progress_count INT,
    delivered_count            INT,
    processing_cancel_count    INT,
    cancelled_count            INT,
    cancel_in_progress_count   INT,
    returned_count             INT,
    return_in_progress_count   INT,

    --- not important column
    delivery_failed            INT,
    return_failed              INT,
    cancel_failed              INT,
    in_session_holding         INT,
    balance                    INT
);


CREATE TABLE if not exists products
(
    id       BIGSERIAL PRIMARY KEY references inventories (id) ON DELETE CASCADE,
    name     VARCHAR(255) NOT NULL UNIQUE,
    in_stock INT,
    price    NUMERIC(10, 2)
);

CREATE TABLE if not exists categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    active      BOOLEAN,
    UNIQUE(name)
-- type   VARCHAR: normal/onboarding_sale/xmas_sale/50%-off/black-friday.....
);


-- always only one active session per user (at any given time)
-- create new session each time the previous session is submitted
CREATE TABLE if not exists sessions
(
    id           BIGSERIAL PRIMARY KEY,
    status       VARCHAR(255)   NOT NULL, -- ACTIVE/INACTIVE
    total_amount NUMERIC(10, 2) NOT NULL,
    created_at   TIMESTAMP      NOT NULL,
    updated_at   TIMESTAMP,
    user_id      BIGINT         NOT NULL references users (id) ON DELETE CASCADE
);


CREATE TABLE if not exists orders
(
    id           BIGSERIAL PRIMARY KEY references sessions (id) ON DELETE CASCADE,
    status       VARCHAR(255) NOT NULL, -- NOT SUBMITTED/PROCESSING_SUBMIT/DELIVERY_IN_PROGRESS/DELIVERED/DELIVERY_FAILED/PROCESSING_CANCEL/RETURN_IN_PROGRESS
    created_at   TIMESTAMP    NOT NULL,
    submitted_at TIMESTAMP,
    history      VARCHAR(255)           -- update history
);

/**
Relationships:
  - user - session:             1 to many
  - session - product:          many to many
// user - order:               1 to many
  - order - session:            1 to 1
  - product - inventory:        1 to 1
  - product - category          many to many
        why category connects to product instead of inventory?
        The demand to query products' categories is larger than to query inventories'

  not necessary
  - order - product:            1 to many

  might come up with different design to store order tracking history
 */

CREATE TABLE if not exists sessions_products
(
    session_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_session FOREIGN KEY (session_id) REFERENCES sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    count      INT
-- ordered_price_per_item???
);


CREATE TABLE if not exists products_categories
(
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);


-- INDEXING
