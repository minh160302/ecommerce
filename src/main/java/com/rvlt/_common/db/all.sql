DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- ENUMS
CREATE TYPE rvlt_role AS ENUM ('rvlt_admin', 'rvlt_mod', 'user');

CREATE TYPE blog_status AS ENUM (
    'UNPUBLISHED', 'WAITING_MOD_APPROVAL'
    , 'MOD_APPROVED', 'MOD_SEND_BACK', 'MOD_REJECTED'
    , 'ADMIN_PUBLISHED', 'ADMIN_REJECTED'
    , 'PUBLIC'
    );

CREATE TYPE order_status AS ENUM (
    'NOT_SUBMITTED',
    'PROCESSING_SUBMIT',
    'DELIVERY_IN_PROGRESS',
    'DELIVERED',
    'DELIVERY_FAILED',
    'PROCESSING_CANCEL'
    );

CREATE TYPE session_status AS ENUM (
    'ACTIVE',
    'INACTIVE'
    );

-- SCHEMAS
CREATE TABLE if not exists users
(
    id         BIGSERIAL PRIMARY KEY,
    firstName  VARCHAR(255) NOT NULL,
    lastName   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    dob        VARCHAR(255),
    created_at TIMESTAMP,
    role       rvlt_role NOT NULL default 'user'
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
    name     VARCHAR(255) NOT NULL UNIQUE references inventories(name) on update cascade,    --- TODO: remove this field as duplicated in inventories. ALTERNATIVE: might represent color,...
    in_stock INT,
    price    NUMERIC(10, 2)
);

CREATE TABLE if not exists categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    active      BOOLEAN,
    UNIQUE (name)
-- type   VARCHAR: normal/onboarding_sale/xmas_sale/50%-off/black-friday.....
);


-- always only one active session per user (at any given time)
-- create new session each time the previous session is submitted
CREATE TABLE if not exists sessions
(
    id           BIGSERIAL PRIMARY KEY,
    status       session_status default 'ACTIVE',
    total_amount NUMERIC(10, 2) NOT NULL,
    created_at   TIMESTAMP      NOT NULL,
    updated_at   TIMESTAMP,
    user_id      BIGINT         NOT NULL references users (id) ON DELETE CASCADE
);


CREATE TABLE if not exists orders
(
    id           BIGSERIAL PRIMARY KEY references sessions (id) ON DELETE cascade, ---- TODO: change schema to set null
    status       order_status default 'NOT_SUBMITTED',
--     status       VARCHAR(255) NOT NULL, -- NOT_SUBMITTED/PROCESSING_SUBMIT/DELIVERY_IN_PROGRESS/DELIVERED/DELIVERY_FAILED/PROCESSING_CANCEL/RETURN_IN_PROGRESS
    created_at   TIMESTAMP NOT NULL,
    submitted_at TIMESTAMP,
    history      VARCHAR(255)                                                      -- update history
);


CREATE TABLE if not exists wishlists
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE references users (id) ON DELETE CASCADE
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
  - wishlist - user             1 to 1
  - wishlist - products         1 to many

  not necessary
  - order - product:            many to many

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
    product_id  BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);


CREATE TABLE if not exists wishlist_product
(
    wishlist_id BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    PRIMARY KEY (wishlist_id, product_id),
    CONSTRAINT fk_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlists (id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);


CREATE TABLE if not exists product_view
(
    user_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, product_id),
    count      BIGINT NOT NULL,
    history    VARCHAR(255),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
-- format: timestamp@count | timestamp@count |  ...
);

-- ------------------------- BLOGS -------------------------
CREATE TABLE if not exists blogs
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT              NOT NULL references users (id), -- NO cascade (aggregation), orphan
    title             VARCHAR(255)        NOT NULL,
    slug              VARCHAR(255) UNIQUE NOT NULL,
    published_content VARCHAR(1000),                                      -- later refactor to saving in file storage
    draft_content     VARCHAR(1000),                                      -- later refactor to saving in file storage
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP,
    status            blog_status default 'UNPUBLISHED',
    view_count        BIGINT      default 0
);

CREATE TABLE if not exists blog_registry_actions
(
    id          BIGSERIAL PRIMARY KEY,
    action      VARCHAR(10) NOT NULL,
    prev_status blog_status,
    cur_status  blog_status,
    user_id     BIGINT      NOT NULL references users (id)
);

-- users
INSERT INTO users(firstname, lastname, dob, email, created_at, role)
VALUES ('admin', 'admin', '01/01/1999', 'admin@rvlt.com', current_timestamp, 'rvlt_admin');
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John1', 'Doe1', '01/01/2000', 'j1@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John2', 'Doe2', '30/09/2003', 'j2@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John3', 'Doe3', '30/09/2003', 'j3@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John4', 'Doe4', '30/09/2003', 'j4@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John5', 'Doe5', '30/09/2003', 'j5@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John6', 'Doe6', '30/09/2003', 'j6@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John7', 'Do7', '30/09/2003', 'j7@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('Joh8', 'Doe8', '30/09/2003', 'j8@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John9', 'Doe9', '30/09/2003', 'j9@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John10', 'Doe10', '30/09/2003', 'j10@gmail.com', current_timestamp);

-- inventories
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count,
                        returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Gaming chair', 50, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count,
                        returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Laptop Backpack', 100, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count,
                        returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Amazon Echo Dot', 250, 250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count,
                        returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Bioderma Facial cleanser', 1000, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);


-- products
insert into products(id, name, in_stock, price)
select id, name, in_stock_count, 10.00
from inventories;


-- sessions
insert into sessions(status, total_amount, created_at, updated_at, user_id)
select 'ACTIVE', 0.0, current_timestamp, NULL, id
from users;


-- orders
insert into orders(id, created_at, history)
select id, current_timestamp, NULL
from sessions;


-- categories
insert into categories(name, description, active)
values ('Technology', 'Tech gadgets for techies', true);
insert into categories(name, description, active)
values ('Household', 'Household items', true);
insert into categories(name, description, active)
values ('Skincare', 'Skincare products', true);


--- products-categories
insert into products_categories(product_id, category_id)
VALUES (1, 1);
insert into products_categories(product_id, category_id)
VALUES (2, 1);
insert into products_categories(product_id, category_id)
VALUES (3, 2);
insert into products_categories(product_id, category_id)
VALUES (4, 3);
