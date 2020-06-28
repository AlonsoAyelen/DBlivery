package ar.edu.unlp.info.bd2.repositories;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ar.edu.unlp.info.bd2.model.Product;

public interface ProductRepository extends CrudRepository<Product,Long> {
	
	public Product getProductByName(String name);
	public List<Product> findAllByNameLike(String name);

	public Optional<Product> findTopByOrderByWeightDesc();
	
	@Query("Select prod from Product prod join prod.prices prc group by prod.id having (count(*)=1)")
	public List<Product> findByGroupByPrices_Product();
	
	@Query("Select row.product from Order o join o.products row where o.dateOfOrder=:day")
	public List<Product> findByDateByOrder_Product(Date day);
	

}
