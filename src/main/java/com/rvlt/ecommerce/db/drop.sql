with ids as
         (select wl.id from users us inner join wishlists wl on us.id = wl.user_id where us.id = 2)
insert into wishlists_products(wishlist_id, product_id)
select ids.id, 2
from ids ON CONFLICT (wishlist_id, product_id) DO NOTHING;
-- insert into wishlist_products(wishlist_id, product_id)
-- select wl.id wishlist_id from wishlists wl left join users us on wl.user_id = us.id;
