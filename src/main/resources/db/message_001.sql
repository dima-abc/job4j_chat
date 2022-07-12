/*схема таблицы сообщения в чате*/
create table messages
(
    id serial primary ket,
    name text,
    person_id int not null references persons(id),
    room_id int not null references rooms(id)
);
