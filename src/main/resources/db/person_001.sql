/*схема таблицы пользователя в чате*/
create table persons
(
    id       serial primary key,
    login    varchar(100) not null,
    password varchar(255) not null,
    role_id  int          not null references roles (id),
    unique (login)
);
/*root pass 111111*/
insert into persons(login, password, role_id)
values ('root',
        '$2a$10$yJmg5mnv2Z.k229.bWqzxOSN7kjTvve820n5IQULg1pRvgnPPWRny',
        (select distinct id from roles where role_name = 'ROLE_ADMIN'));
/*user pass 000000*/
insert into persons(login, password, role_id)
values ('user2',
        '$2a$10$NjjsU2wQXsttq/mvf8GBLeH24cLTkYCxWNeWMzJ0WhXubfTtSUCHm',
        (select distinct id from roles where role_name = 'ROLE_USER'));
insert into persons(login, password, role_id)
values ('user3',
        '$2a$10$Tq4qZnqaD0J41iTSpVYOxOcndkVGZdZw2Twh8b55vrBq1fp5DP1f6',
        (select distinct id from roles where role_name = 'ROLE_USER'));
insert into persons(login, password, role_id)
values ('user4',
        '$2a$10$.NAmbTRrP/W2JnDRFUDMcO5gtrVuPlKHAy6YXB7tT2NhXHJTR5um6',
        (select distinct id from roles where role_name = 'ROLE_USER'));