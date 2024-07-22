-- users
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John1', 'Doe1', '01/01/2000', 'j1@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John2', 'Doe2', '30/09/2003', 'j2@gmail.com', current_timestamp);
INSERT INTO users(firstname, lastname, dob, email, created_at)
VALUES ('John3', 'Doe3', '30/09/2003', 'j3@gmail.com', current_timestamp);

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
