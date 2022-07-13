/*схема таблицы ролей пользователей*/
create table roles
(
    id   serial primary key,
    role_name varchar(100) not null,
    unique (role_name)
);

insert into roles(role_name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');
