package orders.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.payment_request.routing.key}")
    private String paymentRequestRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Push message with exchange + paymentRequestRoutingKey
    // => Will be moved to the paymentRequestQueue
    public void paymentRequest(String message){
        rabbitTemplate.convertAndSend(exchange, paymentRequestRoutingKey, message);
    }
}