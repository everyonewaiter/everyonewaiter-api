create table account
(
    id           bigint primary key,
    email        varchar(50)                         not null,
    password     char(60)                            not null,
    phone_number char(11)                            not null,
    state        enum ('INACTIVE','ACTIVE','DELETE') not null,
    permission   enum ('USER','OWNER','ADMIN')       not null,
    last_sign_in datetime(6)                         not null,
    created_at   datetime(6)                         not null,
    updated_at   datetime(6)                         not null,
    constraint uk_account_email unique (email)
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
    landline      char(13)                                    not null,
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
    landline       char(13)               not null,
    license        char(12)               not null,
    license_image  char(33)               not null,
    status         enum ('OPEN', 'CLOSE') not null,
    last_opened_at datetime(6)            not null,
    last_closed_at datetime(6)            not null,
    created_at     datetime(6)            not null,
    updated_at     datetime(6)            not null,
    constraint uk_store_setting_id unique (setting_id),
    constraint fk_store_setting_id foreign key (setting_id) references store_setting (id)
);

create table device
(
    id              bigint primary key,
    store_id        bigint                                   not null,
    name            varchar(20)                              not null,
    purpose         enum ('POS', 'HALL', 'TABLE', 'WAITING') not null,
    table_no        int                                      not null,
    ksnet_device_no varchar(30)                              not null,
    state           enum ('ACTIVE', 'INACTIVE')              not null,
    payment_type    enum ('PREPAID', 'POSTPAID')             not null,
    secret_key      varchar(30)                              not null,
    created_at      datetime(6)                              not null,
    updated_at      datetime(6)                              not null,
    constraint uk_device_store_id_name unique (store_id, name)
);

create table category
(
    id         bigint primary key,
    store_id   bigint      not null,
    name       varchar(20) not null,
    position   int         not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint uk_category_store_id_name unique (store_id, name)
);

create table menu
(
    id            bigint primary key,
    store_id      bigint                                       not null,
    category_id   bigint                                       not null,
    name          varchar(30)                                  not null,
    description   varchar(100)                                 not null,
    price         bigint                                       not null,
    spicy         int                                          not null,
    state         enum ('DEFAULT', 'HIDE', 'SOLD_OUT')         not null,
    label         enum ('DEFAULT', 'NEW', 'BEST', 'RECOMMEND') not null,
    image         char(30)                                     not null,
    print_enabled boolean                                      not null,
    position      int                                          not null,
    created_at    datetime(6)                                  not null,
    updated_at    datetime(6)                                  not null
);

create table menu_option_group
(
    id            bigint primary key,
    menu_id       bigint                         not null,
    name          varchar(30)                    not null,
    type          enum ('MANDATORY', 'OPTIONAL') not null,
    print_enabled boolean                        not null,
    position      int                            not null,
    constraint fk_menu_option_group_menu_id foreign key (menu_id) references menu (id) on delete cascade
);

create table menu_option
(
    menu_option_group_id bigint      not null,
    name                 varchar(30) not null,
    price                bigint      not null,
    position             int         not null,
    constraint pk_menu_option primary key (menu_option_group_id, name, price, position),
    constraint fk_menu_option_menu_option_group_id foreign key (menu_option_group_id) references menu_option_group (id) on delete cascade
);
