/*схема таблицы пользователя в чате*/
create table persons
(
    id serial primary key,
    login varchar(100) not null,
    password varchar(255) not null,
    role_id int not null references roles(id),
    unique (login)
);

insert into persons(login, password, role_id) values ('root', '123', (select distinct id from roles where role_name = 'ROLE_ADMIN'));
insert into persons(login, password, role_id) values ('user', '1', (select distinct id from roles where role_name = 'ROLE_USER'));