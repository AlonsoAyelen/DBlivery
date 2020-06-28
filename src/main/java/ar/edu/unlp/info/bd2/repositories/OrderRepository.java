package ar.edu.unlp.info.bd2.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ar.edu.unlp.info.bd2.model.Order;

public interface OrderRepository extends CrudRepository<Order,Long> {


	public List<Order> findAllByClient_Username(String username);
	
	@Query("select o from Sent s join s.order o where o.id not in(select d.order.id from Delivered d)")
	public List<Order> findSentOrders();

	@Query("select o from Delivered d join d.order o where d.date between :startDate and :endDate")
	public List<Order> findDeliveredOrdersBetween(@Param("startDate")Date startDate, @Param("endDate") Date endDate);
	
	@Query("select o from Pending p join p.order o where o.id not in(select c.order.id from Cancelled c) and o not in(select s.order.id from Sent s) and o not in(select d.order.id from Delivered d)")
	public List<Order> findPendingOrders();

	@Query("select o from Sent s join s.order o where o.client.username = :username")
	public List<Order> findDeliveredOrdersForUser(@Param("username") String username);
	
}
