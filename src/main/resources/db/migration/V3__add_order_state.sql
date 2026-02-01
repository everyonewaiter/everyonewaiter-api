alter table orders
    modify column state enum ('PENDING', 'REJECT', 'ORDER', 'CANCEL') not null;
