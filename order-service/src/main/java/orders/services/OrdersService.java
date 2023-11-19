package orders.services;

import orders.configs.PaymentRequestProducer;
import orders.entities.Orders;
import orders.repositories.OrdersDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class OrdersService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    OrdersDao ordersDao;

    @Autowired
    LogService logService;

    @Autowired
    PaymentRequestProducer paymentRequestProducer;

    public Orders createOrder(String customerName, String cardNumber) {

        Orders newOrder = new Orders();
        long orderId = System.currentTimeMillis();
        newOrder.setId(orderId);
        newOrder.setStatus("CREATED");
        newOrder.setOrderName("NewOrder: " + orderId);
        newOrder.setCreateDate(new Date());
        newOrder.setCustomerName(customerName);
        newOrder.setCardNumber(cardNumber);
        newOrder.setDescription("Description of orderId: " + orderId);

        logService.log("[Orders] :: Create new order with id: " + orderId + ", status: " + newOrder.getStatus());
        return ordersDao.save(newOrder);
    }

    public void notifyPayment(Orders orders) {
        logService.log("[Orders] :: Notify payment for orderId: " + orders.getId());
        paymentRequestProducer.paymentRequest(orders.getId() + ":NOTIFY_PAYMENT_REQUEST:" + orders.getCardNumber());
    }
}
