package ar.edu.unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ar.edu.unlp.info.bd2.model.Supplier;

public interface SupplierRepository extends CrudRepository<Supplier,Long> {

}
