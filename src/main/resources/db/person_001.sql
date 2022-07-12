/*схема таблицы пользователя в чате*/
create table persons
(
    id serial primary key,
    login varchar(100) not null,
    password varchar(255) not null,
    role_id int not null references roles(id),
    unique (login)
);