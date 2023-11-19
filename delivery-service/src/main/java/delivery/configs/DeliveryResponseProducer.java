package delivery.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeliveryResponseProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.delivery_response.routing.key}")
    private String deliveryResponseRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Push message with exchange + restaurantResponseRoutingKey
    // => Will be moved to the restaurantResponseQueue
    public void deliveryResponse(String message){
        rabbitTemplate.convertAndSend(exchange, deliveryResponseRoutingKey, message);
    }
}