package delivery.services;

import delivery.configs.DeliveryResponseProducer;
import delivery.entities.DeliveryItem;
import delivery.repositories.DeliveryItemDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import delivery.utils.Utils;

import java.util.Date;
import java.util.Random;


@Service
public class DeliveryService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    DeliveryItemDao deliveryItemDao;

    @Autowired
    LogService logService;

    public DeliveryItem process(String orderId, String foodName) {
        DeliveryItem newDeliveryItem = new DeliveryItem();
        newDeliveryItem.setId(new Random().nextLong());
        newDeliveryItem.setOrderId(orderId);
        newDeliveryItem.setFoodName(foodName);
        newDeliveryItem.setDriverName("DriverName: " + orderId);
        newDeliveryItem.setCreateDate(new Date());

        logService.log("[Delivery] :: Process delivery for orderId: " + orderId + ", foodName: " + foodName + ", driverName: " + newDeliveryItem.getDriverName());

        Utils.sleep(3);
        return deliveryItemDao.save(newDeliveryItem);
    }

}
