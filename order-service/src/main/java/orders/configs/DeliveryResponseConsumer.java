package orders.configs;

import orders.entities.Orders;
import orders.repositories.OrdersDao;
import orders.services.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryResponseConsumer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private OrdersDao ordersDao;

    @RabbitListener(queues = {"${rabbitmq.delivery_response.queue.name}"})
    public void consumeDeliveryResponse(String message){
        logService.log(" . . . . . . . . . . . . . ");
        logService.log("[Orders] :: Consume delivery response: " + message);

        // Process the delivery response message
        // orderId:NOTIFY_DELIVERY_SUCCESS
        String[] argMsgs = message.split(":");
        if (argMsgs.length < 2) { return; }

        logService.log("[Orders] :: Get Orders by orderId: " + argMsgs[0]);
        Orders currentOrder = ordersDao.findById(Long.valueOf(argMsgs[0])).orElse(null);

        logService.log("[Orders] :: Change status of Orders from: " + currentOrder.getStatus() + " into status: COMPLETED");
        currentOrder.setStatus("COMPLETED");
        ordersDao.save(currentOrder);

        logService.log("[Orders] :: Complete the order workflow for orderId: " + currentOrder.getId());
    }
}