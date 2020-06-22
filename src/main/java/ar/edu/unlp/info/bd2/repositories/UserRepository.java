package ar.edu.unlp.info.bd2.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ar.edu.unlp.info.bd2.model.User;

public interface UserRepository extends CrudRepository<User,Long> {

	public Optional<User> getById(Long id);
	public Optional<User> getByUsername(String username);
	public Optional<User> getByEmail(String email);
}
