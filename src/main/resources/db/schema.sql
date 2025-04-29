create table account
(
    id           bigint primary key,
    email        varchar(50)                         not null unique,
    password     char(60)                            not null,
    phone_number char(11)                            not null unique,
    state        enum ('INACTIVE','ACTIVE','DELETE') not null,
    permission   enum ('USER','OWNER','ADMIN')       not null,
    last_sign_in datetime(6)                         not null,
    created_at   datetime(6)                         not null,
    updated_at   datetime(6)                         not null
);

create table refresh_token
(
    id               bigint primary key,
    account_id       bigint      not null,
    current_token_id bigint      not null,
    created_at       datetime(6) not null,
    updated_at       datetime(6) not null
);

create table store_license
(
    id       bigint primary key,
    name     varchar(30) not null,
    ceo_name varchar(20) not null,
    address  varchar(50) not null,
    landline char(12)    not null,
    license  char(12)    not null,
    image    char(33)    not null
);

create table store_registration
(
    id            bigint primary key,
    account_id    bigint                                      not null,
    license_id    bigint                                      not null unique,
    status        enum ('APPLY','REAPPLY','APPROVE','REJECT') not null,
    reject_reason varchar(30)                                 not null,
    created_at    datetime(6)                                 not null,
    updated_at    datetime(6)                                 not null,
    constraint fk_registration_license_id foreign key (license_id) references store_license (id)
);
