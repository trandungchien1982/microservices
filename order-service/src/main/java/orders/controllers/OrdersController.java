package orders.controllers;


import orders.entities.Orders;
import orders.services.LogService;
import orders.services.OrdersService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@Transactional
public class OrdersController {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    OrdersService ordersService;

    @Autowired
    LogService logService;

    @GetMapping(path="/submit-order")
    public String submitOrder(
            @RequestParam(name = "customerName", required = false) String customerName,
            @RequestParam(name = "cardNumber", required = false) String cardNumber) {
        logService.log("");
        logService.log(" -------------------------------------------------------------------- ");
        logService.log("[Orders] :: User has submitted a new order ... ");

        customerName = (customerName == null) ? "Customer-00" : customerName;
        cardNumber = (cardNumber == null) ? "CardNumber-00" : cardNumber;

        Orders newOrder = ordersService.createOrder(customerName, cardNumber);
        ordersService.notifyPayment(newOrder);
        return "OK with new orderId: " + newOrder.getId() + ", customerName: " + customerName + ", cardNumber: " + cardNumber;
    }

}