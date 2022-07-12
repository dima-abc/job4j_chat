/*схема таблицы ролей пользователей*/
create table roles
(
    id serial primary key,
    name varchar(100) not null,
    unique (name)
);
