package ar.edu.unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ar.edu.unlp.info.bd2.model.Delivered;

public interface DeliveredRepository extends CrudRepository<Delivered,Long> {

	List<Delivered> findByOrder_Client_Username(String username);
}
