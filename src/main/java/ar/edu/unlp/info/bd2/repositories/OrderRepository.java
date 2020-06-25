package ar.edu.unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ar.edu.unlp.info.bd2.model.Order;

public interface OrderRepository extends CrudRepository<Order,Long> {

	Optional<Order> getOrderById(Long id);

	@Query("select o from Pending p join p.order o where o.id not in(select c.order.id from Cancelled c) and o not in(select s.order.id from Sent s) and o not in(select d.order.id from Delivered d)")
	public List<Order> findPendingOrders();
}
