# Microservices
Series về tìm hiểu Microservices/Demo/Solutions thực tế<br/>
Các ví dụ liên quan đến Microservices từ cơ bản đến nâng cao<br/>
Mỗi nhánh trong Repo sẽ là 1 ví dụ/ giải pháp/ project mẫu dành cho kiến trúc Microservices

# Môi trường phát triển
- Kubernetes : Dùng k3d + k3s
- Docker: Dùng Docker Hub Registry
- Spring Boot
- DBs: MySQL/PostgreSQL/MongoDB/...
- JDK 8/ 11

# Build Tools sử dụng
- Maven + Gradle
- Intelij IDEA

# Folder liên quan trên Windows
```
D:\Projects\microservices
```
==============================================================

# Ví dụ [01.HelloWorld]
==============================================================

**Tham khảo**
- https://viblo.asia/p/distributed-transaction-saga-pattern-naQZRRnPZvx
- https://viblo.asia/p/naQZRBemZvx

**Mô tả hệ thống**<br/>
Ví dụ HelloWorld này sẽ tạo ra 1 hệ thống Microservices đơn giản dành cho việc order với expect happy path:<br/>
( Chưa apply transactions và giả định rằng mọi thứ diễn ra êm đẹp :) )
- Order Service
- Payment Service 
- Restaurant Service
- Delivery Service

**Kịch bản chi tiết**
- (1) khách hàng tiến hành đặt order online, trigger `order service`, order ở state `CREATED`.
- (2) sau khi tạo order với state `CREATED`, `order service` trigger `payment service` để thực hiện thanh toán.
- (3) thanh toán thành công, `payment service` respond về `order service` 
báo đã thanh toán thành công, chuyển order state thành `PAID`, đồng thời trigger tiếp restaurant service để chuẩn bị món ăn.
- (4) sau khi nhà hàng thực hiện xong, respond về `order service` update order state thành `PREPARED`, trigger `delivery service` để giao hàng.
- (5) giao hàng thành công, `delivery service` gửi message cuối cùng về `order service` để `COMPLETED` order.


**App/Tools sử dụng**
- `Spring Boot` (tham khảo: https://github.com/trandungchien1982/spring/tree/04.JPA+MySQL)
- `MySQL` (tham khảo: https://github.com/trandungchien1982/docker/tree/01.MySQL)
- `RabbitMQ` (để đơn giản trong việc implementations)
- Logs tập trung sử dụng `RabbitMQ+LogService` tự chế<br/>
  (tham khảo https://github.com/trandungchien1982/spring/tree/09.LogServices+RabbitMQ)

**Truy cập Tools**
- RabbitMQ:
```shell
http://ubuntu.local:15872/
User: admin
Pass: admin
```
- Service DBs (MySQL 8.1.0)
  - Order Service DB:
  ```shell
  Server: ubuntu.local:4306
  User: root
  Pass: root
  DB: 01.order-service-db
  ```

  - Payment Service DB:
  ```shell
  Server: ubuntu.local:4306
  User: root
  Pass: root
  DB: 02.payement-service-db
  ```
  - Restaurant Service DB:
  ```shell
  Server: ubuntu.local:4306
  User: root
  Pass: root
  DB: 03.restaurant-service-db
  ```
  - Delivery Service DB:
  ```shell
  Server: ubuntu.local:4306
  User: root
  Pass: root
  DB: 04.delivery-service-db
  ```

**Kết quả thực thi**
- (1) khách hàng đặt order online:
```shell
GET http://localhost:8401/submit-order
---------------------------------------------------------------------------------------------
OK with new orderId: 1700469140494, customerName: Customer-00, cardNumber: CardNumber-00
```
- Xem logs của service tương ứng: `order-service`
```shell
15:28:14.155 [main] INFO  - Starting ProtocolHandler ["http-nio-8401"]
15:28:14.173 [main] INFO  - Tomcat started on port(s): 8401 (http) with context path ''
15:28:14.176 [main] INFO  - Attempting to connect to: [ubuntu.local:56820]
15:28:14.206 [main] INFO  - Created new connection: rabbitConnectionFactory#2c02a007:0/SimpleConnection@e88e14 [delegate=amqp://admin@172.27.53.76:56820/, localPort= 43576]
15:28:14.286 [main] INFO  - Started MainApp in 4.701 seconds (JVM running for 5.188)
15:28:14.299 [main] INFO  - [MAIN] Start app [order-service] ...
15:28:14.299 [main] INFO  - --------------------------------------------------------
15:32:20.441 [http-nio-8401-exec-1] INFO  - Initializing Spring DispatcherServlet 'dispatcherServlet'
15:32:20.442 [http-nio-8401-exec-1] INFO  - Initializing Servlet 'dispatcherServlet'
15:32:20.445 [http-nio-8401-exec-1] INFO  - Completed initialization in 3 ms
15:32:20.487 [http-nio-8401-exec-1] INFO  -
15:32:20.494 [http-nio-8401-exec-1] INFO  -  --------------------------------------------------------------------
15:32:20.494 [http-nio-8401-exec-1] INFO  - [Orders] :: User has submitted a new order ...
15:32:20.503 [http-nio-8401-exec-1] INFO  - [Orders] :: Create new order with id: 1700469140494, status: CREATED
15:32:20.546 [http-nio-8401-exec-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:20.561 [http-nio-8401-exec-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:20.592 [http-nio-8401-exec-1] INFO  - [Orders] :: Notify payment for orderId: 1700469140494
15:32:20.623 [http-nio-8401-exec-1] DEBUG -
    insert
    into
        `
        orders` (
            card_number, create_date, customer_name, description, order_name, status, id
        )
    values
        (?, ?, ?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        `
        orders` (
            card_number, create_date, customer_name, description, order_name, status, id
        )
    values
        (?, ?, ?, ?, ?, ?, ?)
15:32:20.625 [http-nio-8401-exec-1] TRACE - binding parameter [1] as [VARCHAR] - [CardNumber-00]
15:32:20.626 [http-nio-8401-exec-1] TRACE - binding parameter [2] as [TIMESTAMP] - [Mon Nov 20 15:32:20 ICT 2023]
15:32:20.627 [http-nio-8401-exec-1] TRACE - binding parameter [3] as [VARCHAR] - [Customer-00]
15:32:20.627 [http-nio-8401-exec-1] TRACE - binding parameter [4] as [VARCHAR] - [Description of orderId: 1700469140494]
15:32:20.627 [http-nio-8401-exec-1] TRACE - binding parameter [5] as [VARCHAR] - [NewOrder: 1700469140494]
15:32:20.627 [http-nio-8401-exec-1] TRACE - binding parameter [6] as [VARCHAR] - [CREATED]
15:32:20.627 [http-nio-8401-exec-1] TRACE - binding parameter [7] as [BIGINT] - [1700469140494]
15:32:21.759 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] INFO  -  . . . . . . . . . . . . .
15:32:21.760 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] INFO  - [Orders] :: Consume payment response: 1700469140494:NOTIFY_PAYMENT_SUCCESS
15:32:21.761 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] INFO  - [Orders] :: Get Orders by orderId: 1700469140494
15:32:21.769 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:21.770 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:21.782 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([card_num2_0_0_] : [VARCHAR]) - [CardNumber-00]
15:32:21.783 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([create_d3_0_0_] : [TIMESTAMP]) - [2023-11-20 00:00:00.0]
15:32:21.783 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([customer4_0_0_] : [VARCHAR]) - [Customer-00]15:32:21.783 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([descript5_0_0_] : [VARCHAR]) - [Description of orderId: 1700469140494]
15:32:21.783 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([order_na6_0_0_] : [VARCHAR]) - [NewOrder: 1700469140494]
15:32:21.784 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([status7_0_0_] : [VARCHAR]) - [CREATED]
15:32:21.793 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] INFO  - [Orders] :: Change status of Orders from: CREATED into status: PAID
15:32:21.796 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:21.797 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:21.799 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([card_num2_0_0_] : [VARCHAR]) - [CardNumber-00]
15:32:21.799 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([create_d3_0_0_] : [TIMESTAMP]) - [2023-11-20 00:00:00.0]
15:32:21.799 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([customer4_0_0_] : [VARCHAR]) - [Customer-00]15:32:21.799 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([descript5_0_0_] : [VARCHAR]) - [Description of orderId: 1700469140494]
15:32:21.799 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([order_na6_0_0_] : [VARCHAR]) - [NewOrder: 1700469140494]
15:32:21.799 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - extracted value ([status7_0_0_] : [VARCHAR]) - [CREATED]
15:32:21.800 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] DEBUG -
    update
        `orders`
    set
        card_number=?,
        create_date=?,
        customer_name=?,
        description=?,
        order_name=?,
        status=?
    where
        id=?
Hibernate:
    update
        `orders`
    set
        card_number=?,
        create_date=?,
        customer_name=?,
        description=?,
        order_name=?,
        status=?
    where
        id=?
15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [1] as [VARCHAR] - [CardNumber-00]
15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [2] as [TIMESTAMP] - [2023-11-20 00:00:00.0]15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [3] as [VARCHAR] - [Customer-00]
15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [4] as [VARCHAR] - [Description of orderId: 1700469140494]
15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [5] as [VARCHAR] - [NewOrder: 1700469140494]15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [6] as [VARCHAR] - [PAID]
15:32:21.801 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] TRACE - binding parameter [7] as [BIGINT] - [1700469140494]
15:32:21.822 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#1-1] INFO  - [Orders] :: Notify message to request the restaurant service for orderId: 1700469140494
15:32:22.998 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] INFO  -  . . . . . . . . . . . . .
15:32:22.999 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] INFO  - [Orders] :: Consume restaurant response: 1700469140494:NOTIFY_RESTAURANT_SUCCESS
15:32:23.000 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] INFO  - [Orders] :: Get Orders by orderId: 1700469140494
15:32:23.005 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:23.006 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:23.008 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([card_num2_0_0_] : [VARCHAR]) - [CardNumber-00]
15:32:23.009 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([create_d3_0_0_] : [TIMESTAMP]) - [2023-11-20 00:00:00.0]
15:32:23.009 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([customer4_0_0_] : [VARCHAR]) - [Customer-00]15:32:23.009 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([descript5_0_0_] : [VARCHAR]) - [Description of orderId: 1700469140494]
15:32:23.009 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([order_na6_0_0_] : [VARCHAR]) - [NewOrder: 1700469140494]
15:32:23.009 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([status7_0_0_] : [VARCHAR]) - [PAID]
15:32:23.013 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] INFO  - [Orders] :: Change status of Orders from: PAID into status: PREPARED
15:32:23.016 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:23.017 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:23.020 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([card_num2_0_0_] : [VARCHAR]) - [CardNumber-00]
15:32:23.021 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([create_d3_0_0_] : [TIMESTAMP]) - [2023-11-20 00:00:00.0]
15:32:23.021 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([customer4_0_0_] : [VARCHAR]) - [Customer-00]15:32:23.021 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([descript5_0_0_] : [VARCHAR]) - [Description of orderId: 1700469140494]
15:32:23.021 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([order_na6_0_0_] : [VARCHAR]) - [NewOrder: 1700469140494]
15:32:23.022 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - extracted value ([status7_0_0_] : [VARCHAR]) - [PAID]
15:32:23.023 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] DEBUG -
    update
        `orders`
    set
        card_number=?,
        create_date=?,
        customer_name=?,
        description=?,
        order_name=?,
        status=?
    where
        id=?
Hibernate:
    update
        `orders`
    set
        card_number=?,
        create_date=?,
        customer_name=?,
        description=?,
        order_name=?,
        status=?
    where
        id=?
15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [1] as [VARCHAR] - [CardNumber-00]
15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [2] as [TIMESTAMP] - [2023-11-20 00:00:00.0]15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [3] as [VARCHAR] - [Customer-00]
15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [4] as [VARCHAR] - [Description of orderId: 1700469140494]
15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [5] as [VARCHAR] - [NewOrder: 1700469140494]15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [6] as [VARCHAR] - [PREPARED]
15:32:23.024 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#2-1] TRACE - binding parameter [7] as [BIGINT] - [1700469140494]
15:32:26.184 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  -  . . . . . . . . . . . . .
15:32:26.184 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Orders] :: Consume delivery response: 1700469140494:NOTIFY_DELIVERY_SUCCESS
15:32:26.185 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Orders] :: Get Orders by orderId: 1700469140494
15:32:26.190 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:26.191 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:26.193 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([card_num2_0_0_] : [VARCHAR]) - [CardNumber-00]
15:32:26.194 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([create_d3_0_0_] : [TIMESTAMP]) - [2023-11-20 00:00:00.0]
15:32:26.194 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([customer4_0_0_] : [VARCHAR]) - [Customer-00]15:32:26.194 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([descript5_0_0_] : [VARCHAR]) - [Description of orderId: 1700469140494]
15:32:26.194 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([order_na6_0_0_] : [VARCHAR]) - [NewOrder: 1700469140494]
15:32:26.194 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([status7_0_0_] : [VARCHAR]) - [PREPARED]
15:32:26.198 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Orders] :: Change status of Orders from: PREPARED into status: COMPLETED
15:32:26.201 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
Hibernate:
    select
        orders0_.id as id1_0_0_,
        orders0_.card_number as card_num2_0_0_,
        orders0_.create_date as create_d3_0_0_,
        orders0_.customer_name as customer4_0_0_,
        orders0_.description as descript5_0_0_,
        orders0_.order_name as order_na6_0_0_,
        orders0_.status as status7_0_0_
    from
        `orders` orders0_
    where
        orders0_.id=?
15:32:26.202 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [BIGINT] - [1700469140494]
15:32:26.204 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([card_num2_0_0_] : [VARCHAR]) - [CardNumber-00]
15:32:26.205 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([create_d3_0_0_] : [TIMESTAMP]) - [2023-11-20 00:00:00.0]
15:32:26.205 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([customer4_0_0_] : [VARCHAR]) - [Customer-00]15:32:26.205 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([descript5_0_0_] : [VARCHAR]) - [Description of orderId: 1700469140494]
15:32:26.205 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([order_na6_0_0_] : [VARCHAR]) - [NewOrder: 1700469140494]
15:32:26.205 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - extracted value ([status7_0_0_] : [VARCHAR]) - [PREPARED]
15:32:26.207 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    update
        `orders`
    set
        card_number=?,
        create_date=?,
        customer_name=?,
        description=?,
        order_name=?,
        status=?
    where
        id=?
Hibernate:
    update
        `orders`
    set
        card_number=?,
        create_date=?,
        customer_name=?,
        description=?,
        order_name=?,
        status=?
    where
        id=?
15:32:26.207 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [VARCHAR] - [CardNumber-00]
15:32:26.207 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [2] as [TIMESTAMP] - [2023-11-20 00:00:00.0]15:32:26.207 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [3] as [VARCHAR] - [Customer-00]
15:32:26.207 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [4] as [VARCHAR] - [Description of orderId: 1700469140494]
15:32:26.207 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [5] as [VARCHAR] - [NewOrder: 1700469140494]15:32:26.208 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [6] as [VARCHAR] - [COMPLETED]
15:32:26.208 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [7] as [BIGINT] - [1700469140494]
15:32:26.231 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Orders] :: Complete the order workflow for orderId: 1700469140494
```

- Xem logs của service tương ứng: `payment-service`
```shell
15:29:01.782 [main] INFO  - Starting ProtocolHandler ["http-nio-8402"]
15:29:01.801 [main] INFO  - Tomcat started on port(s): 8402 (http) with context path ''
15:29:01.803 [main] INFO  - Attempting to connect to: [ubuntu.local:56820]
15:29:01.832 [main] INFO  - Created new connection: rabbitConnectionFactory#33f2eb04:0/SimpleConnection@67d8613 [delegate=amqp://admin@172.27.53.76:56820/, localPort= 43596]
15:29:01.906 [main] INFO  - Started MainApp in 4.464 seconds (JVM running for 4.958)
15:29:01.923 [main] INFO  - [MAIN] Start app [payment-service] ...
15:29:01.923 [main] INFO  - --------------------------------------------------------
15:32:20.609 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  -  . . . . . . . . . . . . .
15:32:20.617 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Payment] :: Consume payment request: 1700469140494:NOTIFY_PAYMENT_REQUEST:CardNumber-00
15:32:20.618 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Payment] :: Process the payment for orderId: 1700469140494
15:32:21.669 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    select
        payment0_.id as id1_0_0_,
        payment0_.card_number as card_num2_0_0_,
        payment0_.create_date as create_d3_0_0_,
        payment0_.order_id as order_id4_0_0_
    from
        `payment` payment0_
    where
        payment0_.id=?
Hibernate:
    select
        payment0_.id as id1_0_0_,
        payment0_.card_number as card_num2_0_0_,
        payment0_.create_date as create_d3_0_0_,
        payment0_.order_id as order_id4_0_0_
    from
        `payment` payment0_
    where
        payment0_.id=?
15:32:21.683 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [BIGINT] - [8201303440061197534]
15:32:21.715 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    insert
    into
        `
        payment` (
            card_number, create_date, order_id, id
        )
    values
        (?, ?, ?, ?)
Hibernate:
    insert
    into
        `
        payment` (
            card_number, create_date, order_id, id
        )
    values
        (?, ?, ?, ?)
15:32:21.716 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [VARCHAR] - [CardNumber-00]
15:32:21.717 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [2] as [TIMESTAMP] - [Mon Nov 20 15:32:20 ICT 2023]
15:32:21.717 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [3] as [VARCHAR] - [1700469140494]
15:32:21.717 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [4] as [BIGINT] - [8201303440061197534]
15:32:21.744 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Payment] :: Create new payment successfully for orderId: 1700469140494
15:32:21.744 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Payment] :: Send NOTIFY_PAYMENT_SUCCESS for orderId: 1700469140494

```

- Xem logs của service tương ứng: `restaurant-service`
```shell
15:29:48.519 [main] INFO  - Starting ProtocolHandler ["http-nio-8403"]
15:29:48.536 [main] INFO  - Tomcat started on port(s): 8403 (http) with context path ''
15:29:48.538 [main] INFO  - Attempting to connect to: [ubuntu.local:56820]
15:29:48.570 [main] INFO  - Created new connection: rabbitConnectionFactory#2d3c501b:0/SimpleConnection@89465d9 [delegate=amqp://admin@172.27.53.76:56820/, localPort= 43617]
15:29:48.633 [main] INFO  - Started MainApp in 4.642 seconds (JVM running for 5.149)
15:29:48.649 [main] INFO  - [MAIN] Start app [restaurant-service] ...
15:29:48.649 [main] INFO  - --------------------------------------------------------
15:32:21.841 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  -  . . . . . . . . . . . . .
15:32:21.848 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Restaurant] :: Consume restaurant request: 1700469140494:NOTIFY_RESTAURANT_REQUEST:Food_NumberTop
15:32:21.856 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Restaurant] :: Process preparing food for orderId: 1700469140494, foodName: Food_NumberTop
15:32:22.914 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    select
        fooditem0_.id as id1_0_0_,
        fooditem0_.create_date as create_d2_0_0_,
        fooditem0_.food_name as food_nam3_0_0_,
        fooditem0_.order_id as order_id4_0_0_
    from
        `food_item` fooditem0_
    where
        fooditem0_.id=?
Hibernate:
    select
        fooditem0_.id as id1_0_0_,
        fooditem0_.create_date as create_d2_0_0_,
        fooditem0_.food_name as food_nam3_0_0_,
        fooditem0_.order_id as order_id4_0_0_
    from
        `food_item` fooditem0_
    where
        fooditem0_.id=?
15:32:22.928 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [BIGINT] - [-533253128636816380]
15:32:22.962 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    insert
    into
        `
        food_item` (
            create_date, food_name, order_id, id
        )
    values
        (?, ?, ?, ?)
Hibernate:
    insert
    into
        `
        food_item` (
            create_date, food_name, order_id, id
        )
    values
        (?, ?, ?, ?)
15:32:22.964 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [TIMESTAMP] - [Mon Nov 20 15:32:21 ICT 2023]
15:32:22.965 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [2] as [VARCHAR] - [Food_NumberTop]
15:32:22.965 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [3] as [VARCHAR] - [1700469140494]
15:32:22.965 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [4] as [BIGINT] - [-533253128636816380]
15:32:22.992 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Restaurant] :: Prepare food successfully for orderId: 1700469140494
15:32:22.993 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Restaurant] :: Send NOTIFY_RESTAURANT_SUCCESS for orderId: 1700469140494
15:32:22.993 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Restaurant] :: Request to delivery service for orderId: 1700469140494
```

- Xem logs của service tương ứng: `delivery-service`
```shell
15:30:25.352 [main] INFO  - Starting ProtocolHandler ["http-nio-8404"]
15:30:25.371 [main] INFO  - Tomcat started on port(s): 8404 (http) with context path ''
15:30:25.372 [main] INFO  - Attempting to connect to: [ubuntu.local:56820]
15:30:25.402 [main] INFO  - Created new connection: rabbitConnectionFactory#2ed7978c:0/SimpleConnection@6d41200c [delegate=amqp://admin@172.27.53.76:56820/, localPort= 43636]
15:30:25.475 [main] INFO  - Started MainApp in 4.461 seconds (JVM running for 4.978)
15:30:25.491 [main] INFO  - [MAIN] Start app [delivery-service] ...
15:30:25.491 [main] INFO  - --------------------------------------------------------
15:32:23.011 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  -  . . . . . . . . . . . . .
15:32:23.019 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Delivery] :: Consume delivery request: 1700469140494:NOTIFY_DELIVERY_REQUEST:Food_NumberTop
15:32:23.035 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Delivery] :: Process delivery for orderId: 1700469140494, foodName: Food_NumberTop, driverName: DriverName: 1700469140494
15:32:26.095 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    select
        deliveryit0_.id as id1_0_0_,
        deliveryit0_.create_date as create_d2_0_0_,
        deliveryit0_.driver_name as driver_n3_0_0_,
        deliveryit0_.food_name as food_nam4_0_0_,
        deliveryit0_.order_id as order_id5_0_0_
    from
        `delivery_item` deliveryit0_
    where
        deliveryit0_.id=?
Hibernate:
    select
        deliveryit0_.id as id1_0_0_,
        deliveryit0_.create_date as create_d2_0_0_,
        deliveryit0_.driver_name as driver_n3_0_0_,
        deliveryit0_.food_name as food_nam4_0_0_,
        deliveryit0_.order_id as order_id5_0_0_
    from
        `delivery_item` deliveryit0_
    where
        deliveryit0_.id=?
15:32:26.110 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [BIGINT] - [-900169331876939356]
15:32:26.144 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] DEBUG -
    insert
    into
        `
        delivery_item` (
            create_date, driver_name, food_name, order_id, id
        )
    values
        (?, ?, ?, ?, ?)
Hibernate:
    insert
    into
        `
        delivery_item` (
            create_date, driver_name, food_name, order_id, id
        )
    values
        (?, ?, ?, ?, ?)
15:32:26.145 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [1] as [TIMESTAMP] - [Mon Nov 20 15:32:23 ICT 2023]
15:32:26.146 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [2] as [VARCHAR] - [DriverName: 1700469140494]
15:32:26.147 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [3] as [VARCHAR] - [Food_NumberTop]
15:32:26.147 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [4] as [VARCHAR] - [1700469140494]
15:32:26.147 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] TRACE - binding parameter [5] as [BIGINT] - [-900169331876939356]
15:32:26.180 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Delivery] :: Process delivery successfully for orderId: 1700469140494, foodName: Food_NumberTop
15:32:26.181 [org.springframework.amqp.rabbit.RabbitListenerEndpointContainer#0-1] INFO  - [Delivery] :: Send NOTIFY_DELIVERY_SUCCESS for orderId: 1700469140494
```

**Xem logs tổng thể của Workflow order chạy qua tất cả service tương ứng:**<br/>
  (nằm trong container của `log-service`)
```shell
15:04:20.817 INFO  - Tomcat started on port(s): 8700 (http) with context path ''
15:04:20.819 INFO  - Attempting to connect to: [rabbitmq-java-logs-service:5672]
15:04:20.934 INFO  - Created new connection: rabbitConnectionFactory#50b472aa:0/SimpleConnection@60015ef5 [delegate=amqp://admin@172.18.0.7:5672/, localPort= 52910]
15:04:21.048 INFO  - Started LogServicesApplication in 3.993 seconds (JVM running for 4.986)
15:04:21.076 INFO  - ~> log-service, Try to send 3 sample logs :) to make sure RabbitMQ is working properly :)
15:04:21.077 INFO  - ~> log-service, Sample message idx 0
15:04:21.078 INFO  - ~> log-service, Sample message idx 1
15:04:21.079 INFO  - ~> log-service, Sample message idx 2
15:09:55.489 INFO  - ~>
15:09:55.490 INFO  - ~>  --------------------------------------------------------------------
15:09:55.490 INFO  - ~> [Orders] :: User has submitted a new order ...
15:09:55.499 INFO  - ~> [Orders] :: Create new order with id: 1700469140494, status: CREATED
15:09:55.588 INFO  - ~> [Orders] :: Notify payment for orderId: 1700469140494
15:09:55.612 INFO  - ~>  . . . . . . . . . . . . .
15:09:55.613 INFO  - ~> [Payment] :: Consume payment request: 1700469140494:NOTIFY_PAYMENT_REQUEST:CardNumber-00
15:09:55.614 INFO  - ~> [Payment] :: Process the payment for orderId: 1700469140494
15:09:56.740 INFO  - ~> [Payment] :: Create new payment successfully for orderId: 1700469140494
15:09:56.741 INFO  - ~> [Payment] :: Send NOTIFY_PAYMENT_SUCCESS for orderId: 1700469140494
15:09:56.755 INFO  - ~>  . . . . . . . . . . . . .
15:09:56.756 INFO  - ~> [Orders] :: Consume payment response: 1700469140494:NOTIFY_PAYMENT_SUCCESS
15:09:56.758 INFO  - ~> [Orders] :: Get Orders by orderId: 1700469140494
15:09:56.790 INFO  - ~> [Orders] :: Change status of Orders from: CREATED into status: PAID
15:09:56.818 INFO  - ~> [Orders] :: Notify message to request the restaurant service for orderId: 1700469140494
15:09:56.843 INFO  - ~>  . . . . . . . . . . . . .
15:09:56.844 INFO  - ~> [Restaurant] :: Consume restaurant request: 1700469140494:NOTIFY_RESTAURANT_REQUEST:Food_NumberTop
15:09:56.852 INFO  - ~> [Restaurant] :: Process preparing food for orderId: 1700469140494, foodName: Food_NumberTop
15:09:57.987 INFO  - ~> [Restaurant] :: Prepare food successfully for orderId: 1700469140494
15:09:57.988 INFO  - ~> [Restaurant] :: Send NOTIFY_RESTAURANT_SUCCESS for orderId: 1700469140494
15:09:57.989 INFO  - ~> [Restaurant] :: Request to delivery service for orderId: 1700469140494
15:09:57.993 INFO  - ~>  . . . . . . . . . . . . .
15:09:57.995 INFO  - ~> [Orders] :: Consume restaurant response: 1700469140494:NOTIFY_RESTAURANT_SUCCESS
15:09:57.996 INFO  - ~> [Orders] :: Get Orders by orderId: 1700469140494
15:09:58.009 INFO  - ~> [Orders] :: Change status of Orders from: PAID into status: PREPARED
15:09:58.014 INFO  - ~>  . . . . . . . . . . . . .
15:09:58.015 INFO  - ~> [Delivery] :: Consume delivery request: 1700469140494:NOTIFY_DELIVERY_REQUEST:Food_NumberTop
15:09:58.030 INFO  - ~> [Delivery] :: Process delivery for orderId: 1700469140494, foodName: Food_NumberTop, driverName: DriverName: 1700469140494
15:10:01.176 INFO  - ~> [Delivery] :: Process delivery successfully for orderId: 1700469140494, foodName: Food_NumberTop
15:10:01.176 INFO  - ~> [Delivery] :: Send NOTIFY_DELIVERY_SUCCESS for orderId: 1700469140494
15:10:01.179 INFO  - ~>  . . . . . . . . . . . . .
15:10:01.180 INFO  - ~> [Orders] :: Consume delivery response: 1700469140494:NOTIFY_DELIVERY_SUCCESS
15:10:01.181 INFO  - ~> [Orders] :: Get Orders by orderId: 1700469140494
15:10:01.194 INFO  - ~> [Orders] :: Change status of Orders from: PREPARED into status: COMPLETED
15:10:01.226 INFO  - ~> [Orders] :: Complete the order workflow for orderId: 1700469140494
```

**Trường hợp User Order có thông tin customerName+cardNumber**
```shell
GET http://localhost:8401/submit-order?customerName=CustomerABC&cardNumber=Card123
-------------------------------------------------------------------------------------------
OK with new orderId: 1700469688961, customerName: CustomerABC, cardNumber: Card123
```
<br/>Log chi tiết trong `log-service`
```shell
15:19:03.958 INFO  - ~>  --------------------------------------------------------------------
15:19:03.958 INFO  - ~> [Orders] :: User has submitted a new order ...
15:19:03.959 INFO  - ~> [Orders] :: Create new order with id: 1700469688961, status: CREATED
15:19:03.962 INFO  - ~> [Orders] :: Notify payment for orderId: 1700469688961
15:19:03.966 INFO  - ~>  . . . . . . . . . . . . .
15:19:03.967 INFO  - ~> [Payment] :: Consume payment request: 1700469688961:NOTIFY_PAYMENT_REQUEST:Card123
15:19:03.967 INFO  - ~> [Payment] :: Process the payment for orderId: 1700469688961
15:19:05.003 INFO  - ~> [Payment] :: Create new payment successfully for orderId: 1700469688961
15:19:05.004 INFO  - ~> [Payment] :: Send NOTIFY_PAYMENT_SUCCESS for orderId: 1700469688961
15:19:05.007 INFO  - ~>  . . . . . . . . . . . . .
15:19:05.007 INFO  - ~> [Orders] :: Consume payment response: 1700469688961:NOTIFY_PAYMENT_SUCCESS
15:19:05.008 INFO  - ~> [Orders] :: Get Orders by orderId: 1700469688961
15:19:05.016 INFO  - ~> [Orders] :: Change status of Orders from: CREATED into status: PAID
15:19:05.041 INFO  - ~> [Orders] :: Notify message to request the restaurant service for orderId: 1700469688961
15:19:05.045 INFO  - ~>  . . . . . . . . . . . . .
15:19:05.045 INFO  - ~> [Restaurant] :: Consume restaurant request: 1700469688961:NOTIFY_RESTAURANT_REQUEST:Food_NumberTop
15:19:05.046 INFO  - ~> [Restaurant] :: Process preparing food for orderId: 1700469688961, foodName: Food_NumberTop
15:19:06.079 INFO  - ~> [Restaurant] :: Prepare food successfully for orderId: 1700469688961
15:19:06.080 INFO  - ~> [Restaurant] :: Send NOTIFY_RESTAURANT_SUCCESS for orderId: 1700469688961
15:19:06.081 INFO  - ~> [Restaurant] :: Request to delivery service for orderId: 1700469688961
15:19:06.084 INFO  - ~>  . . . . . . . . . . . . .
15:19:06.084 INFO  - ~> [Orders] :: Consume restaurant response: 1700469688961:NOTIFY_RESTAURANT_SUCCESS
15:19:06.085 INFO  - ~> [Orders] :: Get Orders by orderId: 1700469688961
15:19:06.086 INFO  - ~>  . . . . . . . . . . . . .
15:19:06.086 INFO  - ~> [Delivery] :: Consume delivery request: 1700469688961:NOTIFY_DELIVERY_REQUEST:Food_NumberTop
15:19:06.087 INFO  - ~> [Delivery] :: Process delivery for orderId: 1700469688961, foodName: Food_NumberTop, driverName: DriverName: 1700469688961
15:19:06.093 INFO  - ~> [Orders] :: Change status of Orders from: PAID into status: PREPARED
15:19:09.116 INFO  - ~> [Delivery] :: Process delivery successfully for orderId: 1700469688961, foodName: Food_NumberTop
15:19:09.117 INFO  - ~> [Delivery] :: Send NOTIFY_DELIVERY_SUCCESS for orderId: 1700469688961
15:19:09.119 INFO  - ~>  . . . . . . . . . . . . .
15:19:09.120 INFO  - ~> [Orders] :: Consume delivery response: 1700469688961:NOTIFY_DELIVERY_SUCCESS
15:19:09.121 INFO  - ~> [Orders] :: Get Orders by orderId: 1700469688961
15:19:09.133 INFO  - ~> [Orders] :: Change status of Orders from: PREPARED into status: COMPLETED
15:19:09.177 INFO  - ~> [Orders] :: Complete the order workflow for orderId: 1700469688961
```

