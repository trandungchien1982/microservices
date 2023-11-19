package delivery.configs;

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
     * The config relating to Delivery Request Queue
     **************************************************/
    @Value("${rabbitmq.delivery_request.queue.name}")
    private String deliveryRequestQueueName;

    @Bean
    public Queue deliveryRequestQueue(){
        return new Queue(deliveryRequestQueueName);
    }


    /**************************************************
     * The config relating to Delivery Response Queue
     **************************************************/
    @Value("${rabbitmq.delivery_response.queue.name}")
    private String deliveryResponseQueueName;

    @Value("${rabbitmq.delivery_response.routing.key}")
    private String deliveryResponseRoutingKey;
    @Bean
    public Queue deliveryResponseQueue(){
        return new Queue(deliveryResponseQueueName);
    }
    @Bean
    public Binding bindingDeliveryResponse(){
        return BindingBuilder
                .bind(deliveryResponseQueue())
                .to(exchange())
                .with(deliveryResponseRoutingKey);
    }

}