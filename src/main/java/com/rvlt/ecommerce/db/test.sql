-- users
INSERT INTO users(firstname, lastname, dob, created_at)
VALUES ('John1', 'Doe1', '01/01/2000', current_timestamp);
INSERT INTO users(firstname, lastname, dob, created_at)
VALUES ('John2', 'Doe2', '30/09/2003', current_timestamp);
INSERT INTO users(firstname, lastname, dob, created_at)
VALUES ('John3', 'Doe3', '30/09/2003', current_timestamp);

-- inventories
insert into inventories(name, total_count, in_stock_count, processing_count, delivered_count, in_session_holding,
                        balance)
values ('Gaming chair', 50, 50, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_count, delivered_count, in_session_holding,
                        balance)
values ('Laptop Backpack', 100, 100, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_count, delivered_count, in_session_holding,
                        balance)
values ('Amazon Echo Dot', 250, 250, 0, 0, 0, 0);
insert into inventories(name, total_count, in_stock_count, processing_count, delivered_count, in_session_holding,
                        balance)
values ('Bioderma Facial cleanser', 1000, 1000, 0, 0, 0, 0);


-- products
insert into products(id, name, in_stock, price)
select id, name, in_stock_count, 10
from inventories;


-- sessions
insert into sessions(status, created_at, updated_at, user_id)
select 'IN_PROGRESS', current_timestamp, NULL, id
from users;


-- orders
