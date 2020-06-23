package ar.edu.unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import ar.edu.unlp.info.bd2.model.Order;

public interface OrderRepository extends CrudRepository<Order,Long> {

	Optional<Order> getOrderById(Long id);

}
