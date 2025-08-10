create table apk_version
(
    id                bigint primary key,
    apk_major_version int          not null,
    apk_minor_version int          not null,
    apk_patch_version int          not null,
    apk_download_url  varchar(255) not null,
    created_at        datetime(6)  not null,
    updated_at        datetime(6)  not null
);

create table contact
(
    id           bigint primary key,
    store_name   varchar(30)                                not null,
    license      char(12)                                   not null,
    phone_number char(11)                                   not null,
    state        enum ('PENDING', 'PROCESSING', 'COMPLETE') not null,
    created_at   datetime(6)                                not null,
    updated_at   datetime(6)                                not null
);
create index idx_contact_name_license_phone_number on contact (store_name, license, phone_number);
create index idx_contact_name_phone_number on contact (store_name, phone_number);
create index idx_contact_license_phone_number on contact (license, phone_number);
create index idx_contact_phone_number on contact (phone_number);

create table account
(
    id           bigint primary key,
    email        varchar(150)                        not null,
    password     char(60)                            not null,
    phone_number char(11)                            not null,
    state        enum ('INACTIVE','ACTIVE','DELETE') not null,
    permission   enum ('USER','OWNER','ADMIN')       not null,
    last_sign_in datetime(6)                         not null,
    created_at   datetime(6)                         not null,
    updated_at   datetime(6)                         not null
);
create index idx_account_phone_number on account (phone_number);
create index idx_account_email_permission_state on account (email, permission, state);
create index idx_account_email_state on account (email, state);
create index idx_account_permission_state on account (permission, state);
create index idx_account_state on account (state);

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
create index idx_store_registration_account_id_name_status on store_registration (account_id, name, status);
create index idx_store_registration_account_id_status_name on store_registration (account_id, status, name);

create table store_setting
(
    id                     bigint primary key,
    ksnet_device_no        varchar(30)          not null,
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
    constraint fk_store_setting_id foreign key (setting_id) references store_setting (id)
);
create index idx_store_account_id on store (account_id);

create table device
(
    id           bigint primary key,
    store_id     bigint                                   not null,
    name         varchar(20)                              not null,
    purpose      enum ('POS', 'HALL', 'TABLE', 'WAITING') not null,
    table_no     int                                      not null,
    state        enum ('ACTIVE', 'INACTIVE')              not null,
    payment_type enum ('PREPAID', 'POSTPAID')             not null,
    secret_key   varchar(30)                              not null,
    created_at   datetime(6)                              not null,
    updated_at   datetime(6)                              not null,
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
create index idx_menu_store_id_position on menu (store_id, position asc);
create index idx_menu_category_id_store_id on menu (category_id, store_id);

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

create table waiting
(
    id                      bigint primary key,
    store_id                bigint                                      not null,
    phone_number            char(11)                                    not null,
    adult                   int                                         not null,
    infant                  int                                         not null,
    number                  int                                         not null,
    init_waiting_team_count int                                         not null,
    access_key              varchar(30)                                 not null,
    call_count              int                                         not null,
    last_call_time          datetime(6)                                 not null,
    state                   enum ('REGISTRATION', 'CANCEL', 'COMPLETE') not null,
    created_at              datetime(6)                                 not null,
    updated_at              datetime(6)                                 not null
);
create index idx_waiting_store_id_created_at on waiting (store_id, created_at desc);
create index idx_waiting_store_id_state_created_at on waiting (store_id, state, created_at desc);
create index idx_waiting_store_id_access_key on waiting (store_id, access_key);
create index idx_waiting_phone_number_state on waiting (phone_number, state);

create table pos_table
(
    id         bigint primary key,
    store_id   bigint      not null,
    name       varchar(20) not null,
    table_no   int         not null,
    active     boolean     not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null
);
create index idx_pos_table_store_id_active_table_no on pos_table (store_id, active, table_no);

create table pos_table_activity
(
    id           bigint primary key,
    store_id     bigint      not null,
    pos_table_id bigint      not null,
    discount     bigint      not null,
    active       boolean     not null,
    created_at   datetime(6) not null,
    updated_at   datetime(6) not null
);
create index idx_pos_table_activity_store_id_created_at on pos_table_activity (store_id, created_at desc);
create index idx_pos_table_activity_pos_table_id on pos_table_activity (pos_table_id);

create table orders
(
    id                    bigint primary key,
    store_id              bigint                         not null,
    pos_table_activity_id bigint                         not null,
    category              enum ('INITIAL', 'ADDITIONAL') not null,
    type                  enum ('PREPAID', 'POSTPAID')   not null,
    state                 enum ('ORDER', 'CANCEL')       not null,
    price                 bigint                         not null,
    memo                  varchar(30)                    not null,
    served                boolean                        not null,
    served_time           datetime(6)                    not null,
    created_at            datetime(6)                    not null,
    updated_at            datetime(6)                    not null
);
create index idx_orders_store_id_created_at on orders (store_id, created_at desc);
create index idx_orders_pos_table_activity_id on orders (pos_table_activity_id);

create table orders_menu
(
    id            bigint primary key,
    orders_id     bigint      not null,
    name          varchar(30) not null,
    price         bigint      not null,
    quantity      int         not null,
    served        boolean     not null,
    served_time   datetime(6) not null,
    print_enabled boolean     not null,
    constraint fk_orders_menu_orders_id foreign key (orders_id) references orders (id) on delete cascade
);

create table orders_option_group
(
    id             bigint primary key,
    orders_menu_id bigint      not null,
    name           varchar(30) not null,
    print_enabled  boolean     not null,
    constraint fk_orders_option_group_orders_menu_id foreign key (orders_menu_id) references orders_menu (id) on delete cascade
);

create table orders_option
(
    orders_option_group_id bigint      not null,
    name                   varchar(30) not null,
    price                  bigint      not null,
    position               int         not null,
    constraint pk_orders_option primary key (orders_option_group_id, name, price, position),
    constraint fk_orders_option_orders_option_group_id foreign key (orders_option_group_id) references orders_option_group (id) on delete cascade
);

create table orders_payment
(
    id                    bigint primary key,
    store_id              bigint                              not null,
    pos_table_activity_id bigint                              not null,
    method                enum ('CASH', 'CARD')               not null,
    state                 enum ('APPROVE', 'CANCEL')          not null,
    amount                bigint                              not null,
    cancellable           boolean                             not null,
    approval_no           varchar(30)                         not null,
    installment           varchar(10)                         not null,
    card_no               varchar(30)                         not null,
    issuer_name           varchar(30)                         not null,
    purchase_name         varchar(30)                         not null,
    merchant_no           varchar(30)                         not null,
    trade_time            varchar(20)                         not null,
    trade_unique_no       varchar(30)                         not null,
    vat                   bigint                              not null,
    supply_amount         bigint                              not null,
    cash_receipt_no       varchar(30)                         not null,
    cash_receipt_type     enum ('NONE', 'DEDUCTION', 'PROOF') not null,
    created_at            datetime(6)                         not null,
    updated_at            datetime(6)                         not null
);
create index idx_orders_payment_store_id_created_at on orders_payment (store_id, created_at desc);
create index idx_orders_payment_pos_table_activity_id on orders_payment (pos_table_activity_id);

create table staff_call
(
    id            bigint primary key,
    store_id      bigint                          not null,
    table_no      int                             not null,
    name          varchar(20)                     not null,
    state         enum ('INCOMPLETE' ,'COMPLETE') not null,
    complete_time datetime(6)                     not null,
    created_at    datetime(6)                     not null,
    updated_at    datetime(6)                     not null
);
create index idx_staff_call_store_id_state_created_at on staff_call (store_id, state, created_at desc);
