package restaurant.configs;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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
     * The config relating to Restaurant Request Queue
     **************************************************/
    @Value("${rabbitmq.restaurant_request.queue.name}")
    private String restaurantRequestQueueName;

    @Bean
    public Queue restaurantRequestQueue(){
        return new Queue(restaurantRequestQueueName);
    }


    /**************************************************
     * The config relating to Restaurant Response Queue
     **************************************************/
    @Value("${rabbitmq.restaurant_response.queue.name}")
    private String restaurantResponseQueueName;

    @Value("${rabbitmq.restaurant_response.routing.key}")
    private String restaurantResponseRoutingKey;
    @Bean
    public Queue restaurantResponseQueue(){
        return new Queue(restaurantResponseQueueName);
    }
    @Bean
    public Binding bindingRestaurantResponse(){
        return BindingBuilder
                .bind(restaurantResponseQueue())
                .to(exchange())
                .with(restaurantResponseRoutingKey);
    }


    /**************************************************
     * The config relating to Delivery Request Queue
     **************************************************/
    @Value("${rabbitmq.delivery_request.queue.name}")
    private String deliveryRequestQueueName;

    @Value("${rabbitmq.delivery_request.routing.key}")
    private String deliveryRequestRoutingKey;
    @Bean
    public Queue deliveryRequestQueue(){
        return new Queue(deliveryRequestQueueName);
    }
    @Bean
    public Binding bindingDeliveryRequest(){
        return BindingBuilder
                .bind(deliveryRequestQueue())
                .to(exchange())
                .with(deliveryRequestRoutingKey);
    }
}