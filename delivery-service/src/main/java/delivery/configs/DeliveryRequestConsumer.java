package delivery.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import delivery.entities.DeliveryItem;
import delivery.services.LogService;
import delivery.services.DeliveryService;

@Service
public class DeliveryRequestConsumer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryResponseProducer deliveryResponseProducer;

    @RabbitListener(queues = {"${rabbitmq.delivery_request.queue.name}"})
    public void consumeDeliveryRequest(String message){
        logService.log(" . . . . . . . . . . . . . ");
        logService.log("[Delivery] :: Consume delivery request: " + message);

        // Process the delivery message
        // orderId:NOTIFY_DELIVERY_REQUEST:foodName
        String[] argMsgs = message.split(":");
        if (argMsgs.length < 3) { return; }

        DeliveryItem newDeliveryItem = deliveryService.process(argMsgs[0], argMsgs[2]);
        String orderId = newDeliveryItem.getOrderId();
        logService.log("[Delivery] :: Process delivery successfully for orderId: " + orderId + ", foodName: " + newDeliveryItem.getFoodName());
        logService.log("[Delivery] :: Send NOTIFY_DELIVERY_SUCCESS for orderId: " + orderId);
        deliveryResponseProducer.deliveryResponse(newDeliveryItem.getOrderId() + ":NOTIFY_DELIVERY_SUCCESS");
    }
}