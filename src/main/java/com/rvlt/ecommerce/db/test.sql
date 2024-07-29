-- users
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
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count, returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Gaming chair', 50, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count, returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Laptop Backpack', 100, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count, returned_count,
                        return_in_progress_count, delivery_failed, return_failed, cancel_failed,
                        in_session_holding, balance)
values ('Amazon Echo Dot', 250, 250, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_submit_count, delivery_in_progress_count,
                        delivered_count, processing_cancel_count, cancelled_count, cancel_in_progress_count, returned_count,
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
insert into orders(id, status, created_at, history)
select id, 'NOT_SUBMITTED', current_timestamp, NULL
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
