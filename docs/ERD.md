# ERD (Entity Relationship Diagram)

## 목차

- [헬스 체크](#헬스-체크)
- [계정](#계정)
- [매장 등록](#매장-등록)
- [매장](#매장)
- [메뉴](#메뉴)
- [기기](#기기)
- [웨이팅](#웨이팅)
- [주문](#주문)
- [주문 결제](#주문-결제)
- [POS](#POS)

---

## 헬스 체크

_apk_version_

```mermaid
erDiagram
    apk_version {
        bigint id PK
        int apk_major_version
        int apk_minor_version
        int apk_patch_version
        varchar(255) apk_download_uri
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---

## 계정

_account_

```mermaid
erDiagram
    account {
        bigint id PK
        varchar(150) email
        char(60) password
        char(11) phone_number
        enum state "INACTIVE, ACTIVE, DELETE"
        enum permission "USER, OWNER, ADMIN"
        datetime(6) last_sign_in
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---

## 매장 등록

_store_registration_

```mermaid
erDiagram
    account ||--o{ store_registration: ""

    store_registration {
        bigint id PK
        bigint account_id FK
        varchar(30) name
        varchar(20) ceo_name
        varchar(50) address
        char(13) landline
        char(12) license
        char(33) license_image
        enum status "APPLY, REAPPLY, APPROVE, REJECT"
        varchar(30) reject_reason
        datetime(6) last_sign_in
        datetime(6) created_at
        datetime(6) updated_at
    }
```

## 매장

_store_

_store_setting_

```mermaid
erDiagram
    account ||--o{ store: ""
    store ||--|| store_setting: own

    store {
        bigint id PK
        bigint account_id FK
        bigint setting_id FK
        varchar(30) name
        varchar(20) ceo_name
        varchar(50) address
        char(13) landline
        char(12) license
        char(33) license_image
        enum status "OPEN, CLOSE"
        datetime(6) last_opened_at
        datetime(6) last_closed_at
        datetime(6) created_at
        datetime(6) updated_at
    }

    store_setting {
        bigint id PK
        varchar(30) ksnet_device_no
        int extra_table_count
        enum printer_location "POS, HALL"
        boolean show_menu_popup
        boolean show_order_total_price
        boolean show_order_menu_image
        varchar(255) country_of_origins
        varchar(255) staff_call_options
    }
```

---

## 메뉴

_category_

_menu_

_menu_option_group_

_menu_option_

```mermaid
erDiagram
    store ||--o{ category: ""
    store ||--o{ menu: ""
    category ||--o{ menu: has
    menu ||--o{ menu_option_group: has
    menu_option_group ||--o{ menu_option: has

    category {
        bigint id PK
        bigint store_id FK
        varchar(20) name
        int position
        datetime(6) created_at
        datetime(6) updated_at
    }

    menu {
        bigint id PK
        bigint store_id FK
        bigint category_id FK
        varchar(30) name
        varchar(100) description
        bigint price
        int spicy
        enum state "DEFAULT, HIDE, SOLD_OUT"
        enum label "DEFAULT, NEW, BEST, RECOMMEND"
        char(30) image
        boolean print_enabled
        int position
        datetime(6) created_at
        datetime(6) updated_at
    }

    menu_option_group {
        bigint id PK
        bigint menu_id FK
        varchar(30) name
        enum type "MANDATORY, OPTIONAL"
        boolean print_enabled
        int position
    }

    menu_option {
        bigint menu_option_group_id PK
        varchar(30) name PK
        bigint price PK
        int position PK
    }
```

---

## 기기

_device_

```mermaid
erDiagram
    store ||--o{ device: ""

    device {
        bigint id PK
        bigint store_id FK
        varchar(20) name
        enum purpose "POS, HALL, TABLE, WAITING"
        int table_no
        enum state "ACTIVE, INACTIVE"
        enum payment_type "PREPAID, POSTPAID"
        varchar(30) secret_key
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---

## 웨이팅

_waiting_

```mermaid
erDiagram
    store ||--o{ waiting: ""

    waiting {
        bigint id PK
        bigint store_id FK
        char(11) phone_number
        int adult
        int infant
        int number
        int init_waiting_team_count
        varchar(30) access_key
        int call_count
        datetime(6) last_call_time
        enum state "REGISTRATION, CANCEL, COMPLETE"
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---

## 주문

_orders_

_orders_menu_

_orders_option_group_

_orders_option_

_staff_call_

```mermaid
erDiagram
    store ||--o{ orders: ""
    store ||--o{ staff_call: ""
    orders ||--o{ orders_menu: "has"
    orders o{--|| pos_table_activity: ""
    orders_menu ||--o{ orders_option_group: "has"
    orders_option_group ||--o{ orders_option: "has"

    orders {
        bigint id PK
        bigint store_id FK
        bigint pos_table_activity_id FK
        enum category "INITIAL, ADDITIONAL"
        enum type "PREPAID, POSTPAID"
        enum state "ORDER, CANCEL"
        bigint price
        varchar(10) memo
        boolean served
        datetime(6) served_time
        datetime(6) created_at
        datetime(6) updated_at
    }

    orders_menu {
        bigint id PK
        bigint orders_id FK
        varchar(30) name
        bigint price
        int quantity
        char(30) image
        boolean served
        datetime(6) served_time
        boolean print_enabled
    }

    orders_option_group {
        bigint id PK
        bigint orders_menu_id FK
        varchar(30) name
        boolean print_enabled
    }

    orders_option {
        bigint orders_option_group_id PK
        varchar(30) name PK
        bigint price PK
        int position PK
    }

    staff_call {
        bigint id PK
        bigint store_id FK
        int table_no
        varchar(20) name
        enum state "INCOMPLETE, COMPLETE"
        datetime(6) complete_time
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---

## 주문 결제

_orders_payment_

```mermaid
erDiagram
    store ||--o{ orders_payment: ""
    orders_payment o{--|| pos_table_activity: ""

    orders_payment {
        bigint id PK
        bigint store_id FK
        bigint pos_table_activity_id FK
        enum method "cASH, CARD"
        enum state "APPROVE, CANCEL"
        bigint amount
        boolean cancellable
        varchar(30) approval_no
        varchar(10) installment
        varchar(30) card_no
        varchar(30) issuer_name
        varchar(30) purchase_name
        varchar(30) merchant_no
        varchar(20) trade_time
        varchar(30) trade_unique_no
        bigint vat
        bigint supply_amount
        varchar(30) cash_receipt_no
        enum cash_receipt_type "NONE, DEDUCTION, PROOF"
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---

## POS

_pos_table_

_pos_table_activity_

```mermaid
erDiagram
    store ||--o{ pos_table: ""
    store ||--o{ pos_table_activity: ""
    pos_table ||--o{ pos_table_activity: "has"
    pos_table_activity ||--o{ orders: "has"
    pos_table_activity ||--o{ orders_payment: "has"

    pos_table {
        bigint id PK
        bigint store_id FK
        int table_no
        boolean active
        datetime(6) created_at
        datetime(6) updated_at
    }

    pos_table_activity {
        bigint id PK
        bigint store_id FK
        bigint pos_table_id FK
        int table_no
        bigint discount
        boolean active
        datetime(6) created_at
        datetime(6) updated_at
    }
```

---
