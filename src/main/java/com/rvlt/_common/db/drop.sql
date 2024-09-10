-- select * from product_view where user_id = 1 order by count  desc;

--select p.* from product_view pv left join products p on pv.product_id = p.id where pv.user_id = 1 order by pv.count desc limit 10;

with tmp as
    (select s.id from sessions s inner join users u on s.user_id = u.id where s.status = 'INACTIVE' and u.id = 1)
select o.* from tmp t inner join orders o on t.id = o.id where o.id = 1;
