package payment.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import payment.entities.Payment;
import payment.services.LogService;
import payment.services.PaymentService;

@Service
public class PaymentRequestConsumer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentResponseProducer paymentResponseProducer;


    // Push message with exchange + orderRequestRoutingKey
    // => Will be moved to the orderRequestQueue
    @RabbitListener(queues = {"${rabbitmq.payment_request.queue.name}"})
    public void consumePaymentRequest(String message){
        logService.log(" . . . . . . . . . . . . . ");
        logService.log("[Payment] :: Consume payment request: " + message);

        // Process the payment message
        // orderId:NOTIFY_PAYMENT_PROCESS:cardNumber
        String[] argMsgs = message.split(":");
        if (argMsgs.length < 3) { return; }

        Payment newPayment = paymentService.createPayment(argMsgs[0], argMsgs[2]);
        logService.log("[Payment] :: Create new payment successfully for orderId: " + newPayment.getOrderId());
        logService.log("[Payment] :: Send NOTIFY_PAYMENT_SUCCESS for orderId: " + newPayment.getOrderId());
        paymentResponseProducer.paymentResponse(newPayment.getOrderId() + ":NOTIFY_PAYMENT_SUCCESS");
    }
}
