package orders.configs;

import orders.entities.Orders;
import orders.repositories.OrdersDao;
import orders.services.LogService;
import orders.services.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentResponseConsumer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private RestaurantRequestProducer restaurantRequestProducer;

    @RabbitListener(queues = {"${rabbitmq.payment_response.queue.name}"})
    public void consumePaymentResponse(String message){
        logService.log(" . . . . . . . . . . . . . ");
        logService.log("[Orders] :: Consume payment response: " + message);

        // Process the payment response message
        // orderId:NOTIFY_PAYMENT_SUCCESS
        String[] argMsgs = message.split(":");
        if (argMsgs.length < 2) { return; }

        logService.log("[Orders] :: Get Orders by orderId: " + argMsgs[0]);
        Orders currentOrder = ordersDao.findById(Long.valueOf(argMsgs[0])).orElse(null);

        logService.log("[Orders] :: Change status of Orders from: " + currentOrder.getStatus() + " into status: PAID");
        currentOrder.setStatus("PAID");
        ordersDao.save(currentOrder);

        logService.log("[Orders] :: Notify message to request the restaurant service for orderId: " + currentOrder.getId());
        restaurantRequestProducer.restaurantRequest(
                currentOrder.getId() + ":NOTIFY_RESTAURANT_REQUEST:Food_NumberTop");
    }
}