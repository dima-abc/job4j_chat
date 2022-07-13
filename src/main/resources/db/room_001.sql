/*схема таблицы комнаты чата*/
create table rooms
(
    id       serial primary key,
    room_name     varchar(255) not null,
    admin_id int          not null references persons (id)
);

insert into rooms(room_name, admin_id)
values ('job4j.ru', (select distinct p.id from persons as p where p.login = 'user2'));
insert into rooms(room_name, admin_id)
values ('job4j.1 Trainee', (select distinct p.id from persons as p where p.login = 'user3'));
insert into rooms(room_name, admin_id)
values ('job4j.2 Junior', (select distinct p.id from persons as p where p.login = 'user4'));
insert into rooms(room_name, admin_id)
values ('job4j.3 Midl', (select distinct p.id from persons as p where p.login = 'root'));