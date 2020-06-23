package ar.edu.unlp.info.bd2.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ar.edu.unlp.info.bd2.model.Product;

public interface ProductRepository extends CrudRepository<Product,Long> {
	
	public Product getProductByName(String name);
	public Product getProductById(Long id);
	public List<Product> findAllByName(String name);
	public List<Product> findAllByNameLike(String name);

}
