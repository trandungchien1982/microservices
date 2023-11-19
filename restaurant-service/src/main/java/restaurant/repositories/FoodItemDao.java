package restaurant.repositories;

import restaurant.entities.FoodItem;

import org.springframework.data.repository.CrudRepository;

/**
 * DAO interface, we do not need inject @Bean
 * @author tdc
 */
public interface FoodItemDao extends CrudRepository<FoodItem, Long> {

}
