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
create index idx_account_id_email_state_permission on account (id desc, email asc, state asc, permission asc);

create table refresh_token
(
    id               bigint primary key,
    account_id       bigint      not null,
    current_token_id bigint      not null,
    created_at       datetime(6) not null,
    updated_at       datetime(6) not null
);
create index idx_refresh_token_updated_at on refresh_token (updated_at asc);
create index idx_refresh_token_account_id_current_token_id on refresh_token (account_id asc, current_token_id asc);

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
create index idx_registration_account_id_id on store_registration (account_id asc, id desc);
