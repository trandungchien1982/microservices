package payment.services;

import payment.configs.PaymentResponseProducer;
import payment.entities.Payment;
import payment.repositories.PaymentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payment.utils.Utils;

import java.util.Date;
import java.util.Random;


@Service
public class PaymentService {

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    LogService logService;

    @Autowired
    PaymentResponseProducer paymentResponseProducer;

    public Payment createPayment(String orderId, String cardNumber) {
        Payment newPayment = new Payment();
        newPayment.setId(new Random().nextLong());
        newPayment.setOrderId(orderId);
        newPayment.setCardNumber(cardNumber);
        newPayment.setCreateDate(new Date());

        logService.log("[Payment] :: Process the payment for orderId: " + orderId);
        Utils.sleep(1);
        return paymentDao.save(newPayment);
    }

}
