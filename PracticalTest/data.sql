create table beneficiary
(
    id         bigint auto_increment primary key,
    firstname  varchar(32) not null,
    lastname   varchar(32) not null,
    patronymic varchar(32),
    pin        varchar(44) not null
);

create table account
(
    id             bigint auto_increment primary key,
    balance        numeric(20, 2) not null,
    beneficiary_id bigint         not null,
    FOREIGN KEY (beneficiary_id) references beneficiary (id)
);

create table transaction
(
    id             bigint auto_increment primary key,
    balance_before numeric(20, 2)           not null,
    date           timestamp with time zone not null,
    operation_type varchar(2)               not null,
    account_id     bigint                   not null,
    foreign key (account_id) references account (id)
);
insert into beneficiary
values (1,'ilya', 'romanov', 'olegovich', '1488');
insert into account
values (1,20000, 1);
insert into transaction
values (1,20000, '2004-10-19 10:23:54+02', 1, 1);



