package orders.configs;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**************************************************
     * The config relating to LogService using RabbitMQ
     **************************************************/

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // spring bean for rabbitmq queue
    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    // spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    // binding between queue and exchange using routing key
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }


    /**************************************************
     * The config relating to PaymentRequest Queue
     **************************************************/
    @Value("${rabbitmq.payment_request.queue.name}")
    private String paymentRequestQueueName;

    @Value("${rabbitmq.payment_request.routing.key}")
    private String paymentRequestRoutingKey;

    @Bean
    public Queue paymentRequestQueue(){
        return new Queue(paymentRequestQueueName);
    }
    @Bean
    public Binding bindingPaymentRequest(){
        return BindingBuilder
                .bind(paymentRequestQueue())
                .to(exchange())
                .with(paymentRequestRoutingKey);
    }


    /**************************************************
     * The config relating to RestaurantRequest Queue
     **************************************************/
    @Value("${rabbitmq.restaurant_request.queue.name}")
    private String restaurantRequestQueueName;

    @Value("${rabbitmq.restaurant_request.routing.key}")
    private String restaurantRequestRoutingKey;

    @Bean
    public Queue restaurantRequestQueue(){
        return new Queue(restaurantRequestQueueName);
    }
    @Bean
    public Binding bindingRestaurantRequest(){
        return BindingBuilder
                .bind(restaurantRequestQueue())
                .to(exchange())
                .with(restaurantRequestRoutingKey);
    }

    @Value("${rabbitmq.restaurant_response.queue.name}")
    private String restaurantResponseQueueName;

    @Bean
    public Queue restaurantResponseQueue(){
        return new Queue(restaurantResponseQueueName);
    }

    @Value("${rabbitmq.delivery_response.queue.name}")
    private String deliveryResponseQueueName;

    @Bean
    public Queue deliveryResponseQueue(){
        return new Queue(deliveryResponseQueueName);
    }

}