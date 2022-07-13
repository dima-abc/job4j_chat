/*схема таблицы сообщения в чате*/
create table messages
(
    id serial primary key,
    message_text text,
    person_id int not null references persons(id),
    room_id int not null references rooms(id)
);
