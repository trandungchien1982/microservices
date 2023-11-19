
-- Create main table: payment
CREATE TABLE IF NOT EXISTS food_item (
    id bigint primary key,
    order_id varchar(255),
    food_name varchar(255),
    create_date date
);

INSERT IGNORE INTO food_item (id, order_id, food_name, create_date)
    VALUES
        (0, 'OrderID-1', 'FoodName-01', '2021-10-12'),
        (2, 'OrderID-2', 'FoodName-02', '2022-10-12'),
        (3, 'OrderID-3', 'FoodName-03', '2023-10-12')
;