-- DB Schema for `order-service`
create database if not exists `01.order-service-db`;
use `01.order-service-db`;
-- Create table for Auto Increment ID
create table hibernate_sequence ( next_val bigint );
insert into hibernate_sequence values (1);

-- Create main table: `01.order-service-tables`
create table `01.order-service-tables` (
    id bigint primary key,
    status varchar(255)
);



-- DB Schema for `payment-service`
create database if not exists `02.payment-service-db`;
use `02.payment-service-db`;
-- Create table for Auto Increment ID
create table hibernate_sequence ( next_val bigint );
insert into hibernate_sequence values (1);

-- Create main table: `02.payment-service-tables`
create table `02.payment-service-tables` (
    id bigint primary key,
    status varchar(255)
);


-- DB Schema for `restaurant-service`
create database if not exists `03.restaurant-service-db`;
use `03.restaurant-service-db`;
-- Create table for Auto Increment ID
create table hibernate_sequence ( next_val bigint );
insert into hibernate_sequence values (1);

-- Create main table: `00.restaurant-service-tables`
create table `03.restaurant-service-tables` (
    id bigint primary key,
    status varchar(255)
);

-- DB Schema for `delivery-service`
create database if not exists `04.delivery-service-db`;
use `04.delivery-service-db`;
-- Create table for Auto Increment ID
create table hibernate_sequence ( next_val bigint );
insert into hibernate_sequence values (1);

-- Create main table: `04.delivery-service-tables`
create table `04.delivery-service-tables` (
    id bigint primary key,
    status varchar(255)
);


