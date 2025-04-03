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

drop table if exists store_registration;
create table store_registration
(
    registration_id BIGINT PRIMARY KEY,
    account_id      BIGINT                                         NOT NULL,
    image_id        BIGINT                                         NOT NULL,
    name            VARCHAR(30)                                    NOT NULL,
    ceo_name        VARCHAR(20)                                    NOT NULL,
    address         VARCHAR(100)                                   NOT NULL,
    landline        VARCHAR(20)                                    NOT NULL,
    license         VARCHAR(20)                                    NOT NULL,
    status          ENUM ('APPLY', 'REAPPLY', 'APPROVE', 'REJECT') NOT NULL,
    reason          VARCHAR(255)                                   NOT NULL,
    created_at      DATETIME(6)                                    NOT NULL,
    updated_at      DATETIME(6)                                    NOT NULL
) ENGINE = InnoDB;
