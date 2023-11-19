package payment.repositories;

import payment.entities.Payment;

import org.springframework.data.repository.CrudRepository;

/**
 * DAO interface, we do not need inject @Bean
 * @author tdc
 */
public interface PaymentDao extends CrudRepository<Payment, Long> {

}
