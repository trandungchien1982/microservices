package restaurant.services;

import restaurant.configs.RestaurantResponseProducer;
import restaurant.entities.FoodItem;
import restaurant.repositories.FoodItemDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restaurant.utils.Utils;

import java.util.Date;
import java.util.Random;


@Service
public class RestaurantService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    FoodItemDao foodItemDao;

    @Autowired
    LogService logService;

    @Autowired
    RestaurantResponseProducer restaurantResponseProducer;

    public FoodItem prepareFood(String orderId, String foodName) {
        FoodItem newFoodItem = new FoodItem();
        newFoodItem.setId(new Random().nextLong());
        newFoodItem.setOrderId(orderId);
        newFoodItem.setFoodName(foodName);
        newFoodItem.setCreateDate(new Date());

        logService.log("[Restaurant] :: Process preparing food for orderId: " + orderId + ", foodName: " + foodName);

        Utils.sleep(1);
        return foodItemDao.save(newFoodItem);
    }

    public void notifyRestaurantSuccess(FoodItem foodItem) {
        logService.log("[Restaurant] :: Notify preparing food successfully for orderId: " + foodItem.getOrderId());
        restaurantResponseProducer.restaurantResponse(foodItem.getOrderId() + ":NOTIFY_RESTAURANT_SUCCESS");
    }

}
