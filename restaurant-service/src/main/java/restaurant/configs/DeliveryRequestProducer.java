package restaurant.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeliveryRequestProducer {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.delivery_request.routing.key}")
    private String deliveryRequestRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Push message with exchange + deliveryRequestRoutingKey
    // => Will be moved to the deliveryRequestQueue
    public void deliveryRequest(String message){
        rabbitTemplate.convertAndSend(exchange, deliveryRequestRoutingKey, message);
    }
}