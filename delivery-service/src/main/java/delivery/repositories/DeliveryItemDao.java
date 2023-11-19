package delivery.repositories;

import delivery.entities.DeliveryItem;

import org.springframework.data.repository.CrudRepository;

/**
 * DAO interface, we do not need inject @Bean
 * @author tdc
 */
public interface DeliveryItemDao extends CrudRepository<DeliveryItem, Long> {

}
