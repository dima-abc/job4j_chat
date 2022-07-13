/*схема таблицы сообщения в чате*/
create table messages
(
    id serial primary key,
    message_text text,
    person_id int not null references persons(id),
    room_id int not null references rooms(id)
);

insert into messages(message_text, person_id, room_id)
values ('Привет Job4j.ru маня зовут User2',
       (select distinct pe.id from persons as pe where pe.login = 'user2'),
       (select distinct r.id from rooms as r where r.room_name = 'job4j.ru'));
insert into messages(message_text, person_id, room_id)
values ('Привет job4j.1 Trainee, маня зовут User3',
       (select distinct pe.id from persons as pe where pe.login = 'user3'),
       (select distinct r.id from rooms as r where r.room_name = 'job4j.1 Trainee'));
insert into messages(message_text, person_id, room_id)
values ('Привет job4j.2 Junior, маня зовут User4',
       (select distinct pe.id from persons as pe where pe.login = 'user4'),
       (select distinct r.id from rooms as r where r.room_name = 'job4j.2 Junior'));
insert into messages(message_text, person_id, room_id)
values ('Привет job4j.3 Midl, маня зовут Root',
       (select distinct pe.id from persons as pe where pe.login = 'root'),
       (select distinct r.id from rooms as r where r.room_name = 'job4j.3 Midl'));