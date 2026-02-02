alter table orders_payment
    add column type enum ('PENDING_ORDER', 'ORDERED_ORDER') not null default 'ORDERED_ORDER';

alter table pos_table_activity
    add column pending_discount bigint not null default 0;
