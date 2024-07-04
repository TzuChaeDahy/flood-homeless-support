create database flood_homeless_support;

\connect flood_homeless_support;

create extension if not exists "uuid-ossp";

---------- TABELA ITEM_TYPE ----------
create table item_type
(
    id                 uuid           not null default uuid_generate_v4(),
    name               varchar(255)   not null,
    default_attributes varchar(255)[] not null,
    default_names       varchar(255)[] not null,

    primary key (id)
);

insert into item_type (name, default_attributes, default_names)
values ('roupas', '{tamanho, genero}', '{camisa,agasalho}'),
       ('higiene', '{}', '{sabonete,escova de dente,pasta de dente,absorvente}'),
       ('comidas', '{peso,unidade de medida,validade}', '{arroz,feijao,macarrao,frango}');

---------- TABELA ITEM ----------
create table item
(
    id           uuid         not null default uuid_generate_v4(),
    name         varchar(255) not null,
    attributes   jsonb        not null,
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

insert into distribution_center (name, address)
values ('esperança', 'av. boqueirão, 2450 - igara, canoas - rs, 92032-420'),
       ('prosperidade', 'av. borges de medeiros, 1501 – cidade baixa, porto
alegre - rs, 90119-900'),
       ('reconstruçao', 'r. dr. décio martins costa, 312 - vila eunice nova,
cachoeirinha - rs, 94920-170');

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
    occupation  integer      not null,

    primary key (id)
);

---------- TABELA ITEM POR PEDIDO ----------
create table "order"
(
    id              uuid         not null default uuid_generate_v4(),
    name            varchar(255) not null,
    order_status_id uuid         not null,
    shelter_id      uuid         not null,

    primary key (id),
    foreign key (order_status_id) references order_status (id),
    foreign key (shelter_id) references shelter (id)
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



