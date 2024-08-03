-- select * from product_view where user_id = 1 order by count  desc;

select p.* from product_view pv left join products p on pv.product_id = p.id where pv.user_id = 1 order by pv.count desc limit 10;
