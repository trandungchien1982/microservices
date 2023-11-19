package restaurant.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RestaurantResponseProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.restaurant_response.routing.key}")
    private String restauranResponseRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Push message with exchange + restaurantResponseRoutingKey
    // => Will be moved to the restaurantResponseQueue
    public void restaurantResponse(String message){
        rabbitTemplate.convertAndSend(exchange, restauranResponseRoutingKey, message);
    }
}