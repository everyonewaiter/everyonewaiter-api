create table account
(
    id           bigint primary key,
    email        char(30)                            not null unique,
    password     char(60)                            not null,
    phone_number char(11)                            not null unique,
    state        enum ('INACTIVE','ACTIVE','DELETE') not null,
    permission   enum ('USER','OWNER','ADMIN')       not null,
    last_sign_in datetime(6)                         not null,
    created_at   datetime(6)                         not null,
    updated_at   datetime(6)                         not null
);
