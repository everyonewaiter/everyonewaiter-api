alter table store_setting
    add column show_order_menu_image boolean not null default true;

alter table orders_menu
    add column image char(30) not null default 'menu/preparation.png';
