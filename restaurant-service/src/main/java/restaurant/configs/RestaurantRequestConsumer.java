package restaurant.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import restaurant.entities.FoodItem;
import restaurant.services.LogService;
import restaurant.services.RestaurantService;

@Service
public class RestaurantRequestConsumer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantResponseProducer restaurantResponseProducer;

    @Autowired
    private DeliveryRequestProducer deliveryRequestProducer;

    @RabbitListener(queues = {"${rabbitmq.restaurant_request.queue.name}"})
    public void consumeRestaurantRequest(String message){
        logService.log(" . . . . . . . . . . . . . ");
        logService.log("[Restaurant] :: Consume restaurant request: " + message);

        // Process the restaurant message
        // orderId:NOTIFY_RESTAURANT_REQUEST:cardNumber
        String[] argMsgs = message.split(":");
        if (argMsgs.length < 3) { return; }

        FoodItem newFoodItem = restaurantService.prepareFood(argMsgs[0], argMsgs[2]);
        String orderId = newFoodItem.getOrderId();
        logService.log("[Restaurant] :: Prepare food successfully for orderId: " + orderId);
        logService.log("[Restaurant] :: Send NOTIFY_RESTAURANT_SUCCESS for orderId: " + orderId);
        restaurantResponseProducer.restaurantResponse(newFoodItem.getOrderId() + ":NOTIFY_RESTAURANT_SUCCESS");

        // Request to Delivery Service
        // orderId:NOTIFY_DELIVERY_REQUEST:foodName
        logService.log("[Restaurant] :: Request to delivery service for orderId: " + orderId);
        deliveryRequestProducer.deliveryRequest(orderId + ":NOTIFY_DELIVERY_REQUEST:" + newFoodItem.getFoodName());
    }
}