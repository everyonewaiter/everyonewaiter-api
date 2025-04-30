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

create table store_registration
(
    id            bigint primary key,
    account_id    bigint                                      not null,
    name          varchar(30)                                 not null,
    ceo_name      varchar(20)                                 not null,
    address       varchar(50)                                 not null,
    landline      char(12)                                    not null,
    license       char(12)                                    not null,
    license_image char(33)                                    not null,
    status        enum ('APPLY','REAPPLY','APPROVE','REJECT') not null,
    reject_reason varchar(30)                                 not null,
    created_at    datetime(6)                                 not null,
    updated_at    datetime(6)                                 not null
);

create table store_setting
(
    id                     bigint primary key,
    extra_table_count      int                  not null,
    printer_location       enum ('POS', 'HALL') not null,
    show_menu_popup        boolean              not null,
    show_order_total_price boolean              not null,
    country_of_origins     varchar(500)         not null,
    staff_call_options     varchar(255)         not null
);

create table store
(
    id             bigint primary key,
    account_id     bigint                 not null,
    setting_id     bigint                 not null,
    name           varchar(30)            not null,
    ceo_name       varchar(20)            not null,
    address        varchar(50)            not null,
    landline       char(12)               not null,
    license        char(12)               not null,
    license_image  char(33)               not null,
    status         enum ('OPEN', 'CLOSE') not null,
    last_opened_at datetime(6)            not null,
    last_closed_at datetime(6)            not null,
    created_at     datetime(6)            not null,
    updated_at     datetime(6)            not null,
    constraint fk_store_setting_id foreign key (setting_id) references store_setting (id)
);
