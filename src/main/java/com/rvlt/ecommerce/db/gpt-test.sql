-- Step 1: Update some existing orders to 'SUBMITTED' status
UPDATE orders
SET status = 'PROCESSING_SUBMIT', submitted_at = current_timestamp
WHERE id IN (1, 2, 3, 4, 5);

-- Step 2: Update corresponding sessions to 'INACTIVE'
UPDATE sessions
SET status = 'INACTIVE', updated_at = current_timestamp
WHERE id IN (1, 2, 3, 4, 5);

-- Step 3: Add products to the submitted orders (sessions 1-5)
INSERT INTO sessions_products (session_id, product_id, count)
VALUES
    (1, 1, 2),  -- 2 Gaming chairs
    (1, 3, 1),  -- 1 Amazon Echo Dot
    (2, 2, 1),  -- 1 Laptop Backpack
    (2, 4, 3),  -- 3 Bioderma Facial cleansers
    (3, 3, 2),  -- 2 Amazon Echo Dots
    (4, 1, 1),  -- 1 Gaming chair
    (4, 2, 1),  -- 1 Laptop Backpack
    (5, 4, 5);  -- 5 Bioderma Facial cleansers

-- Step 4: Update the total_amount in the submitted sessions
UPDATE sessions s
SET total_amount = (
    SELECT COALESCE(SUM(sp.count * p.price), 0)
    FROM sessions_products sp
             JOIN products p ON sp.product_id = p.id
    WHERE sp.session_id = s.id
)
WHERE s.id IN (1, 2, 3, 4, 5);

-- Step 5: Create new active sessions for users who just had their orders submitted
INSERT INTO sessions (status, total_amount, created_at, updated_at, user_id)
SELECT 'ACTIVE', 0.0, current_timestamp, NULL, user_id
FROM sessions
WHERE id IN (1, 2, 3, 4, 5);

-- Step 6: Create new 'NOT_SUBMITTED' orders for the new active sessions
INSERT INTO orders (id, status, created_at, history)
SELECT id, 'NOT_SUBMITTED', current_timestamp, NULL
FROM sessions
WHERE status = 'ACTIVE' AND user_id IN (1, 2, 3, 4, 5);

-- Step 7: Add products to the new active sessions
WITH new_active_sessions AS (
    SELECT id
    FROM sessions
    WHERE status = 'ACTIVE' AND user_id IN (1, 2, 3, 4, 5)
)
INSERT INTO sessions_products (session_id, product_id, count)
SELECT id,
       CASE
           WHEN id % 4 = 0 THEN 1  -- Gaming chair
           WHEN id % 4 = 1 THEN 2  -- Laptop Backpack
           WHEN id % 4 = 2 THEN 3  -- Amazon Echo Dot
           ELSE 4  -- Bioderma Facial cleanser
           END,
       1
FROM new_active_sessions;

-- Step 8: Update the total_amount in the new active sessions
UPDATE sessions s
SET total_amount = (
    SELECT COALESCE(SUM(sp.count * p.price), 0)
    FROM sessions_products sp
             JOIN products p ON sp.product_id = p.id
    WHERE sp.session_id = s.id
)
WHERE status = 'ACTIVE' AND user_id IN (1, 2, 3, 4, 5);

-- Step 9: Create additional submitted order for user 1
INSERT INTO sessions (status, total_amount, created_at, updated_at, user_id)
VALUES ('INACTIVE', 0, current_timestamp - interval '2 days', current_timestamp, 1);

INSERT INTO orders (id, status, created_at, submitted_at, history)
SELECT id, 'DELIVERY_IN_PROGRESS', created_at, current_timestamp - interval '1 day', 'Order placed'
FROM sessions
WHERE user_id = 1 ORDER BY id DESC LIMIT 1;

-- Step 10: Add products to the additional order for user 1
WITH new_session AS (
    SELECT id
    FROM sessions
    WHERE user_id = 1
    ORDER BY id DESC
    LIMIT 1
)
INSERT INTO sessions_products (session_id, product_id, count)
SELECT id, 1, 2  -- 2 Gaming chairs
FROM new_session;

-- Step 11: Create additional submitted order for user 2
INSERT INTO sessions (status, total_amount, created_at, updated_at, user_id)
VALUES ('INACTIVE', 0, current_timestamp - interval '3 days', current_timestamp, 2);

INSERT INTO orders (id, status, created_at, submitted_at, history)
SELECT id, 'DELIVERED', created_at, current_timestamp - interval '2 days', 'Order placed|Order delivered'
FROM sessions
WHERE user_id = 2 ORDER BY id DESC LIMIT 1;

-- Step 12: Add products to the additional order for user 2
WITH new_session AS (
    SELECT id
    FROM sessions
    WHERE user_id = 2
    ORDER BY id DESC
    LIMIT 1
)
INSERT INTO sessions_products (session_id, product_id, count)
SELECT id, 3, 2  -- 2 Amazon Echo Dots
FROM new_session;

-- Step 13: Update the total_amount for the additional orders
UPDATE sessions s
SET total_amount = (
    SELECT COALESCE(SUM(sp.count * p.price), 0)
    FROM sessions_products sp
             JOIN products p ON sp.product_id = p.id
    WHERE sp.session_id = s.id
)
WHERE user_id IN (1, 2) AND status = 'INACTIVE';