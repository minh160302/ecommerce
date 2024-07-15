-- Users with diverse backgrounds
INSERT INTO users(firstname, lastname, dob, email, created_at) VALUES
                                                                   ('Alice', 'Johnson', '15/03/1985', 'alice.j@email.com', current_timestamp - interval '2 years'),
                                                                   ('Mohammed', 'Al-Fayed', '22/09/1990', 'mo.fayed@email.com', current_timestamp - interval '1 year'),
                                                                   ('Yuki', 'Tanaka', '07/12/1988', 'yuki.t@email.com', current_timestamp - interval '6 months'),
                                                                   ('Maria', 'Garcia', '30/06/1995', 'maria.g@email.com', current_timestamp - interval '3 months'),
                                                                   ('Olga', 'Petrova', '18/11/1992', 'olga.p@email.com', current_timestamp - interval '1 month');

-- Inventories with various products and quantities
INSERT INTO inventories(name, total_count, in_stock_count, processing_count, delivered_count, in_session_holding, balance) VALUES
                                                                                                                               ('High-End Gaming PC', 20, 15, 3, 2, 0, 0),
                                                                                                                               ('Wireless Earbuds', 500, 450, 30, 20, 0, 0),
                                                                                                                               ('Smart Home Hub', 100, 80, 15, 5, 0, 0),
                                                                                                                               ('4K Ultra HD TV', 50, 40, 8, 2, 0, 0),
                                                                                                                               ('Ergonomic Office Chair', 75, 60, 10, 5, 0, 0),
                                                                                                                               ('Smartphone', 200, 150, 40, 10, 0, 0),
                                                                                                                               ('Digital Camera', 30, 25, 3, 2, 0, 0),
                                                                                                                               ('Robot Vacuum Cleaner', 60, 50, 7, 3, 0, 0);

-- Products with varying prices
INSERT INTO products(id, name, in_stock, price)
SELECT id, name, in_stock_count,
       CASE
           WHEN name = 'High-End Gaming PC' THEN 1599.99
           WHEN name = 'Wireless Earbuds' THEN 129.99
           WHEN name = 'Smart Home Hub' THEN 89.99
           WHEN name = '4K Ultra HD TV' THEN 799.99
           WHEN name = 'Ergonomic Office Chair' THEN 249.99
           WHEN name = 'Smartphone' THEN 699.99
           WHEN name = 'Digital Camera' THEN 449.99
           WHEN name = 'Robot Vacuum Cleaner' THEN 299.99
           ELSE 99.99  -- default price
           END
FROM inventories;

-- Sessions with different statuses and amounts
INSERT INTO sessions(status, total_amount, created_at, updated_at, user_id) VALUES
                                                                                ('ACTIVE', 1599.99, current_timestamp - interval '2 days', current_timestamp - interval '1 day', 1),
                                                                                ('INACTIVE', 219.98, current_timestamp - interval '5 days', current_timestamp - interval '4 days', 2),
                                                                                ('ACTIVE', 899.97, current_timestamp - interval '1 day', current_timestamp, 3),
                                                                                ('INACTIVE', 0, current_timestamp - interval '10 days', NULL, 4),
                                                                                ('ACTIVE', 1049.98, current_timestamp - interval '3 hours', current_timestamp - interval '1 hour', 5);

-- Orders with various statuses
INSERT INTO orders(id, status, created_at, submitted_at, history) VALUES
                                                                      (1, 'PROCESSING', current_timestamp - interval '2 days', current_timestamp - interval '1 day', 'Order received, payment confirmed'),
                                                                      (2, 'DELIVERED', current_timestamp - interval '5 days', current_timestamp - interval '4 days', 'Order received, processed, shipped, delivered'),
                                                                      (3, 'IN_PROGRESS', current_timestamp - interval '1 day', current_timestamp, 'Order received, processing'),
                                                                      (4, 'NOT_SUBMITTED', current_timestamp - interval '10 days', NULL, NULL),
                                                                      (5, 'CANCELLED', current_timestamp - interval '3 hours', current_timestamp - interval '1 hour', 'Order received, cancelled by customer');

-- Sessions-Products relationships
INSERT INTO sessions_products(session_id, product_id, count) VALUES
                                                                 (1, 1, 1),  -- High-End Gaming PC in session 1
                                                                 (2, 2, 1),  -- Wireless Earbuds in session 2
                                                                 (2, 3, 1),  -- Smart Home Hub in session 2
                                                                 (3, 4, 1),  -- 4K Ultra HD TV in session 3
                                                                 (3, 5, 1),  -- Ergonomic Office Chair in session 3
                                                                 (5, 6, 1),  -- Smartphone in session 5
                                                                 (5, 7, 1);  -- Digital Camera in session 5