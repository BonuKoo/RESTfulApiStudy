insert into users(id, join_date, name, password, ssn) values (900001, now(), 'User1', 'test111','707070-1111111');
insert into users(id, join_date, name, password, ssn) values (900002, now(), 'User2', 'test222','801212-1111111');
insert into users(id, join_date, name, password, ssn) values (900003, now(), 'User3', 'test333','901111-1111111');

insert into POST(description, user_id) values ('My first Post', 900001);
insert into POST(description, user_id) values ('My Second Post', 900001);