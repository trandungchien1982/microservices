
-- Create main table: orders
CREATE TABLE IF NOT EXISTS orders (
    id bigint primary key,
    status varchar(255),
    order_name varchar(255),
    customer_name varchar(255),
    card_number varchar(255),
    description varchar(255),
    create_date date
);

INSERT IGNORE  INTO orders (id, status, order_name, customer_name, card_number, description, create_date)
    VALUES
        (0, 'CREATED', 'OrderName 1', 'Customer_01', 'Card01', 'Des-01', '2021-10-12'),
        (1, 'PENDING', 'OrderName 2', 'Customer_02', 'Card02', 'Des-02', '2021-10-14'),
        (2, 'PAID', 'OrderName 3', 'Customer_03', 'Card03', 'Des-03', '2021-10-15'),
        (3, 'FINISH', 'OrderName 4', 'Customer_04', 'Card04', 'Des-04', '2021-10-17')
;