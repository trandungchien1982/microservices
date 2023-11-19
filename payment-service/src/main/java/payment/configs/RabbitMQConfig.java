package payment.configs;

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
     * The config relating to Payment Request Queue
     **************************************************/
    @Value("${rabbitmq.payment_request.queue.name}")
    private String paymentRequestQueueName;

    @Bean
    public Queue paymentRequestQueue(){
        return new Queue(paymentRequestQueueName);
    }


    /**************************************************
     * The config relating to Payment Response Queue
     **************************************************/
    @Value("${rabbitmq.payment_response.queue.name}")
    private String paymentResponseQueueName;

    @Value("${rabbitmq.payment_response.routing.key}")
    private String paymentResponseRoutingKey;
    @Bean
    public Queue paymentResponseQueue(){
        return new Queue(paymentResponseQueueName);
    }
    @Bean
    public Binding bindingPaymentResponse(){
        return BindingBuilder
                .bind(paymentResponseQueue())
                .to(exchange())
                .with(paymentResponseRoutingKey);
    }
}