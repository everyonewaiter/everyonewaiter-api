drop table if exists account;
create table account
(
    account_id   bigint primary key,
    email        varchar(50)                         not null unique,
    password     varchar(255)                        not null,
    phone_number varchar(20)                         not null unique,
    permission   enum ('USER','OWNER','ADMIN')       not null,
    status       enum ('INACTIVE','ACTIVE','DELETE') not null,
    last_sign_in datetime(6)                         not null,
    created_at   datetime(6)                         not null,
    updated_at   datetime(6)                         not null
);
