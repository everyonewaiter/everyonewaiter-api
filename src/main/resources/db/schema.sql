drop table if exists account;
create table account
(
    account_id   bigint primary key,
    email        varchar(50)                         not null unique,
    password     varchar(255)                        not null,
    phone_number varchar(20)                         not null unique,
    permission   enum ('USER','OWNER','ADMIN')       not null,
    state        enum ('INACTIVE','ACTIVE','DELETE') not null,
    last_sign_in datetime(6)                         not null,
    created_at   datetime(6)                         not null,
    updated_at   datetime(6)                         not null
);

drop table if exists image;
create table image
(
    image_id              bigint primary key,
    account_id            bigint                              not null,
    name                  varchar(100)                        not null,
    original_name         varchar(255)                        not null,
    state                 enum ('INACTIVE','ACTIVE','DELETE') not null,
    access_url            varchar(500)                        not null,
    access_url_expiration date                                not null,
    created_at            datetime(6)                         not null,
    updated_at            datetime(6)                         not null
);

drop table if exists store_registration;
create table store_registration
(
    registration_id bigint primary key,
    account_id      bigint                                      not null,
    image_id        bigint                                      not null,
    name            varchar(30)                                 not null,
    ceo_name        varchar(20)                                 not null,
    address         varchar(100)                                not null,
    landline        varchar(20)                                 not null,
    license         varchar(20)                                 not null,
    status          enum ('APPLY','REAPPLY','APPROVE','REJECT') not null,
    reason          varchar(255)                                not null,
    created_at      datetime(6)                                 not null,
    updated_at      datetime(6)                                 not null
);
