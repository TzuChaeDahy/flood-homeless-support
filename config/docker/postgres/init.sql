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


primary key (id) );

insert into
    item_type (
        id,
        name,
        default_attributes,
        default_names
    )
values (
        'b639837c-de33-4328-bccf-a6890a32c72d',
        'roupas',
        '{descricao,tamanho,genero}',
        '{camisa,agasalho}'
    ),
    (
        '1b274519-eb1d-442a-9c54-bd3219934c50',
        'higiene',
        '{descricao}',
        '{sabonete,escova de dente,pasta de dente,absorvente}'
    ),
    (
        'e952f1d9-8813-4712-8be7-3616cb642372',
        'comidas',
        '{descricao,peso,unidade de medida,validade}',
        '{arroz,feijao,macarrao,frango}'
    );
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


primary key (id) );

insert into
    distribution_center (id, name, address)
values (
        'eccbd146-6940-42fb-9b70-2de9506940b3',
        'esperança',
        'av. boqueirão, 2450 - igara, canoas - rs, 92032-420'
    ),
    (
        '173767e8-d2c1-4545-bef5-d8baff788ca3',
        'prosperidade',
        'av. borges de medeiros, 1501 – cidade baixa, porto
alegre - rs, 90119-900'
    ),
    (
        '5179d172-b1c3-4769-b75e-0c34d74c736f',
        'reconstruçao',
        'r. dr. décio martins costa, 312 - vila eunice nova,
cachoeirinha - rs, 94920-170'
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


primary key (id) );

insert into
    order_status (id, name)
values (
        '75040965-0e33-4eba-a94a-4f21d3be3ce0',
        'aceito'
    ),
    (
        '8ad87d55-cac7-4bc3-992b-7de5dc71704e',
        'recusado'
    ),
    (
        '1e6ea845-8af0-4468-afb5-b9e6bed8738b',
        'em espera'
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


primary key (id) );

insert into shelter (id, name, address, responsible, phone, email, capacity, occupation)
values ('2eedd538-c9a1-4e90-b528-9b1f05cbd1fc', 'cuidado', 'r. angelita ferro de melo, 312 - vila eunice nova,
cachoeirinha - rs, 94920-170', 'marcelo', '51999990000', 'marcelo@cuidado.com', 100, 24);

---------- TABELA ITEM POR PEDIDO ----------
create table "order"
(
    id              uuid         not null default uuid_generate_v4(),
    description            varchar(255),
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