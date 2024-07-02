create database flood_homeless_support;

\connect flood_homeless_support;

create extension if not exists "uuid-ossp";

---------- TABELA ITEM_TYPE ----------
create table item_type
(
    id   uuid         not null default uuid_generate_v4(),
    name varchar(255) not null,

    primary key (id)
);

---------- TABELA ITEM ----------
create table item
(
    id           uuid         not null default uuid_generate_v4(),
    name         varchar(255) not null,
    item_type_id uuid         not null default uuid_generate_v4(),

    primary key (id),
    foreign key (item_type_id) references item_type (id)
);

---------- TABELA CENTRO DE DISTRIBUIÇAO ----------
create table distribution_center
(
    id      uuid         not null default uuid_generate_v4(),
    name    varchar(255) not null,
    address varchar(255) not null,

    primary key (id)
);

---------- TABELA DOAÇAO ----------
create table donation
(
    id                     uuid not null default uuid_generate_v4(),
    distribution_center_id uuid not null,

    primary key (id),
    foreign key (distribution_center_id) references distribution_center (id)
);

---------- TABELA ITEM POR DOAÇAO ----------
create table item_donation
(
    item_id     uuid    not null,
    donation_id uuid    not null,
    quantity    integer not null,

    primary key (item_id, donation_id),
    foreign key (item_id) references item (id),
    foreign key (donation_id) references donation (id)
);

---------- TABELA PEDIDO ----------
create table order_status
(
    id   uuid         not null default uuid_generate_v4(),
    name varchar(255) not null,

    primary key (id)
);

---------- TABELA ABRIGO ----------
create table shelter
(
    id          uuid         not null default uuid_generate_v4(),
    name        varchar(255) not null,
    address     varchar(255) not null,
    responsible varchar(255) not null,
    phone       varchar(255) not null,
    email       varchar(255) not null,
    capacity    integer      not null,
    occupation  integer      not null
);

---------- TABELA ITEM POR PEDIDO ----------
create table "order"
(
    id              uuid         not null default uuid_generate_v4(),
    name            varchar(255) not null,
    order_status_id uuid         not null,

    primary key (id),
    foreign key (order_status_id) references order_status (id)
);

---------- TABELA STATUS DO PEDIDO ----------
create table order_item
(
    order_id uuid    not null,
    item_id  uuid    not null,
    quantity integer not null,

    primary key (item_id, order_id),
    foreign key (order_id) references "order" (id),
    foreign key (item_id) references item (id)
);



