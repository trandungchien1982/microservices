package payment.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentResponseProducer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.payment_response.routing.key}")
    private String paymentResponseRoutingKey;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Push message with exchange + paymentResponseRoutingKey
    // => Will be moved to the paymentResponseQueue
    public void paymentResponse(String message){
        rabbitTemplate.convertAndSend(exchange, paymentResponseRoutingKey, message);
    }
}