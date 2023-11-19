
-- Create main table: payment
CREATE TABLE IF NOT EXISTS payment (
    id bigint primary key,
    order_id varchar(255),
    card_number varchar(255),
    create_date date
);

INSERT IGNORE INTO payment (id, order_id, card_number, create_date)
    VALUES
        (0, 'OrderID-1', 'Card-01', '2021-10-12'),
        (2, 'OrderID-2', 'Card-02', '2022-10-12'),
        (3, 'OrderID-3', 'Card-03', '2023-10-12')
;