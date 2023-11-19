package orders.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RestaurantRequestProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.restaurant_request.routing.key}")
    private String restaurantRequestRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Push message with exchange + restaurantRequestRoutingKey
    // => Will be moved to the restaurantRequestQueue
    public void restaurantRequest(String message){
        rabbitTemplate.convertAndSend(exchange, restaurantRequestRoutingKey, message);
    }
}